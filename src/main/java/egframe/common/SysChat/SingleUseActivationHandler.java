/*
 * Decompiled with CFR 0.152.
 */
package egframe.common.SysChat;

import egframe.common.SysChat.ActionDispatcher;
import egframe.common.SysChat.ActivationHandler;

class SingleUseActivationHandler
implements ActivationHandler {
    private ActivationHandler activationHandler;

    SingleUseActivationHandler(ActivationHandler activationHandler) {
        this.activationHandler = activationHandler;
    }

    public void accept(ActionDispatcher actionDispatcher) {
        if (this.activationHandler == null) {
            return;
        }
        this.activationHandler.accept(actionDispatcher);
        this.activationHandler = null;
    }
}
