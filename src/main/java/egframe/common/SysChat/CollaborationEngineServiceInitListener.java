/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.vaadin.flow.server.RequestHandler
 *  com.vaadin.flow.server.ServiceInitEvent
 *  com.vaadin.flow.server.VaadinService
 *  com.vaadin.flow.server.VaadinServiceInitListener
 */
package egframe.common.SysChat;

import com.vaadin.flow.server.RequestHandler;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServiceInitListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CollaborationEngineServiceInitListener
implements VaadinServiceInitListener {
    private static final List<Consumer<VaadinService>> reinitializers = new ArrayList<Consumer<VaadinService>>();

    public static void addReinitializer(Consumer<VaadinService> reinitializer) {
        reinitializers.add(reinitializer);
    }

    public void serviceInit(ServiceInitEvent event) {
        event.addRequestHandler((RequestHandler & Serializable)(session, request, response) -> {
            VaadinService requestService = request.getService();
            if (requestService != null && !reinitializers.isEmpty()) {
                List<Consumer<VaadinService>> list = reinitializers;
                synchronized (list) {
                    for (Consumer<VaadinService> reinitializer : reinitializers) {
                        reinitializer.accept(requestService);
                    }
                    reinitializers.clear();
                }
            }
            return false;
        });
    }
}
