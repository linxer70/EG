/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.vaadin.flow.server.Command
 */
package egframe.common.SysChat;

import com.vaadin.flow.server.Command;
import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

public interface ActionDispatcher
extends Serializable {
    public void dispatchAction(Command var1);

    public <T> CompletableFuture<T> createCompletableFuture();
}
