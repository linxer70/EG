package egframe.iteach4u.views.pds;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSelectionModel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.FileBuffer;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.selection.SingleSelect;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.QueryParameters;
import egframe.iteach4u.freeForm.d_file_upload_free.UploadFile;
import egframe.iteach4u.entity.pds_file_hd;
import egframe.iteach4u.entity.pds_file_row;
import egframe.iteach4u.entity.usy_grade_cd;
import egframe.iteach4u.freeForm.d_file_upload_free.Bean;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.addons.thshsh.upload.UploadField;

import egframe.common.SysBindControl;
import egframe.common.SysFreeLayout;
//import egframe.common.SysDataWindowControl;
//import egframe.common.SysShareControl;
import egframe.common.SysWindow;
import egframe.common.WindowImpl;
import egframe.frame.service.AuthenticatedUser;
import egframe.iteach4u.freeForm.d_pds_file_hd_free;
import egframe.iteach4u.gridForm.d_pds_file_hd;
import egframe.iteach4u.gridForm.d_pds_file_row;
//import egframe.data.entitycontrol.sys_column_dic;
//import egframe.data.entitycontrol.sys_program_sql;
//import egframe.data.entitycontrol.sys_user;
//import egframe.data.service.SysDBO;
//import egframe.iteach4u.entitycontrol.usy_grade_cd;
import egframe.iteach4u.service.Repository.Iteach4uService;
//import egframe.iteach4u.freeformcontrol.d_pds_file_hd_free;
//import egframe.iteach4u.freeformcontrol.d_file_upload_free.Bean;
//import egframe.iteach4u.freeformcontrol.d_file_upload_free.UploadFile;
//import egframe.iteach4u.gridcontrol.d_pds_file_row;
//import egframe.security.AuthenticatedUser;
//import egframe.views.MainLayout;
import jakarta.annotation.security.PermitAll;
import com.vaadin.flow.component.splitlayout.SplitLayout;
//@PageTitle("학습자료/입력 폼")
@Route(value = "pds/dataviewinsert/:id", layout = PdsLayout.class)
@PermitAll
@Uses(Icon.class)
@PreserveOnRefresh
public class PdsDataInsert extends SysWindow  implements WindowImpl,RouterLayout,BeforeEnterObserver{
	//private SysDBO dbo;
	private String db_name;
	private Iteach4uService Jdbc;
	public d_pds_file_row row ;
	public d_pds_file_hd hd ;
	public d_pds_file_hd_free hd_free ;
	public SysBindControl hdshare = new SysBindControl();
	private Long type_id ;
	private Long class_id;
	private Long subject_id ;
	private Long unit_id ;
	private String type_nm ;
	private String class_nm;
	private String subject_nm ;
	private String unit_nm ;
	private String user_id = "";
	//private sys_user sysuser;
	private Long id;
	Upload upload;
	FileBuffer buffer ;
	File uploadDir ;  
	long currentTimeMillis;
	String fileExtension;
	String newFileName ;
	Path targetPath ;
	String filenm;
    
	
   	private String previousRoute;   	
    public PdsDataInsert(AuthenticatedUser securityService,Iteach4uService jdbc) {
    	Jdbc = jdbc;
    	//db_name = dbo.getDbName(); 
        //sysuser = this.dbo.getSysUser(securityService.getAuthenticatedUser().getUsername());
        //user_id = sysuser.getUserCd();
    	this.addClassName("main-layout");
	   	buttonlayout.wnewsheet.setVisible(false);
	   	buttonlayout.wdeleteset.setVisible(false);
	   	buttonlayout.wdelete.setVisible(false);
	   	buttonlayout.winsert.setVisible(false);
	   	buttonlayout.wretrieve.setText("목록보기");
	   	 
    	hdlayout.setSizeFull();
    	hdlayout.setVisible(true);
	   	_init();
	   	addEvent();
    }
        public void _init() {
  		   	setFlexDirection(FlexDirection.COLUMN);
  		   	setSizeFull();
  		   	searchlayout.setVisible(false);
  		   	
  		   	row = new d_pds_file_row(Jdbc);
  		   	
  		   buffer = new FileBuffer();
  		       
  		    uploadDir = new File(createDir());  
  		    if (!uploadDir.exists()) {
  		          uploadDir.mkdir();  
  		    }	
  		   upload = new Upload(buffer);
  		   upload.setMaxFiles(10);
  		   upload.setDropAllowed(true);
  		   	/*
  		   	if(sysuser.getGroupCd().equals("user")) {
  			   	hdshare.share.addToSecondary(row);
  		   	}else {
  			   	hdshare.share.addToSecondary(upload,row);
  		   	}
  		   	*/
  			hd = new d_pds_file_hd(Jdbc);
  		   	hdshare.setGrid(hd);
  		   	
  			hd_free = new d_pds_file_hd_free(Jdbc);//DDDW 때문에 
  		   	hdshare.setFree(hd_free);
  		   	//hdshare.setGridVisible(false);
  		   	hdshare.setBinder();
  		   	
  		   	hdlayout.add(hdshare,upload,row);
  	       hdlayout.setVisible(true);
  		   	hdlayout.setFlexDirection(FlexDirection.COLUMN);
  		   	UI.getCurrent().addBeforeLeaveListener(this::beforeLeave);		   	
  	   }
        public void addEvent() {
  		   	upload.addSucceededListener(event -> {
  		   		currentTimeMillis = Instant.now().toEpochMilli();
  		   		filenm = event.getFileName();
  	           fileExtension = getFileExtension(filenm);
  	           newFileName = currentTimeMillis + fileExtension;
  	           targetPath = Paths.get(uploadDir.getAbsolutePath(), newFileName);
  				try {
  					Files.copy(buffer.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
  		            upload.clearFileList();
	  			} catch (IOException e) {
	  				e.printStackTrace();
	  			}
  	        });
  		   	upload.addFinishedListener(event -> {
  		   	    upload.getElement().callJsFunction("reset"); // Upload 컴포넌트 초기화
  		   	    insertFile();
  		   	});
        	
        }
        private void insertFile() {
	          if(row.dataView== null) {
		        	  row.retrieve();
		          }else {
		        	  
		          }
	          row.insert();
            row.selecteditem.set_display_name(filenm);
            row.selecteditem.set_save_name(newFileName);
            row.selecteditem.set_save_path(targetPath.toString());
            row.selecteditem.set_update_dt(LocalDate.now());
            row.selecteditem.setId(id);
            /*
              Integer _row = hd_free.file_count.getValue();
              if(_row==null) {
            	  hd_free.file_count.setValue(1);
	              row.selecteditem.set_row_num(1);
              }else
              {
            	  hd_free.file_count.setValue(_row+1);
	              row.selecteditem.set_row_num(_row+1);
              }
              */
        }
        private void beforeLeave(BeforeLeaveEvent event) {
	        previousRoute = UI.getCurrent().getInternals().getActiveViewLocation().getPath();
	    }
	    public class NextScreenView extends VerticalLayout {
	        public NextScreenView(BeforeEnterEvent event) {
	            if (previousRoute != null) {
	                add("Previous route: " + previousRoute);
	            } else {
	                add("No previous route.");
	            }
	        }
	    }    	
	   
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
    	/*
        var params = event.getRouteParameters();
     	if(params!=null) {
			String val  = params.get("type").orElse("0");
			if(val.equals("0")) {//조회 하면에서 발생 
				 
			}else {
				title_type_id = Long.valueOf(val);
				type_id = title_type_id;
			}
			val   = params.get("class").orElse("0");    	
			if(val.equals("0")) {//조회 하면에서 발생 
			 
			}else {
				title_class_id = Long.valueOf(val);
				class_id = title_class_id;
			}
			val   = params.get("type_nm").orElse("");    	
			if(val.equals("0")) {//조회 하면에서 발생 
			  	 
			}else {
				title_type_nm = val;
			}
			val   = params.get("class_nm").orElse("");    	
			if(val.equals("0")) {//조회 하면에서 발생 
			  	 
			}else {
				title_class_nm = val;
			}
             title = title_type_nm+"/"+title_class_nm+"/입력화면";
             //wRetrieve();
     	}
    	*/
    	
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
                    hd_free.typeid = type_id;
                    String str = String.valueOf(type_id);
                    hd_free.type_id.setValue(Integer.valueOf(str));
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
                    hd_free.classid = class_id;
                    String str = String.valueOf(class_id);
                    hd_free.class_id.setValue(Integer.valueOf(str));
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
                    hd_free.subjectid = subject_id;
                    String str = String.valueOf(subject_id);
                    hd_free.subject_id.setValue(Integer.valueOf(str));
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
                    hd_free.unitid = unit_id;
                    String str = String.valueOf(unit_id);
                    hd_free.unit_id.setValue(Integer.valueOf(str));
                }else {
                	unit_id=Long.valueOf(0);
                }
            }

            if (parametersMap.containsKey("type_nm")) {
                List<String> paramValues = parametersMap.get("type_nm");
                if (!paramValues.isEmpty()) {
                    type_nm = paramValues.get(0);
                    hd_free.type_nm.setValue(type_nm);

                }else {
                	type_nm = null;
                }
            }
            if (parametersMap.containsKey("class_nm")) {
                List<String> paramValues = parametersMap.get("class_nm");
                if (!paramValues.isEmpty()) {
                    class_nm = paramValues.get(0);
                    hd_free.class_nm.setValue(class_nm);
                }else {
                	class_nm = null;
                }
            }
            if (parametersMap.containsKey("subject_nm")) {
                List<String> paramValues = parametersMap.get("subject_nm");
                if (!paramValues.isEmpty()) {
                    subject_nm = paramValues.get(0);
                    hd_free.subject_nm.setValue(subject_nm);
                }else {
                	subject_nm = null;
                }
            }
            if (parametersMap.containsKey("unit_nm")) {
                List<String> paramValues = parametersMap.get("unit_nm");
                if (!paramValues.isEmpty()) {
                	unit_nm  = paramValues.get(0);
                    hd_free.unit_nm.setValue(unit_nm);
                    
                }else {
                	unit_nm=null;
                }
            }
            if (parametersMap.containsKey("id")) {
                List<String> paramValues = parametersMap.get("id");
               if (!paramValues.isEmpty()&&!paramValues.get(0).equals("null")&&!paramValues.get(0).equals("0")) {
            	   id = Long.valueOf(paramValues.get(0));
            	   String str = String.valueOf(id);
                 hd_free.id.setValue(Integer.valueOf(str));
                 wHdRetrieve();
                }else {
                	id=Long.valueOf(0);
                	wInsert();
                }
            }
        }
    	title_type_id = type_id;
    	title_class_id = class_id;
        title_type_nm = type_nm;
        title_class_nm = class_nm;
        title = "학습자료/"+title_type_nm+"/"+title_class_nm+"/입력화면";
        setTitle(title);
    }
    
    public void Combodata() {
    	/*
        List<usy_class_cd> data =sqlca.getClassCd(type_cd); 
	   	hd_free.combo01.setItems(data);
	   	hd_free.combo01.setItemLabelGenerator(usy_class_cd::get_class_nm);
        
        usy_class_cd target = data.stream()
        	    .filter(usy_class_cd -> usy_class_cd.get_class_cd().equals(class_cd)) 
        	    .findFirst().orElse(null);
        	if (target != null) {
        		hd_free.combo01.setValue(target); // "Charlie" 선택
        	}                    
    	
            List<usy_subject_cd> data1 =sqlca.getSubjectCd(type_cd,class_cd); 
    	   	hd_free.combo02.setItems(data1);
    	   	hd_free.combo02.setItemLabelGenerator(usy_subject_cd::get_subject_nm);
            
            usy_subject_cd target1 = data1.stream()
            	    .filter(usy_subject_cd -> usy_subject_cd.get_subject_cd().equals(subject_cd)) 
            	    .findFirst().orElse(null);
            	if (target != null) {
            		hd_free.combo02.setValue(target1); // "Charlie" 선택
            	}   
            	
                List<usy_unit_cd> data2 =sqlca.getUnitCd(type_cd,class_cd,subject_cd); 
        	   	hd_free.combo03.setItems(data2);
        	   	hd_free.combo03.setItemLabelGenerator(usy_unit_cd::get_unit_nm);
                
                usy_unit_cd target2 = data2.stream()
                	    .filter(usy_unit_cd -> usy_unit_cd.get_unit_cd().equals(class_cd)) 
                	    .findFirst().orElse(null);
                	if (target2 != null) {
                		hd_free.combo03.setValue(target2); // "Charlie" 선택
                	}                    
           */     
            	
    }
    

	    private String getFileExtension(String fileName) {
	        int dotIndex = fileName.lastIndexOf(".");
	        if (dotIndex > 0) {
	            return fileName.substring(dotIndex);  // 확장자 포함 (예: .jpg)
	        } else {
	            return "";  // 확장자가 없을 경우
	        }
	    }	    
	public String  createDir() {
		LocalDate today = LocalDate.now();
        int year = today.getYear();  // 현재 년도
        int month = today.getMonthValue();  // 현재 월 (1~12)

        // 폴더 경로: "upload/{년도}/{월}"
        String baseDir = "uploads";
		File uploadDir = new File("uploads");
       if (!uploadDir.exists()) {
           boolean mk = uploadDir.mkdir(); 
           if(!mk) {
        	   return "";
           }
       }else {
           String Dir = "uploads/files";
	   		File filedDir = new File("uploads/files");
	         if (!uploadDir.exists()) {
	             boolean mk = filedDir.mkdir(); 
	             if(!mk) {
	          	   return "";
	             }
	         }
       }
       
        String yearDir = baseDir + "/" + year;
        String monthDir = yearDir + "/" + String.format("%02d", month);  // 월을 2자리로 포맷
        

        // 년도 폴더가 없으면 생성
        File yearFolder = new File(yearDir);
        if (!yearFolder.exists()) {
            boolean yearCreated = yearFolder.mkdirs();  // 년도 폴더 생성
            if (yearCreated) {
                System.out.println("년도 폴더 생성 성공: " + yearDir);
            } else {
                System.out.println("년도 폴더 생성 실패: " + yearDir);
                return "";
            }
        } else {
            System.out.println("년도 폴더 이미 존재: " + yearDir);
        }

        // 월 폴더가 없으면 생성
        File monthFolder = new File(monthDir);
        if (!monthFolder.exists()) {
            boolean monthCreated = monthFolder.mkdirs();  // 월 폴더 생성
            if (monthCreated) {
                System.out.println("월 폴더 생성 성공: " + monthDir);
            } else {
                System.out.println("월 폴더 생성 실패: " + monthDir);
                return "";
            }
        } else {
            System.out.println("월 폴더 이미 존재: " + monthDir);
        }

        System.out.println("최종 폴더 경로: " + monthDir);
        return monthDir;
    }
	public void wRetrieve() {
		if (this.getUI().isPresent()) {
			
		    UI ui = this.getUI().get();

		    Map<String, List<String>> parametersMap = new HashMap<>();
		    parametersMap.put("type_id", Collections.singletonList(String.valueOf(type_id)));
		    parametersMap.put("class_id", Collections.singletonList(String.valueOf(class_id)));
		    parametersMap.put("subject_id", Collections.singletonList(String.valueOf(subject_id)));
		    parametersMap.put("unit_id", Collections.singletonList(String.valueOf(unit_id)));
		    parametersMap.put("type_nm", Collections.singletonList(String.valueOf(type_nm)));
		    parametersMap.put("class_nm", Collections.singletonList(String.valueOf(class_nm)));
		    parametersMap.put("subject_nm", Collections.singletonList(String.valueOf(subject_nm)));
		    parametersMap.put("unit_nm", Collections.singletonList(String.valueOf(unit_nm)));

		    QueryParameters queryParameters = new QueryParameters(parametersMap);
    ui.navigate("pds/dataview/"+String.valueOf(type_id)+"/"+type_nm+"/"+
			String.valueOf(class_id)+"/"+class_nm, queryParameters);
		} else {
		    // UI가 존재하지 않을 때의 처리 로직
		}		
	}
	public void wHdRetrieve() {
		hdshare.grid.retrieve(id);
		wRowRetrieve();
	}
	public void wRowRetrieve() {
		row.retrieve(id);
	}
	public void wSave() {
		if(hdshare.grid.readBuffer.size()==0) {
			return ;
		}
		 	pds_file_hd hd = (pds_file_hd)hdshare.grid.readBuffer.get(0);
		 	/*
			if(!hd.get_unit_id().equals(user_id)) {
				Notification notification = Notification.show("게시자만이 수정 가능합니다.");
				notification.setPosition(Position.MIDDLE); // 화면 중앙에 표시
			    notification.setDuration(3000); // 3초 동안 표시
				return;
			}
			*/
		 	String upsjson = convertListToJson(hd);
		 	String functionName = "";
		 	String callFunctionQuery = "";
		 	if(hd.getId()==null) {
		 		hd.setId(Jdbc.getPdsHdMaxNo("pds_file_hd"));
		 		upsjson = convertListToJson(hd);
			    functionName = "insert_data_dynamic";
			    callFunctionQuery = "SELECT insert_data_dynamic(?::varchar, ?::json)";
			}else {
				callFunctionQuery = "SELECT update_data_dynamic(?::varchar, ?::json)";
			    functionName = "update_data_dynamic";
		 	}
		    String tableName = "pds_file_hd";  // 대상 테이블명
		    PreparedStatement preparedStatement = null;
		    Connection _con = null;
		    ResultSet rs = null;
			try {
				_con = Jdbc.getRePo().getJdbc().getDataSource().getConnection();
				preparedStatement = _con.prepareStatement(callFunctionQuery);
		        preparedStatement.setString(1, tableName);
		        preparedStatement.setString(2, upsjson);

		        rs = preparedStatement.executeQuery();

			    if (rs.next()) {
			        String result = rs.getString(1);
			        System.out.println(" insert 결과: " + result);
			    }

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
			    try {
			        if (rs != null) {
			            rs.close();
			        }
			        if (preparedStatement != null) {
			        	preparedStatement.close();
			        }
			        if (_con != null) {
			            _con.close();
			        }
			    } catch (SQLException e) {
			        e.printStackTrace();
			    }
			}
			wRowSave(hd);
	}
	public void wRowSave(pds_file_hd _hd) {
	 	Long hd_no = _hd.getId();
        for(int i = 0 ; i < row.readBuffer.size();i++ ) {
		 	
        	pds_file_row row_data = (pds_file_row)row.readBuffer.get(i);
		 	row_data.setId(hd_no);
		 	
		 	String upsjson = convertListToJson(row_data);
		 	String functionName = "";
		 	String callFunctionQuery = "";
		 	if(row_data.get_row()==null) {
		 		row_data.set_row(Jdbc.getPdsHdMaxNo("pds_file_row"));
		 		upsjson = convertListToJson(row_data);
		 		functionName = "insert_data_dynamic";
			    callFunctionQuery = "SELECT insert_data_dynamic(?::varchar, ?::json)";
			}else {
				callFunctionQuery = "SELECT update_data_dynamic(?::varchar, ?::json)";
			    functionName = "update_data_dynamic";
		 	}
		    String tableName = "pds_file_row";  // 대상 테이블명
		    PreparedStatement preparedStatement = null;
		    Connection _con = null ;
		    ResultSet rs = null;
			try {
				_con = Jdbc.getRePo().getJdbc().getDataSource().getConnection();
				preparedStatement = _con.prepareStatement(callFunctionQuery);
		        preparedStatement.setString(1, tableName);
		        preparedStatement.setString(2, upsjson);

		        rs = preparedStatement.executeQuery();

			    if (rs.next()) {
			        String result = rs.getString(1);
			        System.out.println(" insert 결과: " + result);
			    }

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
			    try {
			        if (rs != null) {
			            rs.close();
			        }
			        if (preparedStatement != null) {
			        	preparedStatement.close();
			        }
			        if (_con != null) {
			            _con.close();
			        }
			    } catch (SQLException e) {
			        e.printStackTrace();
			    }
			}
		
        }
		
	}
	
	public void wInsert() {
		pds_file_hd sel = new pds_file_hd();//hd.selecteditem;
		sel.set_type_id(type_id);
		sel.set_type_nm(type_nm);
		sel.set_class_id(class_id);
		sel.set_class_nm(class_nm);
		hdshare.insert(sel);

		/*
    	hd_free.subject_id.setValue(0);
    	hd_free.subject_nm.setValue("");
    	hd_free.subject_combo.setValue(null);
    	hd_free.unit_id.setValue(0);
    	hd_free.unit_nm.setValue("");
    	hd_fr
    	ee.unit_combo.setValue(null);
    	*/
	}
	public void wRowDelete(pds_file_hd _hd) {
	 	Long hd_no = _hd.getId();
        for(int i = 0 ; i < row.deleteBuffer.size();i++ ) {
		 	
        	pds_file_row row_data = (pds_file_row)row.deleteBuffer.get(i);
		 	row_data.setId(hd_no);
		 	
		 	String upsjson = convertListToJson(row_data);
		 	String functionName = "";
		 	String callFunctionQuery = "";
			callFunctionQuery = "SELECT delete_data_dynamic(?::varchar, ?::json)";
		    functionName = "delete_data_dynamic";
		    String tableName = "pds_file_row";  // 대상 테이블명
		    PreparedStatement preparedStatement = null;
		    Connection _con = null ;
		    ResultSet rs = null;
			try {
				_con = Jdbc.getRePo().getJdbc().getDataSource().getConnection();
				preparedStatement = _con.prepareStatement(callFunctionQuery);
		        preparedStatement.setString(1, tableName);
		        preparedStatement.setString(2, upsjson);

		        rs = preparedStatement.executeQuery();

			    if (rs.next()) {
			        String result = rs.getString(1);
			        System.out.println(" insert 결과: " + result);
			    }

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
			    try {
			        if (rs != null) {
			            rs.close();
			        }
			        if (preparedStatement != null) {
			        	preparedStatement.close();
			        }
			        if (_con != null) {
			            _con.close();
			        }
			    } catch (SQLException e) {
			        e.printStackTrace();
			    }
			}
		
        }
		
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
	
}