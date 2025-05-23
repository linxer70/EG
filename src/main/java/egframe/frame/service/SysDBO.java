package egframe.frame.service;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vaadin.flow.component.notification.Notification;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import egframe.frame.entity.sys_country_cd;
import egframe.frame.entity.sys_module;
import egframe.frame.entity.sys_user;
import egframe.frame.entity.sys_user_menu;
@Service
public class SysDBO  implements Serializable,DBServiceImpl  {
	private static final long serialVersionUID = 8963934765838338229L;
	public   JdbcTemplate con = new JdbcTemplate();
	public   DataSource datasource = null;
 	public   DataSource sys_ds = null;
 	public   JdbcTemplate sys_con = new JdbcTemplate();
	public   String user_cd = "";
	public   String pass = "";
	public   String db_name = "";
	public ResultSet rs = null;
	public PreparedStatement preparedStatement= null;
	public Connection connection = null;
	
	@Autowired
	public SysDBO() {
		sys_ds =DataSourceBuilder.create()
			   .url("jdbc:postgresql://linxer.tplinkdns.com:54322/teach4u")
			   .password("qazxsw70")
			   .username("postgres")
			    .driverClassName("org.postgresql.Driver")
			    .type(HikariDataSource.class)  // Type을 명시
			    .build();
		sys_con = new JdbcTemplate(sys_ds);
		con = sys_con;
		datasource = sys_ds;
		System.out.println("DB USER = "+user_cd);
		System.out.println("DBNAME = "+db_name);
		
	}

	@Override
	public void setConnection(String dbname) {
		db_name = dbname;
		datasource =DataSourceBuilder.create()
				//.url("jdbc:postgresql://new.iteach4u.kr:5433/"+db_name)
				//.password("qazxsw")
			    .url("jdbc:postgresql://linxer.tplinkdns.com:54322/"+db_name)
			    .password("qazxsw70")
			    .username("postgres")
			    .driverClassName("org.postgresql.Driver")
			    .build();
		con = new JdbcTemplate(datasource);
	}

	@Override
	public String getDbName() {
		return db_name;
	}

	@Override
	public JdbcTemplate getCon() {
		return this.con;
	}

	@Override
	public DataSource getDataSource() {
		return this.datasource;
	}

	@Override
	public JdbcTemplate getSysConnection() {
		return sys_con;
	}

	public sys_user getUserInfo(String username) {
		String sqlsyntax =  "SELECT * FROM usr_master where user_cd = ? ";
		rs = null;
		preparedStatement= null;
		connection = null;
		sys_user user = new sys_user();
		int sysColumnDic = 0 ;
		try {
			connection = con.getDataSource().getConnection();
			preparedStatement = connection.prepareStatement(sqlsyntax);
			preparedStatement.setString(1,username);
//			System.out.println("실행 쿼리: " + preparedStatement.toString());  // 로깅
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				
				user.setGroupCd(rs.getString("group_cd"));
				user.setUserCd(rs.getString("user_cd"));
				//System.out.println("Login 2 = "+rs.getString("user_cd"));
				user.setUserNm(rs.getString("user_nm"));
				user.setPassword(rs.getString("password"));
				user.setMd5_pass(rs.getString("md5_pass"));
				user.setPasswordSalt(rs.getString("password_salt"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
		    try {
		        if (rs != null) {
		            rs.close();
		        }
		        if (preparedStatement != null) {
		        	preparedStatement.close();
		        }
		        if (connection != null) {
		            connection.close();
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		}
        return user;
	}

	public sys_user getSysUser(String username) {
	   	this.user_cd = username;
    	this.pass = pass;
        return getUserInfo(this.user_cd);
 	}

	public List<sys_country_cd> getCountryCd() {
		String sqlsyntax = "SELECT country_cd ,country_nm FROM sys_country_cd order by country_cd  ";
		List<sys_country_cd> list = new ArrayList<>();
		try {
			connection = con.getDataSource().getConnection();
			preparedStatement = connection.prepareStatement(sqlsyntax);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				sys_country_cd module = new sys_country_cd();
				module.set_country_cd(rs.getString("country_cd"));
				module.set_country_nm(rs.getString("country_nm"));
				list.add(module);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
		    try {
		        if (rs != null) {
		            rs.close();
		        }
		        if (preparedStatement != null) {
		        	preparedStatement.close();
		        }
		        if (connection != null) {
		            connection.close();
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		}
		return list;
		}

	public List<sys_module> getModule(String country_cd) {
		String sqlsyntax = "SELECT module_id,module_nm FROM usy_module WHERE is_use = 'Y' and language_cd =? order by order_num";
		List<sys_module> list = new ArrayList<>();
	
		try {
			connection = con.getDataSource().getConnection();
			preparedStatement = connection.prepareStatement(sqlsyntax);
			preparedStatement.setString(1,country_cd);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				sys_module module = new sys_module();
				module.set_module_id(rs.getString("module_id"));
				module.set_module_nm(rs.getString("module_nm"));
				list.add(module);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
		    try {
		        if (rs != null) {
		            rs.close();
		        }
		        if (preparedStatement != null) {
		        	preparedStatement.close();
		        }
		        if (connection != null) {
		            connection.close();
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		}
		return list;
	}

	public List<sys_user_menu> getUserModuleProgramList(String user_cd, String module_cd) {
		String sqlsyntax =  "SELECT * FROM sys_user_menu where user_cd = ? and module_cd = ?";
		List<sys_user_menu> list = new ArrayList<>();
		try {
			connection = con.getDataSource().getConnection();
			preparedStatement = connection.prepareStatement(sqlsyntax);
			preparedStatement.setString(1,user_cd);
			preparedStatement.setString(2,module_cd);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				sys_user_menu program = new sys_user_menu();
				program.set_program_id(rs.getString("program_id"));
				program.set_program_nm(rs.getString("program_nm"));
				program.setMenuKnd(rs.getString("menu_knd"));
				list.add(program);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
		    try {
		        if (rs != null) {
		            rs.close();
		        }
		        if (preparedStatement != null) {
		        	preparedStatement.close();
		        }
		        if (connection != null) {
		            connection.close();
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		}
		return list;
	}
}