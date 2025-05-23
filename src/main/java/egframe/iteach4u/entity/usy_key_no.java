package egframe.iteach4u.entity;
import egframe.common.SysEntity;
import java.time.LocalDate;
import java.util.Objects;
import java.math.BigDecimal;
import java.sql.Timestamp;
public class usy_key_no implements SysEntity{ 
	public usy_key_no() {
		//System.out.println("Entity");
	}
 public usy_key_no(usy_key_no copy) {
		id= copy.id;
} 
public usy_key_no(long _id, String _type_cd, String _type_nm) {
	id = _id;
	type_cd = _type_cd;
	type_nm = _type_nm;
}
@Override
public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        usy_key_no other = (usy_key_no) obj;
        return
        		Objects.equals(id,other.id)&&
        		Objects.equals(type_cd,other.type_cd)&&
        		Objects.equals(type_nm,other.type_nm
        				);
}

@Override
public int hashCode() {
    return Objects.hash(id);
} 
public String type_cd;
public String type_nm;
private Long id;
private Long count;
 
public String get_type_cd() { return type_cd;}
public String get_type_nm() { return type_nm;}

 
public void set_type_cd(String place_code){ this.type_cd=place_code;}
public void set_type_nm(String place_name){ this.type_nm=place_name;}

@Override
	public Object getValue(String col) {
    	Object obj = null;
    	if(col.equals(
"type_cd")){ obj = type_cd; }
    	else if(col.equals("id")){ obj = id; }
    	else if(col.equals("type_nm")){ obj = type_nm; }
 return obj; 
 }@Override
	public void setValue(String col,Object obj) {
 
if(col.equals( 
"type_cd")){type_cd= (String)obj; }
else if (col.equals("id")){id= (Long)obj; }
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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	}