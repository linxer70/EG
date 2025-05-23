package egframe.common;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.grid.Grid.Column;
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

//import egframe.iteach4u.common.MAT_CONTROL;
//import egframe.iteach4u.common.MAT_UNIT;
//import egframe.iteach4u.entitycontrol.pass_mat_subject_list;
//import egframe.iteach4u.exm.views.MAKE;
//import egframe.iteach4u.gridcontrol.d_pds_file_hd;

public class SysGridLayout extends FlexLayout{
	public Button _title = new Button();
	public Button _pre = new Button("<<");
	public Button _page = new Button();
	public Button _next = new Button(">>");
	public SysGrid _grid ;
	public SysGridChild _gridchild ;
	public SysGridChildLayout _gridchildlayout ;
	public Upload upload = new Upload();;
	public TextField _filter = new TextField("필터");
	public Button _addview = new Button("더보기");
	public FlexLayout _footerlayout = new FlexLayout();
	public FlexLayout _filterlayout = new FlexLayout();
	public int _currentpage = 0 ;
	public int _pagelimit = 20;
	public int _pageoffset = 0 ;
	public String _type_cd,_class_cd,_subject_cd,_unit_cd;
	public String _sortcol,_ordercol;
	public List<String> unitlist;
	public final int PAGE_SIZE = 100;   	
	
	public SysGridLayout() {
		this.addClassName("main-layout");
		_init();
		_addEvent();
	}
	public SysGridLayout(SysGrid dw) {
		_init();
		_filterlayout.setFlexDirection(FlexDirection.ROW);
		_filterlayout.setAlignItems(FlexLayout.Alignment.CENTER); // 세로 중앙 정렬
		_filterlayout.setJustifyContentMode(FlexLayout.JustifyContentMode.CENTER); // 가로 중앙 정렬
        // 각 컴포넌트의 Flex 설정 (본래 크기 유지)
		_filterlayout.setFlexGrow(0, _filter); // 텍스트 필드의 크기 고정
		_filterlayout.setFlexShrink(0, _filter); // 텍스트 필드 크기 축소 방지
		_filterlayout.setFlexBasis("auto",_filter);// 텍스트 필드의 기본 크기 설정 (자동)
		_filterlayout.add(_filter);
		
		
		_footerlayout.setFlexDirection(FlexDirection.ROW);
		_footerlayout.setAlignItems(FlexLayout.Alignment.CENTER); // 세로 중앙 정렬
		_footerlayout.setJustifyContentMode(FlexLayout.JustifyContentMode.CENTER); // 가로 중앙 정렬
		_footerlayout.setHeight("5%");
		_addview.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		_footerlayout.add(_addview);//,_pre,_page,_next);

		_title.setVisible(false);
		_filterlayout.setVisible(false);
		_grid = dw;
		_grid.setParent(this);
		add(this._title,_filterlayout,_grid,_footerlayout);
		_addEvent();
	}
	public void _init() {
		setSizeFull();
		setFlexDirection(FlexDirection.COLUMN);
		_title.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		_footerlayout.setVisible(false);
	}
	
	public void _setDDDW(SysGridChild list) {
		_gridchild= list;
   		List<SysGrid.Column<?>> columns = new ArrayList<>(_grid.getColumns());
   		int columnIndex = -1;
   		for (int i = 0; i < columns.size(); i++) {
   		    if (list.getTitle().equals(columns.get(i).getKey())) {
   		        columnIndex = i;
   		        break;
   		    }
   		}

   		if (columnIndex != -1) {
   		    _grid.removeColumn(columns.get(columnIndex));
   		    SysGrid.Column<?> newColumn = _addColumn(_grid,list);  		    
   		    moveColumnToIndex(_grid,newColumn.getKey(),columnIndex);
   		} else {
   			_addColumn(list);    
   		}
		
	}
	public void _addColumn(SysGridChild list) {
		_grid.addColumn(new ComponentRenderer<>(item -> {
	    	   
	    	   
	         	Span span = new Span();
	        	 Popover popover = new Popover();
	             popover.setTarget(span);
	             popover.setWidth("300px");
	             popover.addThemeVariants(PopoverVariant.ARROW,
	                     PopoverVariant.LUMO_NO_PADDING);
	             popover.setPosition(PopoverPosition.BOTTOM);
	             popover.setModal(true);
	             popover.setAriaLabelledBy("notifications-heading");
	             popover.setOpened(false);
	         	SysGridChildLayout hd_control = new SysGridChildLayout(_grid,list);

	         	hd_control.setHeight("400px");
	         	popover.add(hd_control);
	         	Button button = new Button(list.getTitle()); 
	         	span.add(button,popover);
	         	
	         	button.getElement().executeJs("""
	         		    this.addEventListener('click', function(e) {
	         		        e.stopPropagation();
	         		        e.preventDefault();
	         		        $0.$server.handleButtonClick();
	         		    });
	         		""", _grid.getElement());
	         	
	         	button.addClickListener(e->{
	         		UI.getCurrent().getPage().executeJs("""
	         			    const grid = $0;
	         			    const popover = $1;
	         			    popover.style.width = grid.offsetWidth + 'px';
	         			""", hd_control.getElement(), popover.getElement());       		
	    	   		hd_control.retrieve(item);
	    		   popover.setOpened(true);
	    	   	});
	        	return span;
	        })).setHeader(list.getTitle()).setAutoWidth(true).setSortable(true).setKey(list.getTitle()).setVisible(true);     
	}
	
	public <T> SysGrid.Column<T> _addColumn(SysGrid<T> grid, SysGridChild list) {
	    // ComponentRenderer를 명시적으로 타입 파라미터화
	    ComponentRenderer<Span, T> renderer = new ComponentRenderer<>(item -> {
	        Span span = new Span();
	        Popover popover = new Popover();
	        popover.setTarget(span);
	        popover.setWidth("300px");
	        popover.addThemeVariants(PopoverVariant.ARROW,
	                PopoverVariant.LUMO_NO_PADDING);
	        popover.setPosition(PopoverPosition.BOTTOM);
	        popover.setModal(true);
	        popover.setAriaLabelledBy("notifications-heading");
	        popover.setOpened(false);
	        SysGridChildLayout hd_control = new SysGridChildLayout(grid, list);

	        hd_control.setHeight("400px");
	        popover.add(hd_control);
	        Button button = new Button(list.getTitle()); 
	        span.add(button, popover);
	        
	        button.getElement().executeJs("""
	            this.addEventListener('click', function(e) {
	                e.stopPropagation();
	                e.preventDefault();
	                $0.$server.handleButtonClick();
	            });
	        """, grid.getElement());
	        
	        button.addClickListener(e -> {
	            UI.getCurrent().getPage().executeJs("""
	                const grid = $0;
	                const popover = $1;
	                popover.style.width = grid.offsetWidth + 'px';
	            """, hd_control.getElement(), popover.getElement());
	            hd_control.retrieve(item);
	            popover.setOpened(true);
	        });
	        return span;
	    });

	    SysGrid.Column<T> column = _grid.addColumn(renderer).setHeader(list.getTitle());
	    column.setAutoWidth(true);
	    column.setSortable(true);
	    column.setKey(list.getTitle());
	    column.setVisible(true);
	    
	    return column;
	}
	
	public static <T> void moveColumnToIndex(SysGrid<T> grid, String columnKey, int targetIndex) {
	    List<SysGrid.Column<T>> columns = new ArrayList<>(grid.getColumns());
	    SysGrid.Column<T> column = grid.getColumnByKey(columnKey);
	    
	    if (column != null) {
	        columns.remove(column);
	        columns.add(Math.min(targetIndex, columns.size()), column);
	        grid.setColumnOrder(columns);
	    }
	}

	
	public void _addEvent() {
		_addview.addClickListener(e->{
            getParentRecursively(this, WindowImpl.class).ifPresent(parent -> {
//                ((MAT_UNIT) parent).wDelete();
                ((WindowImpl) parent).addretrieve();
            });            
		
//			_grid.addretrieve();            
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
	
	public <T> void sortEvent(SortEvent<Grid<T>, GridSortOrder<T>> event) {
	}
	public void setParent(SysGridLayout _parent) {
		_grid.setParent(_parent);
		
	}
	public void setParent(FlexLayout _parent) {
		setParent(_parent);
		
	}
	public void setPage(int _pagenum) {
		_page.setText(String.valueOf(_pagenum));
	}
	public SysGridLayout(SysGridChild list) {
		_gridchild = list;
	}
	public void setGrid(SysGrid dw) {
		_grid = dw ;
		add(_grid);
	}
	public int getSelectedRow() {
		return _grid.selectedrow;
	}
	public void setVisibleFooter(boolean flag) {
		_footerlayout.setVisible(flag);
	}
	public void refreshAll() {
		_grid.refreshAll();
	}
	public int getSelectRow() {
		return _grid.selectrow;
	}
	public SysEntity getSelectedItem() {
		return (SysEntity)_grid.selecteditem;
	}
	public SysEntity getSelectItem() {
		return (SysEntity)_grid.selectitem;
	}
	public void setBinder(SysFree freeform) {
	   	_grid.setBinder(freeform);
	}
	public void addretrieve() {
		_grid.addretrieve();
	}
	public void retrieve(long class_id, long subject_id, long unit_id,int offset,int limit) {
		_grid.retrieve(class_id,subject_id,unit_id,offset,limit);
	}
	public void retrieve(long class_id, long subject_id, long unit_id) {
		_grid.retrieve(class_id,subject_id,unit_id);
	}
	public void retrieve(long subject_id,long unit_id) {
		_grid.retrieve(subject_id,unit_id);
	}
	public void retrieve(long unit_id) {
		_grid.retrieve(unit_id);
	}
	public void retrieve(List<Long> args) {
		_grid.retrieve(args);
	}
}