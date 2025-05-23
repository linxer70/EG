package egframe.iteach4u.entity;
import egframe.common.SysEntity;
import java.time.LocalDate;
import java.util.Objects;
import java.math.BigDecimal;
import java.sql.Timestamp;
public class usy_subject_cd implements SysEntity{ 
	public usy_subject_cd() {
		//System.out.println("Entity");
	}
 public usy_subject_cd(usy_subject_cd copy) {
		id= copy.id;
		subject_nm= copy.subject_nm;
		type_id= copy.type_id;
	type_nm= copy.type_nm;
	class_id= copy.class_id;
	class_nm= copy.class_nm;
	 modify = copy.modify;
	 count= copy.count;
} 
 public usy_subject_cd(long _id,String _subject_nm, Long _type_id,String _type_nm, Long _class_id,String _class_nm, long _count) {
		id = _id;
		subject_nm = _subject_nm;
		type_id = _type_id;
		type_nm = _type_nm;
		class_id = _class_id;
		class_nm = _class_nm;
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
        usy_subject_cd other = (usy_subject_cd) obj;
        return
        		
        				Objects.equals(id,other.id)&&
        				Objects.equals(subject_nm,other.subject_nm)&&
        				Objects.equals(type_id,other.type_id)&&
        				Objects.equals(type_nm,other.type_nm)&&
				Objects.equals(class_id,other.class_id)&&
Objects.equals(class_nm,other.class_nm)&&
Objects.equals(count,other.count);
}

@Override
public int hashCode() {
    return Objects.hash(id);
}
private Long id;
private String subject_nm;
private Long type_id;
 private String type_nm;
 private Long class_id;
 private String class_nm;
 private Long count;
 private usy_subject_cd  usy_subject_cd;
 public Long getId() { return id;}
 public String get_subject_nm() { return subject_nm;}
 public Long get_type_id() { return type_id;}
public String get_type_nm() { return type_nm;}
public Long get_class_id() { return class_id;}
public String get_class_nm() { return class_nm;}
public Long getCount() { return count;}

 
public void setId(Long id){ this.id=id;}
public void set_subject_nm(String subject_nm){ this.subject_nm=subject_nm;}
public void set_type_id(Long type_id){ this.type_id=type_id;}
public void set_type_nm(String type_nm){ this.type_nm=type_nm;}
public void set_class_id(Long class_id){ this.class_id=class_id;}
public void set_class_nm(String class_nm){ this.class_nm=class_nm;}
public void setCount(Long count){ this.count=count;}

@Override
	public Object getValue(String col) {
    	Object obj = null;
    	if(col.equals("id")){ obj = id; }
    	else if(col.equals("subject_nm")){ obj = subject_nm; }
    	else if(col.equals("type_id")){ obj = type_id; }
    	else if(col.equals("type_nm")){ obj = type_nm; }
    	else if(col.equals("class_id")){ obj = class_id; }
    	else if(col.equals("class_nm")){ obj = class_nm; }
    	else if(col.equals("count")){ obj = count; }
    	return obj; 
 }@Override
	public void setValue(String col,Object obj) {
	 if (col.equals("id")){id= (Long)obj; }
	 else if (col.equals("subject_cd")){subject_nm= (String)obj; }
	 else if (col.equals("type_id")){type_id= (Long)obj; }
	 else if(col.equals( "type_cd")){type_nm= (String)obj; }
	 else if (col.equals("class_id")){class_id= (Long)obj; }
	 else if (col.equals("class_cd")){class_nm= (String)obj; }
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