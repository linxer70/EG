package egframe;


import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import egframe.common.SysChat.CollaborationEngine;
import egframe.common.SysChat.CollaborationEngineConfiguration;
import egframe.common.SysChat.LicenseEventHandler;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServiceInitListener;
/*
 * 	최초 실행시 DB 초기화 실행 ex)초기 테이블및 초기 데이터 입력
 */
@Service
public class ApplicationServiceListener implements VaadinServiceInitListener {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationServiceListener.class);
	
	@Override
	public void serviceInit(ServiceInitEvent serviceInitEvent) {
	       VaadinService service = serviceInitEvent.getSource();

	        LicenseEventHandler licenseEventHandler = licenseEvent -> {
	            // See Notifications for Updating Licenses
	            switch (licenseEvent.getType()) {
	            case GRACE_PERIOD_STARTED:
	            case LICENSE_EXPIRES_SOON:
	                LOGGER.warn(licenseEvent.getMessage());
	                break;
	            case GRACE_PERIOD_ENDED:
	            case LICENSE_EXPIRED:
	                LOGGER.error(licenseEvent.getMessage());
	                break;
	            }
	            sendEmail("Vaadin Collaboration Kit license needs to be updated",
	                    licenseEvent.getMessage());
	        };

	        CollaborationEngineConfiguration configuration = new CollaborationEngineConfiguration(licenseEventHandler);
	        configuration.setDataDir("/Users/steve/vaadin/collaboration-engine/");
	        CollaborationEngine.configure(service, configuration);		
     }
	private void sendEmail(String subject, String content) {
        // Replace the following information:
        String from = "sender@gmail.com";
        String password = "*****"; // Read, for example, from encrypted config
                                   // file.
        String to = "receiver@gmail.com";
        String host = "smtp.gmail.com";

        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));
            message.setSubject(subject);
            message.setText(content);
            Transport.send(message);
        } catch (MessagingException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }	
}