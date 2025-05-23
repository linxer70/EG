/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.type.TypeReference
 *  com.vaadin.flow.shared.Registration
 */
package egframe.common.SysChat;

import com.fasterxml.jackson.core.type.TypeReference;
import egframe.common.SysChat.HasExpirationTimeout;
import egframe.common.SysChat.ListKey;
import egframe.common.SysChat.ListOperation;
import egframe.common.SysChat.ListOperationResult;
import egframe.common.SysChat.ListSubscriber;
import egframe.common.SysChat.TopicConnection;
import com.vaadin.flow.shared.Registration;
import java.io.Serializable;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public interface CollaborationList
extends HasExpirationTimeout,
Serializable {
    public <T> List<T> getItems(Class<T> var1);

    public <T> List<T> getItems(TypeReference<T> var1);

    public <T> T getItem(ListKey var1, Class<T> var2);

    public <T> T getItem(ListKey var1, TypeReference<T> var2);

    public Stream<ListKey> getKeys();

    public ListOperationResult<Boolean> apply(ListOperation var1);

    default public ListOperationResult<Void> insertFirst(Object item) {
        ListOperation operation = ListOperation.insertFirst(item);
        return this.apply(operation).mapToVoid();
    }

    default public ListOperationResult<Void> insertLast(Object item) {
        ListOperation operation = ListOperation.insertLast(item);
        return this.apply(operation).mapToVoid();
    }

    default public ListOperationResult<Boolean> insertBefore(ListKey key, Object item) {
        ListOperation operation = ListOperation.insertBefore(key, item);
        return this.apply(operation);
    }

    default public ListOperationResult<Boolean> insertAfter(ListKey key, Object item) {
        ListOperation operation = ListOperation.insertAfter(key, item);
        return this.apply(operation);
    }

    default public CompletableFuture<Boolean> moveBefore(ListKey key, ListKey keyToMove) {
        ListOperation operation = ListOperation.moveBefore(key, keyToMove);
        return this.apply(operation).getCompletableFuture();
    }

    default public CompletableFuture<Boolean> moveAfter(ListKey key, ListKey keyToMove) {
        ListOperation operation = ListOperation.moveAfter(key, keyToMove);
        return this.apply(operation).getCompletableFuture();
    }

    default public CompletableFuture<Boolean> set(ListKey key, Object value) {
        ListOperation operation = ListOperation.set(key, value);
        return this.apply(operation).getCompletableFuture();
    }

    default public CompletableFuture<Boolean> remove(ListKey key) {
        ListOperation operation = ListOperation.delete(key);
        return this.apply(operation).getCompletableFuture();
    }

    public Registration subscribe(ListSubscriber var1);

    public TopicConnection getConnection();

    @Override
    public Optional<Duration> getExpirationTimeout();

    @Override
    public void setExpirationTimeout(Duration var1);
}
