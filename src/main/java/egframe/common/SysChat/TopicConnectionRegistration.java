/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.vaadin.flow.function.SerializableConsumer
 *  com.vaadin.flow.server.Command
 *  com.vaadin.flow.shared.Registration
 */
package egframe.common.SysChat;

import egframe.common.SysChat.ActivationHandler;
import egframe.common.SysChat.ConnectionContext;
import egframe.common.SysChat.SerializableExecutor;
import egframe.common.SysChat.SingleUseActivationHandler;
import egframe.common.SysChat.TopicConnection;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.server.Command;
import com.vaadin.flow.shared.Registration;
import java.io.Serializable;
import java.util.EventObject;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class TopicConnectionRegistration
implements Registration {
    private final AtomicReference<TopicConnection> topicConnectionReference;
    private ConnectionContext connectionContext;
    private SerializableExecutor executor;
    private CompletableFuture<Void> pendingFuture;
    private final SerializableConsumer<TopicConnectionRegistration> afterDisconnection;

    TopicConnectionRegistration(TopicConnection topicConnection, ConnectionContext connectionContext, SerializableExecutor executor, SerializableConsumer<TopicConnectionRegistration> afterDisconnection) {
        this.topicConnectionReference = new AtomicReference<TopicConnection>(topicConnection);
        this.connectionContext = connectionContext;
        this.executor = executor;
        this.afterDisconnection = afterDisconnection;
    }

    public void remove() {
        TopicConnection topicConnection = this.topicConnectionReference.getAndSet(null);
        if (topicConnection != null) {
            this.pendingFuture = topicConnection.deactivateAndClose();
            this.pendingFuture.thenRun(() -> {
                this.pendingFuture = null;
                //origin
                //this.afterDisconnection.accept((Object)this);
                this.afterDisconnection.accept(this);
            });
        }
        this.connectionContext = null;
        this.executor = null;
    }

    Optional<CompletableFuture<Void>> getPendingFuture() {
        return Optional.ofNullable(this.pendingFuture);
    }

    public void onConnectionFailed(ConnectionFailedAction connectionFailedAction) {
        Objects.requireNonNull(connectionFailedAction, "The connection failed action can't be null");
        if (this.topicConnectionReference.get() == null) {
            this.connectionContext.init(new SingleUseActivationHandler((ActivationHandler & Serializable)actionDispatcher -> {
                ConnectionFailedEvent event = new ConnectionFailedEvent(this);
                actionDispatcher.dispatchAction((Command & Serializable)() -> connectionFailedAction.onConnectionFailed(event));
            }), this.executor);
        }
    }

    @FunctionalInterface
    public static interface ConnectionFailedAction
    extends Serializable {
        public void onConnectionFailed(ConnectionFailedEvent var1);
    }

    public static class ConnectionFailedEvent
    extends EventObject {
        ConnectionFailedEvent(TopicConnectionRegistration source) {
            super(source);
        }

        @Override
        public TopicConnectionRegistration getSource() {
            return (TopicConnectionRegistration)super.getSource();
        }
    }
}
