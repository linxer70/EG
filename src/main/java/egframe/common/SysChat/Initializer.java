/*
 * Decompiled with CFR 0.152.
 */
package egframe.common.SysChat;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@FunctionalInterface
public  interface Initializer {
    public CompletableFuture<UUID> initialize();
}
