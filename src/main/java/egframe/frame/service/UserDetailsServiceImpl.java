package egframe.frame.service;

import egframe.frame.entity.Member;
import egframe.frame.entity.UsrMaster;
import egframe.frame.entity.sys_user;
import egframe.frame.service.SysDBO;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexDirection;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinResponse;
import com.vaadin.flow.spring.security.AuthenticationContext;

import jakarta.servlet.http.Cookie;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import egframe.iteach4u.service.Repository.Iteach4uMappingClass;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private JdbcTemplate Jdbc;	
	public SysDBO sysuserRepository;
	public Set<GrantedAuthority> authorities;
    public  sys_user sysuser;
    public UsrMaster ustmaster;
    public  AuthenticationContext userDetailsService;
    public UserDetails userdetails ;
    public AuthenticatedUser authuser;
    public UserDetailsServiceImpl(SysDBO sysuserRepository,AuthenticationContext userDetailsService,JdbcTemplate jdbcTemplate ){
       this.sysuserRepository = sysuserRepository;
		this.authorities = null;
		this.sysuser = new sys_user();
		this.userDetailsService = userDetailsService;
		Jdbc = jdbcTemplate;
    }
    @Transactional
    public UserDetails loadUserByUsername(String user_cd) throws UsernameNotFoundException {
    	
    	try {
    	    Thread.sleep(200); // 200ms 대기
    	} catch (InterruptedException e) {
    	    Thread.currentThread().interrupt();
    	}
    	
    	Optional<UserDetails> authenticatedUser = userDetailsService.getAuthenticatedUser(UserDetails.class);
    	if (authenticatedUser.isPresent()) {
    	    UserDetails userDetails = authenticatedUser.get();
    	    String username = userDetails.getUsername();
    	    //object = Optional.ofNullable(userInfo);
    	} else {
    	    //object = Optional.empty();
    	}     	
    	//userdetails= authuser.getAuthenticatedUser();
    	List<UsrMaster> usermater = getUserInfo(user_cd);
    	//ustmaster = getUserInfo(user_cd);
        UserDetails  rtn = null ;
        String encodedPassword = null ;
        if(usermater.size()!=0) {
            encodedPassword = usermater.get(0).getPassword();
        }
        // 데이터베이스에서 가져온 암호화된 비밀번호 사용
       BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
       String _pass = encoder.encode("0000");
       if(usermater.size()==0) {
        	List<Member> member = findByNo(user_cd);
        	if(member.get(0).getPwd()==null) {
        		return null;
        	}
            String md5encodedPassword = member.get(0).getPwd();
            if(md5encodedPassword!=null) {
            	/*
            	String pass = sysuser.getPasswordSalt();
            	if (!compareWithMD5(pass,md5encodedPassword)) {
                System.out.println("잘못된 비밀번호입니다.");
                return rtn;
            	}else {asdfasdfasdf
            	 */  
                String sql = "insert into usr_master (user_cd,user_nm,password,group_cd) values (?,?,?,'user')";
                int updated = Jdbc.update(sql,user_cd,user_cd,_pass.toString());
                System.out.println("insert된 행 수: " + updated);

                encodedPassword =_pass;
                sysuser.setGroupCd("USER");
                sysuser.setPassword(_pass);
            	//}
                //DialogBasic();
                //createDialogLayout();
            }else {
            	throw new UsernameNotFoundException("No user present with username: " + user_cd);
            }
        
       }
        // 권한 설정
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new CustomAuthority("ROLE_"+sysuser.getGroupCd()));
        System.out.println("userCd         : [" + user_cd + "]");
        System.out.println("DB password    : [" + encodedPassword+ "]");
        System.out.println("Authorities    : " + authorities);
        if(usermater.size()!=0) {
            rtn = new org.springframework.security.core.userdetails.User(
                    user_cd,  // 사용자 아이디
                    encodedPassword,     // 데이터베이스에서 가져온 암호화된 비밀번호
                    authorities          // 권한
                    );
        	
        }else {
            rtn = new org.springframework.security.core.userdetails.User(
                    user_cd,  // 사용자 아이디
                    _pass,     // 데이터베이스에서 가져온 암호화된 비밀번호
                    authorities          // 권한
                    );
        	
        }
        /*
        String password = "dikafryo";  // 원본 비밀번호
        
        // bcrypt로 비밀번호 해싱
        String hashedPassword1 = encoder.encode(password);
        
        // 해시 출력
        System.out.println("bcrypt 해시 값: " + hashedPassword1);
        
        String originalPassword = "dikafryo";
        String hashedPassword = "$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe"; // 제공된 해시

        BCryptPasswordEncoder encoder1 = new BCryptPasswordEncoder();
        if (encoder1.matches(originalPassword, hashedPassword)) {
            System.out.println("비밀번호가 일치합니다.");
        } else {
            System.out.println("비밀번호가 일치하지 않습니다.");
        }        
        */
        return  rtn ;
    }
    
    public List<Member> findByNo(String userid) {
        String sql = "SELECT no, userid, pwd FROM member where userid = ?";

        return Jdbc.query(sql, new RowMapper<Member>() {
            @Override
            public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Member(
                    rs.getLong("no"),
                    rs.getString("userid"),
                    rs.getString("pwd")
                );
            }
        },userid);
    }    
    public List<UsrMaster> getUserInfo(String userid) {
        String sql = "SELECT id, user_cd, user_nm,password,group_cd,type_id FROM usr_master where user_cd = ?";
        	return Jdbc.query(sql,new RowMapper<UsrMaster>(){
        		    @Override
        		    public UsrMaster mapRow(ResultSet rs, int rowNum) throws SQLException {
        		    	UsrMaster m = new UsrMaster();
        		        m.setId(rs.getLong("id"));
        		        m.setGroup_cd(rs.getString("group_cd"));
        		        //m.setType_nm(rs.getString("type_nm"));
        		        m.setType_id(rs.getLong("type_id"));
        		        m.setUser_cd(rs.getString("user_cd"));
        		        m.setUser_nm(rs.getString("user_nm"));
        		        m.setPassword(rs.getString("password"));
        		        return m;
        		    };
        	},userid 	);	
    }
	private static VerticalLayout createDialogLayout() {

        TextField firstNameField = new TextField("First name");
        TextField lastNameField = new TextField("Last name");

        VerticalLayout dialogLayout = new VerticalLayout(firstNameField,
                lastNameField);
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");

        return dialogLayout;
    }

    public void  DialogBasic() {
    	
    	
    	HorizontalLayout layout = new HorizontalLayout();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        Button delbutton = new Button("삭제");
        Button exitbutton = new Button("취소");
        Button msg = new Button("데이터를 삭제하시겠습니까?");
    	FlexLayout popupText = new FlexLayout();
    	FlexLayout popuplayout = new FlexLayout();
    	Div popup = new Div();
        
        popuplayout.setFlexDirection(FlexDirection.COLUMN);
        popuplayout.getStyle().set("border", "1px solid black");
    	delbutton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    	exitbutton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delbutton.addClickListener(event -> {
        	layout.remove(delbutton);
        	layout.remove(exitbutton);
        	popupText.remove(msg);
//        	remove(popup);
        });
        exitbutton.addClickListener(event -> {
        	layout.remove(delbutton);
        	layout.remove(exitbutton);
        	popupText.remove(msg);
//        	remove(popup);
        });

        
        layout.add(delbutton, exitbutton);
        popupText.add(msg);
        popuplayout.add(popupText,layout);
        popup.add(popuplayout);
        //add(popup);
        // Center the button within the example
        popup.getStyle().set("position", "fixed").set("top", "0").set("right", "0")
                .set("bottom", "0").set("left", "0").set("display", "flex")
                .set("align-items", "center").set("justify-content", "center");
    }
    
    private Collection<? extends GrantedAuthority> getAuthorities(String user_cd) {
		return null;
	}
    
    public sys_user getSysuser(){
    	return this.sysuser;
    }
    
    public String getUsernm(){
    	return sysuser.getUserNm();
    }
    public static boolean compareWithMD5(String input, String md5Hash) {
    	try { 
    		MessageDigest md = MessageDigest.getInstance("MD5"); 
    		byte[] inputBytes = input.getBytes(); 
    		byte[] hashBytes = md.digest(inputBytes); 
    		StringBuilder sb = new StringBuilder(); 
    		for (byte b : hashBytes) { 
    			sb.append(String.format("%02x", b)); 
    			} 
    		return sb.toString().equals(md5Hash); 
    		} 
    	catch (NoSuchAlgorithmException e) { 
    		e.printStackTrace(); return false; } 
    	}
    }

class CustomAuthority implements GrantedAuthority {
	private static final long serialVersionUID = 1L;
	private String authority;

    public CustomAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}

