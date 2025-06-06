/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.vaadin.flow.function.SerializableFunction
 *  com.vaadin.flow.function.SerializableSupplier
 *  com.vaadin.flow.shared.Registration
 */
package egframe.common.SysChat;

import egframe.common.SysChat.CollaborationEngine;
import egframe.common.SysChat.ConnectionContext;
import egframe.common.SysChat.TopicConnection;
import egframe.common.SysChat.TopicConnectionRegistration;
import egframe.common.SysChat.TopicConnectionRegistration.ConnectionFailedAction;
import egframe.frame.entity.sys_user;

import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.function.SerializableSupplier;
import com.vaadin.flow.shared.Registration;
import java.io.Serializable;
import java.util.Objects;

public abstract class AbstractCollaborationManager implements Serializable {

    /**
     * The callback executed when the manager is activated, i.e. when the
     * connection to the topic is established.
     *
     * @see AbstractCollaborationManager#setActivationHandler(ActivationHandler)
     */
    @FunctionalInterface
    public interface ActivationHandler extends Serializable {

        /**
         * The method executed when the manager is activated. The method might
         * return a callback which will be invoked when the manager is
         * deactivated, i.e. the connection to the topic is closed. This
         * callback can be used to clean-up resources used during activation.
         *
         * @return a callback which will be executed when the manager is
         *         deactivated, or {@code null} if not needed
         */
        Registration onActivation();
    }

    private final SerializableSupplier<CollaborationEngine> ceSupplier;

    private final sys_user localUser;

    private final String topicId;

    private transient TopicConnectionRegistration topicRegistration;

    private ActivationHandler activationHandler;

    private Registration deactivationHandler;

    private transient ConnectionFailedAction connectionFailedAction;

    private boolean active;

    /**
     * Constructs a new manager instance.
     *
     * @param localUser
     *            the local user, not {@code null}
     * @param topicId
     *            the topic id, not {@code null}
     * @param collaborationEngine
     *            the Collaboration Engine instance, not {@code null}
     * @deprecated This constructor is not compatible with serialization
     */
    @Deprecated(since = "6.1", forRemoval = true)
    protected AbstractCollaborationManager(sys_user localUser, String topicId,
            CollaborationEngine collaborationEngine) {
        this(localUser, topicId, () -> collaborationEngine);
    }

    /**
     * Constructs a new manager instance.
     *
     * @param localUser
     *            the local user, not {@code null}
     * @param topicId
     *            the topic id, not {@code null}
     * @param ceSupplier
     *            the Collaboration Engine instance, not {@code null}
     */
    protected AbstractCollaborationManager(sys_user localUser, String topicId,
            SerializableSupplier<CollaborationEngine> ceSupplier) {
        this.localUser = Objects.requireNonNull(localUser);
        this.topicId = Objects.requireNonNull(topicId);
        this.ceSupplier = Objects.requireNonNull(ceSupplier);
    }

    /**
     * Opens a connection to the topic of this manager using the provided
     * context and activation callback.
     *
     * @param context
     *            the connection context, not {@code null}
     * @param connectionActivationCallback
     *            the callback to be executed when a connection is activated,
     *            not {@code null}
     */
    protected void openTopicConnection(ConnectionContext context,
            SerializableFunction<TopicConnection, Registration> connectionActivationCallback) {
        if (topicRegistration != null) {
            topicRegistration.remove();
        }
        topicRegistration = getCollaborationEngine().openTopicConnection(
                context, topicId, localUser, connection -> {
                    active = true;
                    Registration callbackRegistration = connectionActivationCallback
                            .apply(connection);
                    if (activationHandler != null) {
                        deactivationHandler = activationHandler.onActivation();
                    }
                    return callbackRegistration != null
                            ? Registration.combine(callbackRegistration,
                                    this::onTopicRegistrationRemove)
                            : this::onTopicRegistrationRemove;
                });
        if (connectionFailedAction != null) {
            topicRegistration.onConnectionFailed(connectionFailedAction);
        }
    }

    /**
     * Sets a handler that will be executed when the manager is activated, i.e.
     * the connection to the topic is established.
     *
     * @param handler
     *            the handler, {@code null} to remove an existing handler
     */
    public void setActivationHandler(ActivationHandler handler) {
        if (deactivationHandler != null) {
            deactivationHandler.remove();
            deactivationHandler = null;
        }
        activationHandler = handler;
        if (active && activationHandler != null) {
            deactivationHandler = activationHandler.onActivation();
        }
    }

    /**
     * Adds an action to be executed if the topic connection fails. The
     * connection can fail in production mode if your Collaboration Engine
     * license has expired, or if the number of unique monthly end users has
     * exceeded the quota in your license.
     * <p>
     * If the connection has already failed when calling this method, the action
     * runs immediately.
     *
     * @param connectionFailedAction
     *            the action to handle topic connection failure, or {@code null}
     *            to remove an existing action
     */
    public void onConnectionFailed(
            ConnectionFailedAction connectionFailedAction) {
        this.connectionFailedAction = connectionFailedAction;
        if (this.connectionFailedAction != null && topicRegistration != null) {
            topicRegistration.onConnectionFailed(this.connectionFailedAction);
        }
    }

    private void onTopicRegistrationRemove() {
        if (deactivationHandler != null) {
            deactivationHandler.remove();
        }
        active = false;
    }

    /**
     * Gets the {@link CollaborationEngine} of this manager.
     *
     * @return the {@link CollaborationEngine}, not {@code null}
     */
    protected CollaborationEngine getCollaborationEngine() {
        return ceSupplier.get();
    }

    /**
     * Gets the local user of this manager.
     *
     * @return the local user, not {@code null}
     */
    public sys_user getLocalUser() {
        return localUser;
    }

    /**
     * Gets the topic id of this manager.
     *
     * @return the topic id, not {@code null}
     */
    public String getTopicId() {
        return topicId;
    }

    /**
     * Closes the manager connection to the topic. This is typically handled
     * automatically by the connection-context and in most cases is not needed
     * to be done explicitly, e.g. with {@link ComponentConnectionContext} when
     * the component is detached.
     */
    public void close() {
        if (topicRegistration != null) {
            topicRegistration.remove();
            topicRegistration = null;
        }
    }
}
