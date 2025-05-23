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
public class pds_file_hd implements SysEntity{ 
	public pds_file_hd() {
		//System.out.println("Entity");
	}
 public pds_file_hd(pds_file_hd copy) {
	 row_num= copy.row_num;
	 id= copy.id;
	 user_id= copy.user_id;
	 title= copy.title;
	 update_dt= copy.update_dt;
	 file_counter= copy.file_counter;
	 down_counter= copy.down_counter;
	 grade_cd= copy.grade_cd;
	 type_id= copy.type_id;
	 class_id= copy.class_id;
	 subject_id= copy.subject_id;
	 unit_id= copy.unit_id;
	 type_nm= copy.type_nm;
	 class_nm= copy.class_nm;
	 subject_nm= copy.subject_nm;
	 unit_nm= copy.unit_nm;
	 memo= copy.memo;
	 cov_yn= copy.cov_yn;
	 nick_name= copy.nick_name;
	modify = copy.modify;
} 
 public pds_file_hd(
		 Long _id,
		 Integer _row_num,
		 String _user_id,
		 String _title,
		 String _memo,
		 LocalDate _update_dt,
		 Integer _down_counter,
		 Integer _file_counter,
		 Long _type_id,
		 Long _class_id,
		 Long _subject_id,
		 Long _unit_id,
		 String _type_nm,
		 String _class_nm,
		 String _subject_nm,
		 String _unit_nm,
		 String _grade_cd,
		 String _cov_yn,
		 String _nick_name		 
		 ) {
	 row_num= _row_num;
	 id= _id;
	 user_id= _user_id;
	 title= _title;
	 update_dt= _update_dt;
	 file_counter= _file_counter;
	 down_counter= _down_counter;
	 grade_cd= _grade_cd;
	 type_id= _type_id;
	 class_id= _class_id;
	 subject_id= _subject_id;
	 unit_id= _unit_id;
	 type_nm= _type_nm;
	 class_nm= _class_nm;
	 subject_nm= _subject_nm;
	 unit_nm= _unit_nm;
	 memo= _memo;
	 cov_yn= _cov_yn;
	 nick_name= _nick_name;
} 

@Override
public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        pds_file_hd other = (pds_file_hd) obj;
        return
        		Objects.equals(
id,other.id)&&
        		Objects.equals(row_num,other.row_num)&&
Objects.equals(user_id,other.user_id)&&
Objects.equals(title,other.title)&&
Objects.equals(update_dt,other.update_dt)&&
Objects.equals(grade_cd,other.grade_cd)&&
Objects.equals(type_id,other.type_id)&&
Objects.equals(class_id,other.class_id)&&
Objects.equals(subject_id,other.subject_id)&&
Objects.equals(unit_id,other.unit_id)&&
Objects.equals(type_nm,other.type_nm)&&
Objects.equals(class_nm,other.class_nm)&&
Objects.equals(subject_nm,other.subject_nm)&&
Objects.equals(unit_nm,other.unit_nm)&&
Objects.equals(memo,other.memo)&&
Objects.equals(cov_yn,other.cov_yn)&&
Objects.equals(nick_name,other.nick_name)&&
Objects.equals(file_counter,other.file_counter)&&
Objects.equals(down_counter,other.down_counter);
}
 
 private Long id;
 private Integer row_num;
 private String user_id;
 private String title;
 private String memo;
 private LocalDate update_dt;
 private Integer down_counter;
 private Integer file_counter;
 private Long type_id;
 private Long class_id;
 private Long subject_id;
 private Long unit_id;
 private String type_nm;
 private String class_nm;
 private String subject_nm;
 private String unit_nm;
 private String grade_cd;
 private String cov_yn;
 private String nick_name;

 
public Long getId() { return id;}
public Integer get_row_num() { return row_num;}
public String get_user_id() { return user_id;}
public String get_title() { return title;}
public LocalDate get_update_dt() { return update_dt;}
public Integer get_down_count() { return down_counter;}
public Integer get_file_count() { return file_counter;}
public String get_grade_cd() { return grade_cd;}
public Long get_type_id() { return type_id;}
public Long get_class_id() { return class_id;}
public Long get_subject_id() { return subject_id;}
public Long get_unit_id() { return unit_id;}
public String get_type_nm() { return type_nm;}
public String get_class_nm() { return class_nm;}
public String get_subject_nm() { return subject_nm;}
public String get_unit_nm() { return unit_nm;}
public String get_memo() { return memo;}
public String get_nick_name() { return nick_name;}
public String get_cov_yn() { return cov_yn;}

 
public void setId(Long id){ this.id=id;}
public void set_row_num(Integer row_num){ this.row_num=row_num;}
public void set_user_id(String user_id){ this.user_id=user_id;}
public void set_title(String title){ this.title=title;}
public void set_update_dt(LocalDate update_dt){ this.update_dt=update_dt;}
public void set_down_count(Integer down_counter){ this.down_counter=down_counter;}
public void set_file_count(Integer file_counter){ this.file_counter=file_counter;}
public void set_grade_cd(String grade_cd){ this.grade_cd=grade_cd;}
public void set_type_id(Long type_id){ this.type_id=type_id;}
public void set_class_id(Long class_id){ this.class_id=class_id;}
public void set_subject_id(Long subject_id){ this.subject_id=subject_id;}
public void set_unit_id(Long unit_id){ this.unit_id=unit_id;}
public void set_type_nm(String type_nm){ this.type_nm=type_nm;}
public void set_class_nm(String class_nm){ this.class_nm=class_nm;}
public void set_subject_nm(String subject_nm){ this.subject_nm=subject_nm;}
public void set_unit_nm(String unit_nm){ this.unit_nm=unit_nm;}
public void set_memo(String memo){ this.memo=memo;}
public void set_nick_name(String nick_name){ this.nick_name=nick_name;}
public void set_cov_yn(String cov_yn){ this.cov_yn=cov_yn;}

@Override
	public Object getValue(String col) {
    	Object obj = null;
    	if(col.equals(
"id")){ obj = id; }
    	 else if(col.equals("row_num")){ obj = row_num; }
 else if(col.equals("user_id")){ obj = user_id; }
 else if(col.equals("title")){ obj = title; }
 else if(col.equals("update_dt")){ obj = update_dt; }
 else if(col.equals("grade_cd")){ obj = grade_cd; }
 else if(col.equals("type_id")){ obj = type_id; }
 else if(col.equals("class_id")){ obj = class_id; }
 else if(col.equals("subject_id")){ obj = subject_id; }
 else if(col.equals("unit_id")){ obj = unit_id; }
 else if(col.equals("type_nm")){ obj = type_nm; }
 else if(col.equals("class_nm")){ obj = class_nm; }
 else if(col.equals("subject_nm")){ obj = subject_nm; }
 else if(col.equals("unit_nm")){ obj = unit_nm; }
 else if(col.equals("memo")){ obj = memo; }
 else if(col.equals("nick_name")){ obj = nick_name; }
 else if(col.equals("cov_yn")){ obj = cov_yn; }
else if(col.equals("down_counter")){ obj = down_counter; }
else if(col.equals("file_counter")){ obj = file_counter; }
 return obj; 
 }@Override
	public void setValue(String col,Object obj) {
 
if(col.equals( 
"id")){id= (Long)obj; }
else if (col.equals("row_num")){row_num= (Integer)obj; }
 else if (col.equals("user_id")){user_id= (String)obj; }
 else if (col.equals("title")){title= (String)obj; }
 else if (col.equals("update_dt")){update_dt= convertToLocalDate((Timestamp)obj) ;}
 else if (col.equals("grade_cd")){grade_cd= (String)obj; }
 else if (col.equals("type_id")){type_id= (Long)obj; }
 else if (col.equals("class_id")){class_id= (Long)obj; }
 else if (col.equals("subject_id")){subject_id= (Long)obj; }
 else if (col.equals("unit_id")){unit_id= (Long)obj; }
 else if (col.equals("type_nm")){type_nm= (String)obj; }
 else if (col.equals("class_nm")){class_nm= (String)obj; }
 else if (col.equals("subject_nm")){subject_nm= (String)obj; }
 else if (col.equals("unit_nm")){unit_nm= (String)obj; }
 else if (col.equals("memo")){memo= (String)obj; }
 else if (col.equals("nick_name")){nick_name= (String)obj; }
 else if (col.equals("cov_yn")){cov_yn= (String)obj; }
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