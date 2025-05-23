/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.vaadin.flow.component.Component
 *  com.vaadin.flow.function.SerializableFunction
 *  com.vaadin.flow.function.SerializableSupplier
 *  com.vaadin.flow.internal.UsageStatistics
 *  com.vaadin.flow.server.VaadinService
 *  com.vaadin.flow.shared.Registration
 */
package egframe.common.SysChat;

import egframe.common.SysChat.AbstractCollaborationManager;
import egframe.common.SysChat.CollaborationEngine;
import egframe.common.SysChat.CollaborationEngineServiceInitListener;
import egframe.common.SysChat.CollaborationList;
import egframe.common.SysChat.CollaborationMessage;
import egframe.common.SysChat.CollaborationMessagePersister;
import egframe.common.SysChat.CollaborationMessagePersister.FetchQuery;
import egframe.common.SysChat.CollaborationMessagePersister.PersistRequest;
import egframe.common.SysChat.ComponentConnectionContext;
import egframe.common.SysChat.ConnectionContext;
import egframe.common.SysChat.ListChangeEvent;
import egframe.common.SysChat.ListKey;
import egframe.common.SysChat.ListOperation;
import egframe.common.SysChat.ListOperationResult;
import egframe.common.SysChat.MessageHandler;
import egframe.common.SysChat.MessageHandler.MessageContext;
import egframe.common.SysChat.TopicConnection;
import egframe.frame.entity.sys_user;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.function.SerializableSupplier;
import com.vaadin.flow.internal.UsageStatistics;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.shared.Registration;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;
public class MessageManager extends AbstractCollaborationManager {
    static {
        UsageStatistics.markAsUsed(
                CollaborationEngine.COLLABORATION_ENGINE_NAME
                        + "/MessageManager",
                CollaborationEngine.COLLABORATION_ENGINE_VERSION);
    }
    private static final Object FETCH_LOCK = new Object();
    private static final String MISSING_RECENT_MESSAGES = "The messages "
            + "returned invoking CollaborationMessagePersister.fetchMessages() "
            + "do not include the last fetched message of the previous call. "
            + "Please update the implementation to fetch all messages whose "
            + "timestamp is greater OR EQUAL with the query's timestamp.";
    static final String LIST_NAME = MessageManager.class.getName();
    private final CollaborationMessagePersister persister;
    private ConnectionContext context;
    private transient CollaborationList list;
    private MessageHandler messageHandler;
    private CollaborationMessage lastSeenMessage;
    private ListKey lastMessageKey;
    private boolean catchupMode = false;
    private final Map<CompletableFuture<Void>, CollaborationMessage> pendingMessageFutures = new LinkedHashMap<>();
    private final Map<CollaborationMessage, CompletableFuture<Void>> persistedMessageFutures = new LinkedHashMap<>();
    private static MessageManager instance; 
    public List<String> topicUsers = new ArrayList();
    public MessageManager(Component component, sys_user localUser,
            String topicId) {
        this(component, localUser, topicId, null);
    }
    public MessageManager(Component component, sys_user localUser,
            String topicId, CollaborationMessagePersister persister) {
        this(new ComponentConnectionContext(component), localUser, topicId,
                persister, CollaborationEngine::getInstance);
    }
    @Deprecated(since = "6.1", forRemoval = true)
    public MessageManager(ConnectionContext context, sys_user localUser,
            String topicId, CollaborationEngine collaborationEngine) {
        this(context, localUser, topicId, null, () -> collaborationEngine);
    }
    public MessageManager(ConnectionContext context, sys_user localUser,
            String topicId,
            SerializableSupplier<CollaborationEngine> ceSupplier) {
        this(context, localUser, topicId, null, ceSupplier);
    }
    @Deprecated(since = "6.1", forRemoval = true)
    public MessageManager(ConnectionContext context, sys_user localUser,
            String topicId, CollaborationMessagePersister persister,
            CollaborationEngine collaborationEngine) {
        this(context, localUser, topicId, persister, () -> collaborationEngine);
    }
    public MessageManager(ConnectionContext context, sys_user localUser,
            String topicId, CollaborationMessagePersister persister,
            SerializableSupplier<CollaborationEngine> ceSupplier) {
        super(localUser, topicId, ceSupplier);
        this.context = context;
        this.persister = persister;
        openTopicConnection(context, this::onConnectionActivate);
    	topicUsers.add(localUser.getUserNm());  
    }
    @Serial
    private void readObject(ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        CollaborationEngineServiceInitListener
                .addReinitializer(this::reinitialize);
    }
    private void reinitialize(VaadinService service) {
        openTopicConnection(context, this::onConnectionActivate);
    }
    public void setMessageHandler(MessageHandler handler) {
        messageHandler = handler;
        lastSeenMessage = null;
        catchupMode = false;
        if (messageHandler != null) {
            getMessages().forEach(this::applyHandler);
        }
    }
    public CompletableFuture<Void> submit(String text) {
        Objects.requireNonNull(text);
        sys_user user = getLocalUser();
        Instant now = getCollaborationEngine().getClock().instant();
        CollaborationMessage message = new CollaborationMessage(user, text,
                now);
        return submit(message);
    }
    public CompletableFuture<Void> submit(CollaborationMessage message) {
        Objects.requireNonNull(message);
        if (list == null) {
            CompletableFuture<Void> future = new CompletableFuture<>();
            pendingMessageFutures.put(future, message);
            return future;
        } else {
            return appendOrPersist(message);
        }
    }

    private CompletableFuture<Void> appendOrPersist(
            CollaborationMessage message) {
        if (persister != null) {
            String topicId = getTopicId();
            PersistRequest request = new PersistRequest(this, topicId, message);
            persister.persistMessage(request);
            CompletableFuture<Void> future = new CompletableFuture<>();
            persistedMessageFutures.put(message, future);
            fetchPersistedList();
            return future;
        } else {
            return list.insertLast(message).getCompletableFuture();
        }
    }

    private Registration onConnectionActivate(TopicConnection topicConnection) {
        list = topicConnection.getNamedList(LIST_NAME);
        list.subscribe(this::onListChange);
        fetchPersistedList();
        pendingMessageFutures.entrySet().removeIf(entry -> {
            CompletableFuture<Void> future = entry.getKey();
            CollaborationMessage message = entry.getValue();
            appendOrPersist(message).whenComplete((result, throwable) -> {
                if (throwable != null) {
                    future.completeExceptionally(throwable);
                } else {
                    future.complete(result);
                }
            });
            return true;
        });
        return this::onConnectionDeactivate;
    }

    private void onConnectionDeactivate() {
        list = null;
        catchupMode = true;
    }

    private void onListChange(ListChangeEvent event) {
        CollaborationMessage message = event
                .getValue(CollaborationMessage.class);
        lastMessageKey = event.getKey();
        if (message != null) {
            CompletableFuture<Void> future = persistedMessageFutures
                    .remove(message);
            if (future != null) {
                future.complete(null);
            }
            applyHandler(message);
        }
    }

    private void applyHandler(CollaborationMessage message) {
        if (!catchupMode) {
            lastSeenMessage = message;
            if (messageHandler != null) {
                MessageContext context = new DefaultMessageContext(message);
                messageHandler.handleMessage(context);
            }
        } else if (message.equals(lastSeenMessage)) {
            catchupMode = false;
        }
    }

    private void fetchPersistedList() {
        if (persister != null && list != null) {
            String topicId = getTopicId();
            synchronized (FETCH_LOCK) {
                List<CollaborationMessage> recentMessages = getRecentMessages();
                Instant since = recentMessages.isEmpty() ? Instant.EPOCH
                        : recentMessages.get(0).getTime();
                FetchQuery query = new FetchQuery(this, topicId, since);
                List<CollaborationMessage> messages = persister
                        .fetchMessages(query)
                        .sorted(Comparator
                                .comparing(CollaborationMessage::getTime))
                        .filter(message -> !recentMessages.remove(message))
                        .collect(Collectors.toList());
                if (!recentMessages.isEmpty()) {
                    throw new IllegalStateException(MISSING_RECENT_MESSAGES);
                }
                if (!messages.isEmpty()) {
                    query.throwIfPropsNotUsed();
                    insertPersistedMessages(messages);
                }
            }
        }
    }

    private void insertPersistedMessages(List<CollaborationMessage> messages) {
        ListKey ifLast = lastMessageKey;
        List<CompletableFuture<Boolean>> futures = new ArrayList<>();
        for (CollaborationMessage message : messages) {
            ListOperation op = ListOperation.insertLast(message);
            if (ifLast != null) {
                op.ifLast(ifLast);
            } else {
                op.ifEmpty();
            }
            ListOperationResult<Boolean> insert = list.apply(op);
            futures.add(insert.getCompletableFuture());
            ifLast = insert.getKey();
        }
        CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new))
                .thenAccept(result -> fetchPersistedList());
    }

    private List<CollaborationMessage> getRecentMessages() {
        List<CollaborationMessage> messages = getMessages()
                .collect(Collectors.toList());
        CollaborationMessage lastMessage = messages.isEmpty() ? null
                : messages.get(messages.size() - 1);
        List<CollaborationMessage> recentMessages = new ArrayList<>();
        if (lastMessage != null) {
            Instant lastMessageTime = lastMessage.getTime();
            for (int i = messages.size() - 1; i >= 0; i--) {
                CollaborationMessage m = messages.get(i);
                if (m.getTime().equals(lastMessageTime)) {
                    recentMessages.add(m);
                } else {
                    break;
                }
            }
        }
        return recentMessages;
    }
    // 토픽에 연결된 모든 사용자 반환
    public List<String> getTopicUsers() {
        return topicUsers; // 토픽에 연결된 사용자 목록
    }
    // Package protected for testing
    Stream<CollaborationMessage> getMessages() {
        if (list != null) {
            return list.getItems(CollaborationMessage.class).stream();
        } else {
            return Stream.empty();
        }
    }

    static class DefaultMessageContext implements MessageContext {

        private final CollaborationMessage message;

        public DefaultMessageContext(CollaborationMessage message) {
            this.message = message;
        }

        @Override
        public CollaborationMessage getMessage() {
            return message;
        }
    }
}
