package egframe.frame.service;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
@Service
public interface DBServiceImpl   {
	void setConnection(String dbname);
	String getDbName();
	JdbcTemplate getCon();
	DataSource getDataSource();
	JdbcTemplate getSysConnection();
}
