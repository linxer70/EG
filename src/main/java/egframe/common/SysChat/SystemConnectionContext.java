/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.vaadin.flow.function.SerializableSupplier
 *  com.vaadin.flow.internal.CurrentInstance
 *  com.vaadin.flow.server.Command
 *  com.vaadin.flow.server.ServiceDestroyListener
 *  com.vaadin.flow.server.VaadinService
 *  com.vaadin.flow.shared.Registration
 */
package egframe.common.SysChat;

import egframe.common.SysChat.ActionDispatcher;
import egframe.common.SysChat.ActivationHandler;
import egframe.common.SysChat.AsyncRegistration;
import egframe.common.SysChat.CollaborationEngine;
import egframe.common.SysChat.ConnectionContext;
import egframe.common.SysChat.ExecutionQueue;

import com.vaadin.flow.function.SerializableSupplier;
import com.vaadin.flow.internal.CurrentInstance;
import com.vaadin.flow.server.Command;
import com.vaadin.flow.server.ServiceDestroyListener;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.shared.Registration;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class SystemConnectionContext implements ConnectionContext {

    private final class ActionDispatcherImplementation
            implements ActionDispatcher {
        private final Executor executor;
        private final ExecutionQueue inbox = new ExecutionQueue();
        private final CompletableFuture<Void> shutdownFuture = new CompletableFuture<>();
        private boolean shuttingDown;

        private ActionDispatcherImplementation(Executor executor) {
            this.executor = executor;
        }

        @Override
        public void dispatchAction(Command action) {
            inbox.add(() -> {
                Map<Class<?>, CurrentInstance> oldInstances = CurrentInstance
                        .getInstances();
                try {
                    VaadinService.setCurrent(
                            getCollaborationEngine().getVaadinService());
                    action.execute();
                } finally {
                    CurrentInstance.restoreInstances(oldInstances);
                }
            });
            executor.execute(() -> {
                synchronized (this) {
                    inbox.runPendingCommands();
                    if (shuttingDown) {
                        shutdownFuture.complete(null);
                        shuttingDown = false;
                    }
                }
            });
        }

        @Override
        public <T> CompletableFuture<T> createCompletableFuture() {
            return new CompletableFuture<>();
        }

        synchronized private void shutdown() {
            if (inbox.isEmpty()) {
                shutdownFuture.complete(null);
            } else {
                shuttingDown = true;
            }
        }
    }

    private final SerializableSupplier<CollaborationEngine> ceSupplier;

    // Active handlers to deactivate if the service is destroyed
    private final Set<ActivationHandler> activeHandlers = new HashSet<>();

    private Registration serviceDestroyRegistration;

    /**
     * Creates a new system connection context instance for the given
     * Collaboration Engine instance. It is typically recommended reusing an
     * existing instance through {@link #getInstance()} or
     * {@link CollaborationEngine#getSystemContext()} rather than creating new
     * instances.
     *
     * @param ce
     *            the collaboration engine instance to use, not
     *            <code>null</code>
     * @deprecated This constructor is not compatible with serialization
     */
    @Deprecated(since = "6.2", forRemoval = true)
    public SystemConnectionContext(CollaborationEngine ce) {
        this(() -> ce);
    }

    /**
     * Creates a new system connection context instance for the given
     * Collaboration Engine instance. It is typically recommended reusing an
     * existing instance through {@link #getInstance()} or
     * {@link CollaborationEngine#getSystemContext()} rather than creating new
     * instances.
     *
     * @param ceSupplier
     *            the collaboration engine instance to use, not
     *            <code>null</code>
     */
    public SystemConnectionContext(
            SerializableSupplier<CollaborationEngine> ceSupplier) {
        this.ceSupplier = Objects.requireNonNull(ceSupplier);
    }

    /**
     * Gets the system connection context associated with the current
     * Collaboration Engine instance. This method can be used only when
     * {@link CollaborationEngine#getInstance()} is available.
     *
     * @return a system connection context instance, not <code>null</code>
     * @throws IllegalStateException
     *             in case no current collaboration engine instance is available
     */
    public static SystemConnectionContext getInstance() {
        CollaborationEngine ce = CollaborationEngine.getInstance();
        if (ce == null) {
            throw new IllegalStateException(
                    "This method cannot be used when CollaborationEngine has not been configured for the current VaadinService.");
        }
        return ce.getSystemContext();
    }

    @Override
    public Registration init(ActivationHandler activationHandler,
            Executor executor) {
        Objects.requireNonNull(activationHandler);
        Objects.requireNonNull(executor);

        synchronized (activeHandlers) {
            if (activeHandlers.isEmpty()) {
                serviceDestroyRegistration = ceSupplier.get().getVaadinService()
                        .addServiceDestroyListener(e -> {
                            synchronized (activeHandlers) {
                                activeHandlers.forEach(
                                        handler -> handler.accept(null));
                                activeHandlers.clear();
                            }
                        });
            }

            if (!activeHandlers.add(activationHandler)) {
                throw new IllegalStateException(
                        "The provided activation handler was already active");
            }

            ActionDispatcherImplementation actionDispatcher = new ActionDispatcherImplementation(
                    executor);
            activationHandler.accept(actionDispatcher);

            return new AsyncRegistration(actionDispatcher.shutdownFuture,
                    () -> {
                        synchronized (activeHandlers) {
                            if (activeHandlers.remove(activationHandler)) {
                                activationHandler.accept(null);
                                actionDispatcher.shutdown();

                                if (activeHandlers.isEmpty()) {
                                    serviceDestroyRegistration.remove();
                                    serviceDestroyRegistration = null;
                                }
                            }
                        }
                    });
        }
    }

    // For testing
    CollaborationEngine getCollaborationEngine() {
        return ceSupplier.get();
    }
}
