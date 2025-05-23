/*
 * Decompiled with CFR 0.152.
 */
package egframe.common.SysChat;

import egframe.common.SysChat.ListChangeEvent;

@FunctionalInterface
public interface ListSubscriber {
    public void onListChange(ListChangeEvent var1);
}
