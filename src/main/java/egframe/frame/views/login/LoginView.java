package egframe.frame.views.login;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.AuthenticationException;

import egframe.common.tinymce.Plugin;
import egframe.frame.service.AuthenticatedUser;

import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexDirection;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.html.Image;
@AnonymousAllowed
@PageTitle("Login")
@Route(value = "login")
@UIScope
@SpringComponent
public class LoginView extends FlexLayout implements RouterLayout,BeforeEnterObserver{
	@Autowired
	private JdbcTemplate jdbcTemplate;	
    private final AuthenticatedUser authService;
    //private VerticalLayout loginform = new VerticalLayout() ;
    private FlexLayout form = new FlexLayout();
    private Div contentArea = new Div();   
    private final AuthenticationContext context;
    public LoginView(AuthenticatedUser authService,AuthenticationContext authenticationContext,JdbcTemplate jdbcTemplate) {
       this.authService = authService;
		this.context = authenticationContext;
		this.jdbcTemplate = jdbcTemplate;
       LoginForm loginForm = new LoginForm();
       loginForm.setAction("login");
       Div wrapper = new Div(loginForm);  
       getStyle().set("background-color", "#f0f0f0");
       wrapper.getStyle().set("background-color", "#f0f0f0");
       loginForm.getStyle().set("background-color", "#f0f0f0");
       form.setFlexDirection(FlexDirection.COLUMN);
       form.setSizeFull();
       setSizeFull();
       addClassName("login-background"); 
       add(wrapper,form);
    }
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Map<String, List<String>> params = event.getLocation().getQueryParameters().getParameters();

        if (params.containsKey("error")) {
            String errorType = params.get("error").get(0); // 첫 번째 값 가져오기

            switch (errorType) {
                case "bad-password":
                    Notification.show("비밀번호가 올바르지 않습니다.");
                    break;
                case "user-not-found":
                    Notification.show("해당 사용자가 존재하지 않습니다.");
                    break;
                default:
        			Notification notification = Notification.show("최초 사용자를 등록 합니다.\n 비밀번호는 0000 입니다.");
        			notification.setPosition(Position.MIDDLE); // 화면 중앙에 표시
        		    notification.setDuration(3000); // 3초 동안 표시
                    
            }
        }
    }    private void authenticate(String username, String password) {
        try {
            authService.authenticate(username, password);
            UI.getCurrent().navigate("home");
        } catch (AuthenticationException e) {
            Notification.show("Invalid credentials", 3000, Position.TOP_CENTER);
        }
    }
    @Override
    public void showRouterLayoutContent(HasElement content) {
        contentArea.removeAll();
        contentArea.getElement().appendChild(content.getElement());
    }   
    private Map<Class<?>, Object> dependencies = new HashMap<>();
    public void addDependency(Class<?> type, Object instance) {
        dependencies.put(type, instance);
    }
    
    // 동적 뷰 생성
    public Component loadView(Class<? extends Component> viewClass) throws Exception {
        Constructor<?>[] constructors = viewClass.getDeclaredConstructors();
        Constructor<?> constructor = constructors[2];
        
        // 생성자 파라미터 준비
        Class<?>[] paramTypes = constructor.getParameterTypes();
        Object[] args = new Object[paramTypes.length];
        
        for (int i = 0; i < paramTypes.length; i++) {
            args[i] = dependencies.get(paramTypes[i]); // 등록된 의존성 사용
        }
        
        return (Component) constructor.newInstance(args);
    }}
