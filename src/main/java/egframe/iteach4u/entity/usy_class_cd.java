package egframe.iteach4u.entity;
import egframe.common.SysEntity;
import java.time.LocalDate;
import java.util.Objects;
import java.math.BigDecimal;
import java.sql.Timestamp;
public class usy_class_cd implements SysEntity{ 
	public usy_class_cd() {
		//System.out.println("Entity");
	}
 public usy_class_cd(usy_class_cd copy) {
		id= copy.id;
		class_nm= copy.class_nm;
		type_id= copy.type_id;
		type_nm= copy.type_nm;
		modify = copy.modify;
} 
public usy_class_cd(long _id,String _class_nm, long _type_id, String _type_nm, long _count) {
	id = _id;
	class_nm = _class_nm;
	type_id = _type_id;
	type_nm = _type_nm;
	count = _count;
}
@Override
public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        usy_class_cd other = (usy_class_cd) obj;
        return
        		Objects.equals(id,other.id)&&
        		Objects.equals(class_nm,other.class_nm)&&
        		Objects.equals(type_id,other.type_id)&&
        		Objects.equals(type_nm,other.type_nm
        				);
}

@Override
public int hashCode() {
    return Objects.hash(id);
} 
private Long id;
public String class_nm;
private Long type_id;
public String type_nm;
private Long count;
 
public Long getId() {	return id;}
public String get_class_nm() { return class_nm;}
public Long get_type_id() { return type_id;}
public String get_type_nm() { return type_nm;}

 
public void setId(Long id) {	this.id = id;}
public void set_class_nm(String place_name){ this.class_nm=place_name;}
public void set_type_id(Long place_code){ this.type_id=place_code;}
public void set_type_nm(String place_name){ this.type_nm=place_name;}

@Override
	public Object getValue(String col) {
    	Object obj = null;
    	if(col.equals("id")){ obj = id; }
    	else if(col.equals("class_nm")){ obj = class_nm; }
    	else if(col.equals("type_id")){ obj = type_id; }
    	else if(col.equals("type_nm")){ obj = type_nm; }
 return obj; 
 }@Override
	public void setValue(String col,Object obj) {
 
	 if (col.equals("id")){id= (Long)obj; }
	 else if (col.equals("class_nm")){class_nm= (String)obj; }
 else if(col.equals( "type_id")){type_id= (Long)obj; }
 else if (col.equals("type_nm")){type_nm= (String)obj; }
 ; 
 }	private String modify;
	public String getModify() {        return modify;    }
    public void setModify(String groupcd) {        this.modify = groupcd;    }
	@Override
	public boolean isSelected() {
        return selected;
	}
	@Override
	public void setSelected(boolean selected) {
		 selected = selected;
	}
	@Override
	public String getPropertyValue(String propertyName) {
		return null;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	}