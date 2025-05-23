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
import egframe.iteach4u.entity.usy_unit_cd;
import egframe.iteach4u.service.Repository.Iteach4uService;

@Service
public class UNIT_CODE extends FlexLayout{
	private static final long serialVersionUID = 1L;
	private FlexLayout checkgroup = new FlexLayout();
	public CheckboxGroup<usy_unit_cd> unitGroup = new CheckboxGroup<>();
	private Long type_id;
	private Long class_id;
	private Long subject_id;
	private List<usy_unit_cd> unit_data = new ArrayList<>();
	@Autowired
    private Iteach4uService Jdbc;	
	
	private List<usy_unit_cd> itemList;
	public Map<usy_unit_cd, Checkbox> checkboxMap = new HashMap();	
	public UNIT_CODE() {
		setFlexWrap(FlexLayout.FlexWrap.WRAP);
		setJustifyContentMode(FlexLayout.JustifyContentMode.START);

	}
	@Autowired
	public UNIT_CODE(AuthenticatedUser securityService,Iteach4uService _jdbc) {
		Jdbc=_jdbc;
		setFlexWrap(FlexLayout.FlexWrap.WRAP);
		setJustifyContentMode(FlexLayout.JustifyContentMode.START);
//		add(unitGroup, checkgroup);

	}
	public void setCheckedUnits(Set<usy_unit_cd> selectedUnits) {
	    unitGroup.setValue(selectedUnits);
		for (usy_unit_cd item : checkboxMap.keySet()) {
			for (usy_unit_cd chk : selectedUnits) {
				if(item.getId().equals(chk.getId())) {
					Checkbox checkbox = checkboxMap.get(item);
					if (checkbox != null) {
					    checkbox.setValue(true);  
					}
				}
			}
		}		
	}	
	public void _setUnitData(Long _type_cd,Long _class_cd,Long _subject_cd) {
		type_id = _type_cd;
		class_id = _class_cd;
		subject_id = _subject_cd;
		
		checkgroup.setFlexWrap(FlexLayout.FlexWrap.WRAP);
		checkgroup.getStyle().set("display", "grid");
		checkgroup.getStyle().set("grid-template-columns", "repeat(5, 1fr)"); // 5열 설정
		checkgroup.getStyle().set("gap", "10px");		
		
		unitGroup.clear();
		checkboxMap.clear();
		checkgroup.removeAll();
		remove(unitGroup, checkgroup);
	   	unitGroup.setItemLabelGenerator(usy_unit_cd -> usy_unit_cd.get_unit_nm()+" : ( "+usy_unit_cd.getCount() + " ) 문항");		
		unit_data = Jdbc.getUsyUnitCdList(subject_id);
		unitGroup.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
		unitGroup.setItems(unit_data);
		unitGroup.setValue(Collections.emptySet());
		
		unitGroup.getDataProvider().fetch(new Query<>()).forEach(item -> {
		    Div wrapper = new Div();
		    wrapper.getStyle().set("display", "flex");
		    wrapper.getStyle().set("align-items", "center");
		    Checkbox checkbox = new Checkbox(unitGroup.getItemLabelGenerator().apply(item));
		    checkbox.addValueChangeListener(e -> {
		        Set<usy_unit_cd> selected = new HashSet<>(unitGroup.getValue());
		        if (e.getValue()) {
		            selected.add(item);
		        } else {
		            selected.remove(item);
		        }
		        unitGroup.setValue(selected); // 변경된 값 적용
		        });

	        checkboxMap.put(item,checkbox);
		    wrapper.add(checkbox);
		    checkgroup.add(wrapper);
		});		
		unitGroup.getStyle().set("display", "none");

		// 레이아웃 추가
		add(unitGroup, checkgroup);

	}
    
	public void _setUnitSingleData(Long _type_id, Long _class_id, Long _subject_id) {
	    type_id = _type_id;
	    class_id = _class_id;
	    subject_id = _subject_id;

	    checkgroup.setFlexWrap(FlexLayout.FlexWrap.WRAP);
	    checkgroup.getStyle().set("display", "grid");
	    checkgroup.getStyle().set("grid-template-columns", "repeat(5, 1fr)"); // 5 columns
	    checkgroup.getStyle().set("gap", "10px");

	    unitGroup.clear();
	    checkgroup.removeAll();
	    remove(unitGroup, checkgroup);
	    unitGroup.setItemLabelGenerator(usy_unit_cd -> usy_unit_cd.get_unit_nm() + " : ( " + usy_unit_cd.getCount() + " ) 문항");
//	    unit_data = sysdbo.getUnitList(subject_cd);
	    unitGroup.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
	    unitGroup.setItems(unit_data);
	    unitGroup.setValue(Collections.emptySet());

	    unitGroup.getDataProvider().fetch(new Query<>()).forEach(item -> {
	        Div wrapper = new Div();
	        wrapper.getStyle().set("display", "flex");
	        wrapper.getStyle().set("align-items", "center");

	        Checkbox checkbox = new Checkbox(unitGroup.getItemLabelGenerator().apply(item));
	        // Set initial checked state
	        checkbox.setValue(unitGroup.getValue().contains(item));

	        // Update checkbox state when unitGroup's value changes
	        unitGroup.addValueChangeListener(event -> {
	            checkbox.setValue(event.getValue().contains(item));
	        });

	        // Handle checkbox selection changes
	        checkbox.addValueChangeListener(e -> {
	            if (e.getValue()) {
	                // Select only this item
	                unitGroup.setValue(Collections.singleton(item));
	            } else {
	                // Deselect all
	                unitGroup.setValue(Collections.emptySet());
	            }
	        });

	        wrapper.add(checkbox);
	        checkgroup.add(wrapper);
	    });
	    unitGroup.getStyle().set("display", "none");

	    add(unitGroup, checkgroup);
	}
	public void selectUnitByCd(String unitCd) {
	    Optional<usy_unit_cd> targetItem = unit_data.stream()
	        .filter(item -> item.getId().equals(unitCd)) // 실제 필드명에 맞게 수정
	        .findFirst();
	        
	    if (targetItem.isPresent()) {
	        unitGroup.setValue(Collections.singleton(targetItem.get()));
	    } else {
	        unitGroup.setValue(Collections.emptySet());
	    }
	}	
	
	public usy_unit_cd getSelectedUnit() {
	    Set<usy_unit_cd> selected = unitGroup.getValue();
	    return selected.isEmpty() ? null : selected.iterator().next();
	}

	// 모든 선택 해제
	public void clearSelection() {
	    unitGroup.setValue(Collections.emptySet());
	}	
}
