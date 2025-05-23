/*
 * Decompiled with CFR 0.152.
 */
package egframe.common.SysChat;

import egframe.common.SysChat.LicenseEvent;

@Deprecated(since="6.3", forRemoval=true)
@FunctionalInterface
public interface LicenseEventHandler {
    public void handleLicenseEvent(LicenseEvent var1);
}
