package egframe.common;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;

import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.provider.ListDataProvider;

import egframe.iteach4u.entity.pds_file_hd;
import egframe.iteach4u.entity.usy_subject_cd;
import egframe.iteach4u.service.Repository.Iteach4uService;
import elemental.json.JsonObject;

public class SysFree  extends FormLayout{
	public boolean changeFlag = false;
	public TextField modify = new TextField("modify");
	private SysEntity item;
	protected BeanValidationBinder binder;
	private JdbcTemplate con;
	public Iteach4uService sqlca;
	public boolean position_chk = false;
	public List<String> fields;
	public ListDataProvider<?> dataProvider ;
	public SysFree() {
    	setSizeFull();
    	setResponsiveSteps(
		   new ResponsiveStep("0", 1), // 기본적으로 두 열 구조 사용
		   new ResponsiveStep("600px", 1) 
    			);
		modify.setVisible(false);
		add(modify);
		
//		binder.bindInstanceFields(this); // 자동 바인딩
		this.addAttachListener(e->{
		});
/*		
		UI.getCurrent().getPage().executeJs(""
				+ "var rectA = $0.getBoundingClientRect();"
				+ " return {heightA : rectA.height + window.scrollY};",this).then(result -> {
	                if (result instanceof JsonObject) {
	                    JsonObject json = (JsonObject) result;
	                    double topA = json.getNumber("heightA");
	                    System.out.println("Attafter = "+topA);
//	                    setPosition();
	                }
				});
		//setPosition();
*/		
	}
    protected void setCon(JdbcTemplate connection) {
    	setSizeFull();
    	setResponsiveSteps(
		   new ResponsiveStep("0", 1), // 기본적으로 두 열 구조 사용
		   new ResponsiveStep("600px", 1) 
    			);
	}
    public void addEvent(String field,ComponentValueChangeEvent<?, String> e) {
    	this.changeFlag = true;
    	if(getModify().equals("R")) {
        	setModify("U");
    	}
    	if(binder != null) {
    		if(item != null) {
    			//binder.setBean(item);
            	//binder.writeBeanIfValid(item);
    		}
    		//binder.writeBeanIfValid(item);
    	}
    	valuChange(e.getSource(),field,e.getOldValue(),e.getValue()); //여기서 다시 그리드로 데이터 보냄 
	}
    public void addComboEvent(String field,Object oldval, Object val) {
    	this.changeFlag = true;
    	if(getModify().equals("R")) {
        	setModify("U");
    	}
    	if(binder != null) {
    		if(item != null) {
    			//binder.setBean(item);
            	//binder.writeBeanIfValid(item);
    		}
    		//binder.writeBeanIfValid(item);
    	}
    	valuChange(field,oldval,val); //여기서 다시 그리드로 데이터 보냄 
	}
    public void valuChange(Object object, String field,String oldval, String val) {
        getParentRecursively(this, SysBindControl.class).ifPresent(parent -> {
            ((SysBindControl) parent).valueChange(object,field,oldval,val);
        });            
   	
    }	
    public void valuChange(String field,Object oldval, Object val) {
        getParentRecursively(this, SysBindControl.class).ifPresent(parent -> {
            ((SysBindControl) parent).valueChange(field,oldval,val);
        });            
   	
    }	
	public void setModify(String mod) {
    	modify.setValue(mod);
    }
    public String getModify() {
    	return modify.getValue();
    }
	public void setBinder(BeanValidationBinder bind) {
//		binder = bind; 
	}
	public BeanValidationBinder getBinder() {
		return binder;
	}
	public void setItem(SysEntity item) {
		this.item = item; 
	}
	public Object getControl() {
		// TODO Auto-generated method stub
		return null;
	}
	public void setData(SysEntity selectedItem) {
	    if (selectedItem != null) {
            binder.readBean(selectedItem); // 폼에 데이터 표시
        } else {
            binder.removeBean(); // 선택 해제 시 폼 초기화
        }	
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
	public List<String> getFields() {
		// TODO Auto-generated method stub
		return fields;
	}	
	
}
