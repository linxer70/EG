/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.vaadin.flow.component.Component
 *  com.vaadin.flow.component.Composite
 *  com.vaadin.flow.component.HasSize
 *  com.vaadin.flow.component.HasStyle
 *  com.vaadin.flow.component.messages.MessageList
 *  com.vaadin.flow.component.messages.MessageListItem
 *  com.vaadin.flow.function.SerializableSupplier
 *  com.vaadin.flow.internal.UsageStatistics
 *  com.vaadin.flow.shared.Registration
 */
package egframe.common.SysChat;

import egframe.common.SysChat.CollaborationAvatarGroup;
import egframe.common.SysChat.CollaborationEngine;
import egframe.common.SysChat.CollaborationMessage;
import egframe.common.SysChat.CollaborationMessagePersister;
import egframe.common.SysChat.CollaborationMessageSubmitter;
import egframe.common.SysChat.ComponentConnectionContext;
import egframe.common.SysChat.ConnectionContext;
import egframe.common.SysChat.MessageManager;
import egframe.frame.entity.sys_user;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.function.SerializableSupplier;
import com.vaadin.flow.internal.UsageStatistics;
import com.vaadin.flow.shared.Registration;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class CollaborationMessageList extends Composite<MessageList> implements HasSize,
HasStyle {
    private final SerializableSupplier<CollaborationEngine> ceSupplier;
    private CollaborationAvatarGroup.ImageProvider imageProvider;
    private final sys_user localUser;
    private CollaborationMessageSubmitter submitter;
    private Registration submitterRegistration;
    private MessageConfigurator messageConfigurator;
    private MessageManager messageManager;
    private final CollaborationMessagePersister persister;
    private final List<CollaborationMessage> messageCache = new ArrayList<CollaborationMessage>();
    public List<String> topicUsers = new ArrayList();
    @FunctionalInterface
    public static interface MessageConfigurator {
        public void configureMessage(MessageListItem var1, sys_user var2);
    }

    public CollaborationMessageList(sys_user localUser, String topicId) {
        this(localUser, topicId, null, (SerializableSupplier<CollaborationEngine>)((SerializableSupplier & Serializable)CollaborationEngine::getInstance));
    }

    public CollaborationMessageList(sys_user localUser, String topicId, CollaborationMessagePersister persister) {
        this(localUser, topicId, persister, (SerializableSupplier<CollaborationEngine>)((SerializableSupplier & Serializable)CollaborationEngine::getInstance));
    }

    CollaborationMessageList(sys_user localUser, String topicId, CollaborationMessagePersister persister, SerializableSupplier<CollaborationEngine> ceSupplier) {
        this.localUser = Objects.requireNonNull(localUser, "User cannot be null");
        this.ceSupplier = ceSupplier;
        this.persister = persister;
        this.setTopic(topicId);
    }
    public void setTopic(String topicId) {
        String currentTopic;
        //String string = currentTopic = this.messageManager != null ? this.messageManager.getTopicId() : null;
        String string; // string 변수를 선언
        if (this.messageManager != null) {
            currentTopic = this.messageManager.getTopicId(); // currentTopic에 topicId 할당
        } else {
            currentTopic = null; // currentTopic에 null 할당
        }
        string = currentTopic; // currentTopic 값을 string에 할당        
        if (Objects.equals(currentTopic, topicId)) {
            return;
        }
        if (this.messageManager != null) {
            this.messageManager.close();
            this.messageManager = null;
            this.unregisterSubmitter();
            this.messageCache.clear();
            this.refreshMessages();
        }
        if (topicId != null) {
            this.messageManager = new MessageManager((ConnectionContext)new ComponentConnectionContext((Component)this), this.localUser, topicId, this.persister, this.ceSupplier);
            this.messageManager.setMessageHandler(context -> {
                CollaborationMessage message = context.getMessage();
                this.messageCache.add(message);
                this.refreshMessages();
            });
            this.messageManager.setActivationHandler(this::onManagerActivation);
        }
        // 새로 연결된 사용자 추가
        topicUsers.add(this.localUser.getUserNm());  

        // 만약 사용자 목록을 별도로 관리하고 싶다면:
        //this.messageManager.setUserHandler(userId -> topicUsers.add(userId));  
        
    }
    public MessageManager getMessageManager() {
    	return this.messageManager;
    }
    public void setSubmitter(CollaborationMessageSubmitter submitter) {
        this.unregisterSubmitter();
        this.submitter = submitter;
        if (this.messageManager != null) {
            this.messageManager.setActivationHandler(this::onManagerActivation);
        }
    }

    private Registration onManagerActivation() {
        this.registerSubmitter();
        return (Registration & Serializable)() -> {
            this.unregisterSubmitter();
            this.refreshMessages();
        };
    }

    private void registerSubmitter() {
        if (this.submitter != null) {
            this.submitterRegistration = this.submitter.onActivation(this::appendMessage);
            Objects.requireNonNull(this.submitterRegistration, "The submitter should return a non-null registration object");
        }
    }

    private void unregisterSubmitter() {
        if (this.submitterRegistration != null) {
            this.submitterRegistration.remove();
            this.submitterRegistration = null;
        }
    }

    private CollaborationEngine getCollaborationEngine() {
        return (CollaborationEngine)this.ceSupplier.get();
    }

    void appendMessage(String text) {
        Objects.requireNonNull(text, "Cannot append a null message");
        CollaborationMessage message = new CollaborationMessage(this.localUser, text, this.getCollaborationEngine().getClock().instant());
        this.messageManager.submit(message);
    }

    public void setImageProvider(CollaborationAvatarGroup.ImageProvider imageProvider) {
        this.imageProvider = imageProvider;
        this.refreshMessages();
    }

    public CollaborationAvatarGroup.ImageProvider getImageProvider() {
        return this.imageProvider;
    }

    public void setMessageConfigurator(MessageConfigurator messageConfigurator) {
        this.messageConfigurator = messageConfigurator;
        this.refreshMessages();
    }

    public MessageConfigurator getMessageConfigurator() {
        return this.messageConfigurator;
    }

    private void refreshMessages() {
        List messageListItems = this.messageCache.stream().map(this::convertToMessageListItem).collect(Collectors.toList());
        ((MessageList)this.getContent()).setItems(messageListItems);
    }

    private MessageListItem convertToMessageListItem(CollaborationMessage message) {
        MessageListItem messageListItem = new MessageListItem(message.getText(), message.getTime(), message.getUser().getUserNm());
        if (this.imageProvider == null) {
            messageListItem.setUserImage(message.getUser().getImage());
        } else {
            messageListItem.setUserImageResource(this.imageProvider.getImageResource(message.getUser()));
        }
        messageListItem.setUserAbbreviation(message.getUser().getAbbreviation());
        messageListItem.setUserColorIndex(Integer.valueOf(this.getCollaborationEngine().getUserColorIndex(message.getUser())));
        if (this.messageConfigurator != null) {
            this.messageConfigurator.configureMessage(messageListItem, message.getUser());
        }
        return messageListItem;
    }

    static {
        UsageStatistics.markAsUsed((String)"vaadin-collaboration-engine/CollaborationMessageList", (String)"6.4");
    }


    // 토픽에 연결된 모든 사용자 반환
    public List<String> getTopicUsers() {
        return topicUsers; // 토픽에 연결된 사용자 목록
    }
}
