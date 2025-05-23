package egframe.frame.views.home;


import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexDirection;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import egframe.frame.service.AuthenticatedUser;
import egframe.frame.service.SysDBO;
import egframe.frame.views.MainLayout;
import egframe.frame.views.login.LoginView;

import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import jakarta.annotation.security.PermitAll;

@PageTitle("home")
@Route(value = "home", layout = MainLayout.class)
@PermitAll
@AnonymousAllowed
@SpringComponent
@UIScope 
public class HomeView extends FlexLayout  implements RouterLayout,BeforeEnterObserver  {
	private FlexLayout toplayout = new FlexLayout();
	private FlexLayout topleftlayout = new FlexLayout();
	private FlexLayout toprightlayout = new FlexLayout();
	
	private FlexLayout bottomlayout = new FlexLayout();
	private FlexLayout bottomleftlayout = new FlexLayout();
	private FlexLayout bottomrightlayout = new FlexLayout();
	
	private Button topleftbtn = new Button("자료실");
	private Button toprightbtn = new Button("문제풀기");
	private Button bottomleftbtn = new Button("인터넷 강의");
	private Button bottomrightbtn = new Button("대화방");
	AuthenticatedUser securityservice;
	private SysDBO sqlca;
	@Autowired
	private JdbcTemplate jdbcTemplate;	
//	public HomeView() {
	public HomeView(AuthenticatedUser securityService,SysDBO jdbc) {
		securityservice = securityService;
		setFlexDirection(FlexDirection.COLUMN);
		setSizeFull();
		topleftbtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		toprightbtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		bottomleftbtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		bottomrightbtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		topleftbtn.setWidth("120px");
		toprightbtn.setWidth("120px");
		bottomleftbtn.setWidth("120px");
		bottomrightbtn.setWidth("120px");
		add(toplayout,bottomlayout);
		setTop();
		setBottom();
		addEvent();
   	}
	@Override
	protected void onAttach(AttachEvent attachEvent) {
	}

	public void setTop() {
		toplayout.getStyle().set("border", "1px solid red");
		toplayout.setFlexDirection(FlexDirection.ROW);
		toplayout.setSizeFull();
		toplayout.add(topleftlayout,toprightlayout);
		setTopLeft();
		setTopRight();
	}
	public void setTopLeft() {
		topleftlayout.getStyle().set("border", "1px solid red");
		topleftlayout.setFlexDirection(FlexDirection.ROW);
		topleftlayout.setSizeFull();
		topleftlayout.addClassName("topleftlayout-background"); 
		topleftlayout.setFlexDirection(FlexDirection.ROW); 				// 가로 방향 (기본값)
		topleftlayout.setJustifyContentMode(JustifyContentMode.END); 	// 주축(가로) 오른쪽 정렬
		topleftlayout.setAlignItems(Alignment.END); 						// 교차축(세로) 아래 정렬
		topleftlayout.add(topleftbtn);
	}
	public void setTopRight() {
		toprightlayout.getStyle().set("border", "1px solid red");
		toprightlayout.setFlexDirection(FlexDirection.ROW);
		toprightlayout.setSizeFull();
		toprightlayout.addClassName("toprightlayout-background"); 
		toprightlayout.setFlexDirection(FlexDirection.COLUMN); 			// 가로 방향 (기본값)
		toprightlayout.setJustifyContentMode(JustifyContentMode.END); 	// 주축(가로) 오른쪽 정렬
		toprightlayout.setAlignItems(Alignment.START); 						// 교차축(세로) 아래 정렬
		toprightlayout.add(toprightbtn);
	}
	public void setBottom() {
		bottomlayout.getStyle().set("border", "1px solid red");
		bottomlayout.setFlexDirection(FlexDirection.ROW);
		bottomlayout.setSizeFull();
		bottomlayout.add(bottomleftlayout,bottomrightlayout);
		setBottomLeft();
		setBottomRight();
	}
	public void setBottomLeft() {
		bottomleftlayout.getStyle().set("border", "1px solid red");
		bottomleftlayout.setFlexDirection(FlexDirection.ROW);
		bottomleftlayout.setSizeFull();
		bottomleftlayout.addClassName("bottomleftlayout-background"); 
		bottomleftlayout.setFlexDirection(FlexDirection.ROW); // 가로 방향 (기본값)
		bottomleftlayout.setJustifyContentMode(JustifyContentMode.END); // 주축(가로) 오른쪽 정렬
		bottomleftlayout.setAlignItems(Alignment.START); // 교차축(세로) 아래 정렬
		bottomleftlayout.add(bottomleftbtn);
	}
	public void setBottomRight() {
		bottomrightlayout.getStyle().set("border", "1px solid red");
		bottomrightlayout.setFlexDirection(FlexDirection.ROW);
		bottomrightlayout.setSizeFull();
		bottomrightlayout.addClassName("bottomrightlayout-background"); 
		bottomrightlayout.setFlexDirection(FlexDirection.ROW); // 가로 방향 (기본값)
		bottomrightlayout.setJustifyContentMode(JustifyContentMode.START); // 주축(가로) 오른쪽 정렬
		bottomrightlayout.setAlignItems(Alignment.START); // 교차축(세로) 아래 정렬
		bottomrightlayout.add(bottomrightbtn);
	}
	public void addEvent() {
		topleftbtn.addClickListener(e->{
   		    UI ui = this.getUI().get();

		    Map<String, List<String>> parametersMap = new HashMap<>();

		    Location location = UI.getCurrent().getInternals().getActiveViewLocation();
		    String currentRoute = location.getPath();
		    parametersMap.put("beforeroute", Collections.singletonList(currentRoute != null ? currentRoute.toString() : ""));
		    
		    QueryParameters queryParameters = new QueryParameters(parametersMap);
		    
		    ui.navigate("pdsmain", queryParameters);		
		});
		toprightbtn.addClickListener(e->{
   		    UI ui = this.getUI().get();

		    Map<String, List<String>> parametersMap = new HashMap<>();

		    Location location = UI.getCurrent().getInternals().getActiveViewLocation();
		    String currentRoute = location.getPath();
		    parametersMap.put("beforeroute", Collections.singletonList(currentRoute != null ? currentRoute.toString() : ""));
		    
		    QueryParameters queryParameters = new QueryParameters(parametersMap);
		    
		    ui.navigate("exmmain", queryParameters);		
		});
		bottomleftbtn.addClickListener(e->{
			
		});
		bottomrightbtn.addClickListener(e->{
			
		});
	}
	@Override
	public void beforeEnter(BeforeEnterEvent event) {
	}
}