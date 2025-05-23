package egframe.iteach4u.entity;
import egframe.common.SysEntity;
import java.time.LocalDate;
import java.util.Objects;
import java.math.BigDecimal;
import java.sql.Timestamp;
public class usy_grade_cd implements SysEntity{ 
	public usy_grade_cd() {
		//System.out.println("Entity");
	}
 public usy_grade_cd(usy_grade_cd copy) {
	 grade_cd= copy.grade_cd;
	 grade_nm= copy.grade_nm;
	 modify = copy.modify;
} 
@Override
public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        usy_grade_cd other = (usy_grade_cd) obj;
        return
        		Objects.equals(
        				grade_cd,other.grade_cd)&&
Objects.equals(grade_nm,other.grade_nm);
}
 
 public String grade_cd;
 public String grade_nm;

 
public String get_grade_cd() { return grade_cd;}
public String get_grade_nm() { return grade_nm;}

 
public void set_grade_cd(String grade_cd){ this.grade_cd=grade_cd;}
public void set_grade_nm(String grade_nm){ this.grade_nm=grade_nm;}

@Override
	public Object getValue(String col) {
    	Object obj = null;
    	if(col.equals(
"grade_cd")){ obj = grade_cd; }
 else if(col.equals("grade_nm")){ obj = grade_nm; }
 return obj; 
 }@Override
	public void setValue(String col,Object obj) {
 
if(col.equals( 
"grade_cd")){grade_cd= (String)obj; }
 else if (col.equals("grade_nm")){grade_nm= (String)obj; }
 ; 
 }	public String modify;
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