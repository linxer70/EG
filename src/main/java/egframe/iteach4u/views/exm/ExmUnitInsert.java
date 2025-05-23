package egframe.iteach4u.views.exm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;

import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;

import egframe.common.SysWindow;
import egframe.common.WindowImpl;
import egframe.frame.entity.UsrMaster;
import egframe.frame.service.AuthenticatedUser;
import egframe.iteach4u.common.EXAM_UNIT;
import egframe.iteach4u.common.MAT_CONTROL;
import egframe.iteach4u.common.MAT_UNIT;
import egframe.iteach4u.entity.exm_mat_unit_hd;
import egframe.iteach4u.entity.exm_mat_unit_row;
import egframe.iteach4u.entity.exm_mat_unit_view;
import egframe.iteach4u.entity.usy_div_cd;
import egframe.iteach4u.entity.usy_subject_cd;
import egframe.iteach4u.entity.usy_unit_cd;
import egframe.iteach4u.service.Repository.Iteach4uService;
import jakarta.annotation.security.PermitAll;
import java.sql.Types;
//@PageTitle("학습자료/입력 폼")
@Route(value = "exm/unitviewinsert/:id", layout = ExmLayout.class)
@PermitAll
@Uses(Icon.class)
@PreserveOnRefresh
public class ExmUnitInsert extends SysWindow  implements WindowImpl,AfterNavigationObserver,RouterLayout,BeforeEnterObserver{
	
	
	//private SysDBO dbo;
	private String db_name;
	private Iteach4uService sqlca;
	@Autowired
	public JdbcTemplate Jdbc;	

   	private String previousRoute;   	
   	private String view_yn ; 
	private Button tTitle = new Button("개요");
	private TextField viewTitle = new TextField();
	private Button tdiv = new Button("지문유무");
	private Button tsubject = new Button("과목");
	private Button tunit = new Button("단원");
	private String user_id = "";
	private UsrMaster userinfo;
	private UserDetails userdetails;
	private final AuthenticatedUser authenticatedUser;
	
	private Long id;
	private Long hd_id;
	private Long row_id;
	private Long type_id ;
	private Long class_id;
	private Long subject_id ;
	private Long unit_id ;
	private Long div_id;
	
	private String type_nm ;
	private String class_nm;
	private String subject_nm ;
	private String unit_nm ;
	private String div_nm ;

    
	private exm_mat_unit_view viewitem ;
	private exm_mat_unit_hd hditem;
	private exm_mat_unit_row rowitem;
	
	private List<exm_mat_unit_view> viewlist;
	private List<exm_mat_unit_hd> hdlist;
	private List<exm_mat_unit_row> rowlist;
	
	private MAT_CONTROL mat_control ;
	private MAT_UNIT	mat_unit ;
	private EXAM_UNIT	exm_unit ;

	private ComboBox<usy_subject_cd> subject_combo = new ComboBox<>();
	private ComboBox<usy_div_cd> div_combo = new ComboBox<>();
	private ComboBox<String> view_combo = new ComboBox<>();
	private List<usy_subject_cd> subject_combo_data;
	private List<usy_unit_cd> unit_combo_data;
	private List<usy_div_cd> div_combo_data;
	private usy_subject_cd selectedsubject;
	private usy_unit_cd selectedunit;
	private usy_div_cd selecteddiv;
   	
    public ExmUnitInsert(AuthenticatedUser securityService,Iteach4uService jdbc) {
		this.authenticatedUser = securityService;
		sqlca = jdbc;
		Jdbc = jdbc.getCon();
		userdetails = authenticatedUser.getAuthenticatedUser();
		user_id = userdetails.getUsername();
		userinfo = jdbc.findByUserId(user_id);
    	this.addClassName("main-layout");
	   	 
    	hdlayout.setSizeFull();
    	hdlayout.setVisible(true);
	   	_init();
	   	_initView();
	   	_addEvent();
    }
    public void _init() {
	   	setFlexDirection(FlexDirection.COLUMN);
	   	setSizeFull();
	   	buttonlayout.wnewsheet.setVisible(false);
	   	buttonlayout.wdeleteset.setVisible(false);
	   	buttonlayout.wdelete.setVisible(false);
	   	buttonlayout.winsert.setVisible(false);
	   	buttonlayout.wretrieve.setText("목록보기");
	   	searchlayout.setVisible(true);
	   	getElement().getStyle().set("overflow", "hidden");

  	}
    public void _initView() {
	    searchlayout.setJustifyContentMode(JustifyContentMode.START);	//버튼을 우측부터 추가 
	    searchlayout.setAlignItems(FlexLayout.Alignment.CENTER); 
	    searchlayout.getStyle().set("border", "1px solid black");
	    searchlayout.setHeight("5%");
		tTitle.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		tdiv.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
	   	tsubject.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
	   	subject_combo.setItemLabelGenerator(usy_subject_cd::get_subject_nm);
	   	div_combo.setItemLabelGenerator(usy_div_cd::get_div_nm);
       searchlayout.add(tdiv,view_combo,tsubject,subject_combo,tTitle,viewTitle);//,btn04,user_filter);
	   	view_combo.setItems("Y", "N"); 
    }
    public void _addEvent() {
    	subject_combo.addValueChangeListener(e->{
    		if(e.getOldValue()==null) {
    			//return;
    		}
    		if(e.getValue()!=null) {
    			
    			//_setSubject(e.getValue().getId());
    			//_initSubjectComboData();
    			
    			
    			_setSubjectChanged(e.getValue().getId());
    		}
 	   });
 	   view_combo.addValueChangeListener(e->{
	   		if(e.getOldValue()==null) {
				//return;
			}
	   		if(e.getValue()!=null) {
	   			
	   			//_setViewYn(e.getValue());
	   			//_initDivComboData();
	   			
	   			_setViewYnChanged(e.getValue());
	   		}
	   });
    }
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // QueryParameters 가져오기
        QueryParameters queryParameters = event.getLocation().getQueryParameters();
        // 특정 키의 파라미터 값 읽기
        Map<String, List<String>> parametersMap = queryParameters.getParameters();
        if (parametersMap.containsKey("beforeroute")) {
            List<String> paramValues = parametersMap.get("beforeroute");
            if (!paramValues.isEmpty()) {
            	 String paramValue =  paramValues.get(0);
            	 previousRoute =   paramValue;    
            }
        }
        if(!parametersMap.isEmpty()) {

            if (parametersMap.containsKey("type_id")) {
                List<String> paramValues = parametersMap.get("type_id");
                if (!paramValues.isEmpty()) {
                    String val = paramValues.get(0);
                	if(val.equals("null")) {
                		val = "0" ;
                	}
                    type_id = Long.valueOf(val);
                    String str = String.valueOf(type_id);
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
                    String str = String.valueOf(class_id);
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
                    String str = String.valueOf(subject_id);
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
                    String str = String.valueOf(unit_id);
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
            if (parametersMap.containsKey("id")) {
                List<String> paramValues = parametersMap.get("id");
               if (!paramValues.isEmpty()&&!paramValues.get(0).equals("null")&&!paramValues.get(0).equals("0")) {
            	   	id = Long.valueOf(paramValues.get(0));
            	   	String str = String.valueOf(id);
            	   	_makeMatControl();
                }else {
                	id=Long.valueOf(0);
                	_initMatControl();
                }
            }
        }
    	title_type_id = type_id;
    	title_class_id = class_id;
        title_type_nm = type_nm;
        title_class_nm = class_nm;
        title = "문항관리/"+title_type_nm+"/"+title_class_nm+"/입력화면";
        setTitle(title);
    }
    
    public void _initMatControl() {//빈행
    	exm_mat_unit_view view = new exm_mat_unit_view() ;
    	view.setId(Long.valueOf(0));
    	view.set_user_id(user_id);
    	view.set_type_id(type_id);
    	view.set_class_id(class_id);
    	view.set_subject_id(Long.valueOf(0));
    	view.set_view_yn("Y");
  	   	mat_control = new MAT_CONTROL(sqlca,view);
  	   	hdlayout.add(mat_control);
  	   	view_combo.setValue("Y");
  	   	hdlayout.getElement().getStyle().set("overflow-y", "auto");
  	   	hdlayout.getStyle().set("border", "5px solid red");	
	   	subject_combo_data = sqlca.getSubjectList(class_id);
	   	subject_combo.setItems(subject_combo_data);

  	   	subject_id = view.get_subject_id();
  	   	
    }
    
    public void _makeMatControl() {//뷰에 해당하는 기존 데이터 조회 
    	exm_mat_unit_view view = sqlca.getExmMatUnitViewById(id);
    	view.set_user_id(user_id);
    	Optional<exm_mat_unit_view> result = Optional.ofNullable(view);
    	if(!result.isPresent()) {
    	    System.out.println("데이터 없음");
    	}else {
    	    System.out.println("데이터 있음");
    	}
 	   	mat_control = new MAT_CONTROL(sqlca,view);
 	   	hdlayout.add(mat_control);
  	   	hdlayout.getElement().getStyle().set("overflow-y", "auto");
 	  	  hdlayout.getStyle().set("border", "5px solid red");	
  	   	view_combo.setValue(view.get_view_yn());
  	   	
	   	subject_combo_data = sqlca.getSubjectList(class_id);
	   	subject_combo.setItems(subject_combo_data);
  	   	
  	   	subject_id = view.get_subject_id();
		selectedsubject = subject_combo_data.stream().filter(usy_subject_cd -> usy_subject_cd.getId().equals(subject_id)).findFirst().orElse(null);
		if (selectedsubject != null) {
			subject_combo.setValue(selectedsubject); 
			//mat_unit.exam_unit.unit_combo.setClassName("hide-dropdown-arrow-combobox");
			//subject_combo.setEnabled(false);
		}    			
    }
    public void _setSubjectChanged(Long in_subject) {
    	subject_id = in_subject;
		mat_control._subjectChanged(subject_id);
    }
    public void _setViewYnChanged(String _flag) {
    	view_yn = _flag;
    	mat_control._viewYnChanged(_flag);
    }
    public void _addMatControl() {//빈행
    }
    public void wRetrieve() {
    	openList();
    }
    
    public void wDelete() {
		exm_mat_unit_row row = new exm_mat_unit_row();
    }
    /*
	public void wHdDelete(int _view_no,int _hd_no) {
	 	String callFunctionQuery;
		callFunctionQuery = "delete from  exam_mat_unit_hd where view_no = ? and no = ?;";
	    String tableName = "exam_mat_unit_hd";  // 대상 테이블명
	    sqlca.sys_con.update(callFunctionQuery,_view_no,_hd_no);
	    view.delete();
	}
	
	public void wRowDelete(int _view_no,int _hd_no) {
	 	String callFunctionQuery;
			callFunctionQuery = "delete from exam_mat_unit_row where view_no =? and hd_no = ?;";
	    String tableName = "exam_mat_unit_row";  // 대상 테이블명
	    sqlca.sys_con.update(callFunctionQuery,_view_no,_hd_no);
	}
    */
    public void wSave() {
    	if(view_yn==null ||view_yn.equals("")) {
			Notification notification = Notification.show("지문 유무는 필수 항목입니다.");
			notification.setPosition(Position.MIDDLE); // 화면 중앙에 표시
		    notification.setDuration(2000); // 3초 동안 표시
    		
    		return ;
    	}
    	if(subject_id==null ||subject_id==0) {
			Notification notification = Notification.show("과목 선택은 필수 항목입니다.");
			notification.setPosition(Position.MIDDLE); // 화면 중앙에 표시
		    notification.setDuration(2000); // 3초 동안 표시
    		return ;
    	}
		 for (int j = 0 ;j < mat_control.viewlist.size();j++) {
			viewitem = mat_control.viewlist.get(j);
			id = sqlca.wExmMatUnitViewSave(viewitem);
			viewitem.setId(id);
			 int k = mat_control.hdlist.size();
			for (int i = 0 ;i < k ;i++) {
				//hdlist.get(i).set_view_id(id);
				MAT_UNIT mat =mat_control.mat_unit_list.get(i); 
				mat.hditem.set_view_id(id);
				wHdSave(mat);
			}
		}
   	
    }
	public void wHdSave(MAT_UNIT _mat) {
		exm_mat_unit_hd hd = _mat.hditem;
		//hd.set_view_id(id);
		hd_id = sqlca.wExamHdSave(hd);
		_mat.hditem.setId(hd_id);
		for (int j = 0 ;j < _mat.exam_unit.mat_cnt;j++) {
			exm_mat_unit_row row = _mat.exam_unit.rowlist.get(j); 
			row.set_view_id(id);
			row.set_hd_id(hd_id);
			sqlca.wExamRowSave(row);
		}
		openList();
	}
    
	public void openList() {
		if (this.getUI().isPresent()) {
		    UI ui = this.getUI().get();
		    if(this.previousRoute.equals("MAKE")) {
		    	
		    }
		    Map<String, List<String>> parametersMap = new HashMap<>();
		    parametersMap.put("type_id", Collections.singletonList(String.valueOf(type_id)));
		    parametersMap.put("class_id", Collections.singletonList(String.valueOf(class_id)));
		    parametersMap.put("subject_id", Collections.singletonList(String.valueOf(subject_id)));
		    parametersMap.put("unit_id", Collections.singletonList(String.valueOf(unit_id)));
		    
		    Location location = UI.getCurrent().getInternals().getActiveViewLocation();
		    String currentRoute = location.getPath();
		    parametersMap.put("beforeroute", Collections.singletonList(currentRoute != null ? currentRoute.toString() : ""));

		    QueryParameters queryParameters = new QueryParameters(parametersMap);
		    String navi = "exm/unitviewinsert/"+String.valueOf(type_id) +"/"+String.valueOf(type_nm)+"/"+
		    		String.valueOf(class_id)+"/"+String.valueOf(class_nm);
		    ui.navigate("exm/unitview/"+String.valueOf(type_id) +"/"+String.valueOf(type_nm)+"/"+
		    		String.valueOf(class_id)+"/"+String.valueOf(class_nm), queryParameters);
		} else {
		}
	}

}