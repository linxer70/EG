/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.type.TypeReference
 *  com.vaadin.flow.shared.Registration
 */
package egframe.common.SysChat;

import com.fasterxml.jackson.core.type.TypeReference;
import egframe.common.SysChat.EntryScope;
import egframe.common.SysChat.HasExpirationTimeout;
import egframe.common.SysChat.MapSubscriber;
import egframe.common.SysChat.TopicConnection;
import com.vaadin.flow.shared.Registration;
import java.io.Serializable;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public interface CollaborationMap
extends HasExpirationTimeout,
Serializable {
    public <T> T get(String var1, Class<T> var2);

    public <T> T get(String var1, TypeReference<T> var2);

    default public CompletableFuture<Void> put(String key, Object value) {
        return this.put(key, value, EntryScope.TOPIC);
    }

    default public CompletableFuture<Void> remove(String key) {
        return this.put(key, null);
    }

    default public CompletableFuture<Boolean> remove(String key, Object expectedValue) {
        return this.replace(key, expectedValue, null);
    }

    public CompletableFuture<Void> put(String var1, Object var2, EntryScope var3);

    public CompletableFuture<Boolean> replace(String var1, Object var2, Object var3);

    public Stream<String> getKeys();

    public Registration subscribe(MapSubscriber var1);

    public TopicConnection getConnection();

    @Override
    public Optional<Duration> getExpirationTimeout();

    @Override
    public void setExpirationTimeout(Duration var1);
}
