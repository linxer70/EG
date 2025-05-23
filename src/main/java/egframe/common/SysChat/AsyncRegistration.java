/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.vaadin.flow.shared.Registration
 */
package egframe.common.SysChat;

import com.vaadin.flow.shared.Registration;
import java.util.concurrent.CompletableFuture;

class AsyncRegistration
implements Registration {
    private final transient CompletableFuture<Void> future;
    private final Registration registration;

    AsyncRegistration(CompletableFuture<Void> future, Registration registration) {
        this.future = future;
        this.registration = registration;
    }

    CompletableFuture<Void> getFuture() {
        return this.future;
    }

    public void remove() {
        this.registration.remove();
    }
}
