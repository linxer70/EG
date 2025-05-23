package egframe.frame.entity;

import jakarta.persistence.*;
import java.util.Collection;
import java.util.Optional;

import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

//@Entity
//@Table(name = "usr_master") // 데이터베이스 테이블 이름
@Getter @Setter // Lombok 사용
public class UsrMaster  {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // 식별자 필드
    //@Column(unique = true, nullable = false,name = "user_nm")
    private String user_nm;
    
    @Column(nullable = false)
    private String password;
    private String email;
    private String group_cd;
    private Long group_id;
    private String user_cd;
    private String modify;
    private String type_nm;
    private Long type_id;
    
    public UsrMaster() {
    }
    // 모든 필드를 포함한 생성자 (선택사항)
    public UsrMaster(String username, String password, Collection<String> roles) {
        this.user_nm = username;
        this.password = password;
        this.roles = roles;
    }    
    
	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
	public String getUserNm() {
		// TODO Auto-generated method stub
		return user_nm;
	}
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}
	public void setPassword(String bcryptPassword) {
		// TODO Auto-generated method stub
		password = bcryptPassword;
	}
	
	    
	    
	    @ElementCollection(fetch = FetchType.EAGER)
	    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
	    @Column(name = "role")
	    
	    private Collection<String> roles;
	    public Collection<String> getRoles() {
	        return roles;
	    }
		   public void setRoles(Collection<String> roles) {
		        this.roles = roles;
		    }
		   public String getEmail() {
			return email;
		   }
		   public void setEmail(String email) {
			this.email = email;
		   }
		   public String getModify() {
			return modify;
		   }
		   public void setModify(String modify) {
			this.modify = modify;
		   }
		   public String getGroup_cd() {
			return group_cd;
		   }
		   public void setGroup_cd(String group_cd) {
			this.group_cd = group_cd;
		   }
		   public String getUser_cd() {
			return user_cd;
		   }
		   public void setUser_cd(String user_cd) {
			this.user_cd = user_cd;
		   }
		   public Long getGroup_id() {
			return group_id;
		   }
		   public void setGroup_id(Long group_id) {
			this.group_id = group_id;
		   }
		   public String getUser_nm() {
			return user_nm;
		   }
		   public void setUser_nm(String user_nm) {
			this.user_nm = user_nm;
		   }
		   public String getType_nm() {
			return type_nm;
		   }
		   public void setType_nm(String type_nm) {
			this.type_nm = type_nm;
		   }
		   public Long getType_id() {
			return type_id;
		   }
		   public void setType_id(Long type_id) {
			this.type_id = type_id;
		   }
}
