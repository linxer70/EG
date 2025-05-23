package egframe.iteach4u.views.chat;

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
import egframe.common.tinymce.TinyMce;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import egframe.iteach4u.entity.pds_file_hd;
import egframe.iteach4u.entity.usy_class_cd;
import egframe.iteach4u.entity.usy_subject_cd;
import egframe.iteach4u.entity.usy_unit_cd;
import egframe.iteach4u.gridForm.d_pds_file_hd;
import egframe.iteach4u.service.Repository.Iteach4uService;
import jakarta.annotation.security.PermitAll;

@Route(value = "chat/mainview/:type/:type_nm/:class/:class_nm", layout = ChatLayout.class)
@PermitAll
@Uses(Icon.class)
@PreserveOnRefresh
public class ChatMainView extends SysWindow  implements WindowImpl,BeforeEnterObserver {
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
	//private Button btn04 = new Button("질문내용");
	public TextField user_filter		= new TextField ("질문내용")	;
	
	public SysGridLayout hd_control ;
	public d_pds_file_hd hd ;
	public HeaderRow headerRow;
	public ConfirmDialog  dialog;
	private Span status;
	
	private UserDetails userdetails;
	private UsrMaster userinfo;
	private Iteach4uService Jdbc;	
	private final AuthenticatedUser authenticatedUser;
	
	public TinyMce view_mce = new TinyMce();
	
	@Autowired
	public ChatMainView(AuthenticatedUser securityService,Iteach4uService jdbc) {
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
            title = "AI문제풀기/"+title_type_nm+"/"+title_class_nm;
            setTitle(title);
            subject_combo_data =Jdbc.getSubjectList(title_class_id); 
            subject_combo.setItems(subject_combo_data);
            subject_id= null;
            unit_id =null;
            //wRetrieve();
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
            //wRetrieve();
        }
    	
    }
    public void _init() {
    }
    public void callAi() {
    	
    }
   	public void _sendMsg() throws IOException, JSONException{
   		
   		String prompt = "조선 시대의 대표적인 문화유산을 알려줘.";

        // 요청 JSON 구성
        JSONObject json = new JSONObject();
        json.put("prompt", prompt);
        json.put("max_tokens", 200);

        // 연결 설정
        //URL url = new URL("http://172.28.0.12:5000");
        URL url = new URL("https://2f16-34-41-109-230.ngrok-free.app");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");

        // JSON 전송
        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.toString().getBytes("UTF-8"));
        }

        // 결과 읽기
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            JSONObject resultJson = new JSONObject(response.toString());
            view_mce.setPresentationValue(resultJson.getString("result"));
            System.out.println("모델 응답: " + resultJson.getString("result"));
        }

        conn.disconnect();


   		
   	}
   	
    public void wRetrieve() {
    	/*
    	if(subject_nm==null||subject_nm==""){
    		return;
    	}
    	if(unit_nm==null||unit_nm==""){
    		return;
    	}
    	*/
    	String str = user_filter.getValue();
    	System.out.println(str);
    	user_filter.setValue(str);
    	try {
			_runPython(str);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
   	
   	public void _runPython(String arg) throws IOException, InterruptedException {
   		try {
   			view_mce.setValue(null);
		       ProcessBuilder processBuilder = new ProcessBuilder();
		       processBuilder.command("./aitest.sh", arg);    
		       // 작업 디렉토리 설정 (필요한 경우)
		       processBuilder.directory(new File("/media/linxer/develope"));
		
		       // 프로세스 시작
		       Process process = processBuilder.start();
		
		       // 표준 출력 읽기
		       BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		       String line;
		       StringBuilder builder = new StringBuilder();
		       System.out.println("표준 출력:");
		       while ((line = reader.readLine()) != null) {
		    	   builder.append("<br>").append(line);
		           System.out.println(line);
		       }
		       String currentContent = view_mce.getValue();
		       String newContent = currentContent + builder.toString();
		       view_mce.setValue(newContent);
		    	view_mce.getElement().callJsFunction("MathJax.typeset")   	;

		       // 표준 오류 읽기
		       BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		       System.out.println("표준 오류:");
		       while ((line = errorReader.readLine()) != null) {
		           System.err.println(line);
		           
		       }

		       // 프로세스 종료까지 대기
		       int exitCode = process.waitFor();
		       System.out.println("종료 코드: " + exitCode);
		   } catch (Exception e) {
		       e.printStackTrace();
		   }       
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

        

	   	
	   	searchlayout.getStyle().set("flex-wrap", "wrap");
       searchlayout.setJustifyContentMode(JustifyContentMode.START);	//버튼을 우측부터 추가 
       searchlayout.setAlignItems(FlexLayout.Alignment.CENTER); 
        
       searchlayout.getStyle().set("gap", "5px");
       searchlayout.getStyle().set("align-items", "start"); // 세로 정렬
       searchlayout.getStyle().set("align-content", "start"); // 줄 정렬
       searchlayout.setHeight("auto");
       //searchlayout.getStyle().set("overflow-y", "auto"); // 세로 스크롤 활성화
        
        user_filter.setValueChangeMode(ValueChangeMode.EAGER);
        user_filter.setWidthFull();
        searchlayout.add(btn02,subject_combo,btn03,unit_combo,user_filter);
        
        //add(searchlayout);
        searchlayout.setVisible(true);
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
		   		subject_nm = val.get_subject_nm();
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
		   		unit_nm = val.get_unit_nm();
		   	}
        });
    }
    
    public void _inithd() {
    	hdlayout.setVisible(true);
    	hdlayout.add(view_mce);
    	view_mce.getElement().callJsFunction("MathJax.typeset")   	;
    }
    
}