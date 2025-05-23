/*
 * Decompiled with CFR 0.152.
 */
package egframe.common.SysChat;

public class AccessResponse {
    private final boolean hasAccess;

    AccessResponse(boolean hasAccess) {
        this.hasAccess = hasAccess;
    }

    public boolean hasAccess() {
        return this.hasAccess;
    }
}
