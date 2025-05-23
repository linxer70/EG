/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.vaadin.flow.component.Component
 *  com.vaadin.flow.component.ComponentEventListener
 *  com.vaadin.flow.component.ComponentUtil
 *  com.vaadin.flow.component.UI
 *  com.vaadin.flow.server.Command
 *  com.vaadin.flow.server.RequestHandler
 *  com.vaadin.flow.server.SynchronizedRequestHandler
 *  com.vaadin.flow.server.VaadinRequest
 *  com.vaadin.flow.server.VaadinResponse
 *  com.vaadin.flow.server.VaadinSession
 *  com.vaadin.flow.shared.Registration
 */
package egframe.common.SysChat;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.Command;
import com.vaadin.flow.server.RequestHandler;
import com.vaadin.flow.server.SynchronizedRequestHandler;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinResponse;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.shared.Registration;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class BeaconHandler
extends SynchronizedRequestHandler {
    private static final String ID_PARAMETER = "id";
    private static final String REQUEST_TYPE = "beacon";
    private final String id = UUID.randomUUID().toString();
    private final List<Command> listeners = new ArrayList<Command>();
    private final String beaconPath;

    public BeaconHandler(String beaconPath) {
        this.beaconPath = beaconPath;
    }

    private Object writeReplace() throws ObjectStreamException {
        return null;
    }

    static BeaconHandler ensureInstalled(UI ui, String beaconPath) {
        BeaconHandler beaconHandler = (BeaconHandler)((Object)ComponentUtil.getData((Component)ui, BeaconHandler.class));
        if (beaconHandler != null) {
            return beaconHandler;
        }
        BeaconHandler newBeaconHandler = new BeaconHandler(beaconPath);
        ui.getElement().executeJs(BeaconHandler.getUnloadScript(), new Serializable[]{newBeaconHandler.createBeaconUrl()});
        VaadinSession session = ui.getSession();
        session.removeRequestHandler(null);
        session.addRequestHandler((RequestHandler)newBeaconHandler);
        //origin
        //ComponentUtil.setData((Component)ui, BeaconHandler.class, (Object)((Object)newBeaconHandler));
        ComponentUtil.setData((Component)ui, BeaconHandler.class.getName(), (Object)((Object)newBeaconHandler));
        ui.addDetachListener((ComponentEventListener & Serializable)detachEvent -> session.removeRequestHandler((RequestHandler)newBeaconHandler));
        return newBeaconHandler;
    }

    String createBeaconUrl() {
        String requestTypeParameter = BeaconHandler.formatParameter("v-r", REQUEST_TYPE);
        String beaconIdParameter = BeaconHandler.formatParameter(ID_PARAMETER, this.id);
        return "." + this.beaconPath + "?" + requestTypeParameter + "&" + beaconIdParameter;
    }

    private static String formatParameter(String name, String value) {
        return name + "=" + value;
    }

    private static String getUnloadScript() {
        return "window.addEventListener('unload', function() {  if (navigator.sendBeacon) {    navigator.sendBeacon($0);  } else {    var xhr = new XMLHttpRequest();    xhr.open(\"POST\", $0, false);    xhr.send(\"\");  }})";
    }

    protected boolean canHandleRequest(VaadinRequest request) {
        if (!this.beaconPath.equals(request.getPathInfo())) {
            return false;
        }
        String requestType = request.getParameter("v-r");
        String beaconId = request.getParameter(ID_PARAMETER);
        return REQUEST_TYPE.equals(requestType) && this.id.equals(beaconId);
    }

    public boolean synchronizedHandleRequest(VaadinSession session, VaadinRequest request, VaadinResponse response) {
        new ArrayList<Command>(this.listeners).forEach(Command::execute);
        return true;
    }

    Registration addListener(Command listener) {
        this.listeners.add(listener);
        return (Registration & Serializable)() -> this.listeners.remove(listener);
    }

    List<Command> getListeners() {
        return new ArrayList<Command>(this.listeners);
    }
}
