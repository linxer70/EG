/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.type.TypeReference
 *  com.fasterxml.jackson.databind.JsonNode
 *  com.fasterxml.jackson.databind.node.ObjectNode
 *  com.vaadin.flow.function.SerializableBiConsumer
 *  com.vaadin.flow.function.SerializableConsumer
 *  com.vaadin.flow.function.SerializableFunction
 *  com.vaadin.flow.function.SerializableSupplier
 *  com.vaadin.flow.server.Command
 *  com.vaadin.flow.shared.Registration
 */
package egframe.common.SysChat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import egframe.frame.entity.sys_user;

import com.vaadin.flow.function.SerializableBiConsumer;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.function.SerializableSupplier;
import com.vaadin.flow.server.Command;
import com.vaadin.flow.shared.Registration;
import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TopicConnection {
    private final Topic topic;
    private final sys_user localUser;
    private final List<Registration> deactivateRegistrations = new ArrayList<Registration>();
    private final Consumer<Boolean> topicActivationHandler;
    private final Map<String, List<Consumer<MapChange>>> subscribersPerMap = new HashMap<String, List<Consumer<MapChange>>>();
    private final Map<String, List<Consumer<ListChange>>> subscribersPerList = new HashMap<String, List<Consumer<ListChange>>>();
    private final Map<String, Map<String, UUID>> connectionScopedMapKeys = new HashMap<String, Map<String, UUID>>();
    private final Map<String, Map<UUID, UUID>> connectionScopedListItems = new HashMap<String, Map<UUID, UUID>>();
    private volatile boolean cleanupPending;
    private final BiConsumer<UUID, ObjectNode> distributor;
    private final SerializableFunction<TopicConnection, Registration> connectionActivationCallback;
    private Registration closeRegistration;
    private ActionDispatcher actionDispatcher;
    private boolean activated;

    TopicConnection(SerializableSupplier<CollaborationEngine> collaborationEngineSupplier, ConnectionContext context, Topic topic, BiConsumer<UUID, ObjectNode> distributor, sys_user localUser, Consumer<Boolean> topicActivationHandler, SerializableFunction<TopicConnection, Registration> connectionActivationCallback) {
        this.topic = topic;
        this.distributor = distributor;
        this.localUser = localUser;
        this.topicActivationHandler = topicActivationHandler;
        this.connectionActivationCallback = connectionActivationCallback;
        this.closeRegistration = context.init(this::acceptActionDispatcher, command -> ((CollaborationEngine)collaborationEngineSupplier.get()).getExecutorService().execute(command));
    }

    private void handleChange(UUID id, Topic.ChangeDetails change) {
        block4: {
            try {
                if (change instanceof MapChange) {
                    this.handleMapChange(id, (MapChange)change);
                    break block4;
                }
                if (change instanceof ListChange) {
                    this.handleListChange(id, (ListChange)change);
                    break block4;
                }
                throw new UnsupportedOperationException("Type '" + change.getClass().getName() + "' is not a supported change type");
            }
            catch (RuntimeException e) {
                this.deactivateAndClose();
                throw e;
            }
        }
    }

    private void handleMapChange(UUID id, MapChange mapChange) {
        String mapName = mapChange.getMapName();
        String key = mapChange.getKey();
        Map<String, UUID> keys = this.connectionScopedMapKeys.get(mapName);
        if (keys != null) {
            if (keys.containsKey(key) && mapChange.getType() == MapChangeType.REPLACE) {
                keys.put(key, mapChange.getRevisionId());
            }
            if (!Objects.equals(id, keys.get(key))) {
                UUID uuid = keys.get(key);
                if (!Objects.equals(mapChange.getExpectedId(), uuid)) {
                    keys.remove(key);
                }
            }
        }
        if (mapChange.hasChanges()) {
            EventUtil.fireEvents(this.subscribersPerMap.get(mapName), notifier -> notifier.accept(mapChange), false);
        }
    }

    private void handleListChange(UUID id, ListChange listChange) {
        String listName = listChange.getListName();
        UUID key = listChange.getKey();
        Map<UUID, UUID> keys = this.connectionScopedListItems.get(listName);
        if (keys != null && !Objects.equals(id, keys.get(key))) {
            UUID uuid = keys.get(key);
            if (!Objects.equals(listChange.getExpectedId(), uuid)) {
                keys.remove(key);
            }
        }
        EventUtil.fireEvents(this.subscribersPerList.get(listChange.getListName()), notifier -> notifier.accept(listChange), false);
    }

    Topic getTopic() {
        return this.topic;
    }

    public sys_user getsys_user() {
        return this.localUser;
    }

    private boolean isActive() {
        return this.actionDispatcher != null;
    }

    private void addRegistration(Registration registration) {
        if (registration != null) {
            this.deactivateRegistrations.add(registration);
        }
    }

    public CollaborationMap getNamedMap(String name) {
        this.ensureActiveConnection();
        return new CollaborationMapImplementation(name);
    }

    public CollaborationList getNamedList(String name) {
        this.ensureActiveConnection();
        return new CollaborationListImplementation(name);
    }

    CompletableFuture<Void> deactivateAndClose() {
        CompletableFuture<Void> result;
        try {
            this.deactivate();
        }
        finally {
            result = this.closeWithoutDeactivating();
        }
        return result;
    }

    private void deactivate() {
        try {
            this.cleanupScopedData();
            EventUtil.fireEvents(this.deactivateRegistrations, Registration::remove, false);
            this.deactivateRegistrations.clear();
        }
        catch (RuntimeException e) {
            if (this.actionDispatcher != null) {
                this.topicActivationHandler.accept(false);
                this.actionDispatcher = null;
            }
            this.closeWithoutDeactivating();
            throw e;
        }
    }

    private CompletableFuture<Void> closeWithoutDeactivating() {
        if (this.closeRegistration != null) {
            try {
                this.closeRegistration.remove();
                if (this.closeRegistration instanceof AsyncRegistration) {
                    CompletableFuture<Void> completableFuture = ((AsyncRegistration)this.closeRegistration).getFuture();
                    return completableFuture;
                }
            }
            finally {
                this.closeRegistration = null;
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void cleanupScopedData() {
        Topic topic = this.topic;
        synchronized (topic) {
            this.connectionScopedMapKeys.forEach((mapName, mapKeys) -> mapKeys.forEach((key, id) -> {
                ObjectNode change = JsonUtil.createPutChange(mapName, key, null, null, null);
                change.put("expected-id", id.toString());
                this.distributor.accept(UUID.randomUUID(), change);
            }));
            this.connectionScopedMapKeys.clear();
            this.connectionScopedListItems.forEach((listName, listItems) -> listItems.forEach((key, id) -> {
                ObjectNode change = JsonUtil.createListChange(ListOperation.OperationType.SET, listName, key.toString(), null, null, null, Collections.emptyMap(), Collections.emptyMap(), null);
                change.put("expected-id", id.toString());
                this.distributor.accept(UUID.randomUUID(), change);
            }));
            this.connectionScopedListItems.clear();
            this.cleanupPending = false;
        }
    }

    private void ensureActiveConnection() {
        if (!this.isActive()) {
            throw new IllegalStateException("Cannot perform this operation on a connection that is inactive or about to become inactive.");
        }
    }

    private void acceptActionDispatcher(ActionDispatcher actionDispatcher) {
        if (actionDispatcher != null) {
            if (this.activated) {
                throw new IllegalStateException("The topic connection is already active.");
            }
            this.activated = true;
            if (this.actionDispatcher != null) {
                return;
            }
            actionDispatcher.dispatchAction((Command & Serializable)() -> {
                if (!this.activated) {
                    return;
                }
                if (this.actionDispatcher != null) {
                    throw new IllegalStateException("Activation dispatch is run out-of-order.");
                }
                this.actionDispatcher = actionDispatcher;
                this.cleanupPending = true;
                this.topicActivationHandler.accept(true);
                Registration changeRegistration = this.subscribeToChange();
                Registration callbackRegistration = (Registration)this.connectionActivationCallback.apply(this);
                this.addRegistration(callbackRegistration);
                this.addRegistration((Registration & Serializable)() -> {
                    Topic topic = this.topic;
                    synchronized (topic) {
                        changeRegistration.remove();
                    }
                });
                this.distributor.accept(UUID.randomUUID(), JsonUtil.createNodeActivate(this.topic.getCurrentNodeId()));
            });
        } else {
            if (!this.activated) {
                throw new IllegalStateException("The topic connection is already inactive.");
            }
            this.activated = false;
            if (this.actionDispatcher == null) {
                return;
            }
            this.actionDispatcher.dispatchAction((Command & Serializable)() -> {
                if (this.activated) {
                    return;
                }
                if (this.actionDispatcher == null) {
                    throw new IllegalStateException("Deactivation dispatch is run out-of-order.");
                }
                try {
                    this.distributor.accept(UUID.randomUUID(), JsonUtil.createNodeDeactivate(this.topic.getCurrentNodeId()));
                    this.actionDispatcher = null;
                    this.deactivate();
                }
                finally {
                    this.topicActivationHandler.accept(false);
                }
            });
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private Registration subscribeToChange() {
        Topic topic = this.topic;
        synchronized (topic) {
            return this.topic.subscribeToChange((SerializableBiConsumer<UUID, Topic.ChangeDetails>)(SerializableBiConsumer & Serializable)(id, change) -> {
                if (this.actionDispatcher != null) {
                    this.actionDispatcher.dispatchAction((Command & Serializable)() -> this.handleChange((UUID)id, (Topic.ChangeDetails)change));
                }
            });
        }
    }

    class CollaborationMapImplementation
    implements CollaborationMap {
        private final String name;

        private CollaborationMapImplementation(String name) {
            this.name = name;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Registration subscribe(MapSubscriber subscriber) {
            TopicConnection.this.ensureActiveConnection();
            Objects.requireNonNull(subscriber, "Subscriber cannot be null");
            Topic topic = TopicConnection.this.topic;
            synchronized (topic) {
                Consumer<MapChange> mapChangeNotifier = mapChange -> {
                    MapChangeEvent event = new MapChangeEvent(this, (MapChange)mapChange);
                    TopicConnection.this.actionDispatcher.dispatchAction((Command & Serializable)() -> subscriber.onMapChange(event));
                };
                TopicConnection.this.topic.getMapData(this.name).forEach(mapChangeNotifier);
                Registration registration = this.subscribeToMap(this.name, mapChangeNotifier);
                TopicConnection.this.addRegistration(registration);
                return registration;
            }
        }

        @Override
        public CompletableFuture<Boolean> replace(String key, Object expectedValue, Object newValue) {
            TopicConnection.this.ensureActiveConnection();
            Objects.requireNonNull(key, "Key cannot be null");
            ObjectNode change = JsonUtil.createReplaceChange(this.name, key, expectedValue, newValue);
            UUID id = UUID.randomUUID();
            return this.dispatchChangeWithBooleanResult(id, key, false, change);
        }

        @Override
        public CompletableFuture<Void> put(String key, Object value, EntryScope scope) {
            TopicConnection.this.ensureActiveConnection();
            Objects.requireNonNull(key, "Key cannot be null");
            boolean connectionScope = scope == EntryScope.CONNECTION;
            ObjectNode change = JsonUtil.createPutChange(this.name, key, null, value, connectionScope ? TopicConnection.this.topic.getCurrentNodeId() : null);
            UUID id = UUID.randomUUID();
            return this.dispatchChangeWithVoidResult(id, key, connectionScope, change);
        }

        private CompletableFuture<Void> dispatchChangeWithVoidResult(UUID id, String key, boolean connectionScope, ObjectNode change) {
            CompletableFuture<Void> contextFuture = TopicConnection.this.actionDispatcher.createCompletableFuture();
            TopicConnection.this.topic.setChangeResultTracker(id, (SerializableConsumer<Topic.ChangeResult>)(SerializableConsumer & Serializable)result -> {
                if (connectionScope && result == Topic.ChangeResult.ACCEPTED) {
                    TopicConnection.this.connectionScopedMapKeys.computeIfAbsent(this.name, k -> new HashMap()).put(key, id);
                    if (!TopicConnection.this.cleanupPending) {
                        TopicConnection.this.cleanupScopedData();
                    }
                }
                if (TopicConnection.this.actionDispatcher != null) {
                    TopicConnection.this.actionDispatcher.dispatchAction((Command & Serializable)() -> contextFuture.complete(null));
                }
            });
            TopicConnection.this.actionDispatcher.dispatchAction((Command & Serializable)() -> TopicConnection.this.distributor.accept(id, change));
            return contextFuture;
        }

        private CompletableFuture<Boolean> dispatchChangeWithBooleanResult(UUID id, String key, boolean connectionScope, ObjectNode change) {
            CompletableFuture<Boolean> contextFuture = TopicConnection.this.actionDispatcher.createCompletableFuture();
            TopicConnection.this.topic.setChangeResultTracker(id, (SerializableConsumer<Topic.ChangeResult>)(SerializableConsumer & Serializable)result -> {
                if (connectionScope && result == Topic.ChangeResult.ACCEPTED) {
                    TopicConnection.this.connectionScopedMapKeys.computeIfAbsent(this.name, k -> new HashMap()).put(key, id);
                    if (!TopicConnection.this.cleanupPending) {
                        TopicConnection.this.cleanupScopedData();
                    }
                }
                if (TopicConnection.this.actionDispatcher != null) {
                    TopicConnection.this.actionDispatcher.dispatchAction((Command & Serializable)() -> contextFuture.complete(result != Topic.ChangeResult.REJECTED));
                }
            });
            TopicConnection.this.actionDispatcher.dispatchAction((Command & Serializable)() -> TopicConnection.this.distributor.accept(id, change));
            return contextFuture;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Stream<String> getKeys() {
            TopicConnection.this.ensureActiveConnection();
            Topic topic = TopicConnection.this.topic;
            synchronized (topic) {
                List snapshot = TopicConnection.this.topic.getMapData(this.name).map(MapChange::getKey).collect(Collectors.toList());
                return snapshot.stream();
            }
        }

        @Override
        public <T> T get(String key, Class<T> type) {
            return JsonUtil.toInstance(this.get(key), type);
        }

        @Override
        public <T> T get(String key, TypeReference<T> type) {
            return JsonUtil.toInstance(this.get(key), type);
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private JsonNode get(String key) {
            TopicConnection.this.ensureActiveConnection();
            Objects.requireNonNull(key, "Key cannot be null");
            Topic topic = TopicConnection.this.topic;
            synchronized (topic) {
                return TopicConnection.this.topic.getMapValue(this.name, key);
            }
        }

        @Override
        public TopicConnection getConnection() {
            return TopicConnection.this;
        }

        @Override
        public Optional<Duration> getExpirationTimeout() {
            Duration expirationTimeout = TopicConnection.this.topic.mapExpirationTimeouts.get(this.name);
            return Optional.ofNullable(expirationTimeout);
        }

        @Override
        public void setExpirationTimeout(Duration expirationTimeout) {
            TopicConnection.this.ensureActiveConnection();
            ObjectNode change = JsonUtil.createMapTimeoutChange(this.name, expirationTimeout);
            UUID id = UUID.randomUUID();
            TopicConnection.this.actionDispatcher.dispatchAction((Command & Serializable)() -> TopicConnection.this.distributor.accept(id, change));
        }

        private Registration subscribeToMap(String mapName, Consumer<MapChange> mapChangeNotifier) {
            TopicConnection.this.subscribersPerMap.computeIfAbsent(mapName, key -> new ArrayList()).add(mapChangeNotifier);
            return (Registration & Serializable)() -> this.unsubscribeFromMap(mapName, mapChangeNotifier);
        }

        private void unsubscribeFromMap(String mapName, Consumer<MapChange> mapChangeNotifier) {
            List<Consumer<MapChange>> notifiers = TopicConnection.this.subscribersPerMap.get(mapName);
            if (notifiers == null) {
                return;
            }
            notifiers.remove(mapChangeNotifier);
            if (notifiers.isEmpty()) {
                TopicConnection.this.subscribersPerMap.remove(mapName);
            }
        }
    }

    class CollaborationListImplementation
    implements CollaborationList {
        private final String name;

        private CollaborationListImplementation(String name) {
            this.name = name;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Registration subscribe(ListSubscriber subscriber) {
            TopicConnection.this.ensureActiveConnection();
            Objects.requireNonNull(subscriber, "Subscriber cannot be null");
            Topic topic = TopicConnection.this.topic;
            synchronized (topic) {
                Consumer<ListChange> changeNotifier = listChange -> {
                    ListChangeEvent event = new ListChangeEvent(this, (ListChange)listChange);
                    TopicConnection.this.actionDispatcher.dispatchAction((Command & Serializable)() -> subscriber.onListChange(event));
                };
                TopicConnection.this.topic.getListChanges(this.name).forEach(changeNotifier);
                Registration registration = this.subscribeToList(this.name, changeNotifier);
                TopicConnection.this.addRegistration(registration);
                return registration;
            }
        }

        @Override
        public <T> List<T> getItems(Class<T> type) {
            return this.getItems(JsonUtil.fromJsonConverter(type));
        }

        @Override
        public <T> List<T> getItems(TypeReference<T> type) {
            return this.getItems(JsonUtil.fromJsonConverter(type));
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private <T> List<T> getItems(Function<JsonNode, T> converter) {
            TopicConnection.this.ensureActiveConnection();
            Topic topic = TopicConnection.this.topic;
            synchronized (topic) {
                return TopicConnection.this.topic.getListItems(this.name).map(item -> item.value).map(converter).collect(Collectors.toList());
            }
        }

        @Override
        public <T> T getItem(ListKey key, Class<T> type) {
            return this.getItem(key, JsonUtil.fromJsonConverter(type));
        }

        @Override
        public <T> T getItem(ListKey key, TypeReference<T> type) {
            return this.getItem(key, JsonUtil.fromJsonConverter(type));
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private <T> T getItem(ListKey key, Function<JsonNode, T> converter) {
            TopicConnection.this.ensureActiveConnection();
            Objects.requireNonNull(key);
            Topic topic = TopicConnection.this.topic;
            synchronized (topic) {
                return converter.apply(TopicConnection.this.topic.getListValue(this.name, key.getKey()));
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Stream<ListKey> getKeys() {
            TopicConnection.this.ensureActiveConnection();
            Topic topic = TopicConnection.this.topic;
            synchronized (topic) {
                return TopicConnection.this.topic.getListItems(this.name).map(item -> new ListKey(item.id)).collect(Collectors.toList()).stream();
            }
        }

        @Override
        public ListOperationResult<Boolean> apply(ListOperation operation) {
            TopicConnection.this.ensureActiveConnection();
            Objects.requireNonNull(operation, "Operation cannot be null");
            UUID scopeOwnerId = null;
            if (operation.getScope() != null) {
                scopeOwnerId = operation.getScope() == EntryScope.CONNECTION ? TopicConnection.this.topic.getCurrentNodeId() : JsonUtil.TOPIC_SCOPE_ID;
            }
            ListKey referenceKey = operation.getReferenceKey();
            ListKey valueKey = operation.getChangeKey();
            ObjectNode change = JsonUtil.createListChange(operation.getType(), this.name, valueKey != null ? valueKey.getKey().toString() : null, referenceKey != null ? referenceKey.getKey().toString() : null, operation.getValue(), scopeOwnerId, operation.getConditions(), operation.getValueConditions(), operation.getEmpty());
            UUID id = UUID.randomUUID();
            return new ListOperationResult<Boolean>(new ListKey(id), this.dispatchChangeWithBooleanResult(id, valueKey != null ? valueKey.getKey() : id, operation.getScope() == EntryScope.CONNECTION, change));
        }

        private CompletableFuture<Boolean> dispatchChangeWithBooleanResult(UUID id, UUID key, boolean connectionScope, ObjectNode change) {
            CompletableFuture<Boolean> contextFuture = TopicConnection.this.actionDispatcher.createCompletableFuture();
            TopicConnection.this.topic.setChangeResultTracker(id, (SerializableConsumer<Topic.ChangeResult>)(SerializableConsumer & Serializable)result -> {
                if (connectionScope && result == Topic.ChangeResult.ACCEPTED) {
                    TopicConnection.this.connectionScopedListItems.computeIfAbsent(this.name, k -> new HashMap()).put(key, id);
                    if (!TopicConnection.this.cleanupPending) {
                        TopicConnection.this.cleanupScopedData();
                    }
                }
                if (TopicConnection.this.actionDispatcher != null) {
                    TopicConnection.this.actionDispatcher.dispatchAction((Command & Serializable)() -> contextFuture.complete(result != Topic.ChangeResult.REJECTED));
                }
            });
            TopicConnection.this.actionDispatcher.dispatchAction((Command & Serializable)() -> TopicConnection.this.distributor.accept(id, change));
            return contextFuture;
        }

        @Override
        public TopicConnection getConnection() {
            return TopicConnection.this;
        }

        @Override
        public Optional<Duration> getExpirationTimeout() {
            Duration expirationTimeout = TopicConnection.this.topic.listExpirationTimeouts.get(this.name);
            return Optional.ofNullable(expirationTimeout);
        }

        @Override
        public void setExpirationTimeout(Duration expirationTimeout) {
            TopicConnection.this.ensureActiveConnection();
            ObjectNode change = JsonUtil.createListTimeoutChange(this.name, expirationTimeout);
            UUID id = UUID.randomUUID();
            TopicConnection.this.actionDispatcher.dispatchAction((Command & Serializable)() -> TopicConnection.this.distributor.accept(id, change));
        }

        private Registration subscribeToList(String listName, Consumer<ListChange> changeNotifier) {
            TopicConnection.this.subscribersPerList.computeIfAbsent(listName, key -> new ArrayList()).add(changeNotifier);
            return (Registration & Serializable)() -> this.unsubscribeFromList(listName, changeNotifier);
        }

        private void unsubscribeFromList(String listName, Consumer<ListChange> changeNotifier) {
            List<Consumer<ListChange>> notifiers = TopicConnection.this.subscribersPerList.get(listName);
            if (notifiers == null) {
                return;
            }
            notifiers.remove(changeNotifier);
            if (notifiers.isEmpty()) {
                TopicConnection.this.subscribersPerList.remove(listName);
            }
        }
    }
}
