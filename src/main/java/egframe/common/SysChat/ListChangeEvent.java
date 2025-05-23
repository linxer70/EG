/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.type.TypeReference
 */
package egframe.common.SysChat;

import com.fasterxml.jackson.core.type.TypeReference;
import egframe.common.SysChat.CollaborationList;
import egframe.common.SysChat.JsonUtil;
import egframe.common.SysChat.ListChange;
import egframe.common.SysChat.ListChangeType;
import egframe.common.SysChat.ListKey;
import java.util.EventObject;

public class ListChangeEvent
extends EventObject {
    private final ListChange change;

    ListChangeEvent(CollaborationList list, ListChange change) {
        super(list);
        this.change = change;
    }

    @Override
    public CollaborationList getSource() {
        return (CollaborationList)super.getSource();
    }

    ListChangeType getType() {
        return this.change.getType();
    }

    public ListKey getKey() {
        return new ListKey(this.change.getKey());
    }

    public <T> T getValue(Class<T> type) {
        return JsonUtil.toInstance(this.change.getValue(), type);
    }

    public <T> T getValue(TypeReference<T> type) {
        return JsonUtil.toInstance(this.change.getValue(), type);
    }

    public <T> T getOldValue(Class<T> type) {
        return JsonUtil.toInstance(this.change.getOldValue(), type);
    }

    public <T> T getOldValue(TypeReference<T> type) {
        return JsonUtil.toInstance(this.change.getOldValue(), type);
    }

    public ListKey getNext() {
        return ListKey.of(this.change.getNext());
    }

    public ListKey getOldNext() {
        return ListKey.of(this.change.getOldNext());
    }

    public ListKey getPrev() {
        return ListKey.of(this.change.getPrev());
    }

    public ListKey getOldPrev() {
        return ListKey.of(this.change.getOldPrev());
    }
}
