/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.vaadin.flow.component.UI
 *  com.vaadin.flow.server.Command
 *  com.vaadin.flow.server.ServiceDestroyEvent
 *  com.vaadin.flow.server.ServiceDestroyListener
 *  com.vaadin.flow.server.SessionDestroyListener
 *  com.vaadin.flow.server.VaadinService
 *  com.vaadin.flow.server.VaadinSession
 *  com.vaadin.flow.shared.Registration
 */
package egframe.common.SysChat;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.Command;
import com.vaadin.flow.server.ServiceDestroyEvent;
import com.vaadin.flow.server.ServiceDestroyListener;
import com.vaadin.flow.server.SessionDestroyListener;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.shared.Registration;
import java.io.Serializable;
import java.util.ArrayList;

class ServiceDestroyDelegate
implements Serializable {
    private final ArrayList<ServiceDestroyListener> listeners = new ArrayList();
    private final transient Registration serviceRegistration;
    private final transient Registration sessionRegistration;
    private final VaadinSession session;

    public ServiceDestroyDelegate(VaadinSession session) {
        this.session = session;
        VaadinService service = session.getService();
        this.serviceRegistration = service.addServiceDestroyListener(this::notifyListeners);
        this.sessionRegistration = service.addSessionDestroyListener((SessionDestroyListener & Serializable)event -> this.removeRegistrations());
    }

    private Object writeReplace() {
        return null;
    }

    private void removeRegistrations() {
        this.serviceRegistration.remove();
        this.sessionRegistration.remove();
    }

    private void notifyListeners(ServiceDestroyEvent event) {
        this.session.access((Command & Serializable)() -> new ArrayList<ServiceDestroyListener>(this.listeners).forEach(listener -> listener.serviceDestroy(event)));
    }

    public Registration addListener(ServiceDestroyListener listener) {
        return Registration.addAndRemove(this.listeners, listener);
    }

    public static ServiceDestroyDelegate ensureInstalled(UI ui) {
        VaadinSession session = ui.getSession();
        ServiceDestroyDelegate delegate = (ServiceDestroyDelegate)session.getAttribute(ServiceDestroyDelegate.class);
        if (delegate == null) {
            delegate = new ServiceDestroyDelegate(session);
            session.setAttribute(ServiceDestroyDelegate.class, delegate);
        }
        return delegate;
    }
}
