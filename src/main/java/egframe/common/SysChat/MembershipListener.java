/*
 * Decompiled with CFR 0.152.
 */
package egframe.common.SysChat;

import egframe.common.SysChat.MembershipEvent;

@FunctionalInterface
public interface MembershipListener {
    public void handleMembershipEvent(MembershipEvent var1);
}
