package egframe.frame.freeform;

import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

import egframe.common.SysFree;
import egframe.frame.entity.UsrMaster;

public class f_usr_master extends SysFree {
	
	private static final long serialVersionUID = 1L;
	private IntegerField id ;
	private TextField user_cd ;
	private TextField user_nm ;
	private PasswordField password ;
	private IntegerField group_id ;
	private TextField group_cd ;
	private IntegerField type_id ;
	private TextField type_nm ;
	public f_usr_master() {
		init();
		
	}
	
	public f_usr_master(UsrMaster data) {
	}

	public void init() {
		   id = new IntegerField();
		   user_cd	= new TextField ("유저 아이디")	;
		   user_nm	= new TextField ("유저 명")	;
		   password	= new PasswordField ("패스워드")	;
		   group_id	= new IntegerField ("그룹 아이디")	;
		   group_cd	= new TextField ("그룹 아이디")	;
		   type_id = new IntegerField();
		   type_nm	= new TextField ("디폴트 분야")	;
		   
		   type_nm.setValueChangeMode(ValueChangeMode.EAGER);
		   group_cd.setValueChangeMode(ValueChangeMode.EAGER);
		   user_cd.setValueChangeMode(ValueChangeMode.EAGER);
		   user_nm.setValueChangeMode(ValueChangeMode.EAGER);
		   password.setValueChangeMode(ValueChangeMode.EAGER);
		   group_id.setValueChangeMode(ValueChangeMode.EAGER);
		   user_nm.getElement().setAttribute("autocomplete", "new-password");	
		   password.getElement().setAttribute("autocomplete", "new-password");	
		   
		   type_nm.addValueChangeListener(e->{			if(e.getValue()!=null) {				addEvent("type_nm",e);}});
		   user_cd.addValueChangeListener(e->{			if(e.getValue()!=null) {				addEvent("class_nm",e);}});
		   user_nm.addValueChangeListener(e->{			if(e.getValue()!=null) {				addEvent("user_nm",e);}});
		   group_cd.addValueChangeListener(e->{		if(e.getValue()!=null) {				addEvent("group_cd",e);}});
		   
		   add(user_cd,user_nm,password,group_cd,type_nm);
		   System.out.println(user_nm.getValue());
	}
	public void setData(UsrMaster data) {
		id.setValue(data.getId().intValue());
		user_cd.setValue(data.getUser_cd());
		user_nm.setValue(data.getUserNm());
		password.setValue(data.getPassword());
		group_cd.setValue(data.getGroup_cd());
		type_nm.setValue(data.getType_nm());
		Long val = data.getType_id();
		
		type_id.setValue(val.intValue());
	}
	public void addEvent() {
		
	}
	public String getUser_cd() {
		return user_cd.getValue();
	}
	public void setUser_cd(String _user_cd) {
		user_cd.setValue(_user_cd);
	}
	public String getUser_nm() {
		return user_nm.getValue();
	}
	public void setUser_nm(String _user_nm) {
		user_nm.setValue(_user_nm);
	}
	public String getPassword() {
		return password.getValue();
	}
	public void setPassword(String _password) {
		password.setValue(_password);
	}
	public String getGroup_id() {
		return group_cd.getValue();
	}
	public void setGroup_id(String _group_id) {
		group_cd.setValue(_group_id);
	}
	public Long  get_Id() {
		Long val = id.getValue().longValue();
		return val;
	}
	public void set_Id(Long _id) {
		Integer val = _id.intValue();
		id.setValue(val);
	}
	public Long  getType_Id() {
		Long val = id.getValue().longValue();
		return val;
	}
	public void setType_Id(Long _id) {
		Integer val = _id.intValue();
		this.type_id.setValue(val);
	}
	public String getGroup_cd() {
		return group_cd.getValue();
	}
	public void setGroup_cd(String group_cd) {
		this.group_cd.setValue(group_cd);
	}
}
