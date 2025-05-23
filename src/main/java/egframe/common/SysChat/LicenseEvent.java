/*
 * Decompiled with CFR 0.152.
 */
package egframe.common.SysChat;

import egframe.common.SysChat.CollaborationEngine;
import java.text.MessageFormat;
import java.util.EventObject;

@Deprecated(since="6.3", forRemoval=true)
public class LicenseEvent
extends EventObject {
    private final LicenseEventType type;
    private final String message;

    LicenseEvent(CollaborationEngine collaborationEngine, LicenseEventType type, String message) {
        super(collaborationEngine);
        this.type = type;
        this.message = message;
    }

    public LicenseEventType getType() {
        return this.type;
    }

    public String getMessage() {
        return this.message;
    }

    @Override
    public CollaborationEngine getSource() {
        return (CollaborationEngine)super.getSource();
    }

    @Deprecated(since="6.3", forRemoval=true)
    public static enum LicenseEventType {
        GRACE_PERIOD_STARTED("The Vaadin Collaboration Engine license end-user quota has exceeded. Collaboration Engine has started a 30 day grace period ending on {0}, during which the quota is ten times bigger. This grace period gives time to react to the exceeding limit without impacting the user experience. Contact a Vaadin sales representative to obtain a license that fits the application needs."),
        GRACE_PERIOD_ENDED("The Vaadin Collaboration Engine grace period has ended. This means that the licensed end-user quota will be enforced to its original value and exceeding requests to access Collaboration Engine will be denied. Contact a Vaadin sales representative to obtain a license that fits the application needs."),
        LICENSE_EXPIRES_SOON("The Vaadin Collaboration Engine license will expire on {0}. Once the license is expired, collaborative features won't be accessible to the end-users until a new license is obtained. Check the license expiration date and contact a Vaadin sales representative to renew before it expires."),
        LICENSE_EXPIRED("The Vaadin Collaboration Engine license has expired. This means that collaborative features are not accessible to the end-users until a new license is obtained. Contact a Vaadin sales representative to renew the license and restore collaborative features.");

        private final String messageTemplate;

        private LicenseEventType(String messageTemplate) {
            this.messageTemplate = messageTemplate;
        }

        String createMessage(Object ... args) {
            return MessageFormat.format(this.messageTemplate, args);
        }
    }
}
