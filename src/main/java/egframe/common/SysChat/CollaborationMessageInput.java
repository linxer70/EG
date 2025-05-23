/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.vaadin.flow.component.ComponentEventListener
 *  com.vaadin.flow.component.Composite
 *  com.vaadin.flow.component.Focusable
 *  com.vaadin.flow.component.HasEnabled
 *  com.vaadin.flow.component.HasSize
 *  com.vaadin.flow.component.HasStyle
 *  com.vaadin.flow.component.messages.MessageInput
 *  com.vaadin.flow.component.messages.MessageInputI18n
 *  com.vaadin.flow.component.shared.HasTooltip
 *  com.vaadin.flow.component.shared.Tooltip
 *  com.vaadin.flow.internal.UsageStatistics
 *  com.vaadin.flow.shared.Registration
 */
package egframe.common.SysChat;

import egframe.common.SysChat.CollaborationMessageList;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.HasEnabled;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasStyle;
import egframe.common.SysChat.MessageInput;
import egframe.common.SysChat.MessageInputI18n;
import com.vaadin.flow.component.shared.HasTooltip;
import com.vaadin.flow.component.shared.Tooltip;
import com.vaadin.flow.internal.UsageStatistics;
import com.vaadin.flow.shared.Registration;
import java.io.Serializable;
import java.util.Objects;
public class CollaborationMessageInput extends Composite<MessageInput>
        implements Focusable<CollaborationMessageInput>, HasSize, HasStyle,
        HasEnabled, HasTooltip {

    static {
        UsageStatistics.markAsUsed(
                CollaborationEngine.COLLABORATION_ENGINE_NAME
                        + "/CollaborationMessageInput",
                CollaborationEngine.COLLABORATION_ENGINE_VERSION);
    }

    public CollaborationMessageInput(CollaborationMessageList list) {
    		MessageInput messageInput = getContent();
    		MessageInputI18n i18n = new MessageInputI18n();
    		i18n.setSend("보내기");
    		messageInput.setI18n(i18n);
        Objects.requireNonNull(list,
                "A list instance to connect this component to is required");
        messageInput.setEnabled(false);
        list.setSubmitter(activationContext -> {
        	messageInput.setEnabled(true);
            Registration registration =messageInput.addSubmitListener(
                    event -> activationContext.appendMessage(event.getValue()));
            return () -> {
                registration.remove();
                messageInput.setEnabled(false);
            };
        });
    }

    public MessageInputI18n getI18n() {
        return getContent().getI18n();
    }

    public void setI18n(MessageInputI18n i18n) {
        getContent().setI18n(i18n);
    }

    public Tooltip setTooltipText(String text) {
        return getContent().setTooltipText(text);
    }

    public Tooltip getTooltip() {
        return getContent().getTooltip();
    }
}
