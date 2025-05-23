package egframe.iteach4u.common;

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

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.data.provider.Query;

import egframe.frame.service.AuthenticatedUser;
import egframe.iteach4u.entity.usy_class_cd;
import egframe.iteach4u.service.Repository.Iteach4uService;

@Service
public class CLASS_CODE extends FlexLayout{
	private static final long serialVersionUID = 1L;
	private FlexLayout checkgroup = new FlexLayout();
	public CheckboxGroup<usy_class_cd> classGroup = new CheckboxGroup<>();
	private String type_nm;
	private Long type_id;
	private String class_cd;
	private String subject_cd;
	private List<usy_class_cd> class_data = new ArrayList<>();
	@Autowired
    private Iteach4uService Jdbc;	
	
	private List<usy_class_cd> itemList;
	public Map<usy_class_cd, Checkbox> checkboxMap = new HashMap();	
	public CLASS_CODE() {
		setFlexWrap(FlexLayout.FlexWrap.WRAP);
		setJustifyContentMode(FlexLayout.JustifyContentMode.START);
    	getStyle().set("gap", "5px");
    	getStyle().set("align-items", "start"); // 세로 정렬
    	getStyle().set("align-content", "start"); // 줄 정렬
    	setHeight("auto");
    	getStyle().set("overflow-y", "auto"); // 세로 스크롤 활성화

	}
	@Autowired
	public CLASS_CODE(AuthenticatedUser securityService,Iteach4uService _jdbc) {
		Jdbc=_jdbc;
		setFlexWrap(FlexLayout.FlexWrap.WRAP);
		setJustifyContentMode(FlexLayout.JustifyContentMode.START);
    	getStyle().set("gap", "5px");
    	getStyle().set("align-items", "start"); // 세로 정렬
    	getStyle().set("align-content", "start"); // 줄 정렬
    	setHeight("auto");
    	getStyle().set("overflow-y", "auto"); // 세로 스크롤 활성화

	}
	public void setCheckedUnits(Set<usy_class_cd> selectedUnits) {
	    classGroup.setValue(selectedUnits);
		for (usy_class_cd item : checkboxMap.keySet()) {
			for (usy_class_cd chk : selectedUnits) {
				if(item.getId().equals(chk.getId())) {
					Checkbox checkbox = checkboxMap.get(item);
					if (checkbox != null) {
					    checkbox.setValue(true);  
					}
				}
			}
		}		
	}	
	public void _setClassData(Long _type_id) {
		type_id = _type_id;
		
		checkgroup.setFlexWrap(FlexLayout.FlexWrap.WRAP);
		checkgroup.getStyle().set("display", "grid");
		checkgroup.getStyle().set("grid-template-columns", "repeat(5, 1fr)"); // 5열 설정
		checkgroup.getStyle().set("gap", "10px");		
		
		classGroup.clear();
		checkboxMap.clear();
		checkgroup.removeAll();
		remove(classGroup, checkgroup);
	   	classGroup.setItemLabelGenerator(usy_class_cd -> usy_class_cd.get_class_nm()+" : ( "+usy_class_cd.getCount() + " ) 문항");		
		class_data = Jdbc.getClassList(type_id);
		classGroup.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
		classGroup.setItems(class_data);
		classGroup.setValue(Collections.emptySet());
		
		classGroup.getDataProvider().fetch(new Query<>()).forEach(item -> {
		    Div wrapper = new Div();
		    wrapper.getStyle().set("display", "flex");
		    wrapper.getStyle().set("align-items", "center");
		    Checkbox checkbox = new Checkbox(classGroup.getItemLabelGenerator().apply(item));
		    checkbox.addValueChangeListener(e -> {
		        Set<usy_class_cd> selected = new HashSet<>(classGroup.getValue());
		        if (e.getValue()) {
		            selected.add(item);
		        } else {
		            selected.remove(item);
		        }
		        classGroup.setValue(selected); // 변경된 값 적용
		        });

	        checkboxMap.put(item,checkbox);
		    wrapper.add(checkbox);
		    checkgroup.add(wrapper);
		});		
		classGroup.getStyle().set("display", "none");

		// 레이아웃 추가
		add(classGroup, checkgroup);

	}
    
	public void _setUnitSingleData(Long _type_id) {
		type_id = _type_id;

	    checkgroup.setFlexWrap(FlexLayout.FlexWrap.WRAP);
	    checkgroup.getStyle().set("display", "grid");
	    checkgroup.getStyle().set("grid-template-columns", "repeat(5, 1fr)"); // 5 columns
	    checkgroup.getStyle().set("gap", "10px");

	    classGroup.clear();
	    checkgroup.removeAll();
	    remove(classGroup, checkgroup);
	    classGroup.setItemLabelGenerator(usy_class_cd -> usy_class_cd.get_class_nm() + " : ( " + usy_class_cd.getCount() + " ) 문항");
	    class_data = Jdbc.getClassList(_type_id);
	    classGroup.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
	    classGroup.setItems(class_data);
	    classGroup.setValue(Collections.emptySet());

	    classGroup.getDataProvider().fetch(new Query<>()).forEach(item -> {
	        Div wrapper = new Div();
	        wrapper.getStyle().set("display", "flex");
	        wrapper.getStyle().set("align-items", "center");

	        Checkbox checkbox = new Checkbox(classGroup.getItemLabelGenerator().apply(item));
	        // Set initial checked state
	        checkbox.setValue(classGroup.getValue().contains(item));

	        // Update checkbox state when classGroup's value changes
	        classGroup.addValueChangeListener(event -> {
	            checkbox.setValue(event.getValue().contains(item));
	        });

	        // Handle checkbox selection changes
	        checkbox.addValueChangeListener(e -> {
	            if (e.getValue()) {
	                // Select only this item
	                classGroup.setValue(Collections.singleton(item));
	            } else {
	                // Deselect all
	                classGroup.setValue(Collections.emptySet());
	            }
	        });

	        wrapper.add(checkbox);
	        checkgroup.add(wrapper);
	    });
	    classGroup.getStyle().set("display", "none");

	    add(classGroup, checkgroup);
	}
	public void selectUnitByCd(String unitCd) {
	    Optional<usy_class_cd> targetItem = class_data.stream()
	        .filter(item -> item.getId().equals(unitCd)) // 실제 필드명에 맞게 수정
	        .findFirst();
	        
	    if (targetItem.isPresent()) {
	        classGroup.setValue(Collections.singleton(targetItem.get()));
	    } else {
	        classGroup.setValue(Collections.emptySet());
	    }
	}	
	
	public usy_class_cd getSelectedUnit() {
	    Set<usy_class_cd> selected = classGroup.getValue();
	    return selected.isEmpty() ? null : selected.iterator().next();
	}

	// 모든 선택 해제
	public void clearSelection() {
	    classGroup.setValue(Collections.emptySet());
	}	
}
