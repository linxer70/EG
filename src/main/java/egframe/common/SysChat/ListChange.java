/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.databind.JsonNode
 */
package egframe.common.SysChat;

import com.fasterxml.jackson.databind.JsonNode;
import egframe.common.SysChat.ListChangeType;
import egframe.common.SysChat.Topic;
import java.util.UUID;

class ListChange
implements Topic.ChangeDetails {
    private final String listName;
    private final ListChangeType type;
    private final UUID key;
    private final JsonNode oldValue;
    private final JsonNode value;
    private final UUID oldPrev;
    private final UUID prev;
    private final UUID oldNext;
    private final UUID next;
    private final UUID expectedId;
    private final UUID revisionId;

    ListChange(String listName, ListChangeType type, UUID key, JsonNode oldValue, JsonNode value, UUID oldPrev, UUID prev, UUID oldNext, UUID next, UUID expectedId, UUID revisionId) {
        this.listName = listName;
        this.type = type;
        this.key = key;
        this.oldValue = oldValue;
        this.value = value;
        this.oldPrev = oldPrev;
        this.prev = prev;
        this.oldNext = oldNext;
        this.next = next;
        this.expectedId = expectedId;
        this.revisionId = revisionId;
    }

    String getListName() {
        return this.listName;
    }

    ListChangeType getType() {
        return this.type;
    }

    UUID getKey() {
        return this.key;
    }

    JsonNode getOldValue() {
        return this.oldValue;
    }

    JsonNode getValue() {
        return this.value;
    }

    UUID getOldNext() {
        return this.oldNext;
    }

    UUID getNext() {
        return this.next;
    }

    UUID getOldPrev() {
        return this.oldPrev;
    }

    UUID getPrev() {
        return this.prev;
    }

    UUID getExpectedId() {
        return this.expectedId;
    }

    UUID getRevisionId() {
        return this.revisionId;
    }
}
