package egframe.iteach4u.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexDirection;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

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
import egframe.iteach4u.views.exm.ExmMakeInsert;
import egframe.iteach4u.views.exm.ExmMakeView;
import elemental.json.Json;
import elemental.json.JsonObject;
import jakarta.annotation.PreDestroy;
@Service
public class MAT_VIEW extends FlexLayout{
	/*
	 * 지문이 있는 클래스 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
    private JdbcTemplate Jdbc;	
	public FlexLayout mainlayout = new FlexLayout();
	public FlexLayout mathlayout = new FlexLayout();
	public FlexLayout viewlayout = new FlexLayout();
	public TinyMce view_mce = new TinyMce();
	public exm_mat_unit_view item;
	public EXAM_VIEW exam_unit;
	public Button tview = new Button("지문");
	public Button wadd = new Button("추가");
	public List<usy_subject_cd> subject_combo_data;

	public Long type_id;
	public Long class_id;
	public Long subject_id;
	public Long unit_id;
	public Long div_id;
	public String view_yn;
	public ComboBox<usy_unit_cd> unit_combo = new ComboBox<>(); 
	public List<usy_unit_cd> unit_combo_data;
	public usy_unit_cd selectedunit;
	public ComboBox<usy_div_cd> div_combo = new ComboBox<>();
	public List<usy_div_cd> div_combo_data;
	public usy_div_cd selecteddiv;
	public int mat_cnt ;				//예제 수 
	public int mat_num ;
	public exm_mat_unit_view viewitem;
	public exm_mat_unit_hd hditem;
	public exm_mat_unit_row rowitem;
	public List<exm_mat_unit_row> rowlist= new ArrayList<>();
	public List<exm_mat_unit_hd> hdlist= new ArrayList<>();
	public List<exm_mat_unit_view> viewlist= new ArrayList<>();
	
	public MAT_VIEW() {
		_init();
		_initViewLayout();
		_initMathLayout();
		_initEvent();
	}
	public MAT_VIEW(exm_mat_unit_hd _hditem) {
		hditem = _hditem;
		_init();
		_initViewLayout();
		_initMathLayout();
		_initEvent();
	}
	public MAT_VIEW(boolean flag) {
		_init();
		_initViewLayout();
		_initMathLayout();
		_initEvent();
		setViewVisible(flag);
	}

	public void _init() {
		mainlayout.setSizeFull();
		setFlexDirection(FlexDirection.COLUMN);
		tview.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		wadd.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		mainlayout.setFlexDirection(FlexDirection.ROW);
		
		mainlayout.add(viewlayout,mathlayout);
		add(mainlayout);
	}
	public void _setViewLayoutControl(boolean b) {
		viewlayout.setVisible(b);
	}
	public void _initViewLayout() {
		viewlayout.setWidth("50%");
		mathlayout.setWidth("50%");
		mathlayout.setFlexDirection(FlexDirection.COLUMN);
		viewlayout.setFlexDirection(FlexDirection.COLUMN);
		tview.setHeight("40px");
		viewlayout.add(tview,view_mce);
		mainlayout.add(viewlayout);
	}
	public void _initMathLayout() {
		mathlayout.setFlexDirection(FlexDirection.COLUMN);
		exam_unit = new EXAM_VIEW(this);
		mathlayout.add(exam_unit,wadd);
		mainlayout.add(mathlayout);
	}
	
	public void _resizeMce() {

		viewlayout.getStyle().set("border", "1px solid red");
		mathlayout.getStyle().set("border", "1px solid blue");

		
		view_mce.configurePlugin(false,Plugin.AUTORESIZE)
	    .configure("min_height", 30)   // 한 줄 높이로 시작
	    .configure("max_height", 300)  // 자동 크기 제한 (원하는 값으로)
	    .configure("autoresize_bottom_margin", 0);
		view_mce.configure("content_style", 
			    "body { font-size: 14px; line-height: 1.4; }"
			);
		
		
		exam_unit.hd_mce.configurePlugin(false,Plugin.AUTORESIZE)
	    .configure("min_height", 30)   // 한 줄 높이로 시작
	    .configure("max_height", 300)  // 자동 크기 제한 (원하는 값으로)
	    .configure("autoresize_bottom_margin", 0);
		exam_unit.hd_mce.configure("content_style", 
			    "body { font-size: 14px; line-height: 1.4; }"
			);
		
		exam_unit.row_01.configurePlugin(false,Plugin.AUTORESIZE)
	    .configure("min_height", 30)   // 한 줄 높이로 시작
	    .configure("max_height", 300)  // 자동 크기 제한 (원하는 값으로)
	    .configure("autoresize_bottom_margin", 0);
		exam_unit.row_01.configure("content_style", 
			    "body { font-size: 14px; line-height: 1.4; }"
			);
		
		exam_unit.row_02.configurePlugin(false,Plugin.AUTORESIZE)
	    .configure("min_height", 30)   // 한 줄 높이로 시작
	    .configure("max_height", 300)  // 자동 크기 제한 (원하는 값으로)
	    .configure("autoresize_bottom_margin", 0);
		exam_unit.row_02.configure("content_style", 
			    "body { font-size: 14px; line-height: 1.4; }"
			);
		
		exam_unit.row_03.configurePlugin(false,Plugin.AUTORESIZE)
	    .configure("min_height", 30)   // 한 줄 높이로 시작
	    .configure("max_height", 300)  // 자동 크기 제한 (원하는 값으로)
	    .configure("autoresize_bottom_margin", 0);
		exam_unit.row_03.configure("content_style", 
			    "body { font-size: 14px; line-height: 1.4; }"
			);
		
		exam_unit.row_04.configurePlugin(false,Plugin.AUTORESIZE)
	    .configure("min_height", 30)   // 한 줄 높이로 시작
	    .configure("max_height", 300)  // 자동 크기 제한 (원하는 값으로)
	    .configure("autoresize_bottom_margin", 0);
		exam_unit.row_04.configure("content_style", 
			    "body { font-size: 14px; line-height: 1.4; }"
			);
		
		exam_unit.row_05.configurePlugin(false,Plugin.AUTORESIZE)
	    .configure("min_height", 30)   // 한 줄 높이로 시작
	    .configure("max_height", 300)  // 자동 크기 제한 (원하는 값으로)
	    .configure("autoresize_bottom_margin", 0);
		exam_unit.row_05.configure("content_style", 
			    "body { font-size: 14px; line-height: 1.4; }"
			);
		
		/*
  		view_mce.configurePlugin(true,Plugin.AUTORESIZE);
  		exam_unit.hd_mce.configurePlugin(true,Plugin.AUTORESIZE);
  		exam_unit.row_01.configurePlugin(true,Plugin.AUTORESIZE);
  		exam_unit.row_02.configurePlugin(true,Plugin.AUTORESIZE);
  		exam_unit.row_03.configurePlugin(true,Plugin.AUTORESIZE);
  		exam_unit.row_04.configurePlugin(true,Plugin.AUTORESIZE);
  		exam_unit.row_05.configurePlugin(true,Plugin.AUTORESIZE);
  		*/
		
	if(hditem.get_row_num()==0) {
		exam_unit.row_01.setVisible(false);
		exam_unit.row_02.setVisible(false);
		exam_unit.row_03.setVisible(false);
		exam_unit.row_04.setVisible(false);
		exam_unit.row_05.setVisible(false);
		exam_unit.r01.setVisible(false);
		exam_unit.r02.setVisible(false);
		exam_unit.r03.setVisible(false);
		exam_unit.r04.setVisible(false);
		exam_unit.r05.setVisible(false);
  		System.out.println("0 : "+hditem.get_row_num());
	}else if(hditem.get_row_num()==1) {
  		exam_unit.row_02.setVisible(false);
  		exam_unit.row_03.setVisible(false);
  		exam_unit.row_04.setVisible(false);
  		exam_unit.row_05.setVisible(false);
  		exam_unit.r02.setVisible(false);
  		exam_unit.r03.setVisible(false);
  		exam_unit.r04.setVisible(false);
  		exam_unit.r05.setVisible(false);
  		System.out.println("1 : "+hditem.get_row_num());
  	}else if(hditem.get_row_num()==2) {
  		exam_unit.row_03.setVisible(false);
  		exam_unit.row_04.setVisible(false);
  		exam_unit.row_05.setVisible(false);
  		exam_unit.r03.setVisible(false);
  		exam_unit.r04.setVisible(false);
  		exam_unit.r05.setVisible(false);
  		System.out.println("2 : "+hditem.get_row_num());
  	}else if(hditem.get_row_num()==3) {
  		exam_unit.row_04.setVisible(false);
  		exam_unit.row_05.setVisible(false);
  		exam_unit.r04.setVisible(false);
  		exam_unit.r05.setVisible(false);
  		System.out.println("3 : "+hditem.get_row_num());
  	}else if(hditem.get_row_num()==4) {
  		exam_unit.row_05.setVisible(false);
  		exam_unit.r05.setVisible(false);
  		System.out.println("4 : "+hditem.get_row_num());
  	}else if(hditem.get_row_num()==5) {
  		System.out.println("5 : "+hditem.get_row_num());
  	}else {
  		System.out.println("X : "+hditem.get_row_num());
  	}
	}
	public void _initEvent() {
	   	view_mce.addFocusListener(e->{
            getParentRecursively(this, MAT_CONTROL_VIEW.class).ifPresent(parent -> {
//                ((MAT_CONTROL_VIEW) parent).setMce(view_mce);
            });
	   	});

	   	
	   	exam_unit.hd_mce.addFocusListener(e->{
	   		exam_unit.hd_mce.configure("content_style", 
	   			    "html { filter: invert(1) hue-rotate(180deg); }"
	   			);
            getParentRecursively(this, MAT_CONTROL_VIEW.class).ifPresent(parent -> {
//                ((MAT_CONTROL) parent).setMce(hd_mce);
            });
	   	});
	   	exam_unit.row_01.addFocusListener(e->{
            getParentRecursively(this, MAT_CONTROL_VIEW.class).ifPresent(parent -> {
//                ((MAT_CONTROL) parent).setMce(row_01);
            });
	   	});
	   	exam_unit.row_02.addFocusListener(e->{
            getParentRecursively(this, MAT_CONTROL_VIEW.class).ifPresent(parent -> {
//                ((MAT_CONTROL) parent).setMce(row_02);
            });
	   	});
	   	exam_unit.row_03.addFocusListener(e->{
            getParentRecursively(this, MAT_CONTROL_VIEW.class).ifPresent(parent -> {
//                ((MAT_CONTROL) parent).setMce(row_03);
            });
	   	});
	   	exam_unit.row_04.addFocusListener(e->{
            getParentRecursively(this, MAT_CONTROL_VIEW.class).ifPresent(parent -> {
//                ((MAT_CONTROL) parent).setMce(row_04);
            });
	   	});
	   	exam_unit.row_05.addFocusListener(e->{
            getParentRecursively(this, MAT_CONTROL_VIEW.class).ifPresent(parent -> {
//                ((MAT_CONTROL) parent).setMce(row_05);
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
	public void _setToolbar(SysToolBarControl _toolbar) {
		exam_unit._setToolbar(_toolbar);
	}
	public void _removeAdd() {
		mathlayout.remove(wadd);
	}
	public void _insertAdd() {
		mathlayout.add(wadd);
	}
	public void wDelete() {
        getParentRecursively(this, MAT_CONTROL.class).ifPresent(parent -> {
            ((MAT_CONTROL) parent).wDelete(hditem.getId());
        });            
		getParent().ifPresent(parent -> ((FlexLayout) parent).remove(this));
	}
	public void _setData(exm_mat_unit_view _viewitem, exm_mat_unit_hd _hditem, List<exm_mat_unit_row> _rowlist) {
		viewitem = _viewitem;
		hditem = _hditem;
		rowlist=_rowlist;
		exam_unit._setData(viewitem, hditem, rowlist);
	}
	public void setData(exm_mat_unit_view _viewitem, exm_mat_unit_hd _hditem, List<exm_mat_unit_row> _rowlist) {
		viewitem = _viewitem;
		hditem = _hditem;
		rowlist=_rowlist;
		if(viewitem.get_context().equals("")) {
			view_mce.setVisible(false);
		}else {
			view_mce.setPresentationValue(viewitem.get_context());
		}
		exam_unit.hd_mce.setPresentationValue(hditem.get_context());
		for(int i =0 ; i < rowlist.size();i++) {
			if(i==0) {
				exam_unit.row_01.setPresentationValue(rowlist.get(i).get_context());
			}else if(i==1) {
				exam_unit.row_02.setPresentationValue(rowlist.get(i).get_context());
			}else if(i==2) {
				exam_unit.row_03.setPresentationValue(rowlist.get(i).get_context());
			}else if(i==3) {
				exam_unit.row_04.setPresentationValue(rowlist.get(i).get_context());
			}else if(i==4) {
				exam_unit.row_05.setPresentationValue(rowlist.get(i).get_context());
			}
		}
	}
	public void setViewVisible(boolean flag) {
		tview.setVisible(flag);
		view_mce.setVisible(flag);
		if(flag) {
			viewlayout.setHeightFull();
		}
		_setPrintMat(flag);

	}
	private void _setPrintMat(boolean flag) {
		if(hditem!=null) {
			div_id = hditem.get_div_id();
			if(mat_num < 6) {
				if(flag) {//문제를 vieww 아래로 추가 
					mathlayout.remove(exam_unit);
					viewlayout.add(exam_unit);
				}else {//view로 이동 
					mathlayout.remove(exam_unit);
					viewlayout.add(exam_unit);
				}
			}else {
				
			}
		}
		
	}
	
	public void _setTitile(int i) {
		if(i==0) {
			i = 1 ;
		}else {
			i=i+1;
		}
		exam_unit._setTitle(i);
		mat_num = i ;
	}
	public void setViewMat() {
		exam_unit.setViewMat();
		view_mce.setEnabled(false);
	}
	
}
