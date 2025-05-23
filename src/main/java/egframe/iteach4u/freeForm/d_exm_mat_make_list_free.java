package egframe.iteach4u.freeForm;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.jdbc.core.JdbcTemplate;
import org.vaadin.addons.thshsh.upload.UploadField;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;

import egframe.common.SysFree;
import egframe.iteach4u.entity.exm_mat_make_list;
import egframe.iteach4u.entity.pds_file_hd;
import egframe.iteach4u.service.Repository.Iteach4uService;

import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.FileBuffer;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.server.StreamResource;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
public class d_exm_mat_make_list_free extends SysFree{ 


	public IntegerField id	= new IntegerField ("문제지번호")	;
	public TextField passed_nm	= new TextField ("문제지명")	;
	public TextField exam_type	= new TextField ("문제지종류")	;
	public IntegerField tot_num	= new IntegerField ("총 문항수")	;
	public IntegerField time_limit	= new IntegerField ("제한시간")	;
	public TextField update_id		= new TextField ("등록자")	;
	public DatePicker update_dt = new DatePicker("등록일자");
	public Iteach4uService sqlca;
	public exm_mat_make_list list = new exm_mat_make_list();
	public Item selectedexamtype;
	public List<Item> items;
	public ComboBox<Item> combo01;
	public 

	   class Item {
        String name;
        String code;

        public Item(String name, String code) {
            this.name = name;
            this.code = code;
        }

        public String getName() { return name; }
        public String getCode() { return code; }
    }	   
   public d_exm_mat_make_list_free() {
	   _init();
	   update_dt.setValue(LocalDate.now());
   }
	
   public d_exm_mat_make_list_free(Iteach4uService dbo) {
	   sqlca = dbo;
	   _init();
	   update_dt.setValue(LocalDate.now());
   }
   public void _init() {
	   
       binder = new BeanValidationBinder<>(exm_mat_make_list.class);
       binder.setBean(new exm_mat_make_list()); 
       binder.bindInstanceFields(this);
       
	   this.setHeight("100px");
	   setResponsiveSteps(
			   new ResponsiveStep("0", 2) ,
			   new ResponsiveStep("600px", 8)
	   );
	   this.setSizeFull();
	   //getStyle().set("border", "2px solid green");
	   items = List.of(
	            new Item("모의고사", "01"),
	            new Item("중간고사", "02"),
	            new Item("기말고사", "03")
	        );	   
	   combo01 = new ComboBox<>("문제지종류");
	   combo01.setItems(items);
	   combo01.setItemLabelGenerator(item -> item.getName() + " (" + item.getCode() + ")");
	   
	   combo01.setEnabled(false);
	   id.setEnabled(false);
	   update_id.setEnabled(false);
	   update_dt.setEnabled(false);
	   setColspan(passed_nm, 3); // 또는 3, 레이아웃의 column 수에 따라
	   add (id,passed_nm,tot_num,time_limit,update_id,update_dt);
	   setEvent();	
   }
   
   public void setEvent() {
	   id.setValueChangeMode(ValueChangeMode.EAGER);
	   passed_nm.setValueChangeMode(ValueChangeMode.EAGER);
	   tot_num.setValueChangeMode(ValueChangeMode.EAGER);
	   time_limit.setValueChangeMode(ValueChangeMode.EAGER);
	   update_id.setValueChangeMode(ValueChangeMode.EAGER);
	   tot_num.addValueChangeListener(e->{
		   int val = e.getValue();
		   list.set_tot_num(Long.valueOf(val));
	   });
	   time_limit.addValueChangeListener(e->{
		   int val = e.getValue();
		   list.set_time_limit(Long.valueOf(val));
	   });
	   passed_nm.addValueChangeListener(e->{
		   list.set_passed_nm(e.getValue());
	   });
	   combo01.addValueChangeListener(e->{
		   Item item = e.getValue();
		   list.set_exam_type(item.getCode());
	   });
   }
   public void _setData(exm_mat_make_list _list) {
	   list = _list;
	   if(list.getId()!=0) {
		   update_id.setValue(list.get_update_id());
		   tot_num.setValue(list.get_tot_num().intValue());
		   time_limit.setValue(list.get_time_limit().intValue());
		   id.setValue(list.getId().intValue());
		   update_dt.setValue(list.get_update_dt());
		   exam_type.setValue(list.get_exam_type());
		   selectedexamtype = items.stream().filter(item -> item.getCode().equals(list.get_exam_type())).findFirst().orElse(null);
		   if (selectedexamtype != null) {
			   combo01.setValue(selectedexamtype); 
		   }
		   if(list.get_passed_nm()==null) {
			   list.set_passed_nm("");
			   passed_nm.setValue("");
		   }else {
			   passed_nm.setValue(list.get_passed_nm());
		   }
	   }
   }
 }