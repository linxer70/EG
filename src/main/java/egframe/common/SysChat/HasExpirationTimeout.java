/*
 * Decompiled with CFR 0.152.
 */
package egframe.common.SysChat;

import java.time.Duration;
import java.util.Optional;

public interface HasExpirationTimeout {
    public Optional<Duration> getExpirationTimeout();

    public void setExpirationTimeout(Duration var1);
}
