package egframe.common.SysChat;

import egframe.common.SysChat.CollaborationEngine;
import egframe.common.SysChat.MembershipListener;
import com.vaadin.flow.shared.Registration;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public abstract class Backend {
    private CollaborationEngine collaborationEngine;

    public final CollaborationEngine getCollaborationEngine() {
        return this.collaborationEngine;
    }

    public final void setCollaborationEngine(CollaborationEngine collaborationEngine) {
        this.collaborationEngine = Objects.requireNonNull(collaborationEngine);
    }

    public abstract EventLog openEventLog(String var1);

    public abstract Registration addMembershipListener(MembershipListener var1);

    public abstract UUID getNodeId();

    public abstract CompletableFuture<Snapshot> loadLatestSnapshot(String var1);

    public abstract CompletableFuture<Void> replaceSnapshot(String var1, UUID var2, UUID var3, String var4);

    public static interface EventLog {
        public void submitEvent(UUID var1, String var2);

        public Registration subscribe(UUID var1, BiConsumer<UUID, String> var2) throws EventIdNotFoundException;

        public void truncate(UUID var1);
    }

    public static class Snapshot
    implements Serializable {
        private final UUID id;
        private final String payload;

        public Snapshot(UUID id, String payload) {
            this.id = id;
            this.payload = payload;
        }

        public UUID getId() {
            return this.id;
        }

        public String getPayload() {
            return this.payload;
        }
    }

    public static class EventIdNotFoundException
    extends Exception {
        public EventIdNotFoundException(String message) {
            super(message);
        }
    }
    
    
}
