package egframe.iteach4u.entity;
import egframe.common.SysEntity;
import java.time.LocalDate;
import java.util.Objects;
import java.math.BigDecimal;
import java.sql.Timestamp;
public class usy_unit_cd implements SysEntity{ 
	public usy_unit_cd() {
		//System.out.println("Entity");
	}
 public usy_unit_cd(usy_unit_cd copy) {
		id= copy.id;
		unit_nm= copy.unit_nm;
		type_id= copy.type_id;
	type_nm= copy.type_nm;
	class_id= copy.class_id;
	class_nm= copy.class_nm;
	subject_id= copy.subject_id;
	subject_nm= copy.subject_nm;
	 modify = copy.modify;
	 count= copy.count;
} 
 public usy_unit_cd(long _id, String _unit_nm,Long _type_id,String _type_nm, Long _class_id,String _class_nm,Long _subject_id,String _subject_nm, long _count) {
		id = _id;
		unit_nm = _unit_nm;
		type_id = _type_id;
		type_nm = _type_nm;
		class_id = _class_id;
		class_nm = _class_nm;
		subject_id = _subject_id;
		subject_nm = _subject_nm;
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
        usy_unit_cd other = (usy_unit_cd) obj;
        return
        		
        				Objects.equals(id,other.id)&&
        				Objects.equals(unit_nm,other.unit_nm)&&
        				Objects.equals(type_id,other.type_id)&&
        				Objects.equals(type_nm,other.type_nm)&&
				Objects.equals(class_id,other.class_id)&&
Objects.equals(class_nm,other.class_nm)&&
Objects.equals(subject_id,other.subject_id)&&
Objects.equals(subject_nm,other.subject_nm)&&
Objects.equals(count,other.count);
}

@Override
public int hashCode() {
    return Objects.hash(id);
}
private Long id;
private String unit_nm;
private Long type_id;
 private String type_nm;
 private Long class_id;
 private String class_nm;
 private Long subject_id;
 private String subject_nm;
 private Long count;
 
 public Long getId() { return id;}
 public String get_unit_nm() { return unit_nm;}
 public Long get_type_id() { return type_id;}
public String get_type_nm() { return type_nm;}
public Long get_class_id() { return class_id;}
public String get_class_nm() { return class_nm;}
public Long get_subject_id() { return subject_id;}
public String get_subject_nm() { return subject_nm;}
public Long getCount() { return count;}

 
public void setId(Long id){ this.id=id;}
public void set_unit_nm(String unit_nm){ this.unit_nm=unit_nm;}
public void set_type_id(Long type_id){ this.type_id=type_id;}
public void set_type_nm(String type_nm){ this.type_nm=type_nm;}
public void set_class_id(Long class_id){ this.class_id=class_id;}
public void set_class_nm(String class_nm){ this.class_nm=class_nm;}
public void set_subject_id(Long subject_id){ this.subject_id=subject_id;}
public void set_subject_nm(String subject_nm){ this.subject_nm=subject_nm;}
public void setCount(Long count){ this.count=count;}

@Override
	public Object getValue(String col) {
    	Object obj = null;
    	if(col.equals("id")){ obj = id; }
    	else if(col.equals("unit_nm")){ obj = unit_nm; }
    	else if(col.equals("type_id")){ obj = type_id; }
    	else if(col.equals("type_nm")){ obj = type_nm; }
    	else if(col.equals("class_id")){ obj = class_id; }
    	else if(col.equals("class_nm")){ obj = class_nm; }
    	else if(col.equals("subject_id")){ obj = subject_id; }
    	else if(col.equals("subject_nm")){ obj = subject_nm; }
    	else if(col.equals("count")){ obj = count; }
    	return obj; 
 }@Override
	public void setValue(String col,Object obj) {
	 if (col.equals("id")){id= (Long)obj; }
	 else if (col.equals("unit_nm")){unit_nm= (String)obj; } 
	 else if (col.equals("type_id")){type_id= (Long)obj; }
	 else if(col.equals( "type_cd")){type_nm= (String)obj; }
	 else if (col.equals("class_id")){class_id= (Long)obj; }
	 else if (col.equals("class_cd")){class_nm= (String)obj; }
	 else if (col.equals("subject_id")){subject_id= (Long)obj; }
	 else if (col.equals("subject_cd")){subject_nm= (String)obj; }
	 else if (col.equals("count")){count= (Long)obj; }
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
	}