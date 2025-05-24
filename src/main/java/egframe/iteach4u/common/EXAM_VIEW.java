package egframe.iteach4u.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gwt.user.client.ui.RadioButton;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexDirection;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;

import egframe.common.SysToolBarControl;
import egframe.common.tinymce.Menubar;
import egframe.common.tinymce.Plugin;
import egframe.common.tinymce.TinyMce;
import egframe.common.tinymce.Toolbar;
import egframe.iteach4u.entity.exm_mat_unit_hd;
import egframe.iteach4u.entity.exm_mat_unit_row;
import egframe.iteach4u.entity.exm_mat_unit_view;
import egframe.iteach4u.entity.usy_class_cd;
import egframe.iteach4u.entity.usy_div_cd;
import egframe.iteach4u.entity.usy_subject_cd;
import egframe.iteach4u.entity.usy_type_cd;
import egframe.iteach4u.entity.usy_unit_cd;
import egframe.iteach4u.views.exm.ExmMakeView;
@Service
public class EXAM_VIEW extends FlexLayout{
	/*
	 * 지문이 없는 클래스
	 * 	_변수는 내부에서 주어진 변수 
	 * 	 
	 */
	private static final long serialVersionUID = 1L;
	//public SysToolBarControl toolbar = new SysToolBarControl();
	public TinyMce hd_mce = new TinyMce();
	public FlexLayout row01 = new FlexLayout();
	public FlexLayout row02 = new FlexLayout();
	public FlexLayout row03 = new FlexLayout();
	public FlexLayout row04 = new FlexLayout();
	public FlexLayout row05 = new FlexLayout();
	public TinyMce row_01 = new TinyMce();
	public TinyMce row_02 = new TinyMce();
	public TinyMce row_03 = new TinyMce();
	public TinyMce row_04 = new TinyMce();
	public TinyMce row_05 = new TinyMce();
	public MAT_VIEW mat_unit ;
	public Button mat = new Button("문제");
	public Button delete = new Button("삭제");
	public FlexLayout hd = new FlexLayout();
	public FlexLayout row = new FlexLayout();
	public HorizontalLayout title = new HorizontalLayout();
	private Button tunit = new Button("단원");
	private Button tdiv = new Button("유형");
/*	
	public RadioButtonGroup<Long> r01 = new RadioButtonGroup<>();
	public RadioButtonGroup<Long> r02 = new RadioButtonGroup<>();
	public RadioButtonGroup<Long> r03 = new RadioButtonGroup<>();
	public RadioButtonGroup<Long> r04 = new RadioButtonGroup<>();
	public RadioButtonGroup<Long> r05 = new RadioButtonGroup<>();
*/
	public Checkbox r01 = new Checkbox();
	public Checkbox r02 = new Checkbox();
	public Checkbox r03 = new Checkbox();
	public Checkbox r04 = new Checkbox();
	public Checkbox r05 = new Checkbox();

	public List<usy_subject_cd> subject_combo_data;
	public exm_mat_unit_view viewitem;
	public exm_mat_unit_hd hditem;
	public List<exm_mat_unit_row> rowlist = new ArrayList();
	public ComboBox<usy_unit_cd> unit_combo = new ComboBox<>(); 
	public List<usy_unit_cd> unit_combo_data;
	public usy_unit_cd selectedunit;
	public ComboBox<usy_div_cd> div_combo = new ComboBox<>();
	public List<usy_div_cd> div_combo_data;
	public usy_div_cd selecteddiv;
	public Long type_id;
	public Long class_id;
	public Long subject_id;
	public Long unit_id;
	public Long div_id;
	public int mat_cnt;
	

	public EXAM_VIEW(MAT_VIEW _mat_unit) {
		mat_unit = _mat_unit;
		_init();
		_initLayout();
		_initTitle();
		_initEvent();
	}
	public void _init() {
		setFlexDirection(FlexDirection.COLUMN);
		//setHeight("600px");
	}
	public void _initTitle() {
		
	}
	
	public void _initLayout() {
		_initHdLayout();
		_initRowLayout();
	}
	public void _initHdLayout() {
		delete.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		tunit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		tdiv.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		mat.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		unit_combo.setItemLabelGenerator(usy_unit_cd::get_unit_nm);
		div_combo.setItemLabelGenerator(usy_div_cd::get_div_nm);

		//title.setHeight("40px");
		title.add(mat,tdiv,div_combo,tunit,unit_combo,delete);
		
		hd.setFlexDirection(FlexDirection.COLUMN);
		//hd.setHeight("300px");
		hd.add(title,hd_mce);
		add(hd);
	}
	public void _initRowLayout() {
		row.setFlexDirection(FlexDirection.COLUMN);
		row01.setFlexDirection(FlexDirection.ROW);
		row02.setFlexDirection(FlexDirection.ROW);
		row03.setFlexDirection(FlexDirection.ROW);
		row04.setFlexDirection(FlexDirection.ROW);
		row05.setFlexDirection(FlexDirection.ROW);
		
		//r01.setItems(1L);
		//r01.getStyle().set("color", "black");
		r01.addValueChangeListener(event -> {
		    boolean oldValue = event.getOldValue();
		    boolean newValue = event.getValue();

		    // 사용자가 동일한 항목을 다시 클릭했을 경우 → 해제
		    if (newValue==true) {
		       r01.setValue(true);
		    	r02.setValue(false);
		    	r03.setValue(false);
		    	r04.setValue(false);
		    	r05.setValue(false);
		    }else {
		    }
		});
		//r02.setItems(2L);
		//r02.getStyle().set("color", "black");
		r02.addValueChangeListener(event -> {
			boolean oldValue = event.getOldValue();
			boolean newValue = event.getValue();

		    // 사용자가 동일한 항목을 다시 클릭했을 경우 → 해제
		    if (newValue==true) {
			       r01.setValue(false);
			    	r02.setValue(true);
			    	r03.setValue(false);
			    	r04.setValue(false);
			    	r05.setValue(false);
		    }
		});
		//r03.setItems(3L);
		//r03.getStyle().set("color", "black");
		r03.addValueChangeListener(event -> {
			boolean oldValue = event.getOldValue();
			boolean newValue = event.getValue();

		    // 사용자가 동일한 항목을 다시 클릭했을 경우 → 해제
		    if (newValue==true) {
			       r01.setValue(false);
			    	r02.setValue(false);
			    	r03.setValue(true);
			    	r04.setValue(false);
			    	r05.setValue(false);
		    }
		});
		
		//r04.setItems(4L);
		//r04.getStyle().set("color", "black");
		r04.addValueChangeListener(event -> {
			boolean oldValue = event.getOldValue();
			boolean newValue = event.getValue();

		    // 사용자가 동일한 항목을 다시 클릭했을 경우 → 해제
		    if (newValue==true) {
			       r01.setValue(false);
			    	r02.setValue(false);
			    	r03.setValue(false);
			    	r04.setValue(true);
			    	r05.setValue(false);
		    }
		});
		//r05.setItems(5L);
		//r05.getStyle().set("color", "black");
		r05.addValueChangeListener(event -> {
			boolean oldValue = event.getOldValue();
			boolean newValue = event.getValue();

		    // 사용자가 동일한 항목을 다시 클릭했을 경우 → 해제
		    if (newValue==true) {
			       r01.setValue(false);
			    	r02.setValue(false);
			    	r03.setValue(false);
			    	r04.setValue(false);
			    	r05.setValue(true);
		    }
		});
		
		row01.add(r01,row_01);
		row02.add(r02,row_02);
		row03.add(r03,row_03);
		row04.add(r04,row_04);
		row05.add(r05,row_05);
		row.add(row01,row02,row03,row04,row05);
		//row.setHeight("300px");
		add(row);
	}
	public void _setTitle(int title) {
		mat.setText(title+" 문제");
	}
	public void reSizeExm() {
		//hd_mce.setHeight("20%");
		//row_01.setHeight("20%");
		//row_02.setHeight("20%");
		//row_03.setHeight("20%");
		//row_04.setHeight("20%");
		//row_05.setHeight("20%");
	}
	public void _initEvent() {
		delete.addClickListener(event -> {
            getParentRecursively(this, MAT_VIEW.class).ifPresent(parent -> {
                ((MAT_VIEW) parent).wDelete();
            });            
        });
		div_combo.addValueChangeListener(e->{
	   		usy_div_cd val = new usy_div_cd();
	   		val = e.getValue();
	   		if(val== null) {
	   			return ;
	   		}
	   		if(hditem != null) {
		   		hditem.set_div_id(e.getValue().getId());
		   		mat_cnt = val.get_mat_cnt();
		   		_setMathCnt(mat_cnt);
	   		}
		});
		hd_mce.addValueChangeListener(e->{
			hditem.set_context(hd_mce.getValue());
	   	});
	   	row_01.addValueChangeListener(e->{
			rowlist.get(0).set_context(row_01.getValue());
	   	});
	   	row_02.addValueChangeListener(e->{
			rowlist.get(1).set_context(row_02.getValue());
	   	});
	   	row_03.addValueChangeListener(e->{
			rowlist.get(2).set_context(row_03.getValue());
	   	});
	   	row_04.addValueChangeListener(e->{
			rowlist.get(3).set_context(row_04.getValue());
	   	});
	   	row_05.addValueChangeListener(e->{
			rowlist.get(4).set_context(row_05.getValue());
	   	});

	   	unit_combo.addValueChangeListener(e->{
	   		if(hditem == null) {
	   			return;
	   		}
	   		usy_unit_cd val = new usy_unit_cd();
	   		val = e.getValue();
	   		if(val== null) {
	   			return ;
		   	}else {
		   		unit_id = val.getId();
		   		hditem.set_unit_id(unit_id);
		   	}
       });
		
	   	hd_mce.addFocusListener(e->{
            getParentRecursively(this, MAT_CONTROL_VIEW.class).ifPresent(parent -> {
                ((MAT_CONTROL_VIEW) parent).setMce(hd_mce);
            });
	   	});
	   	row_01.addFocusListener(e->{
            getParentRecursively(this, MAT_CONTROL_VIEW.class).ifPresent(parent -> {
                ((MAT_CONTROL_VIEW) parent).setMce(row_01);
            });
	   	});
	   	row_02.addFocusListener(e->{
            getParentRecursively(this, MAT_CONTROL_VIEW.class).ifPresent(parent -> {
                ((MAT_CONTROL_VIEW) parent).setMce(row_02);
            });
	   	});
	   	row_03.addFocusListener(e->{
            getParentRecursively(this, MAT_CONTROL_VIEW.class).ifPresent(parent -> {
                ((MAT_CONTROL_VIEW) parent).setMce(row_03);
            });
	   	});
	   	row_04.addFocusListener(e->{
            getParentRecursively(this, MAT_CONTROL_VIEW.class).ifPresent(parent -> {
                ((MAT_CONTROL_VIEW) parent).setMce(row_04);
            });
	   	});
	   	row_05.addFocusListener(e->{
            getParentRecursively(this, MAT_CONTROL_VIEW.class).ifPresent(parent -> {
                ((MAT_CONTROL_VIEW) parent).setMce(row_05);
            });
	   	});
	   	
	}
	
	private Optional<Component> getParentRecursively(Component component, Class<?> targetClass) {
	    Optional<Component> parent = component.getParent();
	    while (parent.isPresent()) {
	        if (targetClass.isInstance(parent.get())) {
	            return parent;
	        }
	        parent = parent.get().getParent();
	    }
	    return Optional.empty();
	}	
	public void _setMathCnt(int _cnt) {
		row_01.setVisible(false);
		row_02.setVisible(false);
		row_03.setVisible(false);
		row_04.setVisible(false);
		row_05.setVisible(false);
   		if(_cnt==0) {
		}else if(_cnt==1) {
			row_01.setVisible(true);
//			rowlist.remove(1);
//			rowlist.remove(2);
//			rowlist.remove(3);
//			rowlist.remove(4);
		}else if(_cnt==2) {
			row_01.setVisible(true);
			row_02.setVisible(true);
//			rowlist.remove(2);
//			rowlist.remove(3);
//			rowlist.remove(4);
		}else if(_cnt==3) {
			row_01.setVisible(true);
			row_02.setVisible(true);
			row_03.setVisible(true);
//			rowlist.remove(3);
//			rowlist.remove(4);
		}else if(_cnt==4) {
			row_01.setVisible(true);
			row_02.setVisible(true);
			row_03.setVisible(true);
			row_04.setVisible(true);
//			rowlist.remove(4);
		}else if(_cnt==5) {
			row_01.setVisible(true);
			row_02.setVisible(true);
			row_03.setVisible(true);
			row_04.setVisible(true);
			row_05.setVisible(true);
		}
		
	}
	public void _setMatUnit(MAT_VIEW _mat) {
		mat_unit = _mat;
	}
	public void _setToolbar(SysToolBarControl _toolbar) {
	}
	public void _setRowData(List<exm_mat_unit_row> _item) {
		rowlist = _item;
		if(rowlist.size()==0) {
			row_01.setVisible(false);
			row_02.setVisible(false);
			row_03.setVisible(false);
			row_04.setVisible(false);
			row_05.setVisible(false);
		}else if(rowlist.size()==1) {
			row_02.setVisible(false);
			row_03.setVisible(false);
			row_04.setVisible(false);
			row_05.setVisible(false);
			row_01.setPresentationValue(rowlist.get(0).get_context());
		}else if(rowlist.size()==2) {
			row_03.setVisible(false);
			row_04.setVisible(false);
			row_05.setVisible(false);
			row_01.setPresentationValue(rowlist.get(0).get_context());
			row_02.setPresentationValue(rowlist.get(1).get_context());
		}else if(rowlist.size()==3) {
			row_04.setVisible(false);
			row_05.setVisible(false);
			row_01.setPresentationValue(rowlist.get(0).get_context());
			row_02.setPresentationValue(rowlist.get(1).get_context());
			row_03.setPresentationValue(rowlist.get(2).get_context());
		}else if(rowlist.size()==4) {
			row_05.setVisible(false);
			row_01.setPresentationValue(rowlist.get(0).get_context());
			row_02.setPresentationValue(rowlist.get(1).get_context());
			row_03.setPresentationValue(rowlist.get(2).get_context());
			row_04.setPresentationValue(rowlist.get(3).get_context());
		}else if(rowlist.size()==5) {
			row_01.setPresentationValue(rowlist.get(0).get_context());
			row_02.setPresentationValue(rowlist.get(1).get_context());
			row_03.setPresentationValue(rowlist.get(2).get_context());
			row_04.setPresentationValue(rowlist.get(3).get_context());
			row_05.setPresentationValue(rowlist.get(4).get_context());
		}
	}
	public void _setData(exm_mat_unit_view _viewitem, exm_mat_unit_hd _hditem, List<exm_mat_unit_row> _rowlist) {
		viewitem = _viewitem;
		hditem = _hditem;
		rowlist = _rowlist;
	}
	public void setViewMat() {
		tdiv.setVisible(false);
		tunit.setVisible(false);
		unit_combo.setVisible(false);
		div_combo.setVisible(false);
		delete.setVisible(false);
		row_01.setEnabled(false);
		row_02.setEnabled(false);
		row_03.setEnabled(false);
		row_04.setEnabled(false);
		row_05.setEnabled(false);
		hd_mce.setEnabled(false);

	}
}

