/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.vaadin.flow.shared.Registration
 */
package egframe.common.SysChat;

import egframe.common.SysChat.Backend;
import egframe.common.SysChat.MembershipEvent;
import egframe.common.SysChat.MembershipListener;
import com.vaadin.flow.shared.Registration;
import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class LocalBackend
extends Backend {
    private final UUID id = UUID.randomUUID();

    @Override
    public Backend.EventLog openEventLog(String topicId) {
        return new LocalEventLog(topicId);
    }

    @Override
    public Registration addMembershipListener(MembershipListener membershipListener) {
        membershipListener.handleMembershipEvent(new MembershipEvent(MembershipEvent.MembershipEventType.JOIN, this.id, this.getCollaborationEngine()));
        return (Registration & Serializable)() -> {};
    }

    @Override
    public UUID getNodeId() {
        return this.id;
    }

    @Override
    public CompletableFuture<Backend.Snapshot> loadLatestSnapshot(String name) {
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> replaceSnapshot(String name, UUID expectedId, UUID newId, String payload) {
        return CompletableFuture.completedFuture(null);
    }

    private static class LocalEventLog
    implements Backend.EventLog {
        private final String topicId;
        private BiConsumer<UUID, String> consumer;

        private LocalEventLog(String topicId) {
            this.topicId = topicId;
        }

        @Override
        public Registration subscribe(UUID newerThan, BiConsumer<UUID, String> consumer) throws Backend.EventIdNotFoundException {
            if (this.consumer != null) {
                throw new IllegalStateException("Already subscribed to " + this.topicId);
            }
            this.consumer = consumer;
            return (Registration & Serializable)() -> {
                this.consumer = null;
            };
        }

        @Override
        public void submitEvent(UUID trackingId, String event) {
            if (this.consumer == null) {
                throw new IllegalStateException("Not subscribed to " + this.topicId);
            }
            this.consumer.accept(trackingId, event);
        }

        @Override
        public void truncate(UUID olderThan) {
        }
    }
    
    
}
