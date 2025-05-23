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
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package egframe.common.SysChat;

import egframe.common.SysChat.AbstractCollaborationManager;
import egframe.common.SysChat.CollaborationEngine;
import egframe.common.SysChat.CollaborationEngineServiceInitListener;
import egframe.common.SysChat.CollaborationList;
import egframe.common.SysChat.ComponentConnectionContext;
import egframe.common.SysChat.ConnectionContext;
import egframe.common.SysChat.EntryScope;
import egframe.common.SysChat.ListChangeEvent;
import egframe.common.SysChat.ListKey;
import egframe.common.SysChat.ListOperation;
import egframe.common.SysChat.PresenceHandler;
import egframe.common.SysChat.PresenceHandler.PresenceContext;
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
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PresenceManager extends AbstractCollaborationManager {

    static {
        UsageStatistics.markAsUsed(
                CollaborationEngine.COLLABORATION_ENGINE_NAME
                        + "/PresenceManager",
                CollaborationEngine.COLLABORATION_ENGINE_VERSION);
    }

    static final Logger LOGGER = LoggerFactory.getLogger(PresenceManager.class);

    private static class UserEntry implements Serializable {
        private int count = 0;
        private Registration registration;
    }

    static final String LIST_NAME = PresenceManager.class.getName();

    private final Map<String, UserEntry> userEntries = new LinkedHashMap<>();

    private ListKey ownPresenceKey;

    private ConnectionContext context;

    private transient CollaborationList list;

    private PresenceHandler presenceHandler;

    private boolean markAsPresent = false;

    private transient Registration subscribeRegistration;

    /**
     * Creates a new manager for the provided component.
     * <p>
     * The provided user information is used to set the presence of the local
     * user with {@link #markAsPresent(boolean)} (the default is {@code false}).
     * <p>
     *
     * @param component
     *            the component which holds UI access, not {@code null}
     * @param localUser
     *            the information of the local user, not {@code null}
     * @param topicId
     *            the id of the topic to connect to, not {@code null}
     */
    public PresenceManager(Component component, sys_user localUser,
            String topicId) {
        this(new ComponentConnectionContext(component), localUser, topicId,
                CollaborationEngine::getInstance);
    }

    /**
     * Creates a new manager for the provided connection context.
     * <p>
     * The provided user information is used to set the presence of the local
     * user with {@link #markAsPresent(boolean)} (the default is {@code false}).
     * <p>
     *
     * @param context
     *            the context that manages connection status, not {@code null}
     * @param localUser
     *            the information of the local user, not {@code null}
     * @param topicId
     *            the id of the topic to connect to, not {@code null}
     * @param collaborationEngine
     *            the collaboration engine instance to use, not {@code null}
     * @deprecated This constructor is not compatible with serialization
     */
    @Deprecated(since = "6.1", forRemoval = true)
    public PresenceManager(ConnectionContext context, sys_user localUser,
            String topicId, CollaborationEngine collaborationEngine) {
        this(context, localUser, topicId, () -> collaborationEngine);
    }

    /**
     * Creates a new manager for the provided connection context.
     * <p>
     * The provided user information is used to set the presence of the local
     * user with {@link #markAsPresent(boolean)} (the default is {@code false}).
     * <p>
     *
     * @param context
     *            the context that manages connection status, not {@code null}
     * @param localUser
     *            the information of the local user, not {@code null}
     * @param topicId
     *            the id of the topic to connect to, not {@code null}
     * @param ceSupplier
     *            the collaboration engine instance to use, not {@code null}
     */
    public PresenceManager(ConnectionContext context, sys_user localUser,
            String topicId,
            SerializableSupplier<CollaborationEngine> ceSupplier) {
        super(localUser, topicId, ceSupplier);
        this.context = context;
        openTopicConnection(context, this::onConnectionActivate);
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

    /**
     * Configures the manager to mark the local user present in the topic.
     * <p>
     * If the user wasn't already present in the topic, all managers connected
     * to the same topic will be notified of the change and their handlers will
     * be applied to the user instance.
     *
     * @param markAsPresent
     *            {@code true} to mark the user as present in the topic,
     *            {@code false} to set as not present
     */
    public void markAsPresent(boolean markAsPresent) {
        if (this.markAsPresent != markAsPresent && list != null) {
            if (markAsPresent) {
                addLocalUserToTopic();
            } else {
                removeLocalUserFromTopic();
            }
        }
        this.markAsPresent = markAsPresent;
    }

    private void addLocalUserToTopic() {
        if (ownPresenceKey != null) {
            throw new IllegalArgumentException("userEntry must not be null");
        }        
        ListOperation operation = ListOperation.insertLast(getLocalUser())
                .withScope(EntryScope.CONNECTION);
        ownPresenceKey = list.apply(operation).getKey();
    }

    private void removeLocalUserFromTopic() {
        if (ownPresenceKey == null) {
            throw new IllegalArgumentException("userEntry must not be null");
        }        
        
        list.set(ownPresenceKey, null);
        ownPresenceKey = null;
    }

    /**
     * Sets a handler which will be invoked when a user becomes present.
     * <p>
     * The handler accepts a {@link PresenceContext} instance as a parameter and
     * should return a {@link Registration} which will be removed when the user
     * stops being present.
     * <p>
     * Replacing an existing handler will remove all registrations from the
     * previous one.
     *
     * @param handler
     *            the user presence handler, or {@code null} to remove an
     *            existing handler
     */
    public void setPresenceHandler(PresenceHandler handler) {
        resetEntries();
        this.presenceHandler = handler;
        if (handler != null && list != null) {
            subscribeRegistration = list.subscribe(this::onListChange);
        }
    }

    private Registration onConnectionActivate(TopicConnection topicConnection) {
        list = topicConnection.getNamedList(LIST_NAME);
        if (markAsPresent) {
            addLocalUserToTopic();
        }
        if (this.presenceHandler != null && subscribeRegistration == null) {
            subscribeRegistration = list.subscribe(this::onListChange);
        }
        return this::onConnectionDeactivate;
    }

    private void onConnectionDeactivate() {
        ownPresenceKey = null;
        list = null;
        resetEntries();
    }

    private void onListChange(ListChangeEvent event) {
        switch (event.getType()) {
        case INSERT:
            handleNewUser(event.getValue(sys_user.class));
            break;
        case SET:
            if (event.getValue(sys_user.class) == null) {
                handleRemovedUser(event.getOldValue(sys_user.class));
            } else {
                throw new UnsupportedOperationException(
                        "Cannot update an existing entry");
            }
            break;
        case MOVE:
            // Unexpected, but no problem in ignoring
            break;
        }
    }

    private void handleRemovedUser(sys_user removedUser) {
        UserEntry userEntry = userEntries.get(removedUser.getId());
        logUserOperation("remove", removedUser, userEntry != null);
        if (userEntry == null) {
            throw new IllegalArgumentException("userEntry must not be null");
        }        
         
        if (--userEntry.count == 0) {
            removeRegistration(userEntry);
            userEntries.remove(removedUser.getId());
        }
    }

    private void logUserOperation(String operation, sys_user userInfo,
            boolean present) {
        LOGGER.debug("{}: handle {} user {} ({}): {}present", this, operation,
                userInfo.getUserNm(), userInfo.getUserCd(), !present ? "not " : "");
    }

    private void handleNewUser(sys_user addedUser) {
        UserEntry userEntry = userEntries.computeIfAbsent(addedUser.getUserCd(),
                ignore -> new UserEntry());
        logUserOperation("add", addedUser, userEntry.count == 0);
        if (userEntry.count++ == 0) {
            if (presenceHandler != null) {
                if (userEntry.registration != null) {
                    throw new IllegalArgumentException("userEntry must not be null");
                }        
               
                userEntry.registration = presenceHandler
                        .handlePresence(new DefaultPresenceContext(addedUser));
            }
        }
    }

    private void removeRegistration(UserEntry entry) {
        Registration registration = entry.registration;
        if (registration != null) {
            registration.remove();
            entry.registration = null;
        }
    }

    private void resetEntries() {
        if (subscribeRegistration != null) {
            subscribeRegistration.remove();
            subscribeRegistration = null;
        }

        userEntries.values().forEach(this::removeRegistration);
        LOGGER.debug("{}: clear user entries", this);
        userEntries.clear();
    }

    static class DefaultPresenceContext implements PresenceContext {

        private final sys_user user;

        public DefaultPresenceContext(sys_user user) {
            this.user = user;
        }

        @Override
        public sys_user getUser() {
            return user;
        }
    }
}
