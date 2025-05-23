package egframe.iteach4u.views.pds;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;

import egframe.common.SysGridLayout;
import egframe.common.SysWindow;
import egframe.common.WindowImpl;
import egframe.frame.entity.UsrMaster;
import egframe.frame.service.AuthenticatedUser;

import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.QueryParameters;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import egframe.iteach4u.entity.pds_past_hd;
import egframe.iteach4u.entity.usy_class_cd;
import egframe.iteach4u.entity.usy_subject_cd;
import egframe.iteach4u.entity.usy_unit_cd;
import egframe.iteach4u.gridForm.d_pds_past_hd;
import egframe.iteach4u.service.Repository.Iteach4uService;
import jakarta.annotation.security.PermitAll;

@Route(value = "pds/pastview/:type/:type_nm/:class/:class_nm", layout = PdsLayout.class)
@PermitAll
@Uses(Icon.class)
@PreserveOnRefresh
public class PdsPastView extends SysWindow  implements WindowImpl,BeforeEnterObserver {
	private FlexLayout popupText = new FlexLayout();
	private FlexLayout popuplayout = new FlexLayout();
	private Div popup = new Div();
	
	
	private ComboBox<usy_subject_cd> subject_combo = new ComboBox<>();
	private ComboBox<usy_unit_cd> unit_combo = new ComboBox<>();

	private Long type_id ;
	private Long id ;
	private Long class_id;
	private Long subject_id ;
	private Long unit_id;
	private String type_nm = "";
	private String class_nm = "";
	private String subject_nm = "";
	private String unit_nm = "";
	private String user_id = "";
	
	private List<usy_subject_cd> subject_combo_data;
	private List<usy_unit_cd> unit_combo_data;
	
	private usy_subject_cd selectedsubject;
	private usy_unit_cd selectedunit;
	
	//private SysDBO sqlca;
	
	private Button btn02 = new Button("과목");
	private Button btn03 = new Button("단원");
	private Button btn04 = new Button("작성자");
	public TextField user_filter		= new TextField ()	;
	
	public SysGridLayout hd_control ;
	public d_pds_past_hd hd ;
	public PersonFilter personFilter ;
	public HeaderRow headerRow;
	public ConfirmDialog  dialog;
	private Span status;
	
	private UserDetails userdetails;
	private UsrMaster userinfo;
	private Iteach4uService Jdbc;	
	private final AuthenticatedUser authenticatedUser;
	@Autowired
	public PdsPastView(AuthenticatedUser securityService,Iteach4uService jdbc) {
		this.authenticatedUser = securityService;
		Jdbc = jdbc;
		userdetails = authenticatedUser.getAuthenticatedUser();
		String username = userdetails.getUsername();
		userinfo = jdbc.findByUserId(username);
	   	buttonlayout.wnewsheet.setVisible(false);
	   	buttonlayout.wdeleteset.setVisible(false);
	   	buttonlayout.wsave.setVisible(false);
	   	//buttonlayout.winsert.setVisible(false);
	   	this.addClassName("main-layout");
	   	getElement().getStyle().set("background-color", "linear-gradient(45deg, #2196F3, #E3F2FD)");
    	setSizeFull();
    	setFlexDirection(FlexDirection.COLUMN);
	   	_init();
	   	_initsearch();
	   	_inithd();
	   	_getComboData();
	   	_addEvent();
    }
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
       var params = event.getRouteParameters();
    	if(params!=null) {
            String val  = params.get("type").orElse("0");
            title_type_id = Long.valueOf(val);
            type_id = title_type_id;
            val   = params.get("class").orElse("0");    	
            title_class_id = Long.valueOf(val);
            class_id = title_class_id;
            val   = params.get("type_nm").orElse("");    	
            title_type_nm = val;
            type_nm = title_type_nm;	
            val   = params.get("class_nm").orElse("");    	
            title_class_nm = val;
            class_nm = title_class_nm;
            title = "학습자료/"+title_type_nm+"/"+title_class_nm;
            setTitle(title);
            subject_combo_data =Jdbc.getSubjectList(title_class_id); 
            subject_combo.setItems(subject_combo_data);
            subject_id= null;
            unit_id =null;
            wRetrieve();
    	}
    	
        // QueryParameters 가져오기
        QueryParameters queryParameters = event.getLocation().getQueryParameters();
        // 특정 키의 파라미터 값 읽기
        Map<String, List<String>> parametersMap = queryParameters.getParameters();
        if(!parametersMap.isEmpty()) {

            if (parametersMap.containsKey("type_id")) {
                List<String> paramValues = parametersMap.get("type_id");
                if (!paramValues.isEmpty()) {
                    String val = paramValues.get(0);
                	if(val.equals("null")) {
                		val = "0" ;
                	}
                    type_id = Long.valueOf(val);
                }else {
                	type_id = Long.valueOf(0);
                }
            }
            if (parametersMap.containsKey("class_id")) {
                List<String> paramValues = parametersMap.get("class_id");
                if (!paramValues.isEmpty()) {
                    String val = paramValues.get(0);
                	if(val.equals("null")) {
                		val = "0" ;
                	}
                    class_id = Long.valueOf(val);
                }else {
                	class_id =  Long.valueOf(0);
                }
            }
            if (parametersMap.containsKey("subject_id")) {
                List<String> paramValues = parametersMap.get("subject_id");
                if (!paramValues.isEmpty()) {
                    String val = paramValues.get(0);
                	if(val.equals("null")) {
                		val = "0" ;
                	}
                    subject_id = Long.valueOf(val);
                }else {
                	subject_id = Long.valueOf(0);
                }
            }
            if (parametersMap.containsKey("unit_id")) {
                List<String> paramValues = parametersMap.get("unit_id");
                if (!paramValues.isEmpty()) {
                    String val = paramValues.get(0);
                    	if(val.equals("null")) {
                    		val = "0" ;
                    	}
                    unit_id = Long.valueOf(val);
                }else {
                	unit_id=Long.valueOf(0);
                }
            }
            if (parametersMap.containsKey("type_nm")) {
                List<String> paramValues = parametersMap.get("type_nm");
                if (!paramValues.isEmpty()) {
                    type_nm = paramValues.get(0);
                }else {
                	type_nm = null;
                }
            }
            if (parametersMap.containsKey("class_nm")) {
                List<String> paramValues = parametersMap.get("class_nm");
                if (!paramValues.isEmpty()) {
                    class_nm = paramValues.get(0);
                }else {
                	class_nm = null;
                }
            }
            if (parametersMap.containsKey("subject_nm")) {
                List<String> paramValues = parametersMap.get("subject_nm");
                if (!paramValues.isEmpty()) {
                    subject_nm = paramValues.get(0);
                }else {
                	subject_nm = null;
                }
            }
            if (parametersMap.containsKey("unit_nm")) {
                List<String> paramValues = parametersMap.get("unit_nm");
                if (!paramValues.isEmpty()) {
                	unit_nm  = paramValues.get(0);
                    
                }else {
                	unit_nm=null;
                }
            }
            
            //_getComboData();
            wRetrieve();
        }
    	
    }

    
   	public void _init() {
   		
   	}
   	private String previousRoute;   	
    private void beforeLeave(BeforeLeaveEvent event) {
        previousRoute = UI.getCurrent().getInternals().getActiveViewLocation().getPath();
    }   	
   	public void _initsearch() {
   		
    	searchlayout.setSizeFull();
    	searchlayout.getStyle().set("border", "1px solid yellow");

   		btn02.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
   		btn02.getStyle().set("flex-grow", "1"); 
   		btn02.getStyle().set("flex-grow", "0"); // 공간을 차지하지 않음
   		btn02.getStyle().set("flex-shrink", "0"); // 공간이 부족해도 줄어들지 않음
   		btn02.getStyle().set("flex-basis", "auto"); // 원래    		

	   	subject_combo.getStyle().set("flex-grow", "1"); 
	   	subject_combo.setItemLabelGenerator(usy_subject_cd::get_subject_nm);
	   	subject_combo.getStyle().set("flex-grow", "0"); // 공간을 차지하지 않음
	   	subject_combo.getStyle().set("flex-shrink", "0"); // 공간이 부족해도 줄어들지 않음
	   	subject_combo.getStyle().set("flex-basis", "auto"); // 원래    		

	   	btn03.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
   		btn03.getStyle().set("flex-grow", "1"); 
   		btn03.getStyle().set("flex-grow", "0"); // 공간을 차지하지 않음
   		btn03.getStyle().set("flex-shrink", "0"); // 공간이 부족해도 줄어들지 않음
   		btn03.getStyle().set("flex-basis", "auto"); // 원래    		
        
	   	unit_combo.getStyle().set("flex-grow", "1"); 
	   	unit_combo.setItemLabelGenerator(usy_unit_cd::get_unit_nm);
	   	unit_combo.getStyle().set("flex-grow", "0"); // 공간을 차지하지 않음
	   	unit_combo.getStyle().set("flex-shrink", "0"); // 공간이 부족해도 줄어들지 않음
	   	unit_combo.getStyle().set("flex-basis", "auto"); // 원래    		

	   	btn04.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
   		btn04.getStyle().set("flex-grow", "1"); 
   		btn04.getStyle().set("flex-grow", "0"); // 공간을 차지하지 않음
   		btn04.getStyle().set("flex-shrink", "0"); // 공간이 부족해도 줄어들지 않음
   		btn04.getStyle().set("flex-basis", "auto"); // 원래    		
        

	   	
	   	searchlayout.getStyle().set("flex-wrap", "wrap");
       searchlayout.setJustifyContentMode(JustifyContentMode.START);	//버튼을 우측부터 추가 
       searchlayout.setAlignItems(FlexLayout.Alignment.CENTER); 
        
       searchlayout.getStyle().set("gap", "5px");
       searchlayout.getStyle().set("align-items", "start"); // 세로 정렬
       searchlayout.getStyle().set("align-content", "start"); // 줄 정렬
       searchlayout.setHeight("auto");
       //searchlayout.getStyle().set("overflow-y", "auto"); // 세로 스크롤 활성화
        
        user_filter.setValueChangeMode(ValueChangeMode.EAGER);
        
        searchlayout.add(btn02,subject_combo,btn03,unit_combo,btn04,user_filter);
        
        add(searchlayout);
        searchlayout.setVisible(true);
   	}
    
    public void _getComboData() {
    	/*
        class_combo_data = sqlca.getClassCd(type_cd); 
        selectedclass = class_combo_data.stream().filter(usy_class_cd -> usy_class_cd.get_class_cd().equals(class_cd)).findFirst().orElse(null);
    	if (selectedclass != null) {
    		class_combo.setValue(selectedclass); 
    	}                    
    	
		subject_combo_data =sqlca.getSubjectCd(type_cd,class_cd); 
		selectedsubject = subject_combo_data.stream().filter(usy_subject_cd -> usy_subject_cd.get_subject_cd().equals(subject_cd)).findFirst().orElse(null);
		if (selectedsubject != null) {
			subject_combo.setValue(selectedsubject); 
		}   
		
		unit_combo_data =sqlca.getUnitCd(type_cd,class_cd,subject_cd); 
		selectedunit = unit_combo_data.stream().filter(usy_unit_cd -> usy_unit_cd.get_unit_cd().equals(class_cd)).findFirst().orElse(null);
		if (selectedunit != null) {
			unit_combo.setValue(selectedunit); 
		}                    
		*/
            	
    }
    	
    public void _addEvent() {
    	subject_combo.addAttachListener(e->{
    		 System.out.println("데이터 로드 중...");
    		 subject_combo.setItems(subject_combo_data);
    	});
	   	subject_combo.addValueChangeListener(e->{
	   		usy_subject_cd val = new usy_subject_cd();
	   		val = e.getValue();
	   		if(val== null) {
	   			return ;
		   	}else {
		   		subject_id = val.getId();
	   		}
	   		unit_combo.clear();
	   		unit_combo.setItems(Jdbc.getUsyUnitCdList(subject_id));
	    });
	   	unit_combo.addValueChangeListener(e->{
	   		usy_unit_cd val = new usy_unit_cd();
	   		val = e.getValue();
	   		if(val== null) {
	   			return ;
		   	}else {
		   		unit_id = val.getId();
		   	}
        	wRetrieve();
        });
    }
    
    public void _inithd() {
    	hd = new d_pds_past_hd(Jdbc);
	   	hd_control = new SysGridLayout(hd);
	   	hd_control.setHeight("auto");
	    hd_control.setHeightFull();
	    add(hd_control);
	    hd_control._footerlayout.setVisible(true);
    }
    
   	private static class PersonFilter {
   		
    	        private final GridListDataView<pds_past_hd> dataView;

    	        private String _user_id;
    	        private String _title;

    	        public PersonFilter(GridListDataView<pds_past_hd> dataView) {
    	            this.dataView = dataView;
    	            this.dataView.addFilter(this::test);
    	        }

    	        public void set_user_id(String user_id) {
    	            this._user_id = user_id;
    	            this.dataView.refreshAll();
    	        }

    	        public void set_title(String title) {
    	            this._title = title;
    	            this.dataView.refreshAll();
    	        }

     	        public boolean test(pds_past_hd person) {
    	            boolean matchesUser = matches(person.get_user_id(), _user_id);
    	            boolean matchesTitle = matches(person.get_title(),_title);

    	            return matchesUser && matchesTitle ;
    	        }

    	        private boolean matches(String value, String searchTerm) {
    	            boolean val = searchTerm == null || searchTerm.isEmpty()|| value.toLowerCase().contains(searchTerm.toLowerCase());
    	            return val;
    	        }
    	        
   		}
    	
    private static Component createFilterHeader(String labelText,Consumer<String> filterChangeConsumer) {
    	    NativeLabel label = new NativeLabel(labelText);
    	    label.getStyle().set("padding-top", "var(--lumo-space-m)").set("font-size", "var(--lumo-font-size-xs)");
    	    TextField textField = new TextField();
    	    textField.setValueChangeMode(ValueChangeMode.EAGER);
    	    textField.setClearButtonVisible(true);
    	    textField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
    	    textField.setWidthFull();
    	    textField.getStyle().set("max-width", "100%");
    	    textField.addValueChangeListener(
    	    		e -> filterChangeConsumer.accept(e.getValue())
    	    );
    	    VerticalLayout layout = new VerticalLayout(label, textField);
    	    layout.getThemeList().clear();
    	    layout.getThemeList().add("spacing-xs");

    	    return layout;
    	}	
    	
	public String convertListToJson(Object programList) {
	    ObjectMapper objectMapper = new ObjectMapper();
	    objectMapper.registerModule(new JavaTimeModule());
	    try {
	        return objectMapper.writeValueAsString(programList);
	    } catch (JsonProcessingException e) {
	        e.printStackTrace();
	        return null;
	    }
	}	
	
	private static VerticalLayout createDialogLayout() {

        TextField firstNameField = new TextField("First name");
        TextField lastNameField = new TextField("Last name");

        VerticalLayout dialogLayout = new VerticalLayout(firstNameField,
                lastNameField);
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");

        return dialogLayout;
    }

    private static Button createSaveButton(Dialog dialog) {
        Button saveButton = new Button("Add", e -> dialog.close());
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        return saveButton;
    }
	
	public void wRetrieve() {
		hd_control._grid.resetData();
		if(subject_id ==null) {
			subject_id = Long.valueOf(0) ;
		}
		if(unit_id ==null) {
			unit_id = Long.valueOf(0) ;
		}
		if(unit_id==0&&subject_id==0) {
//			addretrieve();
			hd_control.retrieve(class_id,subject_id,unit_id);
		}else {
			hd_control.retrieve(subject_id,unit_id);
		}
	   
	}
	@Override
	public void addretrieve() {
       hd_control._grid.currentPage++;
       int i = hd_control._grid.dataView.getItemCount();
		if(subject_id ==null) {
			subject_id = Long.valueOf(0) ;
		}
		if(unit_id ==null) {
			unit_id = Long.valueOf(0) ;
		}
		hd.type_id = type_id;
		hd.class_id = class_id;
		hd.subject_id = subject_id;
		hd.unit_id = unit_id;
       hd_control.addretrieve();
       hd_control._grid.scrollToIndex( i- 1); // 새 항목 위치로 스크롤
	}		
	
	public void wInsert() {
		if(userinfo.getGroup_cd().equals("user")) {
			Notification notification = Notification.show("사용자 그룹은 게시가 불가능합니다.");
			notification.setPosition(Position.MIDDLE); // 화면 중앙에 표시
		    notification.setDuration(3000); // 3초 동안 표시
			return;
		}
		if (this.getUI().isPresent()) {
			
		    UI ui = this.getUI().get();

		    Map<String, List<String>> parametersMap = new HashMap<>();
		    parametersMap.put("id", Collections.singletonList(String.valueOf(id) != null ? String.valueOf(id) : "0"));
		    parametersMap.put("type_id", Collections.singletonList(String.valueOf(type_id) != null ? String.valueOf(type_id) : "0"));
		    parametersMap.put("class_id", Collections.singletonList(String.valueOf(class_id)  != null ? String.valueOf(class_id) : "0"));
		    parametersMap.put("subject_id", Collections.singletonList(String.valueOf(subject_id) != null ? String.valueOf(subject_id) : "0"));
		    parametersMap.put("unit_id", Collections.singletonList(String.valueOf(unit_id) != null ? String.valueOf(unit_id) : "0"));
		    parametersMap.put("type_nm", Collections.singletonList(String.valueOf(type_nm) != null ? String.valueOf(type_nm) : ""));
		    parametersMap.put("class_nm", Collections.singletonList(String.valueOf(class_nm)  != null ? String.valueOf(class_nm) : ""));
		    parametersMap.put("subject_nm", Collections.singletonList(String.valueOf(subject_nm) != null ? String.valueOf(subject_nm) : ""));
		    parametersMap.put("unit_nm", Collections.singletonList(String.valueOf(unit_nm) != null ? String.valueOf(unit_nm) : ""));
		    parametersMap.put("user_id", Collections.singletonList(user_id != null ? user_id : ""));
		    Location location = UI.getCurrent().getInternals().getActiveViewLocation();
		    String currentRoute = location.getPath();
		    parametersMap.put("beforeroute", Collections.singletonList(currentRoute != null ? currentRoute.toString() : ""));
		    
		    QueryParameters queryParameters = new QueryParameters(parametersMap);
		    
		    String navi = "pds/pastview/"+String.valueOf(type_id) +"/"+String.valueOf(type_nm)+"/"+
		    		String.valueOf(class_id)+"/"+String.valueOf(class_nm);
		    if(currentRoute.equals(navi)) {
			    ui.navigate("pds/pastviewinsert/"+String.valueOf(0), queryParameters);
		    }else {
		    	
		    }
		} else {
		    // UI가 존재하지 않을 때의 처리 로직
		}
		
	}
	public void wDelete() {
		pds_past_hd row = new pds_past_hd();
	 	row = (pds_past_hd)hd_control._grid.selecteditem; 
	 	if(row==null) {
	 		return;
	 	}	 
		if(!row.get_user_id().equals(userinfo.getUser_cd())) {
			Notification notification = Notification.show("게시자만이 삭제 가능합니다.");
			notification.setPosition(Position.MIDDLE); // 화면 중앙에 표시
		    notification.setDuration(3000); // 3초 동안 표시
			return;
		}
   		DialogBasic();
		
	}
	public void wHdDelete() {
		
		pds_past_hd row = new pds_past_hd();
	 	row = (pds_past_hd)hd_control._grid.selecteditem; 
	 	if(row==null) {
	 		return;
	 	}	 	
	 	String upsjson = convertListToJson(row);
	 	String functionName = "";
	 	String callFunctionQuery = "";
	 	if(row.getId()==0) {
	 		
		}else {
			callFunctionQuery = "SELECT delete_data_dynamic(?::varchar, ?::json)";
		    functionName = "delete_data_dynamic";
	 	}
	    String tableName = "pds_past_hd";  // 대상 테이블명
	    PreparedStatement preparedStatement;
		try {
			preparedStatement = Jdbc.getRePo().getJdbc().getDataSource().getConnection().prepareStatement(callFunctionQuery);
	        preparedStatement.setString(1, tableName);
	        preparedStatement.setString(2, upsjson);

	        ResultSet rs = preparedStatement.executeQuery();

		    if (rs.next()) {
		        String result = rs.getString(1);
		        System.out.println(" delete 결과: " + result);
		    }

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		wRetrieve();
	}
	
   public void loadMoreData() {
        hd_control._grid.currentPage++;
        int i = hd_control._grid.dataView.getItemCount();
        //hd_control._grid.addretrieve(class_id,subject_id,unit_id,hd_control.PAGE_SIZE,hd_control._grid.currentPage * hd_control.PAGE_SIZE);
        hd_control._grid.scrollToIndex( i- 1); // 새 항목 위치로 스크롤
    }	
	
	public void  DialogBasic() {
			HorizontalLayout layout = new HorizontalLayout();
	        layout.setAlignItems(FlexComponent.Alignment.CENTER);
	        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

	        Button delbutton = new Button("삭제");
	        Button exitbutton = new Button("취소");
	        Button msg = new Button("데이터를 삭제하시겠습니까?");
	        
	        popuplayout.setFlexDirection(FlexDirection.COLUMN);
	        popuplayout.getStyle().set("border", "1px solid black");
			delbutton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
			exitbutton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
	        delbutton.addClickListener(event -> {
	        	wHdDelete();
	        	layout.remove(delbutton);
	        	layout.remove(exitbutton);
	        	popupText.remove(msg);
	        	remove(popup);
	        });
	        exitbutton.addClickListener(event -> {
	        	layout.remove(delbutton);
	        	layout.remove(exitbutton);
	        	popupText.remove(msg);
	        	remove(popup);
	        });

	        
	        layout.add(delbutton, exitbutton);
	        popupText.add(msg);
	        popuplayout.add(popupText,layout);
	        popup.add(popuplayout);
	        add(popup);
	        // Center the button within the example
	        popup.getStyle().set("position", "fixed").set("top", "0").set("right", "0")
	                .set("bottom", "0").set("left", "0").set("display", "flex")
	                .set("align-items", "center").set("justify-content", "center");
    }

}