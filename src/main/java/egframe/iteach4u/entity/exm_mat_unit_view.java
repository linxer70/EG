package egframe.iteach4u.entity;
import egframe.common.SysEntity;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
public class exm_mat_unit_view implements SysEntity{ 
	public exm_mat_unit_view() {
		//System.out.println("Entity");
	}
 public exm_mat_unit_view(exm_mat_unit_view copy) {
	 row_num= copy.row_num;
	 id= copy.id;
	 hd_cnt= copy.hd_cnt;
	 update_dt= copy.update_dt;
	 type_id= copy.type_id;
	 view_yn= copy.view_yn;
	 class_id= copy.class_id;
	 visible_yn= copy.visible_yn;
	 subject_id= copy.subject_id;
	 type_nm= copy.type_nm;
	 class_nm= copy.class_nm;
	 subject_nm= copy.subject_nm;
	 unit_cnt= copy.unit_cnt;
	 div_id= copy.div_id;
	 unit_id= copy.unit_id;
	 unit_nm= copy.unit_nm;
	 div_nm= copy.div_nm;
	 context= copy.context;
	 hdcontext= copy.hdcontext;
	 modify = copy.modify;
} 
public exm_mat_unit_view(String string) {
	// TODO Auto-generated constructor stub
}
@Override
public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        exm_mat_unit_view other = (exm_mat_unit_view) obj;
        return
        		Objects.equals(id,other.id)&&
        		Objects.equals(row_num,other.row_num)&&
        		Objects.equals(hd_id,other.hd_id)&&
        		Objects.equals(hd_cnt,other.hd_cnt)&&
Objects.equals(user_id,other.user_id)&&
Objects.equals(title,other.title)&&
Objects.equals(update_dt,other.update_dt)&&
Objects.equals(div_id,other.div_id)&&
Objects.equals(view_yn,other.view_yn)&&
Objects.equals(type_id,other.type_id)&&
Objects.equals(class_id,other.class_id)&&
Objects.equals(subject_id,other.subject_id)&&
Objects.equals(visible_yn,other.visible_yn)&&
Objects.equals(type_nm,other.type_nm)&&
Objects.equals(unit_id,other.unit_id)&&
Objects.equals(unit_nm,other.unit_nm)&&
Objects.equals(unit_cnt,other.unit_cnt)&&
Objects.equals(class_nm,other.class_nm)&&
Objects.equals(subject_nm,other.subject_nm)&&
Objects.equals(div_nm,other.div_nm)&&
Objects.equals(context,other.context)&&
Objects.equals(hdcontext,other.hdcontext)&&
Objects.equals(file_counter,other.file_counter)&&
Objects.equals(down_counter,other.down_counter);
}
 
public Long id;
public Long hd_id;
public Integer hd_cnt;
public Integer unit_cnt;
public Integer row_num;
public String user_id;
public String title;
public String view_yn;
@JsonProperty("_context")
public String context;
public String hdcontext;
public LocalDate update_dt;
public Integer down_counter;
public Integer file_counter;
public Long type_id;
public Long class_id;
public Long subject_id;
public Long unit_id;
public String visible_yn;
public String unit_nm;
public String type_nm;
public String class_nm;
public String subject_nm;
public String div_nm;
public Long div_id;

 
public Long getId() { return id;}
public Long get_hd_id() { return hd_id;}
public Integer get_hd_cnt() { return hd_cnt;}
public Integer get_unit_cnt() { return unit_cnt;}
public Integer get_row_num() { return row_num;}
public String get_user_id() { return user_id;}
public String get_view_yn() { return view_yn;}
public String get_title() { return title;}
public LocalDate get_update_dt() { return update_dt;}
public Integer get_down_count() { return down_counter;}
public Integer get_file_count() { return file_counter;}
public String get_unit_nm() { return unit_nm;}
public Long get_type_id() { return type_id;}
public Long get_class_id() { return class_id;}
public Long get_subject_id() { return subject_id;}
public Long get_unit_id() { return unit_id;}
public String get_type_nm() { return type_nm;}
public String get_class_nm() { return class_nm;}
public String get_subject_nm() { return subject_nm;}
public Long get_div_id() { return div_id;}
public String get_div_nm() { return div_nm;}
public String get_visible_yn() { return visible_yn;}
public String get_context() { return context;}
public String get_hdcontext() { return hdcontext;}

 
public void setId(Long id){ this.id=id;}
public void set_hd_id(Long hd_id){ this.hd_id=hd_id;}
public void set_hd_cnt(Integer hd_cnt){ this.hd_cnt=hd_cnt;}
public void set_unit_cnt(Integer unit_cnt){ this.unit_cnt=unit_cnt;}
public void set_row_num(Integer row_num){ this.row_num=row_num;}
public void set_user_id(String user_id){ this.user_id=user_id;}
public void set_view_yn(String view_yn){ this.view_yn=view_yn;}
public void set_title(String title){ this.title=title;}
public void set_update_dt(LocalDate update_dt){ this.update_dt=update_dt;}
public void set_down_count(Integer down_counter){ this.down_counter=down_counter;}
public void set_file_count(Integer file_counter){ this.file_counter=file_counter;}
public void set_unit_nm(String unit_nm){ this.unit_nm=unit_nm;}
public void set_type_id(Long type_id){ this.type_id=type_id;}
public void set_class_id(Long class_id){ this.class_id=class_id;}
public void set_subject_id(Long subject_id){ this.subject_id=subject_id;}
public void set_unit_id(Long unit_id){ this.unit_id=unit_id;}
public void set_type_nm(String type_nm){ this.type_nm=type_nm;}
public void set_class_nm(String class_nm){ this.class_nm=class_nm;}
public void set_subject_nm(String subject_nm){ this.subject_nm=subject_nm;}
public void set_div_id(Long div_id){ this.div_id=div_id;}
public void set_div_nm(String div_nm){ this.div_nm=div_nm;}
public void set_visible_yn(String visible_yn){ this.visible_yn=visible_yn;}
public void set_context(String context){ this.context=context;}
public void set_hdcontext(String hdcontext){ this.hdcontext=hdcontext;}

@Override
	public Object getValue(String col) {
    	Object obj = null;
    	if(col.equals(
"id")){ obj = id; }
   	 else if(col.equals("hd_id")){ obj = hd_id; }
   	 else if(col.equals("hd_cnt")){ obj = hd_cnt; }
    	 else if(col.equals("row_num")){ obj = row_num; }
    	 else if(col.equals("unit_cnt")){ obj = unit_cnt; }
 else if(col.equals("user_id")){ obj = user_id; }
 else if(col.equals("title")){ obj = title; }
 else if(col.equals("update_dt")){ obj = update_dt; }
 else if(col.equals("type_id")){ obj = type_id; }
 else if(col.equals("unit_id")){ obj = unit_id; }
 else if(col.equals("unit_nm")){ obj = unit_nm; }
    	 else if(col.equals("view_yn")){ obj = view_yn; }
 else if(col.equals("class_id")){ obj = class_id; }
 else if(col.equals("subject_id")){ obj = subject_id; }
 else if(col.equals("type_nm")){ obj = type_nm; }
 else if(col.equals("class_nm")){ obj = class_nm; }
 else if(col.equals("subject_nm")){ obj = subject_nm; }
 else if(col.equals("div_id")){ obj = div_id; }
 else if(col.equals("div_nm")){ obj = div_nm; }
 else if(col.equals("visible_yn")){ obj = visible_yn; }
 else if(col.equals("context")){ obj = context; }
 else if(col.equals("hdcontext")){ obj = hdcontext; }
else if(col.equals("down_counter")){ obj = down_counter; }
else if(col.equals("file_counter")){ obj = file_counter; }
 return obj; 
 }@Override
	public void setValue(String col,Object obj) {
 
if(col.equals( 
"id")){id= (Long)obj; }
else if (col.equals("unit_cnt")){unit_cnt= (Integer)obj; }
else if (col.equals("hd_cnt")){hd_cnt= (Integer)obj; }
else if (col.equals("row_num")){row_num= (Integer)obj; }
else if (col.equals("hd_id")){hd_id= (Long)obj; }
 else if (col.equals("user_id")){user_id= (String)obj; }
 else if (col.equals("view_yn")){view_yn= (String)obj; }
 else if (col.equals("title")){title= (String)obj; }
 else if (col.equals("update_dt")){update_dt= convertToLocalDate((Timestamp)obj) ;}
else if (col.equals("type_id")){type_id= (Long)obj; }
else if (col.equals("unit_id")){unit_id= (Long)obj; }
else if (col.equals("unit_nm")){unit_nm= (String)obj; }
 else if (col.equals("class_id")){class_id= (Long)obj; }
 else if (col.equals("subject_id")){subject_id= (Long)obj; }
 else if (col.equals("type_nm")){type_nm= (String)obj; }
 else if (col.equals("class_nm")){class_nm= (String)obj; }
 else if (col.equals("subject_nm")){subject_nm= (String)obj; }
 else if (col.equals("div_id")){div_id= (Long)obj; }
 else if (col.equals("div_nm")){div_nm= (String)obj; }
 else if (col.equals("visible_yn")){visible_yn= (String)obj; }
 else if (col.equals("context")){context= (String)obj; }
 else if (col.equals("hdcontext")){hdcontext= (String)obj; }
else if (col.equals("down_counter")){down_counter= (Integer)obj; }
else if (col.equals("file_counter")){file_counter= (Integer)obj; }
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