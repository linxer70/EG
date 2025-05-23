/*
 * Decompiled with CFR 0.152.
 */
package egframe.common.SysChat;

import java.io.Serializable;

@FunctionalInterface
public interface PropertyChangeHandler
extends Serializable {
    public void handlePropertyChange(PropertyChangeEvent var1);

    public static interface PropertyChangeEvent {
        public String getPropertyName();

        public Object getValue();
    }
}
