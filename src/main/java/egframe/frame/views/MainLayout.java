package egframe.frame.views;
//package com.ebsol.ebframe.views;

import java.awt.FlowLayout;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import egframe.common.OSGiLauncher;
import egframe.common.SysTree;
import egframe.frame.entity.sys_country_cd;
import egframe.frame.service.AuthenticatedUser;
import egframe.frame.entity.UserMenu;
import egframe.frame.entity.UserMenuData;
import egframe.frame.entity.sys_module;
import egframe.frame.service.SysDBO;
import egframe.frame.entity.sys_user;
import egframe.frame.entity.sys_user_menu;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexDirection;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServlet;
//import com.vaadin.flow.server.ServletContext;
import com.vaadin.flow.server.VaadinServletContext;

import jakarta.annotation.security.PermitAll;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.servlet.ServletContext;

/**
 * The main view is a top-level placeholder for other views.
 */
@CssImport("./styles/views/main/main-view.css")
@JsModule("./styles/shared-styles.js")
@Route(value = "")//, layout = egframe.views.MainLayout.class)
@PermitAll
public class MainLayout extends AppLayout implements AfterNavigationObserver{
	private FlexLayout mainlayout = new FlexLayout();
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
	public MainLayout(AuthenticatedUser securityService,SysDBO jdbc) {
		securityservice = securityService;
		mainlayout.setFlexDirection(FlexDirection.COLUMN);
		mainlayout.setSizeFull();
		topleftbtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		toprightbtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		bottomleftbtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		bottomrightbtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		topleftbtn.setWidth("120px");
		toprightbtn.setWidth("120px");
		bottomleftbtn.setWidth("120px");
		bottomrightbtn.setWidth("120px");
		setContent(mainlayout);
		mainlayout.add(toplayout,bottomlayout);
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
   		    UI ui = this.getUI().get();

		    Map<String, List<String>> parametersMap = new HashMap<>();

		    Location location = UI.getCurrent().getInternals().getActiveViewLocation();
		    String currentRoute = location.getPath();
		    parametersMap.put("beforeroute", Collections.singletonList(currentRoute != null ? currentRoute.toString() : ""));
		    
		    QueryParameters queryParameters = new QueryParameters(parametersMap);
		    
		    ui.navigate("chatmain", queryParameters);		
			
		});
	}
	@Override
	public void afterNavigation(AfterNavigationEvent event) {
	}

}