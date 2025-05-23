/*
 * Decompiled with CFR 0.152.
 */
package egframe.common.SysChat;

import egframe.common.SysChat.ListKey;
import java.util.concurrent.CompletableFuture;


public class ListOperationResult<T> {
    private final ListKey key;
    private final CompletableFuture<T> completableFuture;

    ListOperationResult(ListKey key, CompletableFuture<T> completableFuture) {
        this.key = key;
        this.completableFuture = completableFuture;
    }

    /**
     * Gets the key of the item.
     *
     * @return the item key, not <code>null</code>
     */
    public ListKey getKey() {
        return key;
    }

    /**
     * The result of the asynchronous operation.
     *
     * @return the result of the operation, not <code>null</code>
     */
    public CompletableFuture<T> getCompletableFuture() {
        return completableFuture;
    }

    /* Map to a void parameterized type for existing list operations */
    ListOperationResult<Void> mapToVoid() {
        return new ListOperationResult<>(key,
                completableFuture.thenApply(t -> null));
    }
}
