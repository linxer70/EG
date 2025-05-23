package egframe.iteach4u.entity;
import egframe.common.SysEntity;
import java.time.LocalDate;
import java.util.Objects;
import java.math.BigDecimal;
import java.sql.Timestamp;
public class usy_div_cd implements SysEntity{ 
	public usy_div_cd() {
		//System.out.println("Entity");
	}
 public usy_div_cd(usy_div_cd copy) {
	 id= copy.id;
	 div_nm= copy.div_nm;
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
        usy_div_cd other = (usy_div_cd) obj;
        return
        		Objects.equals(id,other.id)&&
        		Objects.equals(view_yn,other.view_yn)&&
        		Objects.equals(mat_cnt,other.mat_cnt)&&
Objects.equals(div_nm,other.div_nm);
}
 
 public Long id;
 public String div_nm;
 public String view_yn;
 public Integer mat_cnt;

 
public Long getId() { return id;}
public String get_div_nm() { return div_nm;}
public String get_view_yn() { return view_yn;}
public Integer get_mat_cnt() { return mat_cnt;}

 
public void setId(Long id){ this.id=id;}
public void set_div_nm(String div_nm){ this.div_nm=div_nm;}
public void set_view_yn(String view_yn){ this.view_yn=view_yn;}
public void set_mat_cnt(Integer mat_cnt){ this.mat_cnt=mat_cnt;}

@Override
	public Object getValue(String col) {
    	Object obj = null;
    	if(col.equals("id")){ obj = id; }
    	else if(col.equals("div_nm")){ obj = div_nm; }
    	else if(col.equals("view_yn")){ obj = view_yn; }
    	else if(col.equals("mat_cnt")){ obj = mat_cnt; }
    	return obj; 
 }
	@Override
	public void setValue(String col,Object obj) {
 
if(col.equals( 
"id")){id= (Long)obj; }
 else if (col.equals("div_nm")){div_nm= (String)obj; }
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