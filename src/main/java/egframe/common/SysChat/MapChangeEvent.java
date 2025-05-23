/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.type.TypeReference
 *  com.fasterxml.jackson.databind.JsonNode
 */
package egframe.common.SysChat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import egframe.common.SysChat.CollaborationMap;
import egframe.common.SysChat.JsonUtil;
import egframe.common.SysChat.MapChange;
import java.util.EventObject;
import java.util.Objects;

public class MapChangeEvent
extends EventObject {
    private final String key;
    private final JsonNode oldValue;
    private final JsonNode value;

    public MapChangeEvent(CollaborationMap source, MapChange change) {
        super(source);
        Objects.requireNonNull(change, "Entry change must not be null");
        this.key = change.getKey();
        this.oldValue = change.getOldValue();
        this.value = change.getValue();
    }

    @Override
    public CollaborationMap getSource() {
        return (CollaborationMap)super.getSource();
    }

    public String getKey() {
        return this.key;
    }

    public <T> T getOldValue(Class<T> type) {
        return JsonUtil.toInstance(this.oldValue, type);
    }

    public <T> T getOldValue(TypeReference<T> typeRef) {
        return JsonUtil.toInstance(this.oldValue, typeRef);
    }

    public <T> T getValue(Class<T> type) {
        return JsonUtil.toInstance(this.value, type);
    }

    public <T> T getValue(TypeReference<T> typeRef) {
        return JsonUtil.toInstance(this.value, typeRef);
    }
}
