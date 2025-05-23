package egframe.common;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
	@Override
	public void onAuthenticationFailure(jakarta.servlet.http.HttpServletRequest request,
			jakarta.servlet.http.HttpServletResponse response, AuthenticationException exception)
			throws IOException, jakarta.servlet.ServletException {

        // 원하는 파라미터 이름과 값을 설정
        String redirectUrl = "/login?error=bad-password";

        if (exception instanceof UsernameNotFoundException) {
            redirectUrl = "/login?error=user-not-found";
        }

        response.sendRedirect(redirectUrl);
		
	}
}
