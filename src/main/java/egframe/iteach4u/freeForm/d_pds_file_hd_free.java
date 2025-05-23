package egframe.iteach4u.freeForm;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import egframe.common.SysBindControl;
import egframe.common.SysEntity;
import egframe.common.SysFree;
import egframe.common.SysFreeLayout;
import egframe.common.SysGrid;
import egframe.iteach4u.entity.pds_file_hd;
import egframe.iteach4u.entity.usy_class_cd;
import egframe.iteach4u.entity.usy_grade_cd;
import egframe.iteach4u.entity.usy_subject_cd;
import egframe.iteach4u.entity.usy_unit_cd;
import egframe.iteach4u.gridForm.d_pds_file_hd;
import egframe.iteach4u.service.Repository.Iteach4uService;
import jakarta.persistence.EntityManager;
import javafx.util.StringConverter;
import lombok.experimental.FieldNameConstants;

import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.PropertyId;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.data.renderer.ComponentRenderer;
//import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.google.gwt.view.client.ListDataProvider;
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
@FieldNameConstants  
public class d_pds_file_hd_free extends SysFree { 


	public TextField cov_yn		= new TextField ("컨버전")	;
	public TextField user_id		= new TextField ("유저 아이디")	;
	public Iteach4uService sqlca;
	VerticalLayout grade_layout = new VerticalLayout();
	public RadioButtonGroup<usy_grade_cd> radio01 = new  RadioButtonGroup<>();
	public List<usy_grade_cd> grade_data ;
	Div radio = new Div();

	public Long typeid;
	public Long classid;
	public Long subjectid;
	public Long unitid;
	
	public IntegerField 	id	= new IntegerField ("순번");
	public IntegerField type_id			= new IntegerField ("구분");
	public TextField 		type_nm		= new TextField ("구분")	;
	public IntegerField class_id		= new IntegerField ("학년")	;
	public TextField 	class_nm			= new TextField ("학년")	;
	public IntegerField subject_id		= new IntegerField ("과목");
	public TextField 	subject_nm			= new TextField ("과목")	;
	public IntegerField unit_id			= new IntegerField ("단원");
	public TextField 	unit_nm			= new TextField ("단원")	;
	public TextField grade_cd			= new TextField ("평가")	;
	public TextArea memo					= new TextArea ("파일 개요")	;
	public TextField title				= new TextField ("제목")	;
	public IntegerField 	down_count		= new IntegerField ("총 다운로드수")	;
	public IntegerField 	file_count		= new IntegerField ("총 파일수")	;
	public DatePicker 	update_dt 		= new DatePicker("등록일자");

	public ComboBox<usy_subject_cd> subject_combo = new ComboBox<>("과목");
	private List<usy_subject_cd> subject_combo_data;
	private usy_subject_cd selectedsubject;

	public ComboBox<usy_unit_cd> unit_combo = new ComboBox<>("단원");
	private List<usy_unit_cd> unit_combo_data;
	private usy_unit_cd selectedunit;
	
   public d_pds_file_hd_free() {
	   this.addClassName("main-layout");
	   init();
   }
   public d_pds_file_hd_free(Iteach4uService dbo) {
	   sqlca = dbo;
	   init();
	   addEvent();
   }
   public void init() {
	   usy_subject_cd subject = new usy_subject_cd();
	   subject_combo.setItemLabelGenerator(usy_subject_cd -> usy_subject_cd.get_subject_nm());
	   unit_combo.setItemLabelGenerator(usy_unit_cd -> usy_unit_cd.get_unit_nm());
	   
      add(type_nm,class_nm,subject_combo,unit_combo,title,memo);
      
       class_nm.addValueChangeListener(e->{			if(e.getValue()!=null) {				addEvent("class_nm",e);}});
       title.addValueChangeListener(e->{			if(e.getValue()!=null) {				addEvent("title",e);}});
       memo.addValueChangeListener(e->{			if(e.getValue()!=null) {				addEvent("memo",e);}});
       
       subject_combo.addValueChangeListener(e->{			if(e.getValue()!=null) {	
		   Long val = e.getValue().getId();
		   String str = String.valueOf(val);
		   subject_id.setValue(Integer.valueOf(str));
		   subject_nm.setValue(e.getValue().get_subject_nm());
       }});
     
       
       unit_combo.addValueChangeListener(e->{			if(e.getValue()!=null) {		
		   Long val = e.getValue().getId();
		   String str = String.valueOf(val);
		   unit_id.setValue(Integer.valueOf(str));
		   unit_nm.setValue(e.getValue().get_unit_nm());
       }});
		
       binder = new BeanValidationBinder<>(pds_file_hd.class);
       binder.setBean(new pds_file_hd()); 
       binder.bindInstanceFields(this);
   }
    
   public void addEvent() {
	   class_id.addValueChangeListener(e->{
		   if(e.getValue()!=null) {
	    	   if(e.getOldValue()!=null) {
				   classid = Long.valueOf(e.getValue());
				   subject_combo_data =sqlca.getSubjectList(Long.valueOf(class_id.getValue())); 
				   subject_combo.setItems(subject_combo_data);
				   setCombo();
	    	   }else {//최초 화면 
	    		   
				   //classid = Long.valueOf(e.getValue());
				   subject_combo_data =sqlca.getSubjectList(Long.valueOf(classid)); 
				   subject_combo.setItems(subject_combo_data);
				   //setCombo();
				   
	    	   }
	    }
	   });
	   subject_id.addValueChangeListener(e->{
		   if(e.getValue()!=null) {
	    	   if(e.getOldValue()!=null) {
	    		   addComboEvent("subject_id",subjectid,Long.valueOf(e.getValue()));
	    	   }else {
	    		   addComboEvent("subject_id","",Long.valueOf(e.getValue()));
	    	   }
			   subjectid = Long.valueOf(e.getValue());
			   unit_combo_data =sqlca.getUsyUnitCdList(Long.valueOf(subject_id.getValue())); 
			   unit_combo.setItems(unit_combo_data);
			   setCombo();
		   }
	   });
	   unit_id.addValueChangeListener(e->{
		   if(e.getValue()!=null) {
    	   if(e.getOldValue()!=null) {
    		   addComboEvent("unit_id",unitid,Long.valueOf(e.getValue()));
    	   }else {
    		   addComboEvent("unit_id","",Long.valueOf(e.getValue()));
    	   }
		   }
		   
	   });
	   subject_nm.addValueChangeListener(e->{
		   if(e.getValue()!=null) {
    	   if(e.getOldValue()!=null) {
    		   addComboEvent("subject_nm",subjectid,e.getValue());
    	   }else {
    		   addComboEvent("subject_nm","",e.getValue());
    	   }
		   }
	   });
	   unit_nm.addValueChangeListener(e->{
		   if(e.getValue()!=null) {
    	   if(e.getOldValue()!=null) {
    		   addComboEvent("unit_nm",e.getOldValue(),e.getValue());
    	   }else {
    		   addComboEvent("unit_nm","",e.getValue());
    	   }
		   }
	   });
   }
   public void setCombo() {
	   
	   if(subject_combo_data!= null) {
			selectedsubject = subject_combo_data.stream().filter(usy_subject_cd -> usy_subject_cd.getId().equals(subjectid)).findFirst().orElse(null);
			if (selectedsubject != null) {
				subject_combo.setValue(selectedsubject); 
			}   
	   }
	   if(unit_combo_data!= null) {
		   selectedunit = unit_combo_data.stream().filter(usy_unit_cd -> usy_unit_cd.getId().equals(unitid)).findFirst().orElse(null);
			if (selectedsubject != null) {
				unit_combo.setValue(selectedunit); 
			}   
	   }
   }   
}