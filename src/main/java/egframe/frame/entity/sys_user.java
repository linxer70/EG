package egframe.frame.entity;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

public class sys_user  {
	private Long id; // 식별자 필드
   private String user_cd;
    private String user_nm;
    private String password;
    private String md5_pass;
    private String group_cd;
    private String profile_picture;
    private String password_salt;
    private String biz_place_cd;
    private String dept_cd;
    private String dept_nm;
    private String posit_cd;
    private String posit_nm;
    private String psn_id;
    private LocalDate birthday;
    //private String birthday;
    private String nationality;
    private String language_cd;
    private String zip_code;
    private String address_full;
    private String address_1;
    private String address_2;
    private String address_3;
    private String address_e;
    private String cell_phone;
    private String phone_tel;
    private String fax_no;
    private String e_mail;
    private String using_permit_yn;
    private String using_permit;
    private String is_admin;
    private String start_dt;
    private String end_dt;
    private int colorIndex ;
    private String image;
    private String abbreviation;
    
    private String modify;
    public sys_user() {
    	}
    public sys_user(String _user_id,String _user_nm) {
    	user_cd = _user_id;
    	user_nm = _user_nm;
	}
    public sys_user(sys_user copy) {
    	user_cd = copy.user_cd;
    	user_nm = copy.user_nm;
    	password = copy.password;
    	md5_pass = copy.md5_pass;
    	group_cd = copy.group_cd;
    	profile_picture = copy.profile_picture;
    	password_salt = copy.password_salt;
    	birthday = copy.birthday;
    	colorIndex = copy.colorIndex;
    	modify = copy.modify;
    }
    
    protected sys_user(String userId, int colorIndex) {
        Objects.requireNonNull(userId, "Null user id isn't supported");
        this.user_cd = userId;
        this.colorIndex = colorIndex;
    }
    
	public String getModify() 				{	return modify;    			}
	public String getProfilePicture() 		{	return profile_picture;   			}
	public String getPasswordSalt() 		{	return password_salt;   			}
	public String getUserCd() 				{	return user_cd;   			}
    public String getUserNm() 				{	return user_nm;   			}
    public String getPassword() 			{	return password;    	}
    public String getMd5_pass() 			{	return md5_pass;    	}
    public String getGroupCd() 				{	return group_cd;			}
    public int getColorIndex() {
        return this.colorIndex;
    }
    public void setColorIndex(int colorIndex) {
        this.colorIndex = colorIndex;
    }
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        sys_user that = (sys_user)o;
        return Objects.equals(this.user_cd, that.user_cd);
    }

    public int hashCode() {
        return Objects.hash(this.id);
    }
    public String getImage() {
        return this.image;
    }

    public void setImage(String imageUrl) {
        this.image = imageUrl;
    }
    public String getAbbreviation() {
        return this.abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }


    public LocalDate getBirthday() 			{	return birthday;			}
    //public String getBirthday() 			{	return birthday;			}
    
    public void setPassword(String hashedPassword) {    	this.password = hashedPassword;   }
    public void setMd5_pass(String md5_pass) {    	this.md5_pass = md5_pass;   }
    public void setModify(String usercd) {
        this.modify = usercd;
    }
    public void setProfilePicture(String usercd) {
        this.profile_picture = usercd;
    }
    public void setPasswordSalt(String usercd) {
        this.password_salt = usercd;
    }
    public void setUserCd(String usercd) {
        this.user_cd = usercd;
    }
    public void setUserNm(String user_nm) {
        this.user_nm = user_nm;
    }
    public void setGroupCd(String group_cd) {
        this.group_cd = group_cd;
    }
    public void setBirthday(LocalDate birthday) {	this.birthday = birthday;    }
    //public void setBirthday(String birthday) {	this.birthday = birthday;    }

    public String getPropertyValue(String propertyName) {
        if ("user_cd".equals(propertyName)) {
            return user_cd;
        } else if ("user_nm".equals(propertyName)){
        	return user_nm;
        }
        return null;
    }    
    public String getComboDisplay() {
        return user_cd+" :: "+user_nm;
    }
	public Object getUsername() {
		// TODO Auto-generated method stub
		return null;
	}
	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
