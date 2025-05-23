/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.databind.JsonNode
 *  com.fasterxml.jackson.databind.node.NullNode
 */
package egframe.common.SysChat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import egframe.common.SysChat.CollaborationList;
import egframe.common.SysChat.CollaborationMap;
import egframe.common.SysChat.EntryScope;
import egframe.common.SysChat.FormManager;
import egframe.common.SysChat.ListKey;
import egframe.common.SysChat.ListOperation;
import egframe.common.SysChat.TopicConnection;
import egframe.frame.entity.sys_user;

import java.util.Objects;

public class CollaborationBinderUtil {
    static final String COLLABORATION_BINDER_MAP_NAME = FormManager.class.getName();

    private CollaborationBinderUtil() {
    }

    public static void setFieldValue(TopicConnection topicConnection, String propertyName, Object value) {
        Objects.requireNonNull(topicConnection, "Topic connection can't be null.");
        Objects.requireNonNull(propertyName, "Property name can't be null.");
        CollaborationBinderUtil.getMap(topicConnection).put(propertyName, value);
    }

    public static void addEditor(TopicConnection topicConnection, String propertyName, sys_user user) {
        CollaborationBinderUtil.addEditor(topicConnection, propertyName, user, 0);
    }

    public static void addEditor(TopicConnection topicConnection, String propertyName, sys_user user, int fieldIndex) {
        Objects.requireNonNull(topicConnection, "Topic connection can't be null.");
        Objects.requireNonNull(propertyName, "Property name can't be null.");
        Objects.requireNonNull(user, "User can't be null.");
        CollaborationList list = topicConnection.getNamedList(COLLABORATION_BINDER_MAP_NAME);
        ListOperation operation = ListOperation.insertLast(new FormManager.FocusedEditor(user, fieldIndex, propertyName)).withScope(EntryScope.CONNECTION);
        list.apply(operation);
    }

    public static void removeEditor(TopicConnection topicConnection, String propertyName, sys_user user) {
        Objects.requireNonNull(topicConnection, "Topic connection can't be null.");
        Objects.requireNonNull(propertyName, "Property name can't be null.");
        Objects.requireNonNull(user, "User can't be null.");
        CollaborationList list = CollaborationBinderUtil.getList(topicConnection);
        list.getKeys().forEach(key -> {
            FormManager.FocusedEditor editor = list.getItem((ListKey)key, FormManager.FocusedEditor.class);
            if (editor.propertyName.equals(propertyName) && editor.user.equals(user)) {
                list.set((ListKey)key, null);
            }
        });
    }

    static JsonNode getFieldValue(TopicConnection topic, String propertyName) {
        JsonNode result = CollaborationBinderUtil.getMap(topic).get(propertyName, JsonNode.class);
        return result != null ? result : NullNode.getInstance();
    }

    static CollaborationMap getMap(TopicConnection topic) {
        return topic.getNamedMap(COLLABORATION_BINDER_MAP_NAME);
    }

    static CollaborationList getList(TopicConnection topic) {
        return topic.getNamedList(COLLABORATION_BINDER_MAP_NAME);
    }
}
