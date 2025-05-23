package egframe.frame.entity;




import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import egframe.common.SysEntity;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.data.provider.DataProviderListener;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalDataProvider;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalQuery;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;
import com.vaadin.flow.shared.Registration;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

public class sys_user_menu   implements SysEntity{
	
	private String user_cd ;
	private String module_id ;
	private String program_id ;
	private String icon_nm;
	private String program_nm;
	private String parent_cd ;
	private Long order_num;
	private String parent_yn;
	private String menu_knd;
	private String module_nm;
	private String modify;
	
	public sys_user_menu() {
	}
	public sys_user_menu(sys_user_menu copy) {
		user_cd = copy.user_cd;
		module_id = copy.module_id;
		program_id = copy.program_id;
		parent_cd = copy.parent_cd;
		icon_nm =  copy.icon_nm;
		program_nm = copy.program_nm;
		order_num = copy.order_num;
		parent_yn = copy.parent_yn;
		module_nm = copy.module_nm;
		menu_knd = copy.menu_knd;
		modify = copy.modify;
	}
	public String getModify() {
        return modify;
    }
    public void setModify(String groupcd) {
        this.modify = groupcd;
    }
	 @Override
	    public String toString() {
	        return program_nm;
	    }
	
    public String get_program_nm() {	        return program_nm;	}
    public String get_program_id() {	        return program_id;	}
    public String get_menu_knd() {        return menu_knd;    }
	public String get_parent_yn() {	      return parent_yn;	}
	public String get_icon_nm() {        return icon_nm;    }
    public String get_module_id() {        return this.module_id;    }
    public String get_module_nm() {        return this.module_nm;    }
    public String get_user_cd() {        return this.user_cd;    }
    public String get_parent_cd() {        return this.parent_cd;    }
    public Long get_order_num() {        return order_num;    }
	
    public void setMenuKnd(String menu_knd) {    	
    	if(menu_knd.equals("P")) {
    		this.menu_knd = "프로그램";
    	}else {
        	this.menu_knd = "메뉴";
    	}
    }
	
    public void set_parent_yn(String programNm) {	      this.parent_yn = programNm;	 }
    public void set_icon_nm(String icon_nm) {        this.icon_nm = icon_nm;    }
    public void set_module_id(String modulecd) {        this.module_id = modulecd;    }
    public void set_module_nm(String modulecd) {        this.module_nm=modulecd;    }
    public void set_user_cd(String userCd) {        this.user_cd= userCd;    }
    public void set_program_id(String programCd) {        this.program_id = programCd;    }
    public void set_program_nm(String programCd) {        this.program_nm = programCd;    }
    public void set_menu_knd(String programCd) {        this.menu_knd = programCd;    }
    public void set_parent_cd(String parentCd) {        this.parent_cd = parentCd;    }
    public void set_order_num(Long orderNum) {     this.order_num = orderNum;    }
    
	public List<sys_user_menu> getChildren() {
		List<sys_user_menu> children = new ArrayList();
		return children;
	}
    public String getPropertyValue(String propertyName) {
        if ("user_cd".equals(propertyName)) {
            return user_cd;
        } else if ("program_id".equals(propertyName)){
        	return program_id;
        } else if ("parent_cd".equals(propertyName)){
        	return parent_cd;
        } else if ("module_id".equals(propertyName)){
        	return module_id;
        }
        return null;
    }    
    public String getComboDisplay() {
        return user_cd+" :: "+program_id;
    }
    public String isSubscriber() {
        return menu_knd;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        sys_user_menu other = (sys_user_menu) obj;
        // 필드(속성) 값을 비교합니다
        return// Objects.equals(type_cd, other.type_cd) &&
               Objects.equals(user_cd, other.user_cd) &&
               Objects.equals(module_id, other.module_id) &&
               Objects.equals(program_id, other.program_id) &&
               Objects.equals(parent_cd, other.parent_cd) &&
               Objects.equals(module_nm, other.module_nm) &&
               Objects.equals(program_nm, other.program_nm) ;
               //Objects.equals(icon_nm, other.icon_nm);
    }        
	@Override
	public Object getValue(String col) {
    	Object obj = null;
    	if(col.equals("program_nm")){ obj = program_nm; }
		else if(col.equals("parent_cd")){ obj = parent_cd; }
		else if(col.equals("program_id")){ obj = program_id; }
		else if(col.equals("module_id")){ obj = module_id; }
		else if(col.equals("module_nm")){ obj = module_nm; }
		else if(col.equals("menu_knd")){ obj = menu_knd; }
		else if(col.equals("user_cd")){ obj = user_cd; }
		else if(col.equals("modify")){ obj = modify; }
		return obj; 
	}
	@Override
	public boolean isSelected() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void setSelected(boolean selected) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setValue(String col, Object obj) {
		// TODO Auto-generated method stub
		
	}
}


