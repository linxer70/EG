package egframe.common;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;



import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.HasValue.ValueChangeEvent;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSelectionModel;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.event.SortEvent;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SingleSelect;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.QueryParameters;

import egframe.iteach4u.entity.pds_file_hd;

public class SysGrid<T> extends Grid<T>  implements DataWindowImpl{
	public List<?> readBuffer = new ArrayList<>();
	public List<?> originalBuffer = new ArrayList<>();
	public List<?> deleteBuffer = new ArrayList<>();
	public List<List<?>> addBuffer = new ArrayList<>();//누적 데이터 
	public GridListDataView<T> dataView ;
	protected ListDataProvider<?> dataProvider;
	public GridSelectionModel<T> selectionModel ;
	public T selecteditem ;			//현재 선택된 고정 아이템 
	public T selectitem;
	public T clickitem ;
	public int selectedrow = -1;			//현재 로우 고정 값
	public int selectrow = -1;			//현재 로우 고정 값
	public int totalRow;
	public int currentPage;
	public int pagelimit = 20;
	public int pageoffset = 0 ;
	public String sortcol ;
	public String ordercol ;
	public SysGridLayout sysgridlayout ;
	public Binder<T> binder ;
	public Editor<T> editor ;
	public SingleSelect<Grid<T>, T> singleSelect = asSingleSelect();
	//protected BeanValidationBinder<T> bind;
	public SysGrid() {
		//getElement().getStyle().set("background-color", "linear-gradient(45deg, #2196F3, #E3F2FD)");
		//getElement().getStyle().set("background-color", "white");
		this.addClassName("main-layout");
		__init();
	}
    public SysGrid(Class<T> beanType, T instance, Class<?>[] constructorParamTypes, Object[] constructorArgs) {
        super(beanType);
        T newInstance;
        try {
            Constructor<T> constructor = beanType.getDeclaredConstructor(constructorParamTypes);
            newInstance = constructor.newInstance(constructorArgs);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create instance");
        }
    	__init();
    }
    public void __init() {
		setSizeFull();

		addItemDoubleClickListener(event -> {
			
        });
			addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
			addItemClickListener(event -> {
	            // 클릭된 행에 대한 작업 수행
	            T clickedItem = event.getItem();
	            clickitem = clickedItem;
	        });		
			this.addItemDoubleClickListener(e->{
				selecteditem =  e.getItem();
				selectedrow = getRow((SysEntity)selecteditem);
			});
			this.addSelectionListener(e -> {
			    e.getFirstSelectedItem().ifPresent(item -> {
			        selecteditem = item;
			        selectedrow = getRow((SysEntity) selecteditem);
			    });
			});	
		
    }
    public void itemChangedEvent(ComponentValueChangeEvent<Grid<T>, T> event) {
        System.out.println("changed");
		selecteditem =  event.getValue();
		selectedrow = getRow((SysEntity)selecteditem);
    	if(event.getOldValue()!=null) {
	    	selectitem =  event.getOldValue();
	    	selectrow = getRow((SysEntity)selectitem);
		}
	}
	public void setRow(T select) {
        for(T row:(List<T>)  readBuffer) {
        	if(select.equals(row)) {
        		break;
        	}
        }
        selectionModel = (GridSelectionModel<T>) getSelectionModel();
        if(selectionModel != null) {
            selectionModel.select(select);	    	
        }
	}
    
	public T selectRow(int rownum ) {
		int size = readBuffer.size();
		int index = 0;
		if(selectedrow == -1) {				//최초 조회시 
	        for(T row:(List<T>)  readBuffer) {
                GridSelectionModel<T> selectionModel = (GridSelectionModel<T>) getSelectionModel();
                selectionModel.select(row);	
                selecteditem = row ;
                //selectrow = 0 ;
            	break;
	        }
		}else {
			if(rownum!=size) {
		        for(T row:(List<T>)  readBuffer) {
		        	if (rownum == index) {
		                GridSelectionModel<T> selectionModel = (GridSelectionModel<T>) getSelectionModel();
		                selectionModel.select(row);	
		                selecteditem = row ;
		                selectedrow = index ;
		            	break;
		        	}else {
		                selecteditem = row ;
		        		index ++;
		                selectedrow = index ;
		        	}
		        }
			}else {
				if(size !=0 ) {
					T row =  ((List<T>)  readBuffer).get(rownum -1);
					GridSelectionModel<T> selectionModel = (GridSelectionModel<T>) getSelectionModel();
		            selectionModel.select(row);	
		            selecteditem = row ;
		            selectedrow = size -1 ;
				}
			}
		}
		return selecteditem;
	}
    
	@Override
	public void insert() {
	}
	@Override
	public void delete() {
	}
	@Override
	public void update() {
	}
	@Override
	public BeanValidationBinder getBinder() {
		//return bind;
		return null;
	}
	@Override
	public void setBinder(BeanValidationBinder _bind) {
		//bind = _bind;
	}
	@Override
	public String itemChanged(ComponentValueChangeEvent event) {
		return null;
	}
	@Override
	public void selectItem(Object select) {
	}
	@Override
	public String getItemString(int row, String col) {
		return null;
	}
	@Override
	public void setItem(int row, SysEntity obj) {
	}
	@Override
	public LocalDate getItemDate(int row, String col) {
		return null;
	}
	@Override
	public Long getItemNumber(int row, String col) {
		return null;
	}
	@Override
	public int getRow(SysEntity obj) {
    	int index = 0 ;
        for(T row:(List<T>)  readBuffer) {
        	if (row.equals(obj)) {
            	break;
        	}else {
        		index ++;
        	}
        }
        if(index >= readBuffer.size()) {
        	index = -1;
        }
        return index;
	}
	@Override
	public void refreshAll() {
	}
	@Override
	public void retrieve(String a) {
	}
	@Override
	public void retrieve(String a, String b) {
	}
	@Override
	public void setClassType(String type) {
	}
	@Override
	public String getClassType() {
		return null;
	}
	public void setParent(SysGridLayout _parent) {
		sysgridlayout = _parent;
	}
	@Override
	public void _setDDDW(SysGridChild list) {
	}
	@Override
	public void retrieve() {
	}
	@Override
	public void resetData() {
	}
	@Override
	public void retrieve(long class_id,long subject_id, long unit_id,int offser,int limit) {
	}
	@Override
	public void retrieve(long class_id,long subject_id, long unit_id) {
	}
	@Override
	public void retrieve(long subject_id, long unit_id) {
	}
	@Override
	public void setDataProvider(ListDataProvider<?> dataprovider) {
		dataProvider = dataprovider;
	}
	@Override
	public void setBinder(SysFree freeform) {
	}
	@Override
	public void retrieve(long id) {
	}
	@Override
	public void insert(SysEntity object) {
	}
	@Override
	public void addretrieve() {
	}
	@Override
	public void retrieve(List<Long> args) {
	}
}
