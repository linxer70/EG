package egframe.iteach4u.entity;
import egframe.common.SysEntity;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
public class exm_mat_unit_row implements SysEntity{ 
	public exm_mat_unit_row() {
		//System.out.println("Entity");
	}
 public exm_mat_unit_row(exm_mat_unit_row copy) {
	 row_num= copy.row_num;
	 id= copy.id;
	 user_id= copy.user_id;
	 title= copy.title;
	 update_dt= copy.update_dt;
	 view_id= copy.view_id;
	 hd_id= copy.hd_id;
	 type_id= copy.type_id;
	 class_id= copy.class_id;
	 subject_id= copy.subject_id;
	 type_id= copy.type_id;
	 type_nm= copy.type_nm;
	 class_nm= copy.class_nm;
	 subject_nm= copy.subject_nm;
	 div_cd= copy.div_cd;
	 div_nm= copy.div_nm;
	 context= copy.context;
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
        exm_mat_unit_row other = (exm_mat_unit_row) obj;
        return
        		Objects.equals(
        				id,other.id)&&
        		Objects.equals(row_num,other.row_num)&&
Objects.equals(user_id,other.user_id)&&
Objects.equals(title,other.title)&&
Objects.equals(update_dt,other.update_dt)&&
Objects.equals(div_cd,other.div_cd)&&
Objects.equals(type_id,other.type_id)&&
Objects.equals(class_id,other.class_id)&&
Objects.equals(subject_id,other.subject_id)&&
Objects.equals(unit_id,other.unit_id)&&
Objects.equals(type_nm,other.type_nm)&&
Objects.equals(class_nm,other.class_nm)&&
Objects.equals(subject_nm,other.subject_nm)&&
Objects.equals(div_nm,other.div_nm)&&
Objects.equals(context,other.context)&&
Objects.equals(view_id,other.view_id)&&
Objects.equals(hd_id,other.hd_id);
}
 
public Long id;
public Integer row_num;
public String user_id;
public String title;
public String context;
public LocalDate update_dt;
public Long hd_id;
public Long view_id;
public Long type_id;
public Long class_id;
public Long subject_id;
public Long unit_id;
public String type_nm;
public String class_nm;
public String subject_nm;
public String div_nm;
public String div_cd;

 
public Long getId() { return id;}
public Integer get_row_num() { return row_num;}
public String get_user_id() { return user_id;}
public String get_title() { return title;}
public LocalDate get_update_dt() { return update_dt;}
public Long get_hd_id() { return hd_id;}
public Long get_view_id() { return view_id;}
public Long get_type_id() { return type_id;}
public Long get_unit_id() { return unit_id;}
public Long get_class_id() { return class_id;}
public Long get_subject_id() { return subject_id;}
public String get_type_nm() { return type_nm;}
public String get_class_nm() { return class_nm;}
public String get_subject_nm() { return subject_nm;}
public String get_div_cd() { return div_cd;}
public String get_div_nm() { return div_nm;}
public String get_context() { return context;}

 
public void setId(Long id){ this.id=id;}
public void set_row_num(Integer row_num){ this.row_num=row_num;}
public void set_user_id(String user_id){ this.user_id=user_id;}
public void set_title(String title){ this.title=title;}
public void set_update_dt(LocalDate update_dt){ this.update_dt=update_dt;}
public void set_hd_id(Long hd_id){ this.hd_id=hd_id;}
public void set_view_id(Long view_id){ this.view_id=view_id;}
public void set_unit_id(Long unit_id){ this.unit_id=unit_id;}
public void set_type_id(Long type_id){ this.type_id=type_id;}
public void set_class_id(Long class_id){ this.class_id=class_id;}
public void set_subject_id(Long subject_id){ this.subject_id=subject_id;}
public void set_type_nm(String type_nm){ this.type_nm=type_nm;}
public void set_class_nm(String class_nm){ this.class_nm=class_nm;}
public void set_subject_nm(String subject_nm){ this.subject_nm=subject_nm;}
public void set_div_cd(String div_cd){ this.div_cd=div_cd;}
public void set_div_nm(String div_nm){ this.div_nm=div_nm;}
public void set_context(String context){ this.context=context;}

@Override
	public Object getValue(String col) {
    	Object obj = null;
    	if(col.equals(
"id")){ obj = id; }
    	 else if(col.equals("row_num")){ obj = row_num; }
 else if(col.equals("user_id")){ obj = user_id; }
 else if(col.equals("title")){ obj = title; }
 else if(col.equals("update_dt")){ obj = update_dt; }
 else if(col.equals("type_id")){ obj = type_id; }
 else if(col.equals("class_id")){ obj = class_id; }
 else if(col.equals("subject_id")){ obj = subject_id; }
 else if(col.equals("unit_id")){ obj = unit_id; }
    	 else if(col.equals("type_nm")){ obj = type_nm; }
 else if(col.equals("class_nm")){ obj = class_nm; }
 else if(col.equals("subject_nm")){ obj = subject_nm; }
 else if(col.equals("div_cd")){ obj = div_cd; }
 else if(col.equals("div_nm")){ obj = div_nm; }
 else if(col.equals("context")){ obj = context; }
else if(col.equals("hd_id")){ obj = hd_id; }
else if(col.equals("view_id")){ obj = view_id; }
 return obj; 
 }@Override
	public void setValue(String col,Object obj) {
 
if(col.equals( 
"id")){id= (Long)obj; }
else if (col.equals("row_num")){row_num= (Integer)obj; }
 else if (col.equals("user_id")){user_id= (String)obj; }
 else if (col.equals("title")){title= (String)obj; }
 else if (col.equals("update_dt")){update_dt= convertToLocalDate((Timestamp)obj) ;}
else if (col.equals("type_cd")){type_id= (Long)obj; }
 else if (col.equals("class_cd")){class_id= (Long)obj; }
 else if (col.equals("subject_cd")){subject_id= (Long)obj; }
 else if (col.equals("unit_id")){unit_id= (Long)obj; }
 else if (col.equals("type_nm")){type_nm= (String)obj; }
 else if (col.equals("class_nm")){class_nm= (String)obj; }
 else if (col.equals("subject_nm")){subject_nm= (String)obj; }
 else if (col.equals("div_cd")){div_cd= (String)obj; }
 else if (col.equals("div_nm")){div_nm= (String)obj; }
 else if (col.equals("context")){context= (String)obj; }
else if (col.equals("hd_id")){hd_id= (Long)obj; }
else if (col.equals("view_id")){view_id= (Long)obj; }
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
	
	private static LocalDate convertToLocalDate(java.sql.Timestamp timestamp) {
			    	Instant instant = Instant.ofEpochMilli(timestamp.getTime());
			    	LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
			    	return localDateTime.toLocalDate();
				}
				private static BigInteger convertToBigInteger(BigDecimal bigDecimal) {
					if(bigDecimal==null) {
						return BigInteger.valueOf(0) ;
					}
			    	return bigDecimal.toBigInteger();
				}
				private static int convertToInteger(BigDecimal bigDecimal) {
					if(bigDecimal==null) {
						return 0 ;
					}
			    	return bigDecimal.intValueExact();
				}
				private static int convertToInteger(BigInteger bigInteger) {
			 	   return bigInteger.intValueExact();
				}	
	}