package egframe.iteach4u.service.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import egframe.frame.entity.UsrMaster;
import egframe.iteach4u.entity.UsyConfigType;
import egframe.iteach4u.entity.exm_mat_make_hd;
import egframe.iteach4u.entity.exm_mat_make_list;
import egframe.iteach4u.entity.exm_mat_make_unit;
import egframe.iteach4u.entity.exm_mat_pass_hd;
import egframe.iteach4u.entity.exm_mat_pass_list;
import egframe.iteach4u.entity.exm_mat_pass_unit;
import egframe.iteach4u.entity.exm_mat_unit_hd;
import egframe.iteach4u.entity.exm_mat_unit_row;
import egframe.iteach4u.entity.exm_mat_unit_view;
import egframe.iteach4u.entity.pds_file_hd;
import egframe.iteach4u.entity.pds_file_row;
import egframe.iteach4u.entity.pds_past_hd;
import egframe.iteach4u.entity.pds_past_row;
import egframe.iteach4u.entity.usy_class_cd;
import egframe.iteach4u.entity.usy_div_cd;
import egframe.iteach4u.entity.usy_key_no;
import egframe.iteach4u.entity.usy_subject_cd;
import egframe.iteach4u.entity.usy_type_cd;
import egframe.iteach4u.entity.usy_unit_cd;

public class Iteach4uMappingClass {

}
/*
class PasMatMakeListMapper implements RowMapper<exm_mat_pass_list> {
    @Override
    public exm_mat_pass_list mapRow(ResultSet rs, int rowNum) throws SQLException {
    	exm_mat_pass_list module = new exm_mat_pass_list();
		module.setId(rs.getLong("id"));
		//module.set_view_id(rs.getLong("view_id"));
		//module.set_hd_id(rs.getLong("hd_id"));
		module.set_context(rs.getString("context"));
        return module;
    }
}
class PasMatMakeHdMapper implements RowMapper<exm_mat_pass_hd> {
    @Override
    public exm_mat_pass_hd mapRow(ResultSet rs, int rowNum) throws SQLException {
    	exm_mat_pass_hd module = new exm_mat_pass_hd();
		module.setId(rs.getLong("id"));
		//module.set_view_id(rs.getLong("view_id"));
		//module.set_div_id(rs.getLong("div_id"));
		module.set_type_id(rs.getLong("type_id"));
		module.set_class_id(rs.getLong("class_id"));
		module.set_subject_id(rs.getLong("subject_id"));
		module.set_unit_id(rs.getLong("unit_id"));
		//module.set_visible_yn(rs.getString("visible_yn"));
		module.set_context(rs.getString("context"));
        return module;
    }
}
class PasMatMakeUnitMapper implements RowMapper<exm_mat_pass_unit> {
    @Override
    public exm_mat_pass_unit mapRow(ResultSet rs, int rowNum) throws SQLException {
    	exm_mat_pass_unit module = new exm_mat_pass_unit();
		module.setId(rs.getLong("id"));
		//module.set_view_id(rs.getLong("view_id"));
		//module.set_div_id(rs.getLong("div_id"));
		module.set_type_id(rs.getLong("type_id"));
		module.set_class_id(rs.getLong("class_id"));
		module.set_subject_id(rs.getLong("subject_id"));
		module.set_unit_id(rs.getLong("unit_id"));
		//module.set_visible_yn(rs.getString("visible_yn"));
		module.set_context(rs.getString("context"));
        return module;
    }
}
class ExmMatMakeListMapper implements RowMapper<exm_mat_pass_list> {
    @Override
    public exm_mat_pass_list mapRow(ResultSet rs, int rowNum) throws SQLException {
    	exm_mat_pass_list module = new exm_mat_pass_list();
		module.setId(rs.getLong("id"));
		//module.set_view_id(rs.getLong("view_id"));
		//module.set_hd_id(rs.getLong("hd_id"));
		module.set_context(rs.getString("context"));
        return module;
    }
}
class ExmMatMakeHdMapper implements RowMapper<exm_mat_pass_hd> {
    @Override
    public exm_mat_pass_hd mapRow(ResultSet rs, int rowNum) throws SQLException {
    	exm_mat_pass_hd module = new exm_mat_pass_hd();
		module.setId(rs.getLong("id"));
		//module.set_view_id(rs.getLong("view_id"));
		//module.set_div_id(rs.getLong("div_id"));
		module.set_type_id(rs.getLong("type_id"));
		module.set_class_id(rs.getLong("class_id"));
		module.set_subject_id(rs.getLong("subject_id"));
		module.set_unit_id(rs.getLong("unit_id"));
		//module.set_visible_yn(rs.getString("visible_yn"));
		module.set_context(rs.getString("context"));
        return module;
    }
}
class ExmMatMakeUnitMapper implements RowMapper<exm_mat_pass_unit> {
    @Override
    public exm_mat_pass_unit mapRow(ResultSet rs, int rowNum) throws SQLException {
    	exm_mat_pass_unit module = new exm_mat_pass_unit();
		module.setId(rs.getLong("id"));
		//module.set_view_id(rs.getLong("view_id"));
		//module.set_div_id(rs.getLong("div_id"));
		module.set_type_id(rs.getLong("type_id"));
		module.set_class_id(rs.getLong("class_id"));
		module.set_subject_id(rs.getLong("subject_id"));
		module.set_unit_id(rs.getLong("unit_id"));
		//module.set_visible_yn(rs.getString("visible_yn"));
		module.set_context(rs.getString("context"));
        return module;
    }
}
*/
class RandomLongListMapper implements RowMapper<Long> {
    @Override
    public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
    	Long module =rs.getLong("id");
        return module;
    }
}

class PasMatPassListMapper implements RowMapper<exm_mat_pass_list> {
    @Override
    public exm_mat_pass_list mapRow(ResultSet rs, int rowNum) throws SQLException {
    	exm_mat_pass_list module = new exm_mat_pass_list();
		module.setId(rs.getLong("id"));
		//module.set_view_id(rs.getLong("view_id"));
		//module.set_hd_id(rs.getLong("hd_id"));
		module.set_context(rs.getString("context"));
        return module;
    }
}
class PasMatPassHdMapper implements RowMapper<exm_mat_pass_hd> {
    @Override
    public exm_mat_pass_hd mapRow(ResultSet rs, int rowNum) throws SQLException {
    	exm_mat_pass_hd module = new exm_mat_pass_hd();
		module.setId(rs.getLong("id"));
		//module.set_view_id(rs.getLong("view_id"));
		//module.set_div_id(rs.getLong("div_id"));
		module.set_type_id(rs.getLong("type_id"));
		module.set_class_id(rs.getLong("class_id"));
		module.set_subject_id(rs.getLong("subject_id"));
		module.set_unit_id(rs.getLong("unit_id"));
		//module.set_visible_yn(rs.getString("visible_yn"));
		module.set_context(rs.getString("context"));
        return module;
    }
}
class ExmMatPassUnitMapper implements RowMapper<exm_mat_pass_unit> {
    @Override
    public exm_mat_pass_unit mapRow(ResultSet rs, int rowNum) throws SQLException {
    	exm_mat_pass_unit module = new exm_mat_pass_unit();
		module.setId(rs.getLong("id"));
		//module.set_view_id(rs.getLong("view_id"));
		//module.set_div_id(rs.getLong("div_id"));
		module.set_type_id(rs.getLong("type_id"));
		module.set_class_id(rs.getLong("class_id"));
		module.set_subject_id(rs.getLong("subject_id"));
		module.set_unit_id(rs.getLong("unit_id"));
		//module.set_visible_yn(rs.getString("visible_yn"));
		module.set_context(rs.getString("context"));
        return module;
    }
}
class ExmMatMakeListMapper implements RowMapper<exm_mat_make_list> {
    @Override
    public exm_mat_make_list mapRow(ResultSet rs, int rowNum) throws SQLException {
    	exm_mat_make_list module = new exm_mat_make_list();
		module.setId(rs.getLong("id"));
		module.set_type_id(rs.getLong("type_id"));
		module.set_class_id(rs.getLong("class_id"));
		module.set_subject_id(rs.getLong("subject_id"));
		module.set_exam_type(rs.getString("exam_type"));
		module.set_type_nm(rs.getString("type_nm"));
		module.set_class_nm(rs.getString("class_nm"));
		module.set_subject_nm(rs.getString("subject_nm"));
		//module.set_unit_nm(rs.getString("unit_nm"));
		module.set_passed_nm(rs.getString("passed_nm"));
		module.set_update_id(rs.getString("update_id"));
		module.set_tot_num(rs.getLong("tot_num"));
		module.set_time_limit(rs.getLong("time_limit"));
		module.set_update_dt(rs.getDate("update_dt").toLocalDate());
        return module;
    }
}
class ExmMatMakeHdViewYnMapper implements RowMapper<exm_mat_make_hd> {
    @Override
    public exm_mat_make_hd mapRow(ResultSet rs, int rowNum) throws SQLException {
    	exm_mat_make_hd module = new exm_mat_make_hd();
		module.setId(rs.getLong("id"));
		module.set_list_id(rs.getLong("list_id"));
		module.set_row_num(rs.getLong("mat_cnt"));
		module.set_hd_id(rs.getLong("hd_id"));
		module.set_type_id(rs.getLong("type_id"));
		module.set_class_id(rs.getLong("class_id"));
		module.set_subject_id(rs.getLong("subject_id"));
		module.set_unit_id(rs.getLong("unit_id"));
		module.set_view_yn(rs.getString("view_yn"));
		module.set_context(rs.getString("context"));
        return module;
    }
}
class ExmMatMakeHdMapper implements RowMapper<exm_mat_make_hd> {
    @Override
    public exm_mat_make_hd mapRow(ResultSet rs, int rowNum) throws SQLException {
    	exm_mat_make_hd module = new exm_mat_make_hd();
		module.setId(rs.getLong("id"));
		module.set_list_id(rs.getLong("list_id"));
		//module.set_div_id(rs.getLong("div_id"));
		module.set_hd_id(rs.getLong("hd_id"));
		module.set_type_id(rs.getLong("type_id"));
		module.set_class_id(rs.getLong("class_id"));
		module.set_subject_id(rs.getLong("subject_id"));
		module.set_unit_id(rs.getLong("unit_id"));
		module.set_context(rs.getString("context"));
        return module;
    }
}
class ExmMatMakeUnitMapper implements RowMapper<exm_mat_make_unit> {
    @Override
    public exm_mat_make_unit mapRow(ResultSet rs, int rowNum) throws SQLException {
    	exm_mat_make_unit module = new exm_mat_make_unit();
		module.setId(rs.getLong("id"));
		//module.set_view_id(rs.getLong("view_id"));
		//module.set_div_id(rs.getLong("div_id"));
		module.set_type_id(rs.getLong("type_id"));
		module.set_class_id(rs.getLong("class_id"));
		module.set_subject_id(rs.getLong("subject_id"));
		module.set_unit_id(rs.getLong("unit_id"));
		//module.set_visible_yn(rs.getString("visible_yn"));
		module.set_context(rs.getString("context"));
        return module;
    }
}
class ExmMatUnitRowMapper implements RowMapper<exm_mat_unit_row> {
    @Override
    public exm_mat_unit_row mapRow(ResultSet rs, int rowNum) throws SQLException {
		exm_mat_unit_row module = new exm_mat_unit_row();
		module.setId(rs.getLong("id"));
		module.set_view_id(rs.getLong("view_id"));
		module.set_hd_id(rs.getLong("hd_id"));
		module.set_context(rs.getString("context"));
        return module;
    }
}
class ExmMatUnitHdMapper implements RowMapper<exm_mat_unit_hd> {
    @Override
    public exm_mat_unit_hd mapRow(ResultSet rs, int rowNum) throws SQLException {
		exm_mat_unit_hd module = new exm_mat_unit_hd();
		module.setId(rs.getLong("id"));
		module.set_view_id(rs.getLong("view_id"));
		module.set_div_id(rs.getLong("div_id"));
		module.set_div_nm(rs.getString("div_nm"));
		module.set_type_id(rs.getLong("type_id"));
		module.set_class_id(rs.getLong("class_id"));
		module.set_row_num(rs.getLong("mat_cnt"));
		module.set_subject_id(rs.getLong("subject_id"));
		module.set_unit_id(rs.getLong("unit_id"));
		module.set_unit_nm(rs.getString("unit_nm"));
		//module.set_visible_yn(rs.getString("visible_yn"));
		module.set_context(rs.getString("context"));
        return module;
    }
}

class ExmMatUnitHdSmailMapper implements RowMapper<exm_mat_unit_hd> {
    @Override
    public exm_mat_unit_hd mapRow(ResultSet rs, int rowNum) throws SQLException {
		exm_mat_unit_hd module = new exm_mat_unit_hd();
		module.setId(rs.getLong("id"));
		module.set_view_id(rs.getLong("view_id"));
		module.set_div_id(rs.getLong("div_id"));
		module.set_div_nm(rs.getString("div_nm"));
		module.set_type_id(rs.getLong("type_id"));
		module.set_class_id(rs.getLong("class_id"));
		module.set_row_num(rs.getLong("mat_cnt"));
		module.set_subject_id(rs.getLong("subject_id"));
		module.set_context(rs.getString("context"));
		module.set_unit_id(rs.getLong("unit_id"));
        return module;
    }
}

class ExmMatUnitViewMapper implements RowMapper<exm_mat_unit_view> {
    @Override
    public exm_mat_unit_view mapRow(ResultSet rs, int rowNum) throws SQLException {
		exm_mat_unit_view module = new exm_mat_unit_view();
		module.setId(rs.getLong("id"));
		module.set_hd_id(rs.getLong("hd_id"));
		module.set_row_num(rs.getInt("row_num"));
		module.set_context(rs.getString("context"));		
		module.set_hdcontext(rs.getString("hdcontext"));

		module.set_div_id(rs.getLong("div_id"));
		module.set_type_id(rs.getLong("type_id"));
		module.set_class_id(rs.getLong("class_id"));
		module.set_subject_id(rs.getLong("subject_id"));
		module.set_div_nm(rs.getString("div_nm"));
		module.set_type_nm(rs.getString("type_nm"));
		module.set_class_nm(rs.getString("class_nm"));
		module.set_subject_nm(rs.getString("subject_nm"));
		module.set_view_yn(rs.getString("view_yn"));
		module.set_update_dt(rs.getDate("update_dt").toLocalDate());
		module.set_user_id(rs.getString("user_id"));
        return module;
    }
}


class PdsFileHdMapper implements RowMapper<pds_file_hd> {
    @Override
    public pds_file_hd mapRow(ResultSet rs, int rowNum) throws SQLException {
		pds_file_hd module = new pds_file_hd();
		module.setId(rs.getLong("id"));
		module.set_row_num(rs.getInt("row_num"));
		module.set_down_count(rs.getInt("down_count"));
		module.set_file_count(rs.getInt("file_count"));
		module.set_title(rs.getString("title"));
		module.set_memo(rs.getString("memo"));
		module.set_nick_name(rs.getString("nick_name"));
		module.set_cov_yn(rs.getString("cov_yn"));
		module.set_grade_cd(rs.getString("grade_cd"));
		module.set_type_id(rs.getLong("type_id"));
		module.set_unit_id(rs.getLong("unit_id"));
		module.set_class_id(rs.getLong("class_id"));
		module.set_subject_id(rs.getLong("subject_id"));
		module.set_type_nm(rs.getString("type_nm"));
		module.set_unit_nm(rs.getString("unit_nm"));
		module.set_class_nm(rs.getString("class_nm"));
		module.set_subject_nm(rs.getString("subject_nm"));
		module.set_update_dt(rs.getDate("update_dt").toLocalDate());
		module.set_user_id(rs.getString("user_id"));
        return module;
    }
}
class PdsFileRowMapper implements RowMapper<pds_file_row> {
    @Override
    public pds_file_row mapRow(ResultSet rs, int rowNum) throws SQLException {
		pds_file_row module = new pds_file_row();
		module.setId(rs.getLong("id"));
		module.set_row(rs.getLong("row"));
		module.set_row_num(rs.getInt("row_num"));
		module.set_down_count(rs.getInt("down_count"));
		module.set_save_name(rs.getString("save_name"));
		module.set_cov_yn(rs.getString("cov_yn"));
		module.set_display_name(rs.getString("display_name"));
		module.set_save_path(rs.getString("save_path"));
		module.set_update_dt(rs.getDate("update_dt").toLocalDate());
        return module;
    }
}
class PdsPastHdMapper implements RowMapper<pds_past_hd> {
    @Override
    public pds_past_hd mapRow(ResultSet rs, int rowNum) throws SQLException {
		pds_past_hd module = new pds_past_hd();
		module.setId(rs.getLong("id"));
		module.set_row_num(rs.getInt("row_num"));
		module.set_down_count(rs.getInt("down_count"));
		module.set_file_count(rs.getInt("file_count"));
		module.set_title(rs.getString("title"));
		module.set_memo(rs.getString("memo"));
		module.set_nick_name(rs.getString("nick_name"));
		module.set_cov_yn(rs.getString("cov_yn"));
		module.set_grade_cd(rs.getString("grade_cd"));
		module.set_type_id(rs.getLong("type_id"));
		module.set_unit_id(rs.getLong("unit_id"));
		module.set_class_id(rs.getLong("class_id"));
		module.set_subject_id(rs.getLong("subject_id"));
		module.set_type_nm(rs.getString("type_nm"));
		module.set_unit_nm(rs.getString("unit_nm"));
		module.set_class_nm(rs.getString("class_nm"));
		module.set_subject_nm(rs.getString("subject_nm"));
		module.set_update_dt(rs.getDate("update_dt").toLocalDate());
		module.set_user_id(rs.getString("user_id"));
        return module;
    }
}
class PdsPastRowMapper implements RowMapper<pds_past_row> {
    @Override
    public pds_past_row mapRow(ResultSet rs, int rowNum) throws SQLException {
    	pds_past_row module = new pds_past_row();
		module.setId(rs.getLong("id"));
		module.set_row(rs.getLong("row"));
		module.set_row_num(rs.getInt("row_num"));
		module.set_down_count(rs.getInt("down_count"));
		module.set_save_name(rs.getString("save_name"));
		module.set_cov_yn(rs.getString("cov_yn"));
		module.set_display_name(rs.getString("display_name"));
		module.set_save_path(rs.getString("save_path"));
		module.set_update_dt(rs.getDate("update_dt").toLocalDate());
        return module;
    }
}
class UsyTypeCdRowMapper implements RowMapper<usy_type_cd> {
    @Override
    public usy_type_cd mapRow(ResultSet rs, int rowNum) throws SQLException {
    	usy_type_cd m = new usy_type_cd();
        m.setId(rs.getLong("id"));
        m.set_type_cd(rs.getString("type_cd"));
        m.set_type_nm(rs.getString("type_nm"));
        return m;
    }
}
class UsyClassCdRowMapper implements RowMapper<usy_class_cd> {
    @Override
    public usy_class_cd mapRow(ResultSet rs, int rowNum) throws SQLException {
    	usy_class_cd m = new usy_class_cd();
        m.setId(rs.getLong("id"));
        m.set_class_nm(rs.getString("class_nm"));
        m.set_type_id(rs.getLong("type_id"));
        m.set_type_nm(rs.getString("type_nm"));
        m.setCount(rs.getLong("count"));
        return m;
    }
}
class UsySubjectCdRowMapper implements RowMapper<usy_subject_cd> {
    @Override
    public usy_subject_cd mapRow(ResultSet rs, int rowNum) throws SQLException {
    	usy_subject_cd m = new usy_subject_cd();
        m.setId(rs.getLong("id"));
        m.set_subject_nm(rs.getString("subject_nm"));
        m.set_type_id(rs.getLong("type_id"));
        m.set_type_nm(rs.getString("type_nm"));
        m.set_class_id(rs.getLong("class_id"));
        m.set_class_nm(rs.getString("class_nm"));
        m.setCount(rs.getLong("count"));
        return m;
    }
}
class UsyUnitCdRowMapper implements RowMapper<usy_unit_cd> {
    @Override
    public usy_unit_cd mapRow(ResultSet rs, int rowNum) throws SQLException {
    	usy_unit_cd m = new usy_unit_cd();
        m.setId(rs.getLong("id"));
        m.set_unit_nm(rs.getString("unit_nm"));
        m.set_type_id(rs.getLong("type_id"));
        m.set_type_nm(rs.getString("type_nm"));
        m.set_class_id(rs.getLong("class_id"));
        m.set_class_nm(rs.getString("class_nm"));
        m.set_subject_id(rs.getLong("subject_id"));
        m.set_subject_nm(rs.getString("subject_nm"));
        m.setCount(rs.getLong("count"));
        return m;
    }
}
class UsyDivCdMapper implements RowMapper<usy_div_cd> {
    @Override
    public usy_div_cd mapRow(ResultSet rs, int rowNum) throws SQLException {
    	usy_div_cd module = new usy_div_cd();
		module.setId(rs.getLong("id"));
		module.set_div_nm(rs.getString("div_nm"));
		module.set_mat_cnt(rs.getInt("mat_cnt"));
		module.set_view_yn(rs.getString("view_yn"));
        return module;
    }
}


class UsrMaterRowMapper implements RowMapper<UsrMaster> {
    @Override
    public UsrMaster mapRow(ResultSet rs, int rowNum) throws SQLException {
    	UsrMaster m = new UsrMaster();
        m.setId(rs.getLong("id"));
        m.setGroup_cd(rs.getString("group_cd"));
        m.setType_nm(rs.getString("type_nm"));
        m.setType_id(rs.getLong("type_id"));
        m.setUser_cd(rs.getString("user_cd"));
        m.setUser_nm(rs.getString("user_nm"));
        m.setPassword(rs.getString("password"));
        return m;
    }
}
class UsyConfigTypeRowMapper implements RowMapper<UsyConfigType> {
    @Override
    public UsyConfigType mapRow(ResultSet rs, int rowNum) throws SQLException {
        UsyConfigType m = new UsyConfigType();
        m.setId(rs.getLong("id"));
        m.setUser_id(rs.getLong("user_id"));
        m.setType_id(rs.getLong("type_id"));
        m.setUser_nm(rs.getString("user_nm"));
        m.setType_nm(rs.getString("type_nm"));
        return m;
    }
}

     class KeyNoMapper implements RowMapper<usy_key_no> {
        @Override
        public usy_key_no mapRow(ResultSet rs, int rowNum) throws SQLException {
        	usy_key_no module = new usy_key_no();
    		module.setId(rs.getLong("id"));
            return module;
        }
    }




