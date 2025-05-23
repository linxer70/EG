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
import egframe.iteach4u.entity.usy_subject_cd;
import egframe.iteach4u.service.Repository.Iteach4uService;

@Service
public class SUBJECT_CODE extends FlexLayout{
	private static final long serialVersionUID = 1L;
	private FlexLayout checkgroup = new FlexLayout();
	public CheckboxGroup<usy_subject_cd> subjectGroup = new CheckboxGroup<>();
	private String type_cd;
	private String class_cd;
	private String subject_cd;
	private List<usy_subject_cd> subject_data = new ArrayList<>();
	@Autowired
    private Iteach4uService Jdbc;	
	
	private List<usy_subject_cd> itemList;
	public Map<usy_subject_cd, Checkbox> checkboxMap = new HashMap();	
	public SUBJECT_CODE() {
		setFlexWrap(FlexLayout.FlexWrap.WRAP);
		setJustifyContentMode(FlexLayout.JustifyContentMode.START);
	}
	@Autowired
	public SUBJECT_CODE(AuthenticatedUser securityService,Iteach4uService _jdbc) {
		Jdbc=_jdbc;
		setFlexWrap(FlexLayout.FlexWrap.WRAP);
		setJustifyContentMode(FlexLayout.JustifyContentMode.START);
	}
	public void setCheckedUnits(Set<usy_subject_cd> selectedUnits) {
	    subjectGroup.setValue(selectedUnits);
		for (usy_subject_cd item : checkboxMap.keySet()) {
			for (usy_subject_cd chk : selectedUnits) {
				if(item.getId().equals(chk.getId())) {
					Checkbox checkbox = checkboxMap.get(item);
					if (checkbox != null) {
					    checkbox.setValue(true);  
					}
				}
			}
		}		
	}	
	public void _setUnitData(String _type_cd,String _class_cd,String _subject_cd) {
		type_cd = _type_cd;
		class_cd = _class_cd;
		subject_cd = _subject_cd;
		
		checkgroup.setFlexWrap(FlexLayout.FlexWrap.WRAP);
		checkgroup.getStyle().set("display", "grid");
		checkgroup.getStyle().set("grid-template-columns", "repeat(5, 1fr)"); // 5열 설정
		checkgroup.getStyle().set("gap", "10px");		
		
		subjectGroup.clear();
		checkboxMap.clear();
		checkgroup.removeAll();
		remove(subjectGroup, checkgroup);
	   	subjectGroup.setItemLabelGenerator(usy_subject_cd -> usy_subject_cd.get_subject_nm()+" : ( "+usy_subject_cd.getCount() + " ) 문항");		
//		subject_data = sysdbo.getUnitList(subject_cd);
		subjectGroup.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
		subjectGroup.setItems(subject_data);
		subjectGroup.setValue(Collections.emptySet());
		
		subjectGroup.getDataProvider().fetch(new Query<>()).forEach(item -> {
		    Div wrapper = new Div();
		    wrapper.getStyle().set("display", "flex");
		    wrapper.getStyle().set("align-items", "center");
		    Checkbox checkbox = new Checkbox(subjectGroup.getItemLabelGenerator().apply(item));
		    checkbox.addValueChangeListener(e -> {
		        Set<usy_subject_cd> selected = new HashSet<>(subjectGroup.getValue());
		        if (e.getValue()) {
		            selected.add(item);
		        } else {
		            selected.remove(item);
		        }
		        subjectGroup.setValue(selected); // 변경된 값 적용
		        });

	        checkboxMap.put(item,checkbox);
		    wrapper.add(checkbox);
		    checkgroup.add(wrapper);
		});		
		subjectGroup.getStyle().set("display", "none");

		// 레이아웃 추가
		add(subjectGroup, checkgroup);

	}
    
	public void _setUnitSingleData(String _type_cd, String _class_cd, String _subject_cd) {
	    type_cd = _type_cd;
	    class_cd = _class_cd;
	    subject_cd = _subject_cd;

	    checkgroup.setFlexWrap(FlexLayout.FlexWrap.WRAP);
	    checkgroup.getStyle().set("display", "grid");
	    checkgroup.getStyle().set("grid-template-columns", "repeat(5, 1fr)"); // 5 columns
	    checkgroup.getStyle().set("gap", "10px");

	    subjectGroup.clear();
	    checkgroup.removeAll();
	    remove(subjectGroup, checkgroup);
	    subjectGroup.setItemLabelGenerator(usy_subject_cd -> usy_subject_cd.get_subject_nm() + " : ( " + usy_subject_cd.getCount() + " ) 문항");
//	    subject_data = sysdbo.getUnitList(subject_cd);
	    subjectGroup.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
	    subjectGroup.setItems(subject_data);
	    subjectGroup.setValue(Collections.emptySet());

	    subjectGroup.getDataProvider().fetch(new Query<>()).forEach(item -> {
	        Div wrapper = new Div();
	        wrapper.getStyle().set("display", "flex");
	        wrapper.getStyle().set("align-items", "center");

	        Checkbox checkbox = new Checkbox(subjectGroup.getItemLabelGenerator().apply(item));
	        // Set initial checked state
	        checkbox.setValue(subjectGroup.getValue().contains(item));

	        // Update checkbox state when subjectGroup's value changes
	        subjectGroup.addValueChangeListener(event -> {
	            checkbox.setValue(event.getValue().contains(item));
	        });

	        // Handle checkbox selection changes
	        checkbox.addValueChangeListener(e -> {
	            if (e.getValue()) {
	                // Select only this item
	                subjectGroup.setValue(Collections.singleton(item));
	            } else {
	                // Deselect all
	                subjectGroup.setValue(Collections.emptySet());
	            }
	        });

	        wrapper.add(checkbox);
	        checkgroup.add(wrapper);
	    });
	    subjectGroup.getStyle().set("display", "none");

	    add(subjectGroup, checkgroup);
	}
	public void selectUnitByCd(String unitCd) {
	    Optional<usy_subject_cd> targetItem = subject_data.stream()
	        .filter(item -> item.getId().equals(unitCd)) // 실제 필드명에 맞게 수정
	        .findFirst();
	        
	    if (targetItem.isPresent()) {
	        subjectGroup.setValue(Collections.singleton(targetItem.get()));
	    } else {
	        subjectGroup.setValue(Collections.emptySet());
	    }
	}	
	
	public usy_subject_cd getSelectedUnit() {
	    Set<usy_subject_cd> selected = subjectGroup.getValue();
	    return selected.isEmpty() ? null : selected.iterator().next();
	}

	// 모든 선택 해제
	public void clearSelection() {
	    subjectGroup.setValue(Collections.emptySet());
	}	
}
