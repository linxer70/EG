/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.vaadin.flow.shared.Registration
 */
package egframe.common.SysChat;

import egframe.common.SysChat.ActivationHandler;
import com.vaadin.flow.shared.Registration;
import java.io.Serializable;
import java.util.concurrent.Executor;

public interface ConnectionContext
extends Serializable {
    public Registration init(ActivationHandler var1, Executor var2);
}
