package egframe.common;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import com.vaadin.data.Binder;
import com.vaadin.data.Binder.Binding;
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;

import egframe.iteach4u.entity.pds_file_hd;

public  class SysBindControl extends HorizontalLayout {
	public FlexLayout grid_layout = new FlexLayout();

	public SysFreeLayout free_control = new SysFreeLayout();
	public SysFree free ;
	public SysGridLayout grid_control = new SysGridLayout();
	public SysGrid grid ;
	public SysEntity item;
	public SysEntity firstitem;
	public BeanValidationBinder  _bind ;
	public SysBindControl() {
    	setSizeFull();
    	getStyle().set("border", "1px solid black");
	}
	public void setGrid(SysGrid gridform) {
		grid_control.setGrid(gridform);
		grid = gridform;
       add(grid_control);
	}
	public void setGrid(SysGridLayout gridform) {
		grid_control = gridform;
		grid = grid_control._grid;
       add(grid_control);
	}
	public void setFree(SysFree freeform) {
		free = freeform;
		free_control.setFreeForm(freeform);
		add(free_control);
	}
	public void setFree(SysFree freeform,SysGrid _grid) {
		free = freeform;
		free_control.setFreeForm(freeform,_grid);
        add(free_control);
	}
	public void setFree(SysFreeLayout freeform) {
		free_control = freeform;
		free = freeform.getFree();
		this.free_control.setFreeForm(free);
        add(free_control);
	}
	public void setBinder() {
		free = (SysFree)this.free_control.getFree();
		_bind = free.getBinder();
		_bind.bindInstanceFields(free);
		setEvent();
	}
	public SysGridLayout getGridLayout() {
		return this.grid_control;
	}
	public SysFreeLayout getEditor() {
		return this.free_control;
	}
	public SysFree getFree() {
		return this.free_control.getFree();
	}
	public SysGrid getGrid() {
		return this.grid_control._grid;
	}
	public BeanValidationBinder getBinder() {
		return this.grid_control._grid.getBinder();
	}
	public void setEvent() {
    	_bind.addStatusChangeListener(event -> {
    	    System.out.println("[Binder 상태]");
    	    System.out.println("유효성: " + event.getBinder().isValid());
    	});
		this.grid.addSelectionListener(e->{
			if(_bind!=null) {
				if(!e.getFirstSelectedItem().isEmpty()) {
					item = (SysEntity)e.getFirstSelectedItem().get();
					grid.getSelectionModel().select(e.getFirstSelectedItem().get());
					_bind.setBean(e.getFirstSelectedItem().get());//폼에 데이터 쓰기
				}
			}
		});
		this.grid.addItemClickListener(e->{
			/*
			if(grid.getSelectionModel().getSelectedItems().isEmpty()) {
				grid.selectItem(item);
			}else {
				grid.getSelectionModel().select(item);				
			}
			*/
		});
		this.grid.singleSelect.addValueChangeListener(e->{
			if(_bind!=null) {
				if(e.getValue()!=null) {
					item = (SysEntity)e.getValue();
					grid.getSelectionModel().select(e.getValue());
					_bind.setBean(e.getValue());//폼에 데이터 쓰기
				}
			}
			
    	});
		
	}
	public void valueChange(Object object,String field, String oldval, String val) {
		
        int index = grid.readBuffer.indexOf(item);
        if (index != -1) {
        	 for (var entry : item.getClass().getDeclaredFields()) {
                 entry.setAccessible(true); // private 필드 접근 허용
                 try {
                     String columnName = entry.getName();
                     Object value = entry.get(item);
                     
                     // 특정 문자열이 포함된 컬럼만 처리
                     if (columnName.contains(field)) {
                    	 	entry.set(item, val);
                         System.out.println("Column Name: " + columnName + ", Value: " + value);
                     	}
                 } catch (IllegalAccessException e) {   
                     e.printStackTrace();
                 }
             }        	
        	 grid.readBuffer.set(index, item);
        	 grid.getDataProvider().refreshAll();
        }		
	}
	public void valueChange(String field, Object oldval, Object val) {
		
        int index = grid.readBuffer.indexOf(item);
        if (index != -1) {
        	 for (var entry : item.getClass().getDeclaredFields()) {
                 entry.setAccessible(true); // private 필드 접근 허용
                 try {
                     String columnName = entry.getName();
                     Object value = entry.get(item);
                     
                     // 특정 문자열이 포함된 컬럼만 처리
                     if (columnName.contains(field)) {
                    	 	entry.set(item, val);
                         System.out.println("Column Name: " + columnName + ", Value: " + value);
                     	}
                 } catch (IllegalAccessException e) {   
                     e.printStackTrace();
                 }
             }        	
        	 grid.readBuffer.set(index, item);
        	 grid.getDataProvider().refreshAll();
        }		
	}
	public void setGridVisible(boolean b) {
		grid_control.setVisible(b);
		grid.setVisible(b);
		if(!b) {
			free_control.setWidthFull();
		}
	}
	public void setFreeVisible(boolean b) {
		free_control.setVisible(b);
		free.setVisible(b);
		if(!b) {
			grid_control.setWidthFull();
		}
	}
	public void insert(SysEntity object) {
		if(_bind!= null) {
			grid.insert(object);
			_bind.setBean(object);//폼에 데이터 쓰기
		}
	}
	
}