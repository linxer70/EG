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
public class exm_mat_pass_list implements SysEntity{ 
	public Long id;
	public String passed_nm;
	public String context;
	public String update_id;
	public LocalDate update_dt;
	public Long type_id;
	public Long class_id;
	public Long subject_id;
	public Long unit_id;
	public String type_nm;
	public String class_nm;
	public String subject_nm;
	public String div_cd;
	public String div_nm;
	public String exam_type;
	public Integer num_no;
	public Integer time_limit;
	public Integer tot_num;
	public Integer hd_no;
	public Integer list_no;
	public boolean chk;
	public boolean selected;
	
	public exm_mat_pass_list() {
		//System.out.println("Entity");
	}
 public exm_mat_pass_list(exm_mat_pass_list copy) {
	 num_no= copy.num_no;
	 id= copy.id;
	 hd_no= copy.hd_no;
	 list_no= copy.list_no;
	 type_id= copy.type_id;
	 class_id= copy.class_id;
	 subject_id= copy.subject_id;
	 type_nm= copy.type_nm;
	 class_nm= copy.class_nm;
	 subject_nm= copy.subject_nm;
	 update_id= copy.update_id;
	 update_dt= copy.update_dt;
	 tot_num= copy.tot_num;
	 exam_type= copy.exam_type;
	 unit_id= copy.unit_id;
	 div_nm= copy.div_nm;
	 div_cd= copy.div_cd;
	 context= copy.context;
	 modify = copy.modify;
	 chk = copy.chk;
} 
@Override
public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        exm_mat_pass_list other = (exm_mat_pass_list) obj;
        return
        		Objects.equals(
        				id,other.id)&&
        		Objects.equals(num_no,other.num_no)&&
        		Objects.equals(update_id,other.update_id)&&
Objects.equals(time_limit,other.time_limit)&&
Objects.equals(passed_nm,other.passed_nm)&&
Objects.equals(update_dt,other.update_dt)&&
Objects.equals(exam_type,other.exam_type)&&
Objects.equals(type_id,other.type_id)&&
Objects.equals(class_id,other.class_id)&&
Objects.equals(subject_id,other.subject_id)&&
Objects.equals(type_nm,other.type_nm)&&
Objects.equals(unit_id,other.unit_id)&&
Objects.equals(tot_num,other.tot_num)&&
Objects.equals(class_nm,other.class_nm)&&
Objects.equals(subject_nm,other.subject_nm)&&
Objects.equals(div_nm,other.div_nm)&&
Objects.equals(div_cd,other.div_cd)&&
Objects.equals(context,other.context)&&
Objects.equals(chk,other.chk)&&
Objects.equals(list_no,other.list_no)&&
Objects.equals(hd_no,other.hd_no);
}
 

 
public Long getId() { return id;}
public String get_update_id() { return update_id;}
public Integer get_tot_num() { return tot_num;}
public Integer get_num_no() { return num_no;}
public Integer get_time_limit() { return time_limit;}
public String get_passed_nm() { return passed_nm;}
public LocalDate get_update_dt() { return update_dt;}
public Integer get_hd_no() { return hd_no;}
public Integer get_list_no() { return list_no;}
public Long get_unit_id() { return unit_id;}
public Long get_type_id() { return type_id;}
public Long get_class_id() { return class_id;}
public Long get_subject_id() { return subject_id;}
public String get_type_nm() { return type_nm;}
public String get_class_nm() { return class_nm;}
public String get_subject_nm() { return subject_nm;}
public String get_exam_type() { return exam_type;}
public String get_div_nm() { return div_nm;}
public String get_div_cd() { return div_cd;}
public String get_context() { return context;}
public boolean get_chk() { return chk;}

 
public void setId(Long id){ this.id=id;}
public void set_update_id(String update_id){ this.update_id=update_id;}
public void set_tot_num(Integer tot_num){ this.tot_num=tot_num;}
public void set_num_no(Integer num_no){ this.num_no=num_no;}
public void set_time_limit(Integer time_limit){ this.time_limit=time_limit;}
public void set_passed_nm(String passed_nm){ this.passed_nm=passed_nm;}
public void set_update_dt(LocalDate update_dt){ this.update_dt=update_dt;}
public void set_hd_no(Integer hd_no){ this.hd_no=hd_no;}
public void set_list_no(Integer list_no){ this.list_no=list_no;}
public void set_unit_id(Long unit_id){ this.unit_id=unit_id;}
public void set_type_id(Long type_id){ this.type_id=type_id;}
public void set_class_id(Long class_id){ this.class_id=class_id;}
public void set_subject_id(Long subject_id){ this.subject_id=subject_id;}
public void set_type_nm(String type_nm){ this.type_nm=type_nm;}
public void set_class_nm(String class_nm){ this.class_nm=class_nm;}
public void set_subject_nm(String subject_nm){ this.subject_nm=subject_nm;}
public void set_exam_type(String exam_type){ this.exam_type=exam_type;}
public void set_div_nm(String div_nm){ this.div_nm=div_nm;}
public void set_div_cd(String div_cd){ this.div_cd=div_cd;}
public void set_context(String context){ this.context=context;}
public void set_chk(boolean chk){ this.chk=chk;}

@Override
	public Object getValue(String col) {
    	Object obj = null;
    	if(col.equals(
"id")){ obj = id; }
   	 else if(col.equals("update_id")){ obj = update_id; }
    	 else if(col.equals("num_no")){ obj = num_no; }
    	 else if(col.equals("tot_num")){ obj = tot_num; }
 else if(col.equals("time_limit")){ obj = time_limit; }
 else if(col.equals("passed_nm")){ obj = passed_nm; }
 else if(col.equals("update_dt")){ obj = update_dt; }
 else if(col.equals("type_id")){ obj = type_id; }
 else if(col.equals("unit_id")){ obj = unit_id; }
 else if(col.equals("class_id")){ obj = class_id; }
 else if(col.equals("subject_id")){ obj = subject_id; }
 else if(col.equals("type_nm")){ obj = type_nm; }
 else if(col.equals("class_nm")){ obj = class_nm; }
 else if(col.equals("subject_nm")){ obj = subject_nm; }
 else if(col.equals("exam_type")){ obj = exam_type; }
 else if(col.equals("div_nm")){ obj = div_nm; }
 else if(col.equals("div_cd")){ obj = div_cd; }
 else if(col.equals("context")){ obj = context; }
else if(col.equals("hd_no")){ obj = hd_no; }
else if(col.equals("list_no")){ obj = list_no; }
else if(col.equals("chk")){ obj = chk; }
 return obj; 
 }@Override
	public void setValue(String col,Object obj) {
 
if(col.equals( 
"id")){id= (Long)obj; }
else if (col.equals("tot_num")){tot_num= (Integer)obj; }
else if (col.equals("num_no")){num_no= (Integer)obj; }
else if (col.equals("update_id")){update_id= (String)obj; }
 else if (col.equals("time_limit")){time_limit= (Integer)obj; }
 else if (col.equals("passed_nm")){passed_nm= (String)obj; }
 else if (col.equals("update_dt")){update_dt= convertToLocalDate((Timestamp)obj) ;}
else if (col.equals("type_id")){type_id= (Long)obj; }
else if (col.equals("unit_id")){unit_id= (Long)obj; }
 else if (col.equals("class_id")){class_id= (Long)obj; }
 else if (col.equals("subject_id")){subject_id= (Long)obj; }
 else if (col.equals("type_nm")){type_nm= (String)obj; }
 else if (col.equals("class_nm")){class_nm= (String)obj; }
 else if (col.equals("subject_nm")){subject_nm= (String)obj; }
 else if (col.equals("exam_type")){exam_type= (String)obj; }
 else if (col.equals("div_nm")){div_nm= (String)obj; }
 else if (col.equals("div_cd")){div_cd= (String)obj; }
 else if (col.equals("context")){context= (String)obj; }
 else if (col.equals("chk")){chk= (boolean)obj; }
else if (col.equals("hd_no")){hd_no= (Integer)obj; }
else if (col.equals("list_no")){list_no= (Integer)obj; }
 ; 
 }	private String modify;
	public String getModify() {        return modify;    }
    public void setModify(String groupcd) {        this.modify = groupcd;    }
	@Override
	public boolean isSelected() {
        return selected;
	}
	@Override
	public void setSelected(boolean _selected) {
		 selected = _selected;
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