package egframe.common;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import egframe.iteach4u.entity.pds_file_hd;
import egframe.iteach4u.service.Repository.Iteach4uService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.HasValue.ValueChangeEvent;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSelectionModel;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.event.SortEvent;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SingleSelect;
import com.vaadin.flow.router.QueryParameters;

public class SysGridChild<T> extends Grid<T>  implements SysGridChildImpl{
	public List<?> readBuffer = new ArrayList<>();
	public List<?> originalBuffer = new ArrayList<>();
	public List<?> deleteBuffer = new ArrayList<>();
	public List<List<?>> addBuffer = new ArrayList<>();//누적 데이터 
	public GridListDataView<T> dataView ;
	//public List<sys_column_dic> arguments = new ArrayList<>();
	public GridSelectionModel<T> selectionModel ;
	public T selecteditem ;			//현재 선택된 고정 아이템 
	public T clickitem ;
	public int selectedrow = -1;			//현재 로우 고정 값
	public int totalRow;
	public int currentPage;
	public int pagelimit = 20;
	public int pageoffset = 0 ;
	public String sortcol ;
	public String ordercol ;
	public SysGridChildLayout _sysgridchildlayout ;
	private String title;

	public SysGridChild() {
		__init();
	}
    public SysGridChild(Class<T> beanType, T instance, Class<?>[] constructorParamTypes, Object[] constructorArgs) {
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
    public SysGridChild(Iteach4uService sqlca) {
		// TODO Auto-generated constructor stub
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
		        		index ++;
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
	public void retrieve(int list_no) {
	}
	@Override
	public <T> void retrieve(T entity) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setTitle(String _title) {
		title = _title;
	}
	@Override
	public String getTitle() {
		return title;
	}
}
