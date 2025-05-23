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
public class Member  {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long no; // 식별자 필드
    //@Column(unique = true, nullable = false,name = "user_nm")
    @Column(nullable = false)
    private String pwd;
    private String userid;
    
    public Member() {
    }
    // 모든 필드를 포함한 생성자 (선택사항)
    public Member(Long _no,String _userid,String _pwd) {
    	no = _no;
    	userid = _userid;
    	pwd = _pwd;
    }
    
	public Long getNo() {
        return no;
    }

    public void setNo(Long no) {
        this.no = no;
    }
	public String getUserId() {
		// TODO Auto-generated method stub
		return userid;
	}
	public String getPwd() {
		// TODO Auto-generated method stub
		return pwd;
	}
	public void setPwd(String bcryptPassword) {
		// TODO Auto-generated method stub
		pwd = bcryptPassword;
	}
	
}
