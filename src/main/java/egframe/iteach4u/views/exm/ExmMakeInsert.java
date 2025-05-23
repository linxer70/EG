package egframe.iteach4u.views.exm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;

import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
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

import egframe.common.SysBindControl;
import egframe.common.SysWindow;
import egframe.common.WindowImpl;
import egframe.frame.entity.UsrMaster;
import egframe.frame.service.AuthenticatedUser;
import egframe.iteach4u.common.EXAM_UNIT;
import egframe.iteach4u.common.MAT_CONTROL;
import egframe.iteach4u.common.MAT_UNIT;
import egframe.iteach4u.common.UNIT_CODE;
import egframe.iteach4u.entity.exm_mat_make_hd;
import egframe.iteach4u.entity.exm_mat_make_list;
import egframe.iteach4u.entity.exm_mat_unit_hd;
import egframe.iteach4u.entity.exm_mat_unit_row;
import egframe.iteach4u.entity.exm_mat_unit_view;
import egframe.iteach4u.entity.usy_div_cd;
import egframe.iteach4u.entity.usy_subject_cd;
import egframe.iteach4u.entity.usy_unit_cd;
import egframe.iteach4u.freeForm.d_exm_mat_make_list_free;
import egframe.iteach4u.freeForm.d_pds_past_hd_free;
import egframe.iteach4u.gridForm.d_exm_mat_make_list;
import egframe.iteach4u.gridForm.d_pds_past_hd;
import egframe.iteach4u.service.Repository.Iteach4uService;
import jakarta.annotation.security.PermitAll;
import java.sql.Types;
import java.time.LocalDate;
//@PageTitle("학습자료/입력 폼")
@Route(value = "exm/makeviewinsert/:id", layout = ExmLayout.class)
@PermitAll
@Uses(Icon.class)
@PreserveOnRefresh
public class ExmMakeInsert extends SysWindow  implements WindowImpl,AfterNavigationObserver,RouterLayout,BeforeEnterObserver{
	
	
	//private SysDBO dbo;
	private String db_name;
	private Iteach4uService sqlca;
	@Autowired
	public JdbcTemplate Jdbc;	

   	private String previousRoute;   	
   	private String view_yn ; 
	private Button tTitle = new Button("개요");
	private TextField viewTitle = new TextField();
	private Button wcreate = new Button("생성");
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
	private UNIT_CODE unit;
	private FlexLayout unitlayout = new FlexLayout();
	private Set<usy_unit_cd> unitsToCheck;
	
	private ComboBox<usy_subject_cd> subject_combo = new ComboBox<>();
	private List<usy_subject_cd> subject_combo_data;
	private usy_subject_cd selectedsubject;
 
	public d_exm_mat_make_list			hd;		//문항 생성과 조회시 키 데이터 선언 	
	public exm_mat_make_list			listitem;		//문항 생성과 조회시 키 데이터 선언 	
	public d_exm_mat_make_list_free free = new d_exm_mat_make_list_free() ;
	public SysBindControl listfree = new SysBindControl();
    public ExmMakeInsert(AuthenticatedUser securityService,Iteach4uService jdbc) {
		this.authenticatedUser = securityService;
		sqlca = jdbc;
		Jdbc = jdbc.getCon();
		userdetails = authenticatedUser.getAuthenticatedUser();
//		user_id = Jdbc.getUserInfo();
		userinfo = jdbc.findByUserId(userdetails.getUsername());
		user_id = userinfo.getUser_cd();
    	this.addClassName("main-layout");
    	getElement().getStyle().set("flexWrap", "wrap");
    	getStyle().set("align-items", "stretch");
    	setHeight(null);
    	getStyle().set("overflow-Y", "auto");
	   	_init();
	   	_initView();
	   	_addEvent();
    }
    public void _init() {
	   	buttonlayout.wnewsheet.setVisible(false);
	   	buttonlayout.wdeleteset.setVisible(false);
	   	buttonlayout.wdelete.setVisible(false);
	   	buttonlayout.winsert.setVisible(true);
	   	buttonlayout.winsert.setText("생성");
	   	buttonlayout.wretrieve.setText("목록보기");
	   	remove(searchlayout,hdlayout,rowlayout,seqlayout);
	   	add(searchlayout,hdlayout,rowlayout);
	   	rowlayout.setWidthFull();
	   	rowlayout.getStyle().set("min-width", "0"); // 핵심!
	   	//rowlayout.getStyle().set("overflow-x", "auto");
	   	rowlayout.getStyle().set("flex", "1 1 auto");
    }
    
    public void _initView() {
    	
	   	searchlayout.setJustifyContentMode(JustifyContentMode.START);	//버튼을 우측부터 추가 
	   	searchlayout.setAlignItems(FlexLayout.Alignment.CENTER); 
	   	searchlayout.setHeight("50px");
		tTitle.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		wcreate.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
	   	tsubject.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
	   	subject_combo.setItemLabelGenerator(usy_subject_cd::get_subject_nm);
       searchlayout.add(tsubject,subject_combo);//,btn04,user_filter);
	   	
	   	subject_combo.setRenderer(new ComponentRenderer<>(usy_subject_cd -> {
	   	    FlexLayout wrapper = new FlexLayout();
	   	    wrapper.setAlignItems(Alignment.CENTER);
   	   	    Div info = new Div();
   	   	    info.setText(usy_subject_cd.get_subject_nm() + " : " + usy_subject_cd.getCount() + " 문항");
	   	    wrapper.add(info);
	   	    return wrapper;
	   	}));	
	   	_initUnit();
			hd = new d_exm_mat_make_list(sqlca);
			listfree.setGrid(hd);
			free = new d_exm_mat_make_list_free(sqlca);//DDDW 때문에 
  		   	listfree.setFree(free);
  		   	listfree.setBinder();
  		   	listfree.grid_control.setVisible(false);
  		   	
  		   	hdlayout.add(unitlayout,listfree);
	   		   	
    }
    
    public void _initUnit() {
    	unit = new UNIT_CODE(authenticatedUser,sqlca);
    	unitlayout.getStyle().set("border", "1px solid blue");
    	unitlayout.getStyle().set("gap", "5px");
    	unitlayout.getStyle().set("align-items", "start"); // 세로 정렬
    	unitlayout.getStyle().set("align-content", "start"); // 줄 정렬
    	unitlayout.getStyle().set("background-color", "black"); // 세로 스크롤 활성화
    	unitlayout.add(unit);
    }

    
    public void _initMatControl() {//빈행
    	
    	subject_combo_data =sqlca.getSubjectList(class_id); 
    	subject_combo.setItems(subject_combo_data);
    	
    	//listitem = new exm_mat_make_list();
    	listfree.grid.insert();
    	listitem = (exm_mat_make_list) listfree.grid.readBuffer.get(0);
    	
    	listitem.setId(id);
    	listitem.set_type_id(type_id);
    	listitem.set_class_id(class_id);
    	listitem.set_update_id(user_id);
    	listitem.set_exam_type("01");
    	listitem.set_time_limit(30L);
    	
    	free.setData(listitem);
       
    	mat_control = new MAT_CONTROL(sqlca,listitem);
       rowlayout.add(mat_control);
    }
    public void _makeMatControl() {//뷰에 해당하는 기존 데이터 조회 
    	
        subject_combo_data =sqlca.getSubjectList(class_id); 
        subject_combo.setItems(subject_combo_data);
       //List<exm_mat_make_list> listitems =sqlca.getExmMatMakeListById(id) ;
    	//listitem = listitems.get(0);
    	hd.retrieve(id);
    	listitem = (exm_mat_make_list) hd.readBuffer.get(0);
       mat_control = new MAT_CONTROL(sqlca,listitem);
       rowlayout.add(mat_control);
       rowlayout.setVisible(true);
       mat_control.wListView();
 	   	subject_id = listitem.get_subject_id();
		selectedsubject = subject_combo_data.stream().filter(usy_subject_cd -> usy_subject_cd.getId().equals(subject_id)).findFirst().orElse(null);
		if (selectedsubject != null) {
			subject_combo.setValue(selectedsubject); 
			//mat_unit.exam_unit.unit_combo.setClassName("hide-dropdown-arrow-combobox");
			//subject_combo.setEnabled(false);
		} 
  	   	//hdlayout.getElement().getStyle().set("overflow-y", "auto");
 	  	//hdlayout.getStyle().set("border", "5px solid red");	
  	   	
    }
    
    
    public void _addEvent() {
    	subject_combo.addValueChangeListener(e->{
    		if(e.getValue()!=null) {
		   		subject_id = e.getValue().getId();
		   		subject_nm = e.getValue().get_subject_nm();
    			_setSubjectChanged(e.getValue().getId());
    		}
 	   });
    }
    public void _setSubjectChanged(Long in_subject) {
    	subject_id = in_subject;
    	listitem.set_subject_id(subject_id);
    	unit._setUnitData(type_id,class_id,subject_id);
		
    }
    private boolean wChkCreate() {
  	   if(listitem.getId()==null) {
  		   listitem.setId(0L);
  	   }
  	   if(listitem.get_tot_num()==0) {
  			Notification notification = new Notification("문항수는 필수 선택입니다.");
  			notification.setDuration(2000);
  			notification.setPosition(Position.MIDDLE);
  		    notification.open();
  			return false;
  	   }/*
  	   if(listitem.get_time_limit()==0||listitem.getId()!=0) {
  			Notification notification = new Notification("제한시간은 필수 선택입니다.");
  			notification.setDuration(2000);
  			notification.setPosition(Position.MIDDLE);
  		    notification.open();
  			return false;
  	   }
  	   */
  		List<Long> unitCodes = new ArrayList<>();
    		Set<usy_unit_cd> selectedUnits = unit.unitGroup.getValue();
  	    for (usy_unit_cd item : selectedUnits) {
  	    		unitCodes.add(item.getId());
         }
  	   
   	    return mat_control.wUnitCreate(unitCodes);
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
   	                unit._setUnitData(type_id,class_id,subject_id);
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
  	     	 	   unitsToCheck = new HashSet<>();
  	     	 	   usy_unit_cd item = new usy_unit_cd();
  	     		 	   item.set_subject_id(49L);
  	     		   	   item.setId(2023L);
  	     	 	   item.set_unit_nm("면담하기");
  	     	 	   item.setCount(12L);
  	     	 	   unitsToCheck.add(item);
  	     		 	   item.set_subject_id(49L);
  	     		 	usy_unit_cd item1 = new usy_unit_cd();
  	     	 	   item1.setId(2025L);
  	     	 	   item1.set_unit_nm("소개하기");
  	     	 	   item1.setCount(8L);
  	     	 	   unitsToCheck.add(item1);
                    
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
            	   	buttonlayout.winsert.setVisible(false);
            	   	buttonlayout.wsave.setVisible(false);
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
        title = "문제지관리/"+title_type_nm+"/"+title_class_nm+"/입력화면";
        setTitle(title);
    }
    
    
    public void _addMatControl() {//빈행
    }
    public void wRetrieve() {
    	openList();
    }
    public void wInsert() {
   		List<Long> unitCodes = new ArrayList<>();
   		Set<usy_unit_cd> selectedUnits = unit.unitGroup.getValue();
 	    for (usy_unit_cd item : selectedUnits) {
 	    	unitCodes.add(item.getId());
 	    	//listitem.setId(item.getId());
        }
	   if(wChkCreate()) {
//		   openList();
	   }
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
    	if(chkData()) {
 			listitem = mat_control.listitem;
 			if(listitem.getId()==0) {
 	 			id = sqlca.wExmMatMakeListSave(listitem);
 	 			int k = mat_control.hddata.size();
 	 			for (int i = 0 ;i < k ;i++) {
 	 				exm_mat_unit_hd  unithd =  mat_control.hddata.get(i);
 	 				exm_mat_make_hd makehd = new exm_mat_make_hd();
 	 				makehd.set_list_id(id);
 	 				makehd.set_hd_id(unithd.getId());
 	 				makehd.set_update_id(user_id);
 	 				makehd.set_update_dt(LocalDate.now());
 	 				wHdSave(makehd);
 	 			}
 			}else {
 	 			id = sqlca.wExmMatMakeListSave(listitem);
 	 			int k = mat_control.makehddata.size();
 	 			for (int i = 0 ;i < k ;i++) {
 	 				exm_mat_make_hd makehd = mat_control.makehddata.get(i);
 	 				wHdSave(makehd);
 	 			}
 			}
 		}
    }
    public boolean chkData() {
   	   if(mat_control.makehddata.size()==0) {
 			Notification notification = new Notification("문제지가 생성되지 않았습니다.");
 			notification.setDuration(2000);
 			notification.setPosition(Position.MIDDLE);
 		    notification.open();
   		   return false;
  	   }
  	   if(listitem.get_subject_id().equals("")) {
  			Notification notification = new Notification("문항수는 필수 선택입니다.");
  			notification.setDuration(2000);
  			notification.setPosition(Position.MIDDLE);
  		    notification.open();
  			return false;
  	   }
  	   if(listitem.get_tot_num()==0) {
  			Notification notification = new Notification("문항수는 필수 선택입니다.");
  			notification.setDuration(2000);
  			notification.setPosition(Position.MIDDLE);
  		    notification.open();
  			return false;
  	   }
  	   /*
  	   if(listitem.get_time_limit()==0) {
  			Notification notification = new Notification("제한시간은 필수 선택입니다.");
  			notification.setDuration(2000);
  			notification.setPosition(Position.MIDDLE);
  		    notification.open();
  			return false;
  	   }
  	   */
    	return true;
    }
	public void wHdSave(exm_mat_make_hd _makehd) {
		Long makehdid = sqlca.wExmMatMakeHdSave(_makehd);
		openList();
	}
    
	public void openList() {
		if (this.getUI().isPresent()) {
		    UI ui = this.getUI().get();
		    if(this.previousRoute.equals("MAKE")) {
		    	
		    }
		    Map<String, List<String>> parametersMap = new HashMap<>();
		    parametersMap.put("id", Collections.singletonList("0"));
		    parametersMap.put("type_id", Collections.singletonList(String.valueOf(type_id)));
		    parametersMap.put("class_id", Collections.singletonList(String.valueOf(class_id)));
		    //parametersMap.put("subject_id", Collections.singletonList(String.valueOf(subject_id)));
		    //parametersMap.put("unit_id", Collections.singletonList(String.valueOf(unit_id)));
		    
		    Location location = UI.getCurrent().getInternals().getActiveViewLocation();
		    String currentRoute = location.getPath();
		    parametersMap.put("beforeroute", Collections.singletonList(currentRoute != null ? currentRoute.toString() : ""));

		    QueryParameters queryParameters = new QueryParameters(parametersMap);
		    String navi = "exm/makeviewinsert/"+String.valueOf(type_id) +"/"+String.valueOf(type_nm)+"/"+
		    		String.valueOf(class_id)+"/"+String.valueOf(class_nm);
		    ui.navigate("exm/makeview/"+String.valueOf(type_id) +"/"+String.valueOf(type_nm)+"/"+
		    		String.valueOf(class_id)+"/"+String.valueOf(class_nm), queryParameters);
		} else {
		}
	}

}