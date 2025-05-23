/*
 * Decompiled with CFR 0.152.
 */
package egframe.common.SysChat;

public class BackendFeatureNotEnabledException
extends RuntimeException {
    BackendFeatureNotEnabledException() {
        super("The Backend API is currently an experimental feature and needs to be explicitly enabled. The feature can be enabled using the Vaadin dev-mode Gizmo, in the experimental features tab, or by adding a `src/main/resources/vaadin-featureflags.properties` file with the following content: `com.vaadin.experimental.collaborationEngineBackend=true`");
    }
}
