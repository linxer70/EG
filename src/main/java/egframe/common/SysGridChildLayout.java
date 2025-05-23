package egframe.common;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexDirection;
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.component.popover.PopoverPosition;
import com.vaadin.flow.component.popover.PopoverVariant;
import com.vaadin.flow.component.shared.Tooltip;
import com.vaadin.flow.component.shared.Tooltip.TooltipPosition;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.event.SortEvent;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.value.ValueChangeMode;


public class SysGridChildLayout extends FlexLayout implements SysGridChildLayoutImpl {//implements DataWindowImpl{
	public SysGridChild _sysgridchild ;//= new SysGridChild();							//실제 보여 주는 DW
   	//public SysGridChildLayout _sysgridchildlayout ;			//실제 보여주는 DDDW 레이아웃
	public SysGrid _sysgrid ;									//DDDW가 추가되는 DW							
   	public SysGridLayout _sysgridlayout ;						//실제 보여주는 Cell 의 DW 레이아웃
 	public Span _cellspan = new Span(); 						//Cell 에 추가되는 Span
 	public Button _cellbutton = new Button();					//Span 에 보여지는 Button 
 	public Popover _popover = new Popover();					//DDDW을 보여주는 popover
 	public Button _title = new Button("제목");					//Span 에 보여지는 Button 
 	
 	
	public SysGridChildLayout() {
		_init();
		//_addEvent();
	}
	public SysGridChildLayout(SysGridLayout dw) {
		_init();
		_sysgridlayout = dw;
		_sysgrid = dw._grid;
		//_addEvent();
	}
	public SysGridChildLayout(SysGrid dw,SysGridChild list) {
		_sysgrid= dw;
		_sysgridchild = list;
		//add(_sysgridchild);
		_init();
		_sysgridchild.setSizeFull();
	}
	public SysGridChildLayout(SysGridLayout dw, SysGridChild dddw) {
		_init();
		_sysgridlayout = dw;
		_sysgrid = dw._grid;
		_sysgridchild = dddw;
	}
	public void _init() {
		setSizeFull();
		setFlexDirection(FlexDirection.COLUMN);
		_sysgridchild.addClassName("gridchild-style");
		
	}
	public void _addEvent() {
	}
	public void setGridChild(String keycolumn) {
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
	public <T> void sortEvent(SortEvent<Grid<T>, GridSortOrder<T>> event) {
	}
	public void setParent(SysGridLayout _parent) {
		_sysgridlayout=_parent;
		
	}
	public void setParent(FlexLayout _parent) {
		setParent(_parent);
		
	}
	public void setPage(int _pagenum) {
	}
	public void setSysGrid(SysGrid dddw) {
		_sysgrid = dddw;
	}
	public void setSysGridLayout(SysGridLayout dw) {
		_sysgridlayout = dw;
		_sysgrid = dw._grid;
	}
	/*
	public void setSysGridChild(SysGridChild dddw) {
		_sysgridchild = dddw;
		add(_sysgridchild);
		_sysgridchild.addClassName("small-font-grid");
		_sysgridchild.getDataProvider().refreshAll();
	}
	*/
	public void setSysGridChildLayout(SysGridChildLayout dw) {
		_sysgridchild = dw._sysgridchild;
	}
	@Override
	public void retrieve() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void insert() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public int getRow(SysEntity obj) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void retrieve(int list_no) {
		add(_sysgridchild);
		_sysgridchild.retrieve(list_no);
		
	}
	@Override
	public <T> void retrieve(T entity) {
		add(_sysgridchild);
		_sysgridchild.addClassName("gridchild-style");
		_sysgridchild.retrieve(entity);
	    System.out.println("Received entity: " + entity.getClass().getName());
	}	
}
