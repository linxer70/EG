package egframe.iteach4u.common;
/*
//다중선택
List<usy_type_cd> list = jdbc.getTypeListById(Long.valueOf(1));
Set<usy_type_cd> set = new HashSet<>(list); // 기
type.setCheckedTypes(set);
//단일 선택
type.selectTypeById(Long.valueOf(2));
*/

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.data.provider.Query;

import egframe.frame.service.AuthenticatedUser;
import egframe.iteach4u.entity.usy_type_cd;
import egframe.iteach4u.service.Repository.Iteach4uService;

@Service
public class TYPE_CODE extends FlexLayout{
	private static final long serialVersionUID = 1L;
	private FlexLayout checkgroup = new FlexLayout();
	public CheckboxGroup<usy_type_cd> typeGroup = new CheckboxGroup<>();
	private Long type_cd;
	private String class_cd;
	private String subject_cd;
	private List<usy_type_cd> type_data = new ArrayList<>();
	@Autowired
    private Iteach4uService Jdbc;	
	
	private List<usy_type_cd> itemList;
	public Map<usy_type_cd, Checkbox> checkboxMap = new HashMap();	
	public TYPE_CODE() {
		setFlexWrap(FlexLayout.FlexWrap.WRAP);
		setJustifyContentMode(FlexLayout.JustifyContentMode.START);
    	getStyle().set("gap", "5px");
    	getStyle().set("align-items", "start"); // 세로 정렬
    	getStyle().set("align-content", "start"); // 줄 정렬
    	setHeight("auto");
    	getStyle().set("overflow-y", "auto"); // 세로 스크롤 활성화

	}
	@Autowired
	public TYPE_CODE(Iteach4uService _jdbc) {
		Jdbc=_jdbc;
		setFlexWrap(FlexLayout.FlexWrap.WRAP);
		setJustifyContentMode(FlexLayout.JustifyContentMode.START);
    	getStyle().set("gap", "5px");
    	getStyle().set("align-items", "start"); // 세로 정렬
    	getStyle().set("align-content", "start"); // 줄 정렬
    	setHeight("auto");
    	getStyle().set("overflow-y", "auto"); // 세로 스크롤 활성화

	}
	public void setCheckedTypes(Set<usy_type_cd> selectedUnits) {
	    typeGroup.setValue(selectedUnits);
		for (usy_type_cd item : checkboxMap.keySet()) {
			for (usy_type_cd chk : selectedUnits) {
				if(item.getId().equals(chk.getId())) {
					Checkbox checkbox = checkboxMap.get(item);
					if (checkbox != null) {
					    checkbox.setValue(true);  
					}
				}
			}
		}		
	}	
	public void _setTypeData() {
		
		checkgroup.setFlexWrap(FlexLayout.FlexWrap.WRAP);
		checkgroup.getStyle().set("display", "grid");
		checkgroup.getStyle().set("grid-template-columns", "repeat(5, 1fr)"); // 5열 설정
		checkgroup.getStyle().set("gap", "10px");		
		
		typeGroup.clear();
		checkboxMap.clear();
		checkgroup.removeAll();
		remove(typeGroup, checkgroup);
	   	typeGroup.setItemLabelGenerator(usy_type_cd -> usy_type_cd.get_type_nm()+" : ( "+usy_type_cd.getCount() + " ) 문항");		
		type_data = Jdbc.getTypeList();
		typeGroup.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
		typeGroup.setItems(type_data);
		typeGroup.setValue(Collections.emptySet());
		
		typeGroup.getDataProvider().fetch(new Query<>()).forEach(item -> {
		    Div wrapper = new Div();
		    wrapper.getStyle().set("display", "flex");
		    wrapper.getStyle().set("align-items", "center");
		    Checkbox checkbox = new Checkbox(typeGroup.getItemLabelGenerator().apply(item));
		    checkbox.addValueChangeListener(e -> {
		        Set<usy_type_cd> selected = new HashSet<>(typeGroup.getValue());
		        if (e.getValue()) {
		            selected.add(item);
		        } else {
		            selected.remove(item);
		        }
		        typeGroup.setValue(selected); // 변경된 값 적용
		        });

	        checkboxMap.put(item,checkbox);
		    wrapper.add(checkbox);
		    checkgroup.add(wrapper);
		});		
		typeGroup.getStyle().set("display", "none");

		// 레이아웃 추가
		add(typeGroup, checkgroup);

	}
    
	public void _setUnitSingleData(Long _type_cd) {
	    type_cd = _type_cd;

	    checkgroup.setFlexWrap(FlexLayout.FlexWrap.WRAP);
	    checkgroup.getStyle().set("display", "grid");
	    checkgroup.getStyle().set("grid-template-columns", "repeat(5, 1fr)"); // 5 columns
	    checkgroup.getStyle().set("gap", "10px");

	    typeGroup.clear();
	    checkgroup.removeAll();
	    remove(typeGroup, checkgroup);
	    typeGroup.setItemLabelGenerator(usy_type_cd -> usy_type_cd.get_type_nm() + " : ( " + usy_type_cd.getCount() + " ) 문항");
//	    type_data = sysdbo.getUnitList(subject_cd);
	    typeGroup.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
	    typeGroup.setItems(type_data);
	    typeGroup.setValue(Collections.emptySet());

	    typeGroup.getDataProvider().fetch(new Query<>()).forEach(item -> {
	        Div wrapper = new Div();
	        wrapper.getStyle().set("display", "flex");
	        wrapper.getStyle().set("align-items", "center");

	        Checkbox checkbox = new Checkbox(typeGroup.getItemLabelGenerator().apply(item));
	        // Set initial checked state
	        checkbox.setValue(typeGroup.getValue().contains(item));

	        // Update checkbox state when typeGroup's value changes
	        typeGroup.addValueChangeListener(event -> {
	            checkbox.setValue(event.getValue().contains(item));
	        });

	        // Handle checkbox selection changes
	        checkbox.addValueChangeListener(e -> {
	            if (e.getValue()) {
	                // Select only this item
	                typeGroup.setValue(Collections.singleton(item));
	            } else {
	                // Deselect all
	                typeGroup.setValue(Collections.emptySet());
	            }
	        });

	        wrapper.add(checkbox);
	        checkgroup.add(wrapper);
	    });
	    typeGroup.getStyle().set("display", "none");

	    add(typeGroup, checkgroup);
	}
	public void selectTypeById(Long TypeId) {
		//typeGroup.setItems(type_data);
	    Optional<usy_type_cd> targetItem = Optional.empty();
	    for (usy_type_cd item : type_data) {
	    	if (item.getId().equals(TypeId)) {
	    		targetItem = Optional.of(item);
	    		break;
	    	}
	    }
		if (targetItem.isPresent()) {
		    usy_type_cd item = targetItem.get();
		    boolean contains = type_data.contains(targetItem.get());
    		System.out.println("contains? " + contains);
		    Set<usy_type_cd> singleSet = Collections.singleton(item);
		    setCheckedTypes(singleSet);
		} else {
		    Set<usy_type_cd> emptySet = Collections.emptySet();
		    typeGroup.setValue(emptySet);
		}
	}	
	
	public usy_type_cd getSelectedType() {
	    Set<usy_type_cd> selected = typeGroup.getValue();
	    return selected.isEmpty() ? null : selected.iterator().next();
	}

	// 모든 선택 해제
	public void clearSelection() {
	    typeGroup.setValue(Collections.emptySet());
	}	
}
