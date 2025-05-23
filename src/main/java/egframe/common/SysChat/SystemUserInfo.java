/*
 * Decompiled with CFR 0.152.
 */
package egframe.common.SysChat;

import egframe.frame.entity.sys_user;

public class SystemUserInfo
extends sys_user {
    private static final String USER_ID = "<{system-user}>";
    private static final SystemUserInfo instance = new SystemUserInfo();

    private SystemUserInfo() {
        super(USER_ID, 0);
    }

    public static SystemUserInfo getInstance() {
        return instance;
    }

    @Override
    public void setUserNm(String name) {
        throw new UnsupportedOperationException("The system user cannot be modified.");
    }

    @Override
    public void setAbbreviation(String abbreviation) {
        throw new UnsupportedOperationException("The system user cannot be modified.");
    }

    @Override
    public void setImage(String imageUrl) {
        throw new UnsupportedOperationException("The system user cannot be modified.");
    }

    @Override
    public void setColorIndex(int colorIndex) {
        throw new UnsupportedOperationException("The system user cannot be modified.");
    }
}
