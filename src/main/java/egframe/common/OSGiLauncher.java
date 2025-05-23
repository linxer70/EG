package egframe.common;

import java.util.HashMap;
import java.util.Map;

import org.apache.felix.framework.Felix;
import org.apache.felix.framework.FrameworkFactory;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;  
public class OSGiLauncher {
    private static Felix felix;

    // 프레임워크 시작 메서드
    public void start() throws Exception {
        Map<String, String> config = new HashMap<>();
        config.put(Constants.FRAMEWORK_STORAGE_CLEAN, "onFirstInit");
        FrameworkFactory factory = new FrameworkFactory();
        felix = (Felix) factory.newFramework(config);
        felix.start(); // 프레임워크 시작
    }

    // 프레임워크 정지 메서드 추가
    public void stop() throws BundleException, InterruptedException {
        if (felix != null) {
            felix.stop(); // 프레임워크 정지
            felix.waitForStop(0); // 완전히 종료될 때까지 대기
        }
    }

    // BundleContext 접근 메서드 (옵션)
    public static BundleContext getBundleContext() {
        return felix.getBundleContext();
    }
    

}
