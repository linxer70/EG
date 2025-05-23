/*
 * Decompiled with CFR 0.152.
 */
package egframe.common.SysChat;

import egframe.common.SysChat.CollaborationMessage;
import java.io.Serializable;

@FunctionalInterface
public interface MessageHandler
extends Serializable {
    public void handleMessage(MessageContext var1);

    public static interface MessageContext
    extends Serializable {
        public CollaborationMessage getMessage();
    }
}
