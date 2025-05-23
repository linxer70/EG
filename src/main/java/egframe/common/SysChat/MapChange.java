/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.databind.JsonNode
 */
package egframe.common.SysChat;

import com.fasterxml.jackson.databind.JsonNode;
import egframe.common.SysChat.MapChangeType;
import egframe.common.SysChat.Topic;
import java.util.Objects;
import java.util.UUID;

class MapChange
implements Topic.ChangeDetails {
    private final String mapName;
    private final MapChangeType type;
    private final String key;
    private final JsonNode value;
    private final JsonNode oldValue;
    private final UUID expectedId;
    private final UUID revisionId;

    MapChange(String mapName, MapChangeType type, String key, JsonNode oldValue, JsonNode newValue, UUID expectedId, UUID revisionId) {
        Objects.requireNonNull(mapName, "Map name can not be null.");
        Objects.requireNonNull(key, "Key cannot be null");
        this.mapName = mapName;
        this.type = type;
        this.key = key;
        this.value = newValue;
        this.oldValue = oldValue;
        this.expectedId = expectedId;
        this.revisionId = revisionId;
    }

    JsonNode getOldValue() {
        return this.oldValue;
    }

    MapChangeType getType() {
        return this.type;
    }

    String getMapName() {
        return this.mapName;
    }

    String getKey() {
        return this.key;
    }

    JsonNode getValue() {
        return this.value;
    }

    UUID getExpectedId() {
        return this.expectedId;
    }

    UUID getRevisionId() {
        return this.revisionId;
    }

    boolean hasChanges() {
        return !Objects.equals(this.getOldValue(), this.getValue());
    }
}
