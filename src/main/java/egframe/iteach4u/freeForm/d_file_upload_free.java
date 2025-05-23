package egframe.iteach4u.freeForm;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.jdbc.core.JdbcTemplate;
import org.vaadin.addons.thshsh.upload.UploadField;

import egframe.common.SysFree;
import egframe.common.SysGrid;
import egframe.iteach4u.entity.pds_file_hd;
import egframe.iteach4u.service.Repository.Iteach4uService;

import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.FileBuffer;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
public class d_file_upload_free extends SysFree{ 


	public TextField file_name		= new TextField ("파일이름")	;
	public TextField save_name	= new TextField ("저장이름")	;
	public TextField save_path	= new TextField ("저장경로")	;
	
   public d_file_upload_free() {
   }
   public d_file_upload_free(Iteach4uService dbo) {
 	   	init();
   }
   public void init() {
//       add(new Span("업로드 파일 선택"));
	   
	   add (file_name,save_name,save_path,new Span("업로드 파일 선택"));
	   setEvent();	
   }
   
   public void setEvent() {
	   file_name.setValueChangeMode(ValueChangeMode.EAGER);
	   save_name.setValueChangeMode(ValueChangeMode.EAGER);
	   save_path.setValueChangeMode(ValueChangeMode.EAGER);
		
	   file_name.addValueChangeListener(e->{			if(e.getValue()!=null) {				addEvent("file_name",e);}});
	   save_name.addValueChangeListener(e->{			if(e.getValue()!=null) {				addEvent("save_name",e);}});
	   save_path.addValueChangeListener(e->{			if(e.getValue()!=null) {				addEvent("save_path",e);}});
   }
   

   public static class Bean {
       List<UploadFile> files;

       public Bean() {
           super();
       }

       public List<UploadFile> getFiles() {
           if (files == null) files = new ArrayList<>();
           return files;
       }

       public void setFiles(List<UploadFile> files) {
           this.files = files;
       }
   }

   public static class UploadFile {
       private String fileName;
       private File file;

       public UploadFile(String fileName, File file) {
           this.fileName = fileName;
           this.file = file;
       }

       public String getFileName() {
           return fileName;
       }

       public File getFile() {
           return file;
       }

       @Override
       public String toString() {
           return fileName;
       }
   }


   
 }