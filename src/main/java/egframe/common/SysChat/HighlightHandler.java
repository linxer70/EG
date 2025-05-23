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
public interface HighlightHandler
extends Serializable {
    public Registration handleHighlight(HighlightContext var1);

    public static interface HighlightContext
    extends Serializable {
        public sys_user getUser();

        public String getPropertyName();

        public int getFieldIndex();
    }
}
