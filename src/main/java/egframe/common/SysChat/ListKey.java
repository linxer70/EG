/*
 * Decompiled with CFR 0.152.
 */
package egframe.common.SysChat;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class ListKey
implements Serializable {
    private final UUID key;

    ListKey(UUID key) {
        this.key = Objects.requireNonNull(key);
    }

    public UUID getKey() {
        return this.key;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof ListKey) {
            ListKey that = (ListKey)obj;
            return this.key.equals(that.key);
        }
        return false;
    }

    public int hashCode() {
        return this.key.hashCode();
    }

    public static ListKey of(UUID key) {
        if (key == null) {
            return null;
        }
        return new ListKey(key);
    }
}
