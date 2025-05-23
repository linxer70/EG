package egframe.frame.views.login;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
@AnonymousAllowed
@PageTitle("register")
@Route("register")
public class RegisterForm extends VerticalLayout {
    public RegisterForm() {
        TextField usernameField = new TextField("사용자 이름");
        PasswordField passwordField = new PasswordField("비밀번호");
        Button registerButton = new Button("등록");

        registerButton.addClickListener(event -> {
            String username = usernameField.getValue();
            String password = passwordField.getValue();
            
            // 사용자 정보 저장 로직 (예: DB 연동)
            Notification.show(username + " 사용자 등록 완료!");
        });

        add(usernameField, passwordField, registerButton);
    }
}