package egframe.frame.entity;


import egframe.frame.entity.UserMenuData;

import jakarta.persistence.Column;

public class UserMenu  {
	
	
	private String user_cd;
	
	private String module_cd;
	
	private String module_nm;
	private String program_cd;
   
	private String program_nm;
	
	private String icon_nm;

	private String parent_yn;
	@Column(columnDefinition = "varchar(1)")
	private String menu_knd;

	private UserMenu parent ; 

    public UserMenu(String user_cd,String module_cd,String program_cd,String program_nm,String icon_nm,String parent_yn,String menu_knd,UserMenu parent) {
        this.user_cd = user_cd;
        this.module_cd = module_cd;
        this.program_cd = program_cd;
        this.program_nm = program_nm;
        this.icon_nm = icon_nm;
        this.parent_yn = parent_yn;
        this.menu_knd = menu_knd;
        this.parent = parent;
    }	
	
    public UserMenu() {
		// TODO Auto-generated constructor stub
	}

	public String getModuleCd() {
        return module_cd;
    } 
    public void setModuleCd(String moduel_cd) {
        this.module_cd = module_cd;
    }
	public String getModuleNm() {
        return module_nm;
    } 
    public void setModuleNm(String moduel_cd) {
        this.module_nm = module_cd;
    }
	public String getUserCd() {
        return user_cd;
    } 
    public void setUserCd(String moduel_cd) {
        this.user_cd = module_cd;
    }
    public String getProgramCd() {
        return program_cd;
    }
    public void setProgramCd(String program_cd) {
        this.program_cd = program_cd;
    }
    public String getProgramNm() {
	        return program_nm;
    }
    public void setProgramNm(String program_nm) {
	        this.program_nm = program_nm;
    }
   public String getParentYn() {
        return parent_yn;
    }
    public void setParentYn(String programNm) {
        this.parent_yn = programNm;
    }
   public String getIconNm() {
        return icon_nm;
    }
    public void setIconNm(String icon_nm) {
        this.icon_nm = icon_nm;
    }
    public String getMenuKnc() {
        return menu_knd;
    }
    public void setMenuKnd(String menu_knd) {
        this.menu_knd = menu_knd;
    }
	public Object getParentCd() {
		return parent;
	}
    public void setParentCd(UserMenu parent) {
        this.parent = parent;
    }
    
}
