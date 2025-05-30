/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.vaadin.flow.component.Component
 *  com.vaadin.flow.component.ComponentEventListener
 *  com.vaadin.flow.component.UI
 *  com.vaadin.flow.component.internal.DeadlockDetectingCompletableFuture
 *  com.vaadin.flow.server.Command
 *  com.vaadin.flow.server.ServiceDestroyListener
 *  com.vaadin.flow.server.VaadinService
 *  com.vaadin.flow.server.VaadinSession
 *  com.vaadin.flow.server.Version
 *  com.vaadin.flow.shared.Registration
 *  com.vaadin.flow.shared.communication.PushMode
 *  org.slf4j.LoggerFactory
 */
package egframe.common.SysChat;

import egframe.common.SysChat.ActionDispatcher;
import egframe.common.SysChat.ActivationHandler;
import egframe.common.SysChat.AsyncRegistration;
import egframe.common.SysChat.BeaconHandler;
import egframe.common.SysChat.CollaborationEngine;
import egframe.common.SysChat.CollaborationEngineServiceInitListener;
import egframe.common.SysChat.ConnectionContext;
import egframe.common.SysChat.ExecutionQueue;
import egframe.common.SysChat.ServiceDestroyDelegate;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.internal.DeadlockDetectingCompletableFuture;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.Command;
import com.vaadin.flow.server.ServiceDestroyListener;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.Version;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.shared.communication.PushMode;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import org.slf4j.LoggerFactory;

public class ComponentConnectionContext implements ConnectionContext {

    private enum State {
        /**
         * Fully active. ui has been set.
         */
        ACTIVE,
        /**
         * Inactivation has been initiated. ui is still set, but will be cleared
         * as soon as the inbox is purged.
         */
        INACTIVATING,
        /**
         * Fully inactive. ui has been cleared.
         */
        INACTIVE
    }

    private final Map<Component, Registration> componentListeners = new HashMap<>();
    private final Set<Component> attachedComponents = new HashSet<>();

    private volatile UI ui;

    private transient ExecutionQueue inbox = new ExecutionQueue();
    private transient ExecutionQueue shutdownCommands = new ExecutionQueue();
    private final ActionDispatcher actionDispatcher = new ActionDispatcherImpl();
    private final AtomicReference<State> state = new AtomicReference<>(
            State.INACTIVE);
    private transient Consumer<ActionDispatcher> activationHandler;
    private transient Executor backgroundRunner;
    private transient Registration beaconListener;
    private transient Registration destroyListener;

    private static AtomicBoolean pushWarningShown = new AtomicBoolean(false);

    /**
     * Creates an empty component connection context.
     *
     * @since 1.0
     */
    public ComponentConnectionContext() {
        // Nothing to do here
    }

    /**
     * Creates a new component connection context which is initially using a
     * single component.
     *
     * @param component
     *            the component to use, not <code>null</code>
     *
     * @since 1.0
     */
    public ComponentConnectionContext(Component component) {
        addComponent(component);
    }

    @Serial
    private void readObject(ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        inbox = new ExecutionQueue();
        shutdownCommands = new ExecutionQueue();
        CollaborationEngineServiceInitListener
                .addReinitializer(this::reinitialize);
    }

    private void reinitialize(VaadinService service) {
        attachedComponents.stream().findFirst().flatMap(Component::getUI)
                .ifPresent(this::attach);
    }

    /**
     * Adds a component instance to track for this context. Calling this method
     * again with a component that is already tracked has no effect.
     *
     * @param component
     *            the component to track, not <code>null</code>
     * @see #removeComponent(Component)
     *
     * @since 1.0
     */
    public void addComponent(Component component) {
        Objects.requireNonNull(component, "Component can't be null.");

        if (!componentListeners.containsKey(component)) {
            Registration attachRegistration = component.addAttachListener(
                    event -> markAsAttached(event.getUI(), event.getSource()));
            Registration detachRegistration = component.addDetachListener(
                    event -> markAsDetached(event.getSource()));

            componentListeners.put(component, Registration
                    .combine(attachRegistration, detachRegistration));

            component.getUI().ifPresent(
                    componentUi -> markAsAttached(componentUi, component));
        }
    }

    /**
     * Stops tracking a component for this context. Calling this method for a
     * component that isn't tracked has no effect.
     *
     * @param component
     *            the component to stop tracking, not <code>null</code>
     * @see #addComponent(Component)
     *
     * @since 1.0
     */
    public void removeComponent(Component component) {
        Objects.requireNonNull(component, "Component can't be null.");

        Registration registration = componentListeners.remove(component);
        if (registration != null) {
            registration.remove();
            markAsDetached(component);
        }
    }

    private void markAsAttached(UI componentUi, Component component) {
        if (attachedComponents.add(component)) {
            if (attachedComponents.size() == 1) {
                // First attach
                attach(componentUi);
            } else if (componentUi != ui) {
                throw new IllegalStateException(
                        "All components in this connection context must be associated with the same UI.");
            }
        }
    }

    private void attach(UI componentUi) {
        this.ui = componentUi;
        checkForPush(ui);

        String beaconPath = CollaborationEngine
                .getInstance(ui.getSession().getService()).getConfiguration()
                .getBeaconPathProperty();
        BeaconHandler beaconHandler = BeaconHandler.ensureInstalled(this.ui,
                beaconPath);
        beaconListener = beaconHandler.addListener(this::deactivateConnection);

        ServiceDestroyDelegate destroyDelegate = ServiceDestroyDelegate
                .ensureInstalled(this.ui);
        destroyListener = destroyDelegate
                .addListener(event -> deactivateConnection());

        flushPendingActionsIfActive();

        if (activationHandler != null) {
            activate();
        }
    }

    private void markAsDetached(Component component) {
        if (attachedComponents.remove(component)) {
            if (attachedComponents.isEmpty()) {
                // Last detach
                deactivateConnection();
            }
        }
    }

    @Override
    public Registration init(ActivationHandler activationHandler,
            Executor backgroundRunner) {
        if (this.activationHandler != null) {
            throw new IllegalStateException(
                    "This context has already been initialized");
        }
        this.activationHandler = Objects.requireNonNull(activationHandler,
                "Activation handler cannot be null");
        this.backgroundRunner = Objects.requireNonNull(backgroundRunner,
                "Background runner cannot be null");

        if (this.ui != null) {
            activate();
        }
        CompletableFuture<Void> deactivationFuture = new CompletableFuture<>();
        return new AsyncRegistration(deactivationFuture, () -> {
            // This instance won't be used again, release all references
            if (state.get() == State.INACTIVE) {
                // If already inactive, complete now
                deactivationFuture.complete(null);
            } else {
                // Otherwise, complete when it becomes inactive
                shutdownCommands.add(() -> deactivationFuture.complete(null));
            }

            componentListeners.values().forEach(Registration::remove);
            componentListeners.clear();
            attachedComponents.clear();
            deactivateConnection();
        });
    }

    private void activate() {
        if (this.activationHandler != null
                && state.getAndSet(State.ACTIVE) != State.ACTIVE) {
            this.activationHandler.accept(this.actionDispatcher);
        }
    }

    private void deactivateConnection() {
        if (beaconListener != null) {
            beaconListener.remove();
            beaconListener = null;
        }
        if (destroyListener != null) {
            destroyListener.remove();
            destroyListener = null;
        }
        if (activationHandler != null && ui != null
                && state.compareAndSet(State.ACTIVE, State.INACTIVATING)) {
            activationHandler.accept(null);
            if (inbox.isEmpty()) {
                inactivateIfDeactivating();
            }
        }
    }

    private void inactivateIfDeactivating() {
        if (state.compareAndSet(State.INACTIVATING, State.INACTIVE)) {
            this.ui = null;
            shutdownCommands.runPendingCommands();
        }
    }

    class ActionDispatcherImpl implements ActionDispatcher {
        /**
         * Executes the given action by holding the session lock. This is done
         * by using {@link UI#access(Command)} on the UI that the component(s)
         * associated with this context belong to. This ensures that any UI
         * changes are pushed to the client in real-time if {@link Push} is
         * enabled.
         * <p>
         * If this context is not active (none of the components are attached to
         * a UI), the action is postponed until the connection becomes active.
         *
         * @param action
         *            the action to dispatch
         */
        @Override
        public void dispatchAction(Command action) {
            inbox.add(action);
            flushPendingActionsIfActive();
        }

        @Override
        public <T> CompletableFuture<T> createCompletableFuture() {
            UI localUI = ComponentConnectionContext.this.ui;
            if (localUI == null) {
                throw new IllegalStateException(
                        "The topic connection within this context maybe deactivated."
                                + "Make sure the context has at least one component attached to the UI.");
            }
            return new DeadlockDetectingCompletableFuture<>(
                    localUI.getSession());
        }
    }

    private void flushPendingActionsIfActive() {
        UI localUI = this.ui;
        if (localUI == null || backgroundRunner == null) {
            return;
        }
        VaadinSession session = localUI.getSession();
        backgroundRunner.execute(() -> session.access(() -> {
            UI currentUI = UI.getCurrent();
            if (currentUI == null) {
                UI.setCurrent(localUI);
            }
            try {
                inbox.runPendingCommands();
                inactivateIfDeactivating();
            } finally {
                if (currentUI == null) {
                    UI.setCurrent(null);
                }
            }
        }));

    }

    private static void checkForPush(UI ui) {
        if (!canPushChanges(ui) && isActivationEnabled(ui)) {
            ui.getPushConfiguration().setPushMode(PushMode.AUTOMATIC);

            boolean warningAlreadyShown = pushWarningShown.getAndSet(true);
            if (!warningAlreadyShown) {
                int flowVersionInVaadin14 = 2;
                String annotationLocation = Version
                        .getMajorVersion() == flowVersionInVaadin14
                                ? "root layout or individual views"
                                : "AppShellConfigurator class";

                LoggerFactory.getLogger(ComponentConnectionContext.class).warn(
                        "Server push has been automatically enabled so updates can be shown immediately. "
                                + "Add @Push annotation on your "
                                + annotationLocation
                                + " to suppress this warning. "
                                + "Set automaticallyActivatePush to false in CollaborationEngineConfiguration if you want to ensure push is not automatically enabled.");
            }
        }
    }

    private static boolean isActivationEnabled(UI ui) {

        CollaborationEngine ce = CollaborationEngine
                .getInstance(ui.getSession().getService());

        return ce != null ? ce.getConfiguration().isAutomaticallyActivatePush()
                : CollaborationEngineConfiguration.DEFAULT_AUTOMATICALLY_ACTIVATE_PUSH;
    }

    private static boolean canPushChanges(UI ui) {
        return ui.getPushConfiguration().getPushMode().isEnabled()
                || ui.getPollInterval() > 0;
    }
}
