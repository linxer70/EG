/*
 * Decompiled with CFR 0.152.
 */
package egframe.common.SysChat;

import egframe.common.SysChat.MapChangeEvent;

@FunctionalInterface
public interface MapSubscriber {
    public void onMapChange(MapChangeEvent var1);
}
