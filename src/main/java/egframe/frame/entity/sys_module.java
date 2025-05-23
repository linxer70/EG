package egframe.frame.entity;

import egframe.common.SysEntity;

public class sys_module  implements SysEntity{
    private String module_id;
    private String module_nm;
    private String icon_nm;
    private String is_use;
    private String code_full_name;
    private String order_num;
    private String language_cd;
    
    public sys_module() {
    	}
    public sys_module(sys_module copy) {
    	module_id = copy.module_id;
    	module_nm = copy.module_nm;
    	icon_nm = copy.icon_nm;
    	is_use = copy.is_use;
    	order_num = copy.order_num;
    	code_full_name = copy.code_full_name;
    	language_cd = copy.language_cd;
    }
	public String get_module_id() {
        return module_id;
    }
    public void set_module_id(String groupcd) {
        this.module_id = groupcd;
    }
    public String get_module_nm() {
        return module_nm;
    }
    public void set_module_nm(String groupnm) {
        this.module_nm = groupnm;
    }
	public String get_is_use() {
        return is_use;
    }
    public void set_is_use(String groupcd) {
        this.is_use = groupcd;
    }
	public String get_icon_nm() {
        return icon_nm;
    }
    public void set_icon_nm(String groupcd) {
        this.icon_nm = groupcd;
    }
	public String get_language_cd() {
        return language_cd;
    }
    public void set_language_cd(String groupcd) {
        this.language_cd = groupcd;
    }
	public String get_order_num() {
        return order_num;
    }
    public void set_code_full_name(String groupcd) {
        this.code_full_name = groupcd;
    }
	public String get_code_full_name() {
        return code_full_name;
    }
    public void set_order_num(String groupcd) {
        this.order_num = groupcd;
    }
    public String getPropertyValue(String propertyName) {
        if ("module_cd".equals(propertyName)) {
            return module_id;
        } else if ("module_nm".equals(propertyName)){
        	return module_nm;
        }
        return null;
    }    
    public String getComboDisplay() {
        return module_id+" :: "+module_nm;
    }
    @Override
    public String toString() {
    	return module_id+":"+module_nm;
    }
	@Override
	public Object getValue(String col) {
    	Object obj = null;
    	if(col.equals("module_id")){ obj = module_id; }
		else if(col.equals("module_nm")){ obj = module_nm; }
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
	@Override
	public void setModify(String col) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getModify() {
		// TODO Auto-generated method stub
		return null;
	}
}
