/*
 * Decompiled with CFR 0.152.
 */
package egframe.common.SysChat;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import egframe.frame.entity.sys_user;

public class CollaborationMessage
implements Serializable {
    private sys_user user;
    private String text;
    private Instant time;

    public CollaborationMessage() {
    }

    public CollaborationMessage(sys_user user, String text, Instant time) {
        this.user = user;
        this.text = text;
        this.time = time;
    }

    public sys_user getUser() {
        return this.user;
    }

    public void setUser(sys_user user) {
        this.user = user;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Instant getTime() {
        return this.time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        CollaborationMessage that = (CollaborationMessage)o;
        return Objects.equals(this.user, that.user) && Objects.equals(this.text, that.text) && Objects.equals(this.time, that.time);
    }

    public int hashCode() {
        return Objects.hash(this.user, this.text, this.time);
    }
}
