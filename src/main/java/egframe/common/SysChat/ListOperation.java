/*
 * Decompiled with CFR 0.152.
 */
package egframe.common.SysChat;

import egframe.common.SysChat.EntryScope;
import egframe.common.SysChat.ListKey;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ListOperation {
    private final Object value;
    private final OperationType type;
    private final ListKey changeKey;
    private final ListKey referenceKey;
    private final Map<ListKey, ListKey> conditions = new HashMap<ListKey, ListKey>();
    private EntryScope scope = EntryScope.TOPIC;
    private Boolean empty;
    private final Map<ListKey, Object> valueConditions = new HashMap<ListKey, Object>();

    public static ListOperation insertFirst(Object value) {
        Objects.requireNonNull(value);
        return new ListOperation(OperationType.INSERT_AFTER, value, null, null);
    }

    public static ListOperation insertLast(Object value) {
        Objects.requireNonNull(value);
        return new ListOperation(OperationType.INSERT_BEFORE, value, null, null);
    }

    public static ListOperation insertBefore(ListKey before, Object value) {
        Objects.requireNonNull(before);
        Objects.requireNonNull(value);
        return new ListOperation(OperationType.INSERT_BEFORE, value, null, before);
    }

    public static ListOperation insertAfter(ListKey after, Object value) {
        Objects.requireNonNull(after);
        Objects.requireNonNull(value);
        return new ListOperation(OperationType.INSERT_AFTER, value, null, after);
    }

    public static ListOperation insertBetween(ListKey prev, ListKey next, Object value) {
        Objects.requireNonNull(prev);
        Objects.requireNonNull(next);
        Objects.requireNonNull(value);
        return ListOperation.insertAfter(prev, value).ifNext(prev, next);
    }

    public static ListOperation moveBefore(ListKey before, ListKey entry) {
        Objects.requireNonNull(before);
        Objects.requireNonNull(entry);
        return new ListOperation(OperationType.MOVE_BEFORE, null, entry, before);
    }

    public static ListOperation moveAfter(ListKey after, ListKey entry) {
        Objects.requireNonNull(after);
        Objects.requireNonNull(entry);
        return new ListOperation(OperationType.MOVE_AFTER, null, entry, after);
    }

    public static ListOperation moveBetween(ListKey prev, ListKey next, ListKey entry) {
        Objects.requireNonNull(prev);
        Objects.requireNonNull(next);
        Objects.requireNonNull(entry);
        return ListOperation.moveAfter(prev, entry).ifNext(prev, next);
    }

    public static ListOperation set(ListKey key, Object value) {
        Objects.requireNonNull(key);
        return new ListOperation(OperationType.SET, value, key, null);
    }

    public static ListOperation delete(ListKey key) {
        return ListOperation.set(key, null);
    }

    private ListOperation(OperationType type, Object value, ListKey changeKey, ListKey referenceKey) {
        this.type = type;
        this.value = value;
        this.changeKey = changeKey;
        this.referenceKey = referenceKey;
    }

    public ListOperation withScope(EntryScope scope) {
        this.scope = Objects.requireNonNull(scope);
        return this;
    }

    public ListOperation ifNext(ListKey key, ListKey nextKey) {
        Objects.requireNonNull(key);
        if (this.conditions.containsKey(key)) {
            throw new IllegalStateException("A requirement for the value after this key is already set");
        }
        this.conditions.put(key, nextKey);
        return this;
    }

    public ListOperation ifLast(ListKey key) {
        Objects.requireNonNull(key);
        return this.ifNext(key, null);
    }

    public ListOperation ifPrev(ListKey key, ListKey prevKey) {
        Objects.requireNonNull(key);
        if (this.conditions.containsValue(key)) {
            throw new IllegalStateException("A requirement for the value before this key is already set");
        }
        this.conditions.put(prevKey, key);
        return this;
    }

    public ListOperation ifFirst(ListKey key) {
        Objects.requireNonNull(key);
        return this.ifPrev(key, null);
    }

    public ListOperation ifEmpty() {
        if (Boolean.FALSE.equals(this.empty)) {
            throw new IllegalStateException("This operation already requires the list not to be empty.");
        }
        this.empty = true;
        return this;
    }

    public ListOperation ifNotEmpty() {
        if (Boolean.TRUE.equals(this.empty)) {
            throw new IllegalStateException("This operation already requires the list to be empty.");
        }
        this.empty = false;
        return this;
    }

    public ListOperation ifValue(ListKey key, Object value) {
        Objects.requireNonNull(key);
        if (this.valueConditions.containsKey(key)) {
            throw new IllegalStateException("A requirement for the value of this key is already set");
        }
        this.valueConditions.put(key, value);
        return this;
    }

    Object getValue() {
        return this.value;
    }

    OperationType getType() {
        return this.type;
    }

    ListKey getReferenceKey() {
        return this.referenceKey;
    }

    ListKey getChangeKey() {
        return this.changeKey;
    }

    EntryScope getScope() {
        return this.scope;
    }

    Map<ListKey, ListKey> getConditions() {
        return Collections.unmodifiableMap(this.conditions);
    }

    Boolean getEmpty() {
        return this.empty;
    }

    Map<ListKey, Object> getValueConditions() {
        return Collections.unmodifiableMap(this.valueConditions);
    }

    public static enum OperationType {
        INSERT_BEFORE,
        INSERT_AFTER,
        MOVE_BEFORE,
        MOVE_AFTER,
        SET;

    }
}
