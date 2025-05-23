package egframe.frame.service;



import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.security.VaadinWebSecurity;

import egframe.common.SysChat.CollaborationEngineConfiguration;
import egframe.frame.views.login.LoginView;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
@EnableWebSecurity
@Configuration
@EnableWebSocketMessageBroker
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "egframe") 
@ConfigurationProperties(prefix = "vaadin.config")
public class SecurityConfiguration extends VaadinWebSecurity  implements WebSocketMessageBrokerConfigurer  {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // X-Frame-Options 헤더를 SAMEORIGIN으로 설정하여 동일 출처의 iframe 허용
        http.headers(headers -> headers
            .frameOptions(frameOptions -> frameOptions.sameOrigin())        )
        .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login") // ★ 반드시 절대 경로로 작성 ★
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .addLogoutHandler((request, response, auth) -> {
                    // Vaadin 세션 정리 (순서 중요!)
                    VaadinSession.getCurrent().close();
                })
            );
        super.configure(http);
        setLoginView(http, LoginView.class);
    }
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }    
    @Bean
    public CollaborationEngineConfiguration ceConfigBean() {
        CollaborationEngineConfiguration configuration = new CollaborationEngineConfiguration(
                licenseEvent -> {
                    // See <<ce.production.license-events>>
                });
        configuration.setDataDir("/Users/steve/vaadin/collaboration-engine/");
        return configuration;
    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
    }
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS();
    } 
}
