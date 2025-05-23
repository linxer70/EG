/*
 * Decompiled with CFR 0.152.
 */
package egframe.common.SysChat;

import egframe.common.SysChat.CollaborationEngine;
import java.util.EventObject;
import java.util.Objects;
import java.util.UUID;

public class MembershipEvent
extends EventObject {
    private final MembershipEventType type;
    private final UUID nodeId;

    public MembershipEvent(MembershipEventType type, UUID nodeId, CollaborationEngine collaborationEngine) {
        super(Objects.requireNonNull(collaborationEngine));
        this.type = Objects.requireNonNull(type);
        this.nodeId = Objects.requireNonNull(nodeId);
    }

    public MembershipEventType getType() {
        return this.type;
    }

    public UUID getNodeId() {
        return this.nodeId;
    }

    @Override
    public CollaborationEngine getSource() {
        return (CollaborationEngine)super.getSource();
    }

    public static enum MembershipEventType {
        JOIN,
        LEAVE;

    }
}
