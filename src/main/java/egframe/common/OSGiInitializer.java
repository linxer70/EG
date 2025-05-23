package egframe.common;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class OSGiInitializer implements ServletContextListener {
    private OSGiLauncher osgiLauncher;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Vaadin 애플리케이션 시작 시 OSGi 프레임워크 실행
        try {
            osgiLauncher = new OSGiLauncher();
            osgiLauncher.start();
            sce.getServletContext().setAttribute("osgiLauncher", osgiLauncher);
        } catch (Exception e) {
            throw new RuntimeException("OSGi 초기화 실패", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        OSGiLauncher launcher = (OSGiLauncher) sce.getServletContext().getAttribute("osgiLauncher");
        if (launcher != null) {
            try {
                launcher.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}