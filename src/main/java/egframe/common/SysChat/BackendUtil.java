/*
 * Decompiled with CFR 0.152.
 */
package egframe.common.SysChat;

import egframe.common.SysChat.CollaborationEngine;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

class BackendUtil {
    BackendUtil() {
    }

    public static CompletableFuture<UUID> initializeFromSnapshot(CollaborationEngine ce, Initializer initializer) {
        CollaborationEngine.LOGGER.debug("Attempting to initialize event log from snapshot.");
        int maxAttempts = ce.getConfiguration().getEventLogSubscribeRetryAttempts();
        CompletableFuture<UUID> future = new CompletableFuture<UUID>();
        BackendUtil.attemptInitialization(0, maxAttempts, initializer, future);
        return future;
    }

    private static void attemptInitialization(int attempt, int maxAttempts, Initializer initializer, CompletableFuture<UUID> future) {
        if (attempt < maxAttempts) {
            CompletableFuture<UUID> initFuture = initializer.initialize();
            initFuture.whenComplete((uuid, e) -> {
                if (e != null) {
                    CollaborationEngine.LOGGER.warn("Initialize event log failed - retry attempt " + (attempt + 1) + "/" + maxAttempts + ".");
                    BackendUtil.attemptInitialization(attempt + 1, maxAttempts, initializer, future);
                } else {
                    future.complete((UUID)uuid);
                }
            });
        } else {
            CollaborationEngine.LOGGER.warn("Initialize event log abandoned after " + maxAttempts + " retries.");
            future.complete(null);
        }
    }

    @FunctionalInterface
    public static interface Initializer {
        public CompletableFuture<UUID> initialize();
    }
}
