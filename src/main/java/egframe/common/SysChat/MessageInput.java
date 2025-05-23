package egframe.common.SysChat;


import java.util.Objects;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.HasEnabled;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import egframe.common.SysChat.MessageInputI18n;
import com.vaadin.flow.component.shared.HasTooltip;
import com.vaadin.flow.internal.JsonUtils;
import com.vaadin.flow.shared.Registration;

@Tag("vaadin-message-input")
@NpmPackage(value = "@vaadin/polymer-legacy-adapter", version = "24.6.0")
@JsModule("@vaadin/polymer-legacy-adapter/style-modules.js")
@JsModule("@vaadin/message-input/src/vaadin-message-input.js")
@NpmPackage(value = "@vaadin/message-input", version = "24.6.0")
public class MessageInput extends Component implements Focusable<MessageInput>,
        HasSize, HasStyle, HasEnabled, HasTooltip {

    private MessageInputI18n i18n;
    @DomEvent("submit")
    public static class SubmitEvent extends ComponentEvent<MessageInput> {

        private final String value;
        public SubmitEvent(MessageInput source, boolean fromClient,@EventData("event.detail.value") String value) {
            super(source, fromClient);
            this.value = value;
            System.out.println("source="+source+": fromclient="+fromClient+": value = "+value);
        }
        public String getValue() {
            return value;
        }
    }

    public MessageInput() {
    }

    public MessageInput(ComponentEventListener<SubmitEvent> listener) {
        addSubmitListener(listener);
    }

    public Registration addSubmitListener(
            ComponentEventListener<SubmitEvent> listener) {
        return addListener(SubmitEvent.class, listener);
    }

    public MessageInputI18n getI18n() {
        return i18n;
    }

    public void setI18n(MessageInputI18n i18n) {
        Objects.requireNonNull(i18n, "The i18n object should not be null");
        this.i18n = i18n;
        getElement().setPropertyJson("i18n", JsonUtils.beanToJson(i18n));
    }
}
