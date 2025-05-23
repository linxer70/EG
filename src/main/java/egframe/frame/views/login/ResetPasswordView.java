package egframe.frame.views.login;

import egframe.frame.service.PasswordResetService;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import org.springframework.beans.factory.annotation.Autowired;
@AnonymousAllowed
@PageTitle("Reset Password")
@Route("reset-password")
public class ResetPasswordView extends VerticalLayout {

    private TextField emailField;
    private Button resetButton;
    private final PasswordResetService passwordResetService; 
    
    @Autowired
    public ResetPasswordView(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;

        emailField = new TextField("Enter your email address");
        resetButton = new Button("Reset Password", event -> handlePasswordReset());

        add(emailField, resetButton);
    }

    // 비밀번호 재설정 요청 처리
    private void handlePasswordReset() {
        String email = emailField.getValue();

        // 비밀번호 재설정 이메일을 전송
        boolean emailSent = passwordResetService.sendPasswordResetEmail(email);

        if (emailSent) {
            Notification.show("A password reset link has been sent to your email.");
        } else {
            Notification.show("There was an issue sending the reset email. Please try again.");
        }
    }
}
/*
@Route("reset-password")
public class ResetPasswordView extends VerticalLayout {

    private PasswordField newPasswordField;
    private PasswordField confirmPasswordField;
    private Button resetButton;

    private final PasswordResetService passwordResetService;  // PasswordResetService 주입

    // PasswordResetService를 생성자로 주입받음
    @Autowired
    public ResetPasswordView(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;

        newPasswordField = new PasswordField("New Password");
        confirmPasswordField = new PasswordField("Confirm Password");
        resetButton = new Button("Reset Password", event -> handlePasswordReset());

        add(newPasswordField, confirmPasswordField, resetButton);

        // URL에서 토큰을 추출하여 처리할 수 있는 로직 구현
        String token = getResetTokenFromQuery();
        if (token != null) {
            // 토큰을 사용하여 비밀번호 재설정 상태를 처리
            // (예: 토큰 유효성 검사)
            boolean validToken = passwordResetService.isValidResetToken(token);
            if (!validToken) {
                Notification.show("Invalid or expired token");
            }
        }
    }

    // URL 쿼리 파라미터에서 reset token을 가져오는 방법
    private String getResetTokenFromQuery() {
        QueryParameters params = getUI().get().getInternals().getActiveViewLocation().getQueryParameters();
        return params.getParameters().getOrDefault("token", null).stream().findFirst().orElse(null);
    }

    // 새 비밀번호 처리
    private void handlePasswordReset() {
        String newPassword = newPasswordField.getValue();
        String confirmPassword = confirmPasswordField.getValue();

        if (newPassword.equals(confirmPassword)) {
            // 비밀번호 변경 처리
            boolean success = passwordResetService.resetPassword(newPassword);
            if (success) {
                Notification.show("Password has been successfully reset.");
            } else {
                Notification.show("Error resetting the password. Please try again.");
            }
        } else {
            Notification.show("Passwords do not match.");
        }
    }
}
*/