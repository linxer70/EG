/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.vaadin.experimental.FeatureFlags
 *  com.vaadin.flow.server.VaadinContext
 *  com.vaadin.flow.server.VaadinService
 */
package egframe.common.SysChat;

import egframe.common.SysChat.Backend;
import egframe.common.SysChat.BackendFeatureNotEnabledException;
import egframe.common.SysChat.LicenseEventHandler;
import egframe.common.SysChat.LicenseStorage;
import egframe.common.SysChat.LocalBackend;
import com.vaadin.experimental.FeatureFlags;
import com.vaadin.flow.server.VaadinContext;
import com.vaadin.flow.server.VaadinService;
import java.util.Objects;
import java.util.concurrent.ExecutorService;


public class CollaborationEngineConfiguration {
    static final String BEACON_PATH_CONFIG_PROPERTY = "ce.beaconPath";
    static final String DEFAULT_BEACON_PATH = "/";
    static final boolean DEFAULT_AUTOMATICALLY_ACTIVATE_PUSH = true;
    static final int DEFAULT_EVENT_LOG_SUBSCRIBE_RETRY_ATTEMPTS = 40;
    private LicenseEventHandler licenseEventHandler;
    private VaadinService vaadinService;
    private String configuredDataDir;
    private String configuredBeaconPath = "/";
    private boolean automaticallyActivatePush = true;
    private Backend backend = new LocalBackend();
    private ExecutorService executorService;
    private LicenseStorage licenseStorage;

    @Deprecated(since="6.3", forRemoval=true)
    public CollaborationEngineConfiguration(LicenseEventHandler licenseEventHandler) {
        this.licenseEventHandler = Objects.requireNonNull(licenseEventHandler, "The license event handler cannot be null");
    }

    public CollaborationEngineConfiguration() {
    }

    @Deprecated(since="6.3", forRemoval=true)
    public LicenseEventHandler getLicenseEventHandler() {
        return this.licenseEventHandler;
    }

    @Deprecated(since="6.3", forRemoval=true)
    public String getDataDir() {
        return this.configuredDataDir;
    }

    @Deprecated(since="6.3", forRemoval=true)
    public void setDataDir(String dataDir) {
        this.configuredDataDir = dataDir;
    }

    public String getBeaconPath() {
        return this.configuredBeaconPath;
    }

    public void setBeaconPath(String beaconPath) {
        this.configuredBeaconPath = beaconPath;
    }

    public void setAutomaticallyActivatePush(boolean automaticallyActivatePush) {
        this.automaticallyActivatePush = automaticallyActivatePush;
    }

    public boolean isAutomaticallyActivatePush() {
        return this.automaticallyActivatePush;
    }

    void setVaadinService(VaadinService vaadinService) {
        this.vaadinService = vaadinService;
        this.requireBackendFeatureEnabled();
    }

    void requireBackendFeatureEnabled() {
        if (this.vaadinService != null && !this.backend.getClass().equals(LocalBackend.class) && !FeatureFlags.get((VaadinContext)this.vaadinService.getContext()).isEnabled(FeatureFlags.COLLABORATION_ENGINE_BACKEND)) {
            throw new BackendFeatureNotEnabledException();
        }
    }

    public void setBackend(Backend backend) {
        this.backend = Objects.requireNonNull(backend);
        this.requireBackendFeatureEnabled();
    }

    public Backend getBackend() {
        return this.backend;
    }

    @Deprecated(since="6.3", forRemoval=true)
    public LicenseStorage getLicenseStorage() {
        return this.licenseStorage;
    }

    @Deprecated(since="6.3", forRemoval=true)
    public void setLicenseStorage(LicenseStorage licenseStorage) {
        this.licenseStorage = licenseStorage;
    }

    public ExecutorService getExecutorService() {
        return this.executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    String getBeaconPathProperty() {
        String beaconPath = this.vaadinService.getDeploymentConfiguration().getStringProperty(BEACON_PATH_CONFIG_PROPERTY, null);
        if (beaconPath == null) {
            beaconPath = this.configuredBeaconPath;
        }
        return beaconPath;
    }

    int getEventLogSubscribeRetryAttempts() {
        return 40;
    }
}
