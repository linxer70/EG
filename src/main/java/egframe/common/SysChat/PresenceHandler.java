/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.vaadin.flow.shared.Registration
 */
package egframe.common.SysChat;

import egframe.frame.entity.sys_user;

import com.vaadin.flow.shared.Registration;
import java.io.Serializable;

@FunctionalInterface
public interface PresenceHandler
extends Serializable {
    public Registration handlePresence(PresenceContext var1);

    public static interface PresenceContext
    extends Serializable {
        public sys_user getUser();
    }
}
