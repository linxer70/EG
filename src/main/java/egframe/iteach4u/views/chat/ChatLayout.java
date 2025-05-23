package egframe.iteach4u.views.chat;
//package com.ebsol.ebframe.views;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import egframe.frame.entity.sys_country_cd;
import egframe.frame.service.AuthenticatedUser;
import egframe.frame.entity.UsrMaster;
import egframe.frame.service.SysDBO;
import egframe.iteach4u.entity.usy_class_cd;
import egframe.iteach4u.entity.usy_type_cd;
import egframe.iteach4u.service.Repository.Iteach4uService;
import jakarta.annotation.security.PermitAll;
import egframe.frame.entity.sys_user;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexDirection;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.component.popover.PopoverPosition;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParam;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**
 * The main view is a top-level placeholder for other views.
 */
@CssImport("./styles/views/main/main-view.css")
@JsModule("./styles/shared-styles.js")
@Route(value = "chatmain")//, layout = MainLayout.class)
//@Layout
@PermitAll
public class ChatLayout extends AppLayout implements AfterNavigationObserver{
	private static final long serialVersionUID = 1L;
	public SysDBO sysDBservice;
	private sys_user sysuser;
	private String group_cd;
	private String user_cd;
	private String module_id;
	private Long type_id;
	
	private VerticalLayout leftlayout = new VerticalLayout();
	private FlexLayout toplayout = new FlexLayout();
	private VerticalLayout mainlayout = new VerticalLayout();
	private VerticalLayout maincontent = new VerticalLayout();
	private HorizontalLayout footerlayout = new HorizontalLayout();
    private HorizontalLayout logoLayout = new HorizontalLayout();
	
	private DrawerToggle toggle = new DrawerToggle();
	
	private SideNavItem leftmenu;
	private SideNavItem pds01 = new SideNavItem("AI와 학습하기");
	private SideNavItem pds02 = new SideNavItem("AI와 문제풀기");
	
	private Button logout = new Button();
	private Button home = new Button("Home");
	private Image userimg = new Image();
	private Scroller leftscroller = new Scroller();
	private Button dropdownButton = new Button("설정");
	private VerticalLayout topmenulayout = new VerticalLayout();
	private Popover topmenupopover = new Popover();
	
	private ComboBox<usy_type_cd> type_combo = new ComboBox<>("분야선택");
	private List<usy_type_cd> type_combo_data;
	private usy_type_cd selectedtype;
	
	private ComboBox<sys_country_cd> country_combo = new ComboBox<>();
	private List<sys_country_cd> country_combo_data = new ArrayList<sys_country_cd>();
//	@Autowired
	private final AuthenticatedUser authenticatedUser;
//	@Autowired
	private Iteach4uService jdbc;
	private UsrMaster userinfo;
	private UserDetails userdetails;
	@Autowired
	public ChatLayout(AuthenticatedUser securityService,Iteach4uService _jdbc) {
		this.authenticatedUser = securityService;
		jdbc = _jdbc;  
		userdetails = authenticatedUser.getAuthenticatedUser();
		String username = userdetails.getUsername();
		userinfo = jdbc.findByUserId(username);
   		setTopLayout();
   		setLogoLayout();
		setLeftLayout();
		setMainLayout();
		setFooterLayout();
		addEvent();
		
		addToNavbar(toggle,dropdownButton,toplayout);
		addToDrawer(logoLayout,leftlayout);
		setContent(mainlayout);

		type_combo.setItemLabelGenerator(usy_type_cd::get_type_nm);
		type_combo_data = jdbc.getTypeList();
		type_combo.setItems(type_combo_data);
		selectedtype = type_combo_data.stream().filter(usy_type_cd -> usy_type_cd.getId().equals(userinfo.getType_id())).findFirst().orElse(null);
		if (selectedtype != null) {
			type_id = selectedtype.getId();
			type_combo.setValue(selectedtype); 
		}   
		
    }
	public ChatLayout() {
		this.authenticatedUser = null;
	
	}
	
	
	public void setTopLayout() {
		
		//dropdownButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
		dropdownButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		// 팝업 메뉴에 표시할 항목들 생성
		topmenulayout.add(new SideNavItem("환경설정", "usy/usyconfig",VaadinIcon.DASHBOARD.create()));

		// Popover 생성 및 설정
		topmenupopover.setTarget(dropdownButton);
		topmenupopover.setPosition(PopoverPosition.BOTTOM_START);
		topmenupopover.setModal(false);
		topmenupopover.setBackdropVisible(false);
		topmenupopover.setOverlayRole("menu");
		topmenupopover.add(topmenulayout);

		// 버튼 클릭 시 팝업 열기
		dropdownButton.addClickListener(e -> topmenupopover.open());
		
		
		toplayout.setFlexDirection(FlexDirection.ROW);
		toplayout.setSizeFull();
		//toplayout.getStyle().set("border", "1px solid red");
		userimg = new Image("themes/iteach4u/images/user.jpg", "photo");
		userimg.setWidth("50px");
		userimg.setHeight("40px");
		
		toplayout.add(userimg,country_combo);
	}
	public void setLeftLayout() {
		
		
		
		leftmenu = new SideNavItem("대화방");
		leftmenu.setClassName("leftmenu-title");
		leftlayout.setSizeFull();
		//leftlayout.getStyle().set("border", "1px solid blue");
		leftlayout.setAlignItems(Alignment.STRETCH);
		
		String username = "Admin Logout";//this.authenticatedUser.getAuthenticatedUser().getUsername();
		logout.setText(username);
		leftlayout.setFlexGrow(1, leftmenu); // nav가 공간 차지
		leftlayout.add(type_combo,leftmenu, logout);
		leftscroller = new Scroller(leftlayout);
 		leftscroller.setClassName(LumoUtility.Padding.SMALL);
 		leftscroller.setScrollDirection(Scroller.ScrollDirection.VERTICAL); // 세로 스크롤 활성화        
 		
 		setLeftMenu();
		
	}
	public void setMainLayout() {
		mainlayout.setSizeFull();

		maincontent.setWidth("100%");
		maincontent.setFlexGrow(1); // 남은 공간 차지
		maincontent.add(new Paragraph("메인뷰"));
		mainlayout.setAlignItems(Alignment.STRETCH);
		mainlayout.add(maincontent, footerlayout);
	}
    private void setLeftMenu() {
    	leftmenu.setPrefixComponent(VaadinIcon.FOLDER.create());
    	leftmenu.setRouterIgnore(true);
       leftmenu.setExpanded(true);

       pds01.setPrefixComponent(VaadinIcon.FOLDER.create());
       pds01.setRouterIgnore(true);
       //pds01.setExpanded(true);
       
   		
       pds02.setPrefixComponent(VaadinIcon.FOLDER.create());
       pds02.setRouterIgnore(true);
       //pds02.setExpanded(true);
       
       leftmenu.addItem(pds01,pds02);
     
    }	
    
    private void setSubMenu() {
    	
    	List<usy_class_cd> class_data = jdbc.getClassList(type_id);
    	int k = class_data.size();
		pds01.removeAll();
		pds02.removeAll();
   	if(k>0) {
    	}else {
    		return;
    	}
    	for (int i = 0 ;i < k ;i++) {
    		pds01.addItem(new SideNavItem(class_data.get(i).get_class_nm(),
    				"chat/mainview/"+String.valueOf(class_data.get(i).get_type_id())+"/"+String.valueOf(class_data.get(i).get_type_nm())+"/"+
					String.valueOf(class_data.get(i).getId())+"/"+String.valueOf(class_data.get(i).get_class_nm())+"",
					VaadinIcon.DASHBOARD.create())
    				);
    		pds02.addItem(new SideNavItem(class_data.get(i).get_class_nm(),
    				"chat/examview/"+String.valueOf(class_data.get(i).get_type_id())+"/"+String.valueOf(class_data.get(i).get_type_nm())+"/"+
					String.valueOf(class_data.get(i).getId())+"/"+String.valueOf(class_data.get(i).get_class_nm())+"",
					VaadinIcon.DASHBOARD.create())
    				);
    				
    	}
    	/*
  		pds01.addItem(new SideNavItem("1학년", "pds/dataview01",VaadinIcon.DASHBOARD.create()));
  		pds01.addItem(new SideNavItem("2학년", "pds/dataview02",VaadinIcon.DASHBOARD.create()));
  		pds01.addItem(new SideNavItem("3학년", "pds/dataview03",VaadinIcon.DASHBOARD.create()));
   		
       pds02.addItem(new SideNavItem("1학년", "pds/dataview01",VaadinIcon.DASHBOARD.create()));
   		pds02.addItem(new SideNavItem("2학년", "pds/dataview02",VaadinIcon.DASHBOARD.create()));
   		pds02.addItem(new SideNavItem("3학년", "pds/dataview03",VaadinIcon.DASHBOARD.create()));
    	*/
    }
    private void setFooterLayout() {
        footerlayout.setWidth("100%");
        footerlayout.addClassName("footer-style");
        footerlayout.add(new Span("© 2025 Iteach4U"));
    }  
    private void setLogoLayout() {
        logoLayout.setId("logo");
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        Image logo = new Image("./themes/iteach4u/images/logo.gif", "logo");
        logoLayout.add(logo);
        //this.logoLayout.add(new H1("ITeach4U"));
        logo.addClickListener(e->{
    		if (this.getUI().isPresent()) {
    			
    		    UI ui = this.getUI().get();

    		    Map<String, List<String>> parametersMap = new HashMap<>();

    		    Location location = UI.getCurrent().getInternals().getActiveViewLocation();
    		    String currentRoute = location.getPath();
    		    parametersMap.put("beforeroute", Collections.singletonList(currentRoute != null ? currentRoute.toString() : ""));
    		    
    		    QueryParameters queryParameters = new QueryParameters(parametersMap);
    		    
    		    ui.navigate("", queryParameters);
    		} else {
    		    // UI가 존재하지 않을 때의 처리 로직
    		}
        	
        });
    }

    public void addEvent() {
    	type_combo.addValueChangeListener(e->{
    		setSubMenu();
    	});
   		logout.addClickListener(e ->{
   			authenticatedUser.logout();
   	    });
   		home.addClickListener(e ->{
    		if (this.getUI().isPresent()) {
    			
    		    UI ui = this.getUI().get();

    		    Map<String, List<String>> parametersMap = new HashMap<>();

    		    Location location = UI.getCurrent().getInternals().getActiveViewLocation();
    		    String currentRoute = location.getPath();
    		    parametersMap.put("beforeroute", Collections.singletonList(currentRoute != null ? currentRoute.toString() : ""));
    		    
    		    QueryParameters queryParameters = new QueryParameters(parametersMap);
    		    
    		    ui.navigate("home", queryParameters);
    		} else {
    		    // UI가 존재하지 않을 때의 처리 로직
    		}
   	    });
     }
	@Override
	public void afterNavigation(AfterNavigationEvent event) {
	}
}