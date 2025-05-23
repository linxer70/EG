package egframe.iteach4u.gridForm;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import egframe.common.SysGrid;
//import egframe.common.SysDataWindow;
//import egframe.common.SysFreeForm;
//import egframe.data.entitycontrol.sys_column_dic;
//import egframe.data.service.SysDBO;
import egframe.iteach4u.entity.pds_file_hd;
import egframe.iteach4u.entity.pds_file_row;
import egframe.iteach4u.service.Repository.Iteach4uService;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSelectionModel;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinResponse;
import com.vaadin.flow.server.VaadinService;
public class d_pds_file_row extends SysGrid<pds_file_row> {//implements DataWindowImpl { 
	private BeanValidationBinder binder ;
	//private SysFreeForm freeform;
	Anchor downloadLink = null;
	private Iteach4uService Jdbc;
    public d_pds_file_row() {		
//    	super(pds_file_row.class);

 	} 
    public d_pds_file_row(@Autowired Iteach4uService dbo) {
//    	super(pds_file_row.class);
      	//setCon(dbo.getCon());
    	//setTransObject(dbo);
    	Jdbc = dbo;
    	init();
    }
    public void init(){
    		setSizeFull();
    		getElement().getStyle().set("z-index", "99");
    		setSelectionMode(Grid.SelectionMode.SINGLE);
        //editbinder = new Binder<>(pds_file_row.class);
    	//columneditor = getEditor();
        //columneditor.setBinder(editbinder);

        dataView = setItems((List<pds_file_row>)  readBuffer);//수정
		removeAllColumns();
		
        addColumn(item -> item.getValue("id")).setHeader("HD순번").setAutoWidth(true).setSortable(true).setKey("id").setVisible(false);
        addColumn(item -> item.getValue("row_num")).setHeader("순번").setAutoWidth(true).setSortable(true).setKey("row_num");//.setVisible(false);
        addColumn(item -> item.getValue("row")).setHeader("순번").setAutoWidth(true).setSortable(true).setKey("row").setVisible(false);
        addColumn(item -> item.getValue("save_path")).setHeader("저장경로").setAutoWidth(true).setSortable(true).setKey("save_path").setVisible(false);
        addColumn(item -> item.getValue("save_name")).setHeader("저장파일명").setAutoWidth(true).setSortable(true).setKey("save_name").setVisible(false);
        addColumn(item -> item.getValue("cov_yn")).setHeader("변환여부").setAutoWidth(true).setSortable(true).setKey("cov_yn").setVisible(false);
        addColumn(item -> item.getValue("display_name")).setHeader("파일명").setAutoWidth(true).setSortable(true).setKey("display_name");
        addColumn(item -> item.getValue("update_dt")).setHeader("등록일자").setAutoWidth(true).setSortable(true).setKey("update_dt").setVisible(false);
        addColumn(item -> item.getValue("down_counter")).setHeader("다운로드").setAutoWidth(true).setSortable(true).setKey("down_counter").setVisible(true);
        addColumn(item -> item.getValue("modify")).setHeader("modify_yn").setAutoWidth(true).setSortable(true).setKey("modify").setVisible(false);
        
        addComponentColumn(item -> createDownloadIcon(item)).setHeader(createAllDownloadIcon());
        addComponentColumn(item -> createDeleteIcon(item)).setHeader(createAllDeleteIcon());
        
        setEvent();
//    	setEditControl(false);
    	
    	
    	
    }
    private Div createDownloadIcon(pds_file_row _item) {
    	if(_item.get_save_name() == null) {
    		return null;
    	}
    	Div download = new Div();
        Button down = new Button("다운");
        down.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        
        if(_item.get_cov_yn()!=null) {
        	if(_item.get_cov_yn().equals("Y")) {
            	String display_name = _item.get_display_name();
            	String fileName = _item.get_save_name();
                	down.addClassName("row-button"); // 클래스 설정
                	downloadLink = new Anchor(fileName,down);
                	
                 downloadLink.getElement().setAttribute("download", display_name);
                 download.add(downloadLink);
        	}
        }else {
        	Path uploadedFilePath = Paths.get(_item.get_save_path());
        	String fileName = _item.get_save_name();
        	String display_name = _item.get_display_name();
            if (Files.exists(uploadedFilePath)) {
                System.out.println("파일이 존재합니다.");
            } else {
                System.out.println("파일이 존재하지 않습니다.");
            }   
            System.out.println("파일 읽기 가능 여부: " + Files.isReadable(uploadedFilePath));
        	
            if (uploadedFilePath != null && Files.exists(uploadedFilePath)) {
            	down.addClassName("row-button"); // 클래스 설정
            	downloadLink = new Anchor(fileName,down);
            	
                downloadLink.getElement().setAttribute("download", display_name);
                download.add(downloadLink);
            }
        }
        down.addClickListener(e->{
        	 //DownLoad(_item);
        });
        return download;
    }
    private void handleDownloadEvent(Path filePath) {
        Integer _row = clickitem.get_down_count();
        if(_row==null) {
        	clickitem.set_down_count(1);
        }else
        {
        	clickitem.set_down_count(_row+1);
        }
        getDataProvider().refreshItem(clickitem);
    	
    }
    // 파일의 MIME 타입을 자동으로 감지하는 메서드
    private String getMimeType(File file) {
        try {
            // 파일의 MIME 타입을 자동으로 감지
            return Files.probeContentType(file.toPath());
        } catch (IOException e) {
            // MIME 타입을 감지할 수 없는 경우 기본값으로 설정
            return "application/octet-stream";  // 기본 바이너리 파일 타입
        }
    }
    private Button createAllDownloadIcon() {
        Button down = new Button("전체다운로드", e -> {
            UI.getCurrent().getPage().executeJs(
                    "document.querySelectorAll('.row-button').forEach(btn => btn.click());"
                );
            });
        down.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
/*
        down.addClickListener(e->{
        	AllDownLoad();
        });
*/        
        return down;
    }
    private Button createAllDeleteIcon() {
        Button down = new Button("전체삭제");
        down.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        down.addClickListener(e->{
        	AllFileDelete();
        });
        return down;
    }
    
    private Button createDeleteIcon(pds_file_row _item) {
        Button delete = new Button("삭제");
        delete.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    	
        delete.addClickListener(e->{
        	FileDelete(_item);
        });
        return delete;
    }
   
  public void AllDownLoad() {
	  
		int size = readBuffer.size();
		int index = 0;
		if(size > 0 ) {				//최초 조회시 
			
		}
	  
	  
  }
  public void AllFileDelete() {
		int size = readBuffer.size();
		int index = 0;
		if(size > 0 ) {				//최초 조회시 
	        for(int i = 0 ;i < size ;i++) {
              delete();
	        }
	        save();
		}
	  
  }
  public void DownLoad(pds_file_row _item) {
      downloadLink.getElement().executeJs("this.click()");
      Integer _row = _item.get_down_count();
      if(_row==null) {
      	_item.set_down_count(1);
      }else
      {
      	_item.set_down_count(_row+1);
      }
      getDataProvider().refreshItem(_item);
      //downloadFile(_item.get_save_path());
	  
  }
  public void FileDelete(pds_file_row _item) {
  	int rowIndex = getDataProvider().fetch(new Query<>()).toList().indexOf(_item);
  	selectRow(rowIndex);
  	delete();
  	save();
  	Notification.show("삭제할 행 번호: " + rowIndex);        	
	  
  }
  
    public void setEvent() {
    	/*
		this.addSelectionListener(e->{
			if(selecteditem != null) {
				copyselect = new pds_file_row(selecteditem);
			}
    	});
    	singleSelect.addValueChangeListener(e->{
    		if(selecteditem!= null) {
    			String entity =selecteditem.get_save_name(); 
    		}else {
    		}
    	});
        this.addItemDoubleClickListener(e->{
            Div pan = new Div();
            columneditor.editItem(e.getItem());
            Component editorComponent =  e.getColumn().getEditorComponent();
            if (editorComponent instanceof Focusable) {
     		   System.out.println("parent="+columneditor.getGrid().getParent().get().getClassName());

                ((Focusable) editorComponent).focus();            
            }  
        });
        this.addItemClickListener(e->{
        });
        columneditor.addCancelListener(e -> {
        });        
    	*/
    }

	public void retrieve(long _id) {
		readBuffer= new ArrayList<>();
		deleteBuffer = new ArrayList<>();
		dataView = null;//수정
		selectedrow = -1;
		readBuffer = new ArrayList<pds_file_row>(Jdbc.getPdsRowList(_id));
		originalBuffer = new ArrayList<>();
		originalBuffer = new ArrayList<>(readBuffer);//추가 
		dataView = setItems((List<pds_file_row>)  readBuffer);//수정
		selectRow(0);
	}
	
	public void insert() {
		pds_file_row select = new pds_file_row();//수정
		 select.setModify("I");
		 if(selectedrow == -1) {
			dataView.addItem(select);
			((List<pds_file_row>)originalBuffer).add(select);
			if(dataView.getItemCount()> 0 ) {
				selectedrow= dataView.getItemCount();
			}
		 }else {
			 dataView.addItemAfter(select, selecteditem);
			 ((List<pds_file_row>)originalBuffer).add(selectedrow+1,select);
		 }
		 dataView.refreshAll();
		 selectRow(selectedrow+1);
	}
	public void lastinsert() {
		pds_file_row select = new pds_file_row();//수정
		 select.setModify("I");
		 int lastrow = originalBuffer.size();
		 dataView.addItem(select);
		 ((List<pds_file_row>)originalBuffer).add(select);
		 dataView.refreshAll();
		 selectRow(selectedrow+1);
	}
	public void save() {
		if(getClassType().equals("G")) {
			if((binder!=null)&&(selecteditem!= null)) {
				binder.setBean(selecteditem);
			}
		}else {
			if((binder!=null)&&(selecteditem!= null)) {
				binder.writeBeanIfValid(selecteditem);
			}
		}
		dataView.refreshAll();
		 List<Object> ins = new ArrayList<>();//수정
		 if(readBuffer.size()==1&&!(((pds_file_hd)readBuffer.get(0)).getModify().equals("I"))) {
			 ((pds_file_hd)readBuffer.get(0)).setModify("U");
		 }
		 ins = (List<Object>)readBuffer;//수정
		 List<Object> del = (List<Object>) deleteBuffer;//수정
		 List<Object> original = (List<Object>) originalBuffer;//수정
		 //this.sqlca.update("sys_module",del, ins); 
//		 this.sqlca.update("pds_file_row",original,del, ins); 

	}
	public void delete() {
		pds_file_row copy = new pds_file_row(selecteditem);
		((List<pds_file_row>) deleteBuffer).add(copy);
		dataView.removeItem(selecteditem);
		originalBuffer.remove(selecteditem);
		if(binder!= null) {
			binder.readBean(null);
		}
		dataView.refreshAll();
		if(readBuffer.size()!=0) {
			selectRow(selectedrow);
		}else {
			selectedrow = -1;
		}
	}
	
}
