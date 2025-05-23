/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonAutoDetect
 *  com.fasterxml.jackson.annotation.JsonAutoDetect$Visibility
 *  com.fasterxml.jackson.annotation.JsonCreator
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  com.fasterxml.jackson.databind.JsonNode
 */
package egframe.common.SysChat;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import egframe.common.SysChat.JsonUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY)
class EntryList {
    private final Map<UUID, ListEntry> entries = new HashMap<UUID, ListEntry>();
    private UUID head;
    private UUID tail;

    EntryList() {
    }

    int size() {
        return this.entries.size();
    }

    void clear() {
        this.entries.clear();
        this.head = null;
        this.tail = null;
    }

    ListEntrySnapshot insertFirst(UUID key, JsonNode value, UUID revisionId, UUID scopeOwnerId) {
        ListEntry item = this.createAndAddItem(key, value, revisionId, scopeOwnerId);
        this.link(key, null, this.head);
        return new ListEntrySnapshot(key, item);
    }

    ListEntrySnapshot insertLast(UUID key, JsonNode value, UUID revisionId, UUID scopeOwnerId) {
        ListEntry item = this.createAndAddItem(key, value, revisionId, scopeOwnerId);
        this.link(key, this.tail, null);
        return new ListEntrySnapshot(key, item);
    }

    ListEntrySnapshot insertBefore(UUID keyToFind, UUID keyToInsert, JsonNode value, UUID revisionId, UUID scopeOwnerId) {
        ListEntry item = this.createAndAddItem(keyToInsert, value, revisionId, scopeOwnerId);
        ListEntry entryToFind = this.entries.get(keyToFind);
        this.link(keyToInsert, entryToFind.prev, keyToFind);
        return new ListEntrySnapshot(keyToInsert, item);
    }

    ListEntrySnapshot insertAfter(UUID keyToFind, UUID keyToInsert, JsonNode value, UUID revisionId, UUID scopeOwnerId) {
        ListEntry item = this.createAndAddItem(keyToInsert, value, revisionId, scopeOwnerId);
        ListEntry entryToFind = this.entries.get(keyToFind);
        this.link(keyToInsert, keyToFind, entryToFind.next);
        return new ListEntrySnapshot(keyToInsert, item);
    }

    ListEntrySnapshot moveBefore(UUID keyToFind, UUID keyToMove, UUID revisionId, UUID scopeOwnerId) {
        ListEntry entryToMove = this.entries.get(keyToMove);
        if (entryToMove != null) {
            entryToMove.revisionId = revisionId;
            if (Objects.equals(scopeOwnerId, JsonUtil.TOPIC_SCOPE_ID)) {
                entryToMove.scopeOwnerId = null;
            } else if (scopeOwnerId != null) {
                entryToMove.scopeOwnerId = scopeOwnerId;
            }
        }
        this.unlink(entryToMove);
        ListEntry entryToFind = this.entries.get(keyToFind);
        this.link(keyToMove, entryToFind.prev, keyToFind);
        return new ListEntrySnapshot(keyToMove, entryToMove);
    }

    ListEntrySnapshot moveAfter(UUID keyToFind, UUID keyToMove, UUID revisionId, UUID scopeOwnerId) {
        ListEntry entryToMove = this.entries.get(keyToMove);
        if (entryToMove != null) {
            entryToMove.revisionId = revisionId;
            if (Objects.equals(scopeOwnerId, JsonUtil.TOPIC_SCOPE_ID)) {
                entryToMove.scopeOwnerId = null;
            } else if (scopeOwnerId != null) {
                entryToMove.scopeOwnerId = scopeOwnerId;
            }
        }
        this.unlink(entryToMove);
        ListEntry entryToFind = this.entries.get(keyToFind);
        this.link(keyToMove, keyToFind, entryToFind.next);
        return new ListEntrySnapshot(keyToMove, entryToMove);
    }

    Stream<ListEntrySnapshot> stream() {
        Stream.Builder<ListEntrySnapshot> builder = Stream.builder();
        UUID key = this.head;
        while (key != null) {
            ListEntry entry = this.entries.get(key);
            builder.add(new ListEntrySnapshot(key, entry));
            key = entry.next;
        }
        return builder.build();
    }

    JsonNode getValue(UUID key) {
        ListEntry item = this.entries.get(key);
        if (item == null) {
            return null;
        }
        return item.value;
    }

    ListEntrySnapshot getEntry(UUID key) {
        ListEntry entry = this.entries.get(key);
        if (entry == null) {
            return null;
        }
        return new ListEntrySnapshot(key, entry);
    }

    void remove(UUID key) {
        ListEntry item = this.entries.remove(key);
        this.unlink(item);
    }

    private void unlink(ListEntry item) {
        if (item != null) {
            this.setPrev(item.next, item.prev);
            this.setNext(item.prev, item.next);
        }
    }

    private void link(UUID key, UUID keyBefore, UUID keyAfter) {
        ListEntry item = this.entries.get(key);
        if (item != null) {
            item.prev = keyBefore;
            item.next = keyAfter;
            this.setPrev(item.next, key);
            this.setNext(item.prev, key);
        }
    }

    private void setNext(UUID target, UUID value) {
        if (target == null) {
            this.head = value;
        } else {
            this.entries.get((Object)target).next = value;
        }
    }

    private void setPrev(UUID target, UUID value) {
        if (target == null) {
            this.tail = value;
        } else {
            this.entries.get((Object)target).prev = value;
        }
    }

    private ListEntry createAndAddItem(UUID key, JsonNode value, UUID revisionId, UUID scopeOwnerId) {
        ListEntry item = new ListEntry();
        item.value = value;
        item.revisionId = revisionId;
        item.scopeOwnerId = scopeOwnerId;
        this.entries.put(Objects.requireNonNull(key), item);
        return item;
    }

    void setValue(UUID key, JsonNode newValue, UUID revisionId, UUID scopeOwnerId) {
        ListEntry listEntry = this.entries.get(key);
        listEntry.value = newValue;
        listEntry.revisionId = revisionId;
        listEntry.scopeOwnerId = scopeOwnerId;
    }

    @JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY)
    private static class ListEntry {
        JsonNode value;
        UUID prev;
        UUID next;
        UUID revisionId;
        UUID scopeOwnerId;

        private ListEntry() {
        }
    }

    @JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY)
    static class ListEntrySnapshot {
        final UUID id;
        final JsonNode value;
        final UUID prev;
        final UUID next;
        final UUID revisionId;
        final UUID scopeOwnerId;

        ListEntrySnapshot(UUID id, ListEntry entry) {
            this.id = id;
            this.value = entry.value;
            this.prev = entry.prev;
            this.next = entry.next;
            this.revisionId = entry.revisionId;
            this.scopeOwnerId = entry.scopeOwnerId;
        }

        @JsonCreator
        ListEntrySnapshot(@JsonProperty(value="id") UUID id, @JsonProperty(value="value") JsonNode value, @JsonProperty(value="prev") UUID prev, @JsonProperty(value="next") UUID next, @JsonProperty(value="revisionId") UUID revisionId, @JsonProperty(value="scopeOwnerId") UUID scopeOwnerId) {
            this.id = id;
            this.value = value;
            this.prev = prev;
            this.next = next;
            this.revisionId = revisionId;
            this.scopeOwnerId = scopeOwnerId;
        }
    }
}
