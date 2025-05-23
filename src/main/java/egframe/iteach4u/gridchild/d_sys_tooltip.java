package egframe.iteach4u.gridchild;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.event.TableModelListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import egframe.common.SysGrid;
import egframe.common.SysGridChild;
import egframe.iteach4u.entity.usy_unit_cd;
import egframe.iteach4u.service.Repository.Iteach4uService;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSelectionModel;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
public class d_sys_tooltip extends SysGridChild<usy_unit_cd> {//implements DataWindowImpl { 
	private JdbcTemplate con;
	private Iteach4uService sqlcb;
	private JdbcTemplate sys_con;
	private Iteach4uService sqlca;
	private BeanValidationBinder binder ;
	//private SysFreeForm freeform;
    public d_sys_tooltip() {
    	//init();
	} 
    public d_sys_tooltip(@Autowired Iteach4uService dbo) {
    	sqlca = dbo;
	   	setCon(dbo.getCon());
	   	setTitle("unit_cd");
    	init();
    }
    public void init(){
    	this.setSizeFull();
        this.getElement().getStyle().set("z-index", "99");
		setSelectionMode(Grid.SelectionMode.SINGLE);
		dataView = setItems((List<usy_unit_cd>)  readBuffer);//수정
		removeAllColumns();
        addColumn(item -> item.getValue("unit_cd")).setHeader("단원").setAutoWidth(true).setSortable(true).setKey("unit_cd");
        addColumn(item -> item.getValue("unit_nm")).setHeader("단원명").setAutoWidth(true).setSortable(true).setKey("unit_nm").setVisible(true);
		addColumn(item -> item.getValue("count")).setHeader("문항수").setAutoWidth(true).setSortable(true).setKey("count").setVisible(true);
		addColumn(item -> item.getValue("modify")).setHeader("modify_yn").setAutoWidth(true).setSortable(true).setKey("modify").setVisible(false);
    	//}
    	
		this.addSelectionListener(e->{
			if(selecteditem != null) {
				//copyselect = new sys_module(selecteditem);
			}
    		//itemChangeEvent(e);
    	});
        this.addItemDoubleClickListener(e->{
        });
        this.addItemClickListener(e->{
        });
    }
    public void setCon(JdbcTemplate conn) {
    	this.con = conn; 
    }
    /*
	public void setBinder(SysFreeForm freeform) {
		binder  = new BeanValidationBinder<>(sys_module.class);//수정
		binder.bindInstanceFields(freeform);
	}
	*/
	public BeanValidationBinder getBinder() {
		return binder ;
	}
	public void retrieve() {
	}
	public void retrieve(int list_no) {
		
		readBuffer= new ArrayList<>();
		deleteBuffer = new ArrayList<>();
		dataView = null;
		selectedrow = -1;
		//readBuffer = new ArrayList<usy_unit_cd>(sqlca.wExamUnitList(list_no));
		originalBuffer = new ArrayList<>();
		originalBuffer = new ArrayList<>(readBuffer);//추가 
    	if(readBuffer.size()!=0) {
			dataView = setItems((List<usy_unit_cd>)  readBuffer);//수정
        	selectRow(0);
    	}else {
    		dataView = setItems((List<usy_unit_cd>)  readBuffer);//수정
        }
		
	}
	public <T> void retrieve(T list) {
		/*
		int list_no = ((pas_mat_make_list)list).get_list_no();
		readBuffer= new ArrayList<>();
		deleteBuffer = new ArrayList<>();
		dataView = null;
		selectedrow = -1;
		//readBuffer = new ArrayList<usy_unit_cd>(sqlca.wExamUnitList(list_no));
		originalBuffer = new ArrayList<>();
		originalBuffer = new ArrayList<>(readBuffer);//추가 
    	if(readBuffer.size()!=0) {
			dataView = setItems((List<usy_unit_cd>)  readBuffer);//수정
        	selectRow(0);
    	}else {
    		dataView = setItems((List<usy_unit_cd>)  readBuffer);//수정
        }
		*/
	}
/*	
	public void setFreeForm(SysFreeForm free) {
		freeform = free;
	}
*/
}
