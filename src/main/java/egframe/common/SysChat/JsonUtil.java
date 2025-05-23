/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.JsonProcessingException
 *  com.fasterxml.jackson.core.TreeNode
 *  com.fasterxml.jackson.core.type.TypeReference
 *  com.fasterxml.jackson.databind.JavaType
 *  com.fasterxml.jackson.databind.JsonNode
 *  com.fasterxml.jackson.databind.Module
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  com.fasterxml.jackson.databind.SerializationFeature
 *  com.fasterxml.jackson.databind.node.ObjectNode
 *  com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
 */
package egframe.common.SysChat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import egframe.common.SysChat.JsonConversionException;
import egframe.common.SysChat.ListKey;
import egframe.common.SysChat.ListOperation;
import egframe.common.SysChat.Topic;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

class JsonUtil {
    static final String CHANGE_TYPE = "type";
    static final String CHANGE_NAME = "name";
    static final String CHANGE_KEY = "key";
    static final String CHANGE_POSITION_KEY = "position-key";
    static final String CHANGE_VALUE = "value";
    static final String CHANGE_EXPECTED_ID = "expected-id";
    static final String CHANGE_EXPECTED_VALUE = "expected-value";
    static final String CHANGE_CONDITIONS = "conditions";
    static final String CHANGE_VALUE_CONDITIONS = "value-conditions";
    static final String CHANGE_EMPTY = "empty";
    static final String CHANGE_TYPE_PUT = "m-put";
    static final String CHANGE_TYPE_REPLACE = "m-replace";
    static final String CHANGE_TYPE_INSERT_BEFORE = "l-insert-before";
    static final String CHANGE_TYPE_INSERT_AFTER = "l-insert-after";
    static final String CHANGE_TYPE_MOVE_BEFORE = "l-move-before";
    static final String CHANGE_TYPE_MOVE_AFTER = "l-move-after";
    static final String CHANGE_TYPE_LIST_SET = "l-set";
    static final String CHANGE_TYPE_MAP_TIMEOUT = "m-timeout";
    static final String CHANGE_TYPE_LIST_TIMEOUT = "l-timeout";
    static final String CHANGE_TYPE_LICENSE_USER = "license-user";
    static final String CHANGE_TYPE_LICENSE_EVENT = "license-event";
    static final String CHANGE_LICENSE_KEY = "license-key";
    static final String CHANGE_YEAR_MONTH = "year-month";
    static final String CHANGE_USER_ID = "user-id";
    static final String CHANGE_EVENT_NAME = "event-name";
    static final String CHANGE_EVENT_OCCURRENCE = "event-occurrence";
    public static final String CHANGE_NODE_ID = "node-id";
    public static final String CHANGE_NODE_ACTIVATE = "node-activate";
    public static final String CHANGE_NODE_DEACTIVATE = "node-deactivate";
    public static final String CHANGE_NODE_JOIN = "node-join";
    public static final String CHANGE_NODE_LEAVE = "node-leave";
    public static final String CHANGE_SCOPE_OWNER = "scope-owner";
    static final UUID TOPIC_SCOPE_ID = UUID.nameUUIDFromBytes(Topic.class.getName().getBytes());
    private static final ObjectMapper mapper = new ObjectMapper();

    private JsonUtil() {
    }

    static ObjectMapper getObjectMapper() {
        return mapper;
    }

    static JsonNode toJsonNode(Object value) {
        try {
            return mapper.valueToTree(value);
        }
        catch (IllegalArgumentException e) {
            throw new JsonConversionException("Failed to encode the object to JSON node. Make sure the value contains a supported type.", e);
        }
    }

    static <T> T toInstance(JsonNode jsonNode, Class<T> type) {
        if (jsonNode == null) {
            return null;
        }
        try {
        	System.out.println(jsonNode.toString());
        	mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);  
            return (T)mapper.treeToValue((TreeNode)jsonNode, type);
        }
        catch (JsonProcessingException e) {
            throw new JsonConversionException("Failed to parse the JSON node to " + type.getName(), e);
        }
    }

    static <T> Function<JsonNode, T> fromJsonConverter(Class<T> type) {
        Objects.requireNonNull(type, "The type can't be null");
        return jsonNode -> JsonUtil.toInstance(jsonNode, type);
    }

    static <T> T toInstance(JsonNode jsonNode, TypeReference<T> type) {
        Objects.requireNonNull(type, "The type reference cannot be null");
        return (T)JsonUtil.toInstance(jsonNode, type.getType());
    }

    static <T> Function<JsonNode, T> fromJsonConverter(TypeReference<T> type) {
        return jsonNode -> JsonUtil.toInstance(jsonNode, type);
    }

    static Object toInstance(JsonNode jsonNode, Type type) {
        if (jsonNode == null) {
            return null;
        }
        JavaType javaType = mapper.getTypeFactory().constructType(type);
        try {
            return mapper.readValue(mapper.treeAsTokens((TreeNode)jsonNode), javaType);
        }
        catch (IOException e) {
            throw new JsonConversionException("Failed to parse the JSON node to " + javaType.getTypeName(), e);
        }
    }

    static UUID toUUID(JsonNode jsonNode) {
        if (jsonNode == null || jsonNode.isNull()) {
            return null;
        }
        return UUID.fromString(jsonNode.asText());
    }

    static ObjectNode createPutChange(String name, String key, Object expectedValue, Object value, UUID scopeOwnerId) {
        ObjectNode change = mapper.createObjectNode();
        change.put(CHANGE_TYPE, CHANGE_TYPE_PUT);
        change.put(CHANGE_NAME, name);
        change.put(CHANGE_KEY, key);
        change.set(CHANGE_VALUE, JsonUtil.toJsonNode(value));
        if (scopeOwnerId != null) {
            change.put(CHANGE_SCOPE_OWNER, scopeOwnerId.toString());
        }
        if (expectedValue != null) {
            change.set(CHANGE_EXPECTED_VALUE, JsonUtil.toJsonNode(expectedValue));
        }
        return change;
    }

    static ObjectNode createReplaceChange(String name, String key, Object expectedValue, Object value) {
        ObjectNode change = mapper.createObjectNode();
        change.put(CHANGE_TYPE, CHANGE_TYPE_REPLACE);
        change.put(CHANGE_NAME, name);
        change.put(CHANGE_KEY, key);
        change.set(CHANGE_VALUE, JsonUtil.toJsonNode(value));
        if (expectedValue != null) {
            change.set(CHANGE_EXPECTED_VALUE, JsonUtil.toJsonNode(expectedValue));
        }
        return change;
    }

    static ObjectNode createListChange(ListOperation.OperationType type, String listName, String changeKey, String positionKey, Object item, UUID scopeOwnerId, Map<ListKey, ListKey> conditions, Map<ListKey, Object> valueConditions, Boolean empty) {
        ObjectNode change = mapper.createObjectNode();
        if (type == ListOperation.OperationType.INSERT_BEFORE) {
            change.put(CHANGE_TYPE, CHANGE_TYPE_INSERT_BEFORE);
        } else if (type == ListOperation.OperationType.INSERT_AFTER) {
            change.put(CHANGE_TYPE, CHANGE_TYPE_INSERT_AFTER);
        } else if (type == ListOperation.OperationType.MOVE_BEFORE) {
            change.put(CHANGE_TYPE, CHANGE_TYPE_MOVE_BEFORE);
        } else if (type == ListOperation.OperationType.MOVE_AFTER) {
            change.put(CHANGE_TYPE, CHANGE_TYPE_MOVE_AFTER);
        } else if (type == ListOperation.OperationType.SET) {
            change.put(CHANGE_TYPE, CHANGE_TYPE_LIST_SET);
        }
        change.put(CHANGE_NAME, listName);
        change.set(CHANGE_VALUE, JsonUtil.toJsonNode(item));
        change.put(CHANGE_KEY, changeKey);
        change.put(CHANGE_POSITION_KEY, positionKey);
        conditions.forEach((refKey, otherKey) -> {
            ObjectNode condition = mapper.createObjectNode();
            condition.put(CHANGE_KEY, refKey != null ? refKey.getKey().toString() : null);
            condition.put(CHANGE_POSITION_KEY, otherKey != null ? otherKey.getKey().toString() : null);
            change.withArray(CHANGE_CONDITIONS).add((JsonNode)condition);
        });
        valueConditions.forEach((key, value) -> {
            ObjectNode valueCondition = mapper.createObjectNode();
            valueCondition.put(CHANGE_KEY, key != null ? key.getKey().toString() : null);
            valueCondition.set(CHANGE_EXPECTED_VALUE, JsonUtil.toJsonNode(value));
            change.withArray(CHANGE_VALUE_CONDITIONS).add((JsonNode)valueCondition);
        });
        if (empty != null) {
            change.put(CHANGE_EMPTY, empty);
        }
        if (scopeOwnerId != null) {
            change.put(CHANGE_SCOPE_OWNER, scopeOwnerId.toString());
        }
        return change;
    }

    static ObjectNode createMapTimeoutChange(String name, Duration timeout) {
        ObjectNode change = mapper.createObjectNode();
        change.put(CHANGE_TYPE, CHANGE_TYPE_MAP_TIMEOUT);
        change.put(CHANGE_NAME, name);
        change.set(CHANGE_VALUE, JsonUtil.toJsonNode(timeout));
        return change;
    }

    static ObjectNode createListTimeoutChange(String name, Duration timeout) {
        ObjectNode change = mapper.createObjectNode();
        change.put(CHANGE_TYPE, CHANGE_TYPE_LIST_TIMEOUT);
        change.put(CHANGE_NAME, name);
        change.set(CHANGE_VALUE, JsonUtil.toJsonNode(timeout));
        return change;
    }

    static ObjectNode createNodeActivate(UUID id) {
        ObjectNode payload = mapper.createObjectNode();
        payload.put(CHANGE_TYPE, CHANGE_NODE_ACTIVATE);
        payload.put(CHANGE_NODE_ID, id.toString());
        return payload;
    }

    static ObjectNode createNodeDeactivate(UUID id) {
        ObjectNode payload = mapper.createObjectNode();
        payload.put(CHANGE_TYPE, CHANGE_NODE_DEACTIVATE);
        payload.put(CHANGE_NODE_ID, id.toString());
        return payload;
    }

    static ObjectNode createUserEntry(String key, YearMonth month, String userId) {
        ObjectNode entry = mapper.createObjectNode();
        entry.put(CHANGE_TYPE, CHANGE_TYPE_LICENSE_USER);
        entry.put(CHANGE_LICENSE_KEY, key);
        entry.put(CHANGE_YEAR_MONTH, month.toString());
        entry.put(CHANGE_USER_ID, userId);
        return entry;
    }

    static ObjectNode createLicenseEvent(String key, String name, LocalDate latestOccurrence) {
        ObjectNode event = mapper.createObjectNode();
        event.put(CHANGE_TYPE, CHANGE_TYPE_LICENSE_EVENT);
        event.put(CHANGE_LICENSE_KEY, key);
        event.put(CHANGE_EVENT_NAME, name);
        event.put(CHANGE_EVENT_OCCURRENCE, latestOccurrence.toString());
        return event;
    }

    static ObjectNode createNodeJoin(UUID id) {
        ObjectNode payload = mapper.createObjectNode();
        payload.put(CHANGE_TYPE, CHANGE_NODE_JOIN);
        payload.put(CHANGE_NODE_ID, id.toString());
        return payload;
    }

    static ObjectNode createNodeLeave(UUID id) {
        ObjectNode payload = mapper.createObjectNode();
        payload.put(CHANGE_TYPE, CHANGE_NODE_LEAVE);
        payload.put(CHANGE_NODE_ID, id.toString());
        return payload;
    }

    static String toString(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return mapper.writeValueAsString(value);
        }
        catch (JsonProcessingException e) {
            throw new JsonConversionException("Failed to serialize the object to string.", e);
        }
    }

    static ObjectNode fromString(String value) {
        if (value == null) {
            return null;
        }
        try {
            return (ObjectNode)mapper.readTree(value);
        }
        catch (JsonProcessingException e) {
            throw new JsonConversionException("Failed to read the object from string.", e);
        }
    }

    static {
        mapper.registerModule((Module)new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }
}
