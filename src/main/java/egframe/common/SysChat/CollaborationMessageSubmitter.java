package egframe.common.SysChat;

import com.vaadin.flow.shared.Registration;
import java.io.Serializable;

@FunctionalInterface
public interface CollaborationMessageSubmitter
extends Serializable {
    public Registration onActivation(ActivationContext var1);

    public static interface ActivationContext
    extends Serializable {
        public void appendMessage(String var1);
    }
}
