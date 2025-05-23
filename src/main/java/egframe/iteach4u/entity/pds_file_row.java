package egframe.iteach4u.entity;
import egframe.common.SysEntity;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

import com.vaadin.flow.component.button.Button;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
public class pds_file_row implements SysEntity{ 
	public pds_file_row() {
		//System.out.println("Entity");
	}
 public pds_file_row(pds_file_row copy) {
	 id= copy.id;
	 row= copy.row;
	 row_num= copy.row_num;
	 display_name= copy.display_name;
	 save_name= copy.save_name;
	 save_path= copy.save_path;
	 cov_yn= copy.cov_yn;
	 update_dt= copy.update_dt;
	 down_counter= copy.down_counter;
	 modify = copy.modify;
} 

 public pds_file_row(
		  Long _id,
		  Long _row,
		  Integer _row_num,
		  String _save_name,
		  String _cov_yn,
		  String _save_path,
		  String _display_name,
		  LocalDate _update_dt,
		  Integer _down_counter,
		  Button _download		 
		 ) {
	 id= _id;
	 row= _row;
	 row_num= _row_num;
	 display_name= _display_name;
	 save_name= _save_name;
	 save_path= _save_path;
	 cov_yn= _cov_yn;
	 update_dt= _update_dt;
	 down_counter= _down_counter;
} 
 
@Override
public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        pds_file_row other = (pds_file_row) obj;
        return
        		Objects.equals(
id,other.id)&&
        		Objects.equals(row,other.row)&&
        		Objects.equals(row_num,other.row_num)&&
        		Objects.equals(display_name,other.display_name)&&
Objects.equals(save_name,other.save_name)&&
Objects.equals(cov_yn,other.cov_yn)&&
Objects.equals(save_path,other.save_path)&&
Objects.equals(update_dt,other.update_dt)&&
Objects.equals(down_counter,other.down_counter);
}
 
 private Long id;
 private Long row;
 private Integer row_num;
 private String save_name;
 private String cov_yn;
 private String save_path;
 private String display_name;
 private LocalDate update_dt;
 private Integer down_counter;
 private Button download;
 
public Long getId() { return id;}
public Long get_row() { return row;}
public Integer get_row_num() { return row_num;}
public String get_display_name() { return display_name;}
public String get_save_name() { return save_name;}
public String get_cov_yn() { return cov_yn;}
public String get_save_path() { return save_path;}
public LocalDate get_update_dt() { return update_dt;}
public Integer get_down_count() { return down_counter;}

 
public void setId(Long id){ this.id=id;}
public void set_row(Long row){ this.row=row;}
public void set_row_num(Integer row_num){ this.row_num=row_num;}
public void set_display_name(String display_name){ this.display_name=display_name;}
public void set_save_name(String save_name){ this.save_name=save_name;}
public void set_cov_yn(String cov_yn){ this.cov_yn=cov_yn;}
public void set_save_path(String save_path){ this.save_path=save_path;}
public void set_update_dt(LocalDate update_dt){ this.update_dt=update_dt;}
public void set_down_count(Integer down_counter){ this.down_counter=down_counter;}

@Override
	public Object getValue(String col) {
    	Object obj = null;
    	if(col.equals(
"id")){ obj = id; }
   	 else if(col.equals("row_num")){ obj = row_num; }
    	 else if(col.equals("row")){ obj = row; }
    	 else if(col.equals("display_name")){ obj = display_name; }
 else if(col.equals("save_name")){ obj = save_name; }
 else if(col.equals("cov_yn")){ obj = cov_yn; }
    	 else if(col.equals("save_path")){ obj = save_path; }
 else if(col.equals("update_dt")){ obj = update_dt; }
    	 else if(col.equals("down_counter")){ obj = down_counter; }
    	 else if(col.equals("download")){ obj = download; }
 return obj; 
 }@Override
	public void setValue(String col,Object obj) {
 
if(col.equals( 
"id")){id= (Long)obj; }
else if (col.equals("row_num")){row_num= (Integer)obj; }
else if (col.equals("row")){row= (Long)obj; }
else if (col.equals("display_name")){display_name= (String)obj; }
 else if (col.equals("save_name")){save_name= (String)obj; }
 else if (col.equals("cov_yn")){cov_yn= (String)obj; }
else if (col.equals("save_path")){save_path= (String)obj; }
 else if (col.equals("update_dt")){update_dt= convertToLocalDate((Timestamp)obj) ;}
 else if (col.equals("down_counter")){down_counter= (Integer)obj; }
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