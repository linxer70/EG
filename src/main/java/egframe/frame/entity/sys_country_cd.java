package egframe.frame.entity;
import egframe.common.SysEntity;
import java.time.LocalDate;
import java.util.Objects;
import java.math.BigDecimal;
import java.sql.Timestamp;
public class sys_country_cd implements SysEntity{ 
	public sys_country_cd() {
		//System.out.println("Entity");
	}
 public sys_country_cd(sys_country_cd copy) {
	 country_cd= copy.country_cd;
	 country_nm= copy.country_nm;
	 view_yn= copy.view_yn;
	 mat_cnt= copy.mat_cnt;
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
        sys_country_cd other = (sys_country_cd) obj;
        return
        		Objects.equals(country_cd,other.country_cd)&&
        		Objects.equals(view_yn,other.view_yn)&&
        		Objects.equals(mat_cnt,other.mat_cnt)&&
Objects.equals(country_nm,other.country_nm);
}
 
 public String country_cd;
 public String country_nm;
 public String view_yn;
 public Integer mat_cnt;

 
public String get_country_cd() { return country_cd;}
public String get_country_nm() { return country_nm;}
public String get_view_yn() { return view_yn;}
public Integer get_mat_cnt() { return mat_cnt;}

 
public void set_country_cd(String country_cd){ this.country_cd=country_cd;}
public void set_country_nm(String country_nm){ this.country_nm=country_nm;}
public void set_view_yn(String view_yn){ this.view_yn=view_yn;}
public void set_mat_cnt(Integer mat_cnt){ this.mat_cnt=mat_cnt;}

@Override
	public Object getValue(String col) {
    	Object obj = null;
    	if(col.equals("country_cd")){ obj = country_cd; }
    	else if(col.equals("country_nm")){ obj = country_nm; }
    	else if(col.equals("view_yn")){ obj = view_yn; }
    	else if(col.equals("mat_cnt")){ obj = mat_cnt; }
    	return obj; 
 }
	@Override
	public void setValue(String col,Object obj) {
 
if(col.equals( 
"country_cd")){country_cd= (String)obj; }
 else if (col.equals("country_nm")){country_nm= (String)obj; }
 else if (col.equals("view_yn")){view_yn= (String)obj; }
 else if (col.equals("mat_cnt")){mat_cnt= (Integer)obj; }
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