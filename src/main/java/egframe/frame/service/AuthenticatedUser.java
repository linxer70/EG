package egframe.frame.service;

import egframe.frame.entity.sys_user;
import egframe.frame.service.SysDBO;
import egframe.frame.views.login.LoginView;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.security.AuthenticationContext;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AuthenticatedUser {

    private final SysDBO sysuserservice;
    private final AuthenticationContext authenticationContext;
    private UserDetails userdetails ;
    Authentication authentication ;
    private final UserDetailsServiceImpl userDetailsService;
    private Optional<Object> object ;
    public sys_user sysuser;
    private boolean isUser = false;
    public AuthenticatedUser(UserDetailsServiceImpl userDetailsService,AuthenticationContext authenticationContext, SysDBO sysuserservice) {
        this.sysuserservice = sysuserservice;
        this.authenticationContext = authenticationContext;
		this.userDetailsService = userDetailsService;
    }
    @Autowired
    private PasswordEncoder passwordEncoder;
    public SysDBO getSysDbo() {
    	return sysuserservice;
    }
    @Transactional
    public Optional<Object> get() {
    	Optional<UserDetails> authenticatedUser = authenticationContext.getAuthenticatedUser(UserDetails.class);
    	if (authenticatedUser.isPresent()) {
    	    UserDetails userDetails = authenticatedUser.get();
    	    String username = userDetails.getUsername();
    	    Object userInfo = sysuserservice.getUserInfo(username);
    	    object = Optional.ofNullable(userInfo);
    	} else {
    	    object = Optional.empty();
    	}  
    	setAuthenticated(true);
    	return object;
    }
    public UserDetails getAuthenticatedUser() {
    	this.userdetails = authenticationContext.getAuthenticatedUser(UserDetails.class).get();
    	this.sysuser = this.sysuserservice.getSysUser(authenticationContext.getAuthenticatedUser(UserDetails.class).get().getUsername());
    	setAuthenticated(true);
    	return userdetails;
    }
    public void logout() {
        authenticationContext.logout();
        setAuthenticated(false);
    }

	public void setAuthenticated(boolean b) {
		this.isUser= b;
		}
	public boolean getAuthenticated() {
		return this.isUser;
	}
	
    public boolean authenticate(String username, String password) {
        try {
        	userdetails = userDetailsService.loadUserByUsername(username);
        } catch (Exception e) {
            return false;
        }

        if (!passwordEncoder.matches(password, userdetails.getPassword())) {
            return false;
        }

        authentication = new UsernamePasswordAuthenticationToken(
        		userdetails, 
        		userdetails.getPassword(), 
        		userdetails.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return true;
    }

}
