package egframe.iteach4u.service.Repository;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.helger.commons.type.TypedObject;

import egframe.common.CommonFunc;
import egframe.frame.entity.UsrMaster;
import egframe.frame.entity.sys_user;
import egframe.iteach4u.entity.UsyConfigType;
import egframe.iteach4u.entity.exm_mat_make_hd;
import egframe.iteach4u.entity.exm_mat_make_list;
import egframe.iteach4u.entity.exm_mat_make_unit;
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

import org.postgresql.util.PGobject;

@Repository
public class Iteach4uRepositoryImpl implements Iteach4uRepository {
	   private final JdbcTemplate jdbc;
	   private CommonFunc comm = new CommonFunc();
	    @Autowired
	    public Iteach4uRepositoryImpl(JdbcTemplate jdbc) {
	        this.jdbc = jdbc;
	    }
	    @Override
	    public JdbcTemplate getJdbc() {
	    	return jdbc;
	    }
	    @Override
		public UsrMaster getUserInfo(String username) {
			String sqlsyntax =  "SELECT * FROM usr_master where user_cd = ? ";
			List<UsrMaster> list;
			list = jdbc.query(
		        		sqlsyntax,
		        	    new UsrMaterRowMapper(), 
		        	    username 
		        	);
	        //1개만 가져올때 	
			UsrMaster result = list.isEmpty() ? null : list.get(0);
	        return result;
		}
	    
	    @Override
	    public Long wExmMatMakeHdSave(exm_mat_make_hd viewitem) {
	        if (viewitem == null) return 0L;
	        String upsjson = comm.convertListToJson(viewitem);
	        String tableName = "exm_mat_make_hd";
	        Long hd_id = viewitem.getId();
	        // INSERT 처리 (ID 생성)
	        if (hd_id == null||hd_id == 0) {

				// 1) CallableStatementCreator + 파라미터 선언
				List<SqlParameter> paramList = List.of(
				    new SqlParameter("v_table_name", Types.VARCHAR),
				    new SqlParameter("v_json_object", Types.OTHER),
				    new SqlOutParameter("v_pk_value", Types.INTEGER)
				);
				
				// 2) 프로시저 호출 및 OUT 파라미터 조회
				Map<String, Object> result = jdbc.call(con -> {
				    CallableStatement cs = con.prepareCall("CALL insert_dynamic_proc(?, ?, ?)");
				    cs.setString(1, tableName);
				
				    // PGobject를 통해 JSON 타입 안전 바인딩
				    org.postgresql.util.PGobject jsonObj = new org.postgresql.util.PGobject();
				    jsonObj.setType("json");
				    jsonObj.setValue(upsjson);
				    cs.setObject(2, jsonObj);
				
				    cs.registerOutParameter(3, Types.INTEGER);
				    return cs;
				}, paramList);  // :contentReference[oaicite:7]{index=7}
				
				// 3) OUT 파라미터 값 꺼내기
				hd_id = ((Number) result.get("v_pk_value")).longValue();
				System.out.println("생성된 VIEW PK 값: " + hd_id);
	        		
	 	        } 
	        // UPDATE 처리
	        else {
	            jdbc.update(
	                "CALL update_dynamic(?::varchar, ?::json)",
	                tableName,
	                upsjson
	            );
	        }
	        return hd_id;
	    }
	    @Override
	    public Long wExmMatMakeListSave(exm_mat_make_list viewitem) {
	        if (viewitem == null) return 0L;
	        String upsjson = comm.convertListToJson(viewitem);
	        String tableName = "exm_mat_make_list";
	        Long hd_id = viewitem.getId();
	        // INSERT 처리 (ID 생성)
	        if (hd_id==null ||hd_id == 0) {

				// 1) CallableStatementCreator + 파라미터 선언
				List<SqlParameter> paramList = List.of(
				    new SqlParameter("v_table_name", Types.VARCHAR),
				    new SqlParameter("v_json_object", Types.OTHER),
				    new SqlOutParameter("v_pk_value", Types.INTEGER)
				);
				
				// 2) 프로시저 호출 및 OUT 파라미터 조회
				Map<String, Object> result = jdbc.call(con -> {
				    CallableStatement cs = con.prepareCall("CALL insert_dynamic_proc(?, ?, ?)");
				    cs.setString(1, tableName);
				
				    // PGobject를 통해 JSON 타입 안전 바인딩
				    org.postgresql.util.PGobject jsonObj = new org.postgresql.util.PGobject();
				    jsonObj.setType("json");
				    jsonObj.setValue(upsjson);
				    cs.setObject(2, jsonObj);
				
				    cs.registerOutParameter(3, Types.INTEGER);
				    return cs;
				}, paramList);  // :contentReference[oaicite:7]{index=7}
				
				// 3) OUT 파라미터 값 꺼내기
				hd_id = ((Number) result.get("v_pk_value")).longValue();
				System.out.println("생성된 VIEW PK 값: " + hd_id);
	        		
	 	        } 
	        // UPDATE 처리
	        else {
	            jdbc.update(
	                "CALL update_dynamic(?::varchar, ?::json)",
	                tableName,
	                upsjson
	            );
	        }
	        return hd_id;
	    }
	    @Override
	    public List<exm_mat_unit_hd> getExmMatUnitHdList(List<Long> keydata) {
	        String placeholders = String.join(", ", Collections.nCopies(keydata.size(), "?"));
	        //String key = String.join(", ", Collections.nCopies(keydata.size(), "?"));
	        String key = keydata.stream()
	        	    .map(String::valueOf)
	        	    .collect(Collectors.joining(", "));
	        /*
	 	    for (int i = 0; i < keydata.size(); i++) {
	 	    	preparedStatement.setInt(i + 1, keydata.get(i));
	 	    }
*/
	        String sqlsyntax = "SELECT ROW_NUMBER() OVER (ORDER BY a.id) AS row_num, \n"
	        		+ "a.id ,a.view_id,a.context,a.div_id,a.type_id,a.class_id,a.subject_id,a.unit_id,a.user_id as user_id ,a.context ,b.div_nm,b.mat_cnt \n"
	        		+ "FROM \n"
	        		+ "exm_mat_unit_hd as a  "
	        		+ " left outer join usy_div_cd as b  on( a.div_id = b.id  )"
	           		+ " WHERE COALESCE(a.id, 0) IN (" + key + ") order by a.view_id,a.id";
	    	
	    	List<exm_mat_unit_hd> list = null;
			list = jdbc.query(
	        		sqlsyntax,
	        	    new ExmMatUnitHdSmailMapper()
	        	);
	    	return list;
	    }
	    
	    @Override
	    public List<exm_mat_make_hd> getExmMatMakeHDList(Long list_id) {
	        String sqlsyntax = "SELECT ROW_NUMBER() OVER (ORDER BY bb.id) AS row_num, \n"
	           		+ " bb.id ,aa.id as list_id,bb.hd_id,a.context,a.div_id,a.type_id,a.class_id,a.subject_id,a.unit_id,b.mat_cnt,"
	           		+ " bb.user_id as user_id ,b.div_nm,c.unit_nm ,b.view_yn\n"
	           		+ " FROM "
	           		+ " exm_mat_make_list aa "
	           		+ " left outer join exm_mat_make_hd as bb on(aa.id = bb.list_id )\n"
	           		+ " left outer join exm_mat_unit_hd as a  on( bb.hd_id = a.id  )"
	           		+ " left outer join usy_div_cd as b  on( a.div_id = b.id  )"
	           		+ " left outer join usy_type_class_subject_unit  as c  on( a.unit_id = c.id  )"
	           		+ " WHERE COALESCE(aa.id, 0) = ? order by a.view_id,a.id";
	    	
	    	List<exm_mat_make_hd> list = null;
			list = jdbc.query(
	        		sqlsyntax,
	        	    new ExmMatMakeHdViewYnMapper(),
	        	    list_id
	        	);
	    	return list;
	    }
	    
	    @Override
	    public List<exm_mat_unit_hd> getMatHDList(Long list_id) {
	        String sqlsyntax = "SELECT ROW_NUMBER() OVER (ORDER BY a.id) AS row_num, \n"
	           		+ " a.id ,a.view_id,a.context,a.div_id,a.type_id,a.class_id,a.subject_id,a.unit_id,b.mat_cnt,"
	           		+ " a.user_id as user_id ,a.context,b.div_nm,c.unit_nm ,b.view_yn\n"
	           		+ " FROM "
	           		+ " exm_mat_make_list aa "
	           		+ " left outer join exm_mat_make_hd as bb on(aa.id = bb.list_id )\n"
	           		+ " left outer join exm_mat_unit_hd as a  on( bb.hd_id = a.id  )"
	           		+ " left outer join usy_div_cd as b  on( a.div_id = b.id  )"
	           		+ " left outer join usy_type_class_subject_unit  as c  on( a.unit_id = c.id  )"
	           		+ " WHERE COALESCE(aa.id, 0) = ? order by a.view_id,a.id";
	    	
	    	List<exm_mat_unit_hd> list = null;
			list = jdbc.query(
	        		sqlsyntax,
	        	    new ExmMatUnitHdMapper(),
	        	    list_id
	        	);
	    	return list;
	    }
	    @Override
		public List<Long> getMatHDAllList(Long type_id,Long class_id,Long subject_id,Long unit_id) {
	    	String sqlsyntax = "SELECT "
					+ "ROW_NUMBER() OVER (ORDER BY a.id) AS row_num, a.id as hd_id"
					+ " FROM exm_mat_unit_hd as a  WHERE COALESCE(a.type_id,0) = ? and COALESCE(a.class_id,0) = ? "
					+ " and COALESCE(a.subject_id,0) = ? and COALESCE(a.unit_id,0) = ?   "
					+ "";
			List<Long> list ;
			list = jdbc.query(
	        		sqlsyntax,
	        	    new RandomLongListMapper(),
	        	    type_id,class_id,subject_id,unit_id
	        	);
			return list;
		}
	    @Override
		public List<Long> getExamMatUnitHdUnitAllList(Long type_id,Long class_id,Long subject_id,List<Long> unitCodes) {
	        String placeholders = String.join(", ", Collections.nCopies(unitCodes.size(), "?"));
	        //String key = String.join(", ", Collections.nCopies(keydata.size(), "?"));
	        String key = unitCodes.stream()
	        	    .map(String::valueOf)
	        	    .collect(Collectors.joining(", "));
	    	String sqlsyntax = "SELECT "
					+ "ROW_NUMBER() OVER (ORDER BY a.id) AS row_num, a.id as id"
					+ " FROM "
					+ " exm_mat_unit_hd as a  "
					+ " WHERE COALESCE(a.type_id,0) = ? and COALESCE(a.class_id,0) = ? "
					+ " and COALESCE(a.subject_id,0) = ? "
					+ "";
			List<Long> list ;
			if(unitCodes.size()==0){
				list = jdbc.query(
		        		sqlsyntax,
		        	    new RandomLongListMapper(),
		        	    type_id,class_id,subject_id
		        	);
			}else {
				sqlsyntax = "SELECT "
						+ "	ROW_NUMBER() OVER (ORDER BY a.id) AS row_num, a.id as id"
						+ " FROM exm_mat_unit_hd as a  WHERE COALESCE(a.type_id,0) = ? and COALESCE(a.class_id,0) = ? "
						+ " and COALESCE(a.subject_id,0) = ? and  COALESCE(a.unit_id,0) in ( "+key+" ) "
						+ "";
				list = jdbc.query(
		        		sqlsyntax,
		        	    new RandomLongListMapper(),
		        	    type_id,class_id,subject_id
		        	);
			}
			return list;
		}
		public List<Long> getExamMatUnitHdUnitAllList(Long type_id,Long class_id,Long subject_id) {
	    	String sqlsyntax = "SELECT "
					+ "ROW_NUMBER() OVER (ORDER BY a.id) AS row_num, a.id as id"
					+ " FROM exm_mat_unit_hd as a  WHERE COALESCE(a.type_id,0) = ? and COALESCE(a.class_id,0) = ? "
					+ " and COALESCE(a.subject_id,0) = ?  "
					+ "";
			List<Long> list ;
			list = jdbc.query(
	        		sqlsyntax,
	        	    new RandomLongListMapper(),
	        	    type_id,class_id,subject_id
	        	);
			return list;
		}
	    @Override
		public List<exm_mat_unit_hd> getRandomExmMatUnitHd(exm_mat_make_list list,List<Long> unitCodes) {
			List<Long> alldata  = getExamMatUnitHdUnitAllList(list.get_type_id(),list.get_class_id(),list.get_subject_id(),unitCodes);
			List<Long> randomNumbers = null;
			List<exm_mat_unit_hd> data = null;
			if(alldata.size()!=0) {
				randomNumbers = comm.pickRandomElements(alldata, list.get_tot_num());
				 data= getExmMatUnitHdList(randomNumbers);
			}
			return data;
		}
	    
	    @Override
		public List<exm_mat_unit_hd> getRandomExmMatUnitHd(exm_mat_make_list list) {
			List<Long> alldata  = getExamMatUnitHdUnitAllList(list.get_type_id(),list.get_class_id(),list.get_subject_id());
			List<Long> randomNumbers = null;
			List<exm_mat_unit_hd> data = null;
			if(alldata.size()!=0) {
				randomNumbers = comm.pickRandomElements(alldata, list.get_tot_num());
				 data= getExmMatUnitHdList(randomNumbers);
			}
			return data;
		}
	    
		@Override
		public List<exm_mat_unit_hd> wPassUnitHdMake(exm_mat_make_list list) {
			List<Long> alldata  = getMatHDAllList(list.get_type_id(),list.get_class_id(),list.get_subject_id(),list.get_unit_id());
			List<Long> randomNumbers = null;
			List<exm_mat_unit_hd> data = null;
			if(alldata.size()!=0) {
				randomNumbers = comm.pickRandomElements(alldata, list.get_tot_num());
				 data= getExmMatUnitHdList(randomNumbers);
			}
			return data;
		}
	    
		@Override
		public List<exm_mat_make_list> getExmMatMakeListList(List<Long> args) {
			String sqlsyntax ="";
			String subject_id = "%";
			for(int i =0 ; i< args.size();i++) {
				if(i==0) {
					args.get(i);
				}else if(i==1) {
					
				}else if(i==2) {
					if(args.get(i)!=0) {
						subject_id = String.valueOf(args.get(i));
					}
				}
			}
			List<exm_mat_make_list> list;
			sqlsyntax = "SELECT "
					+ " COALESCE(a.list_id,0) as list_id,COALESCE(aa.id,0) as id,"
					//+ " COALESCE(abc.view_id,0) as view_id,"
					//+ " COALESCE(a.hd_id,0) as hd_id,"
					//+ " COALESCE(abc.div_id,0) as div_id,"
					+ " COALESCE(abc.type_id,0) as type_id,COALESCE(abc.class_id,0) as class_id, "
					+ " COALESCE(abc.subject_id,0) as subject_id,"
					//+ " COALESCE(abc.unit_id,0) as unit_id,  "
					//+ " COALESCE(e.div_nm,'') as div_nm,"
					+ " COALESCE(b.type_nm,'') as type_nm,COALESCE(c.class_nm,'') as class_nm, "
					+ " COALESCE(d.subject_nm,'') as subject_nm, "
					//+ " COALESCE(f.unit_nm,'') as unit_nm, "
					+ " COALESCE(aa.tot_num,0) as tot_num, "
					+ " COALESCE(aa.time_limit,0) as time_limit, "
					+ " COALESCE(aa.update_dt,now()) as update_dt, "
					+ " COALESCE(aa.passed_nm,'') as passed_nm,  "
					+ " COALESCE(aa.exam_type,'') as exam_type, "
					+ " COALESCE(aa.update_id,'') as update_id  "
					//+ " COALESCE(e.view_yn,'') as view_yn "
					+ " FROM "
					+ " exm_mat_make_list as aa "
					+ " left outer join exm_mat_make_hd as a on (aa.id = a.list_id  )"
					+ " left outer join exm_mat_unit_hd as abc on (a.hd_id = abc.id  )"
					+ " left outer join exm_mat_unit_view as ab on (abc.view_id = ab.id  )"
					+ " left outer join usy_type as b on (abc.type_id = b.id ) "
					+ " left outer join usy_type_class as c on (abc.class_id = c.id) "
					+ " left outer join usy_type_class_subject  as d on (abc.subject_id = d.id  ) "
					//+ " left outer join usy_type_class_subject_unit as f on (abc.unit_id = f.id) "
					//+ " left outer join usy_div_cd e  on (abc.div_id = e.id) "
					+ " where b.id = ? and c.id = ? and d.id::varchar like ?"
					+ "	group by list_id,aa.id,abc.type_id,abc.class_id,abc.subject_id,type_nm,class_nm,subject_nm,"
					+ " aa.update_dt,aa.update_id,aa.passed_nm,aa.tot_num,aa.time_limit"
					+ " order by aa.id desc ";
			list = jdbc.query(
		        		sqlsyntax,
		        	    new ExmMatMakeListMapper(),
		        	    args.get(0),
		        	    args.get(1),
		        	    subject_id
		        	);
	        return list;
		}
		/*
		@Override
		public exm_mat_make_list getExmMatMakeListById(long _list_id) {
			String sqlsyntax ="";
			List<exm_mat_make_list> list;
			sqlsyntax = ""
					+ " SELECT ROW_NUMBER() OVER (ORDER BY ab.id) AS row_num,COALESCE(aa.tot_num,0) as tot_num,COALESCE(aa.time_limit,0) as time_limit,COALESCE(aa.exam_type,'') as exam_type,"
					+ " ab.id as hd_id,aa.id as list_id,aa.id as id,COALESCE(a.div_id,0) as div_id,COALESCE(a.type_id,0) as type_id,COALESCE(a.class_id,0) as class_id, COALESCE(a.subject_id,0) as subject_id,  "
					+ " COALESCE(e.div_nm,'') as div_nm,COALESCE(b.type_nm,'') as type_nm,COALESCE(c.class_nm,'') as class_nm, COALESCE(d.subject_nm,'') as subject_nm,  "
					+ " COALESCE(aa.update_dt,now()) as update_dt, "
					+ " COALESCE(a.context,'') as hdcontext,  COALESCE(aa.passed_nm,'') as passed_nm, COALESCE(aa.update_id,'') as update_id , COALESCE(e.view_yn,'') as view_yn "
					+ " FROM "
					+ " exm_mat_make_list as aa "
					+ " left outer join exm_mat_make_hd as ab on (aa.id = ab.list_id  )"
					+ " left outer join exm_mat_unit_hd as a on (ab.hd_id = a.id  )"
					+ " left outer join usy_type as b on (a.type_id = b.id ) "
					+ " left outer join usy_type_class as c on (a.class_id = c.id) "
					+ " left outer join usy_type_class_subject  as d on (a.subject_id = d.id  ) "
					+ " left outer join usy_type_class_subject_unit as f on (a.unit_id = f.id) "
					+ " left outer join usy_div_cd e  on (a.div_id = e.id) "
					+ " WHERE  "
					+ " COALESCE(aa.id,0) = ?    "
					+ " order by aa.id desc ,row_num  ";
			list = jdbc.query(
		        		sqlsyntax,
		        	    new ExmMatMakeListMapper(), 
		        	    _list_id 
		        	);
	        //1개만 가져올때 	
			exm_mat_make_list result = list.isEmpty() ? null : list.get(0);
	        return result;
		}
		*/
		@Override
		public List<exm_mat_make_list> getExmMatMakeListById(long _list_id) {
			String sqlsyntax ="";
			List<exm_mat_make_list> list;
			sqlsyntax = ""
					+ " SELECT ROW_NUMBER() OVER (ORDER BY ab.id) AS row_num,COALESCE(aa.tot_num,0) as tot_num,COALESCE(aa.time_limit,0) as time_limit,COALESCE(aa.exam_type,'') as exam_type,"
					+ " ab.id as hd_id,aa.id as list_id,aa.id as id,COALESCE(a.div_id,0) as div_id,COALESCE(a.type_id,0) as type_id,COALESCE(a.class_id,0) as class_id, COALESCE(a.subject_id,0) as subject_id,  "
					+ " COALESCE(e.div_nm,'') as div_nm,COALESCE(b.type_nm,'') as type_nm,COALESCE(c.class_nm,'') as class_nm, COALESCE(d.subject_nm,'') as subject_nm,  "
					+ " COALESCE(aa.update_dt,now()) as update_dt, "
					+ " COALESCE(a.context,'') as hdcontext,  COALESCE(aa.passed_nm,'') as passed_nm, COALESCE(aa.update_id,'') as update_id , COALESCE(e.view_yn,'') as view_yn "
					+ " FROM "
					+ " exm_mat_make_list as aa "
					+ " left outer join exm_mat_make_hd as ab on (aa.id = ab.list_id  )"
					+ " left outer join exm_mat_unit_hd as a on (ab.hd_id = a.id  )"
					+ " left outer join usy_type as b on (a.type_id = b.id ) "
					+ " left outer join usy_type_class as c on (a.class_id = c.id) "
					+ " left outer join usy_type_class_subject  as d on (a.subject_id = d.id  ) "
					+ " left outer join usy_type_class_subject_unit as f on (a.unit_id = f.id) "
					+ " left outer join usy_div_cd e  on (a.div_id = e.id) "
					+ " WHERE  "
					+ " COALESCE(aa.id,0) = ?    "
					+ " order by aa.id desc ,row_num  ";
			list = jdbc.query(
		        		sqlsyntax,
		        	    new ExmMatMakeListMapper(), 
		        	    _list_id 
		        	);
	        //1개만 가져올때 	
			//exm_mat_make_list result = list.isEmpty() ? null : list.get(0);
	        return list;
		}
	    
		@Override
		public List<exm_mat_make_list> getExmMatMakeListList(long _unit_id) {
			String sqlsyntax ="";
			List<exm_mat_make_list> list;
			sqlsyntax = "select "
					+ " ROW_NUMBER() OVER (ORDER BY aaa.view_id) AS row_num,COALESCE(aaa.hd_id,0) as hd_id,"
					+ " COALESCE(aaa.div_id,0) as div_id,COALESCE(aaa.type_id,0) as type_id,COALESCE(aaa.class_id,0) as class_id, COALESCE(aaa.subject_id,0) as subject_id,"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN COALESCE(aaa.div_nm,'') ELSE NULL END AS div_nm,"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN aaa.view_id ELSE NULL END AS id,\n"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN aaa.view_id ELSE aaa.view_id END AS view_id,\n"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN COALESCE(aaa.type_nm,'') ELSE NULL END AS type_nm,\n"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN COALESCE(aaa.class_nm,'') ELSE NULL END AS class_nm,\n"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN COALESCE(aaa.subject_nm,'') ELSE NULL END AS subject_nm,\n"
					+ "  COALESCE(aaa.update_dt,now()) as update_dt, "
					+ " COALESCE(aaa.context,'') as context, COALESCE(aaa.context,'') as hdcontext, COALESCE(aaa.user_id,'') as user_id, COALESCE(aaa.view_yn,'') as view_yn   "
					+ " from "
					+ " ("
					+ ""
					+ ""
					+ " SELECT "
					+ " a.id as hd_id,aa.id as view_id,COALESCE(a.div_id,0) as div_id,COALESCE(a.type_id,0) as type_id,COALESCE(a.class_id,0) as class_id, COALESCE(a.subject_id,0) as subject_id,  "
					+ " COALESCE(e.div_nm,'') as div_nm,COALESCE(b.type_nm,'') as type_nm,COALESCE(c.class_nm,'') as class_nm, COALESCE(d.subject_nm,'') as subject_nm,  "
					+ " COALESCE(a.update_dt,now()) as update_dt, "
					+ " COALESCE(a.context,'') as context, COALESCE(aa.user_id,'') as user_id , COALESCE(e.view_yn,'') as view_yn "
					+ " FROM "
					+ " exm_mat_unit_view as aa "
					+ " left outer join exm_mat_unit_hd as a on (aa.id = a.view_id  )"
					+ " left outer join usy_type as b on (a.type_id = b.id ) "
					+ " left outer join usy_type_class as c on (a.class_id = c.id) "
					+ " left outer join usy_type_class_subject  as d on (a.subject_id = d.id  ) "
					+ " left outer join usy_type_class_subject_unit as f on (a.unit_id = f.id) "
					+ " left outer join usy_div_cd e  on (a.div_id = e.id) "
					+ " WHERE  "
					+ " COALESCE(a.unit_id,0) = ?  order by aa.id desc  "
					+ " ) as aaa "
					+ " order by aaa.view_id desc ,row_num  ";
			list = jdbc.query(
		        		sqlsyntax,
		        	    new ExmMatMakeListMapper(), 
		        	    _unit_id 
		        	);
	        //1개만 가져올때 	
	        //UsrMaster result = list.isEmpty() ? null : list.get(0);
	        return list;
		}
		@Override
		public exm_mat_unit_view getExmMatMakeById(long _id) {
			String sqlsyntax ="";
			List<exm_mat_unit_view> list;
			sqlsyntax = "select "
					+ " ROW_NUMBER() OVER (ORDER BY aaa.view_id) AS row_num,COALESCE(aaa.hd_id,0) as hd_id,"
					+ " COALESCE(aaa.div_id,0) as div_id,COALESCE(aaa.type_id,0) as type_id,COALESCE(aaa.class_id,0) as class_id, COALESCE(aaa.subject_id,0) as subject_id,"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN COALESCE(aaa.div_nm,'') ELSE NULL END AS div_nm,"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN aaa.view_id ELSE NULL END AS id,\n"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN aaa.view_id ELSE aaa.view_id END AS view_id,\n"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN COALESCE(aaa.type_nm,'') ELSE NULL END AS type_nm,\n"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN COALESCE(aaa.class_nm,'') ELSE NULL END AS class_nm,\n"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN COALESCE(aaa.subject_nm,'') ELSE NULL END AS subject_nm,\n"
					+ "  COALESCE(aaa.update_dt,now()) as update_dt, "
					+ " COALESCE(aaa.context,'') as context, COALESCE(aaa.context,'') as hdcontext, COALESCE(aaa.user_id,'') as user_id, COALESCE(aaa.view_yn,'') as view_yn   "
					+ " from "
					+ " ("
					+ ""
					+ ""
					+ " SELECT "
					+ " a.id as hd_id,aa.id as view_id,COALESCE(a.div_id,0) as div_id,COALESCE(a.type_id,0) as type_id,COALESCE(a.class_id,0) as class_id, COALESCE(a.subject_id,0) as subject_id,  "
					+ " COALESCE(e.div_nm,'') as div_nm,COALESCE(b.type_nm,'') as type_nm,COALESCE(c.class_nm,'') as class_nm, COALESCE(d.subject_nm,'') as subject_nm,  "
					+ " COALESCE(a.update_dt,now()) as update_dt, "
					+ " COALESCE(aa.context,'') as context, COALESCE(aa.user_id,'') as user_id , COALESCE(e.view_yn,'') as view_yn "
					+ " FROM "
					+ " exm_mat_unit_view as aa "
					+ " left outer join exm_mat_unit_hd as a on (aa.id = a.view_id  )"
					+ " left outer join usy_type as b on (a.type_id = b.id ) "
					+ " left outer join usy_type_class as c on (a.class_id = c.id) "
					+ " left outer join usy_type_class_subject  as d on (a.subject_id = d.id  ) "
					+ " left outer join usy_type_class_subject_unit as f on (a.unit_id = f.id) "
					+ " left outer join usy_div_cd e  on (a.div_id = e.id) "
					+ " WHERE  "
					+ " COALESCE(aa.id,0) = ?  order by aa.id desc  "
					+ " ) as aaa "
					+ " order by aaa.view_id desc ,row_num  ";
			list = jdbc.query(
		        		sqlsyntax,
		        	    new ExmMatUnitViewMapper(), 
		        	    _id 
		        	);
	        //1개만 가져올때 	
			exm_mat_unit_view result = list.isEmpty() ? null : list.get(0);
	        return result;
		}
		@Override
		public exm_mat_unit_hd getExmMatUnitViewHdById(long _id) {
			String sqlsyntax ="";
			List<exm_mat_unit_hd> list;
			sqlsyntax = "select "
					+ " ROW_NUMBER() OVER (ORDER BY aaa.view_id) AS row_num,COALESCE(aaa.hd_id,0) as hd_id,"
					+ " COALESCE(aaa.div_id,0) as div_id,COALESCE(aaa.type_id,0) as type_id,COALESCE(aaa.class_id,0) as class_id, COALESCE(aaa.subject_id,0) as subject_id,"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN COALESCE(aaa.div_nm,'') ELSE NULL END AS div_nm,"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN aaa.view_id ELSE NULL END AS id,\n"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN aaa.view_id ELSE aaa.view_id END AS view_id,\n"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN COALESCE(aaa.type_nm,'') ELSE NULL END AS type_nm,\n"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN COALESCE(aaa.class_nm,'') ELSE NULL END AS class_nm,\n"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN COALESCE(aaa.subject_nm,'') ELSE NULL END AS subject_nm,\n"
					+ "  COALESCE(aaa.update_dt,now()) as update_dt, "
					+ " COALESCE(aaa.context,'') as context, COALESCE(aaa.context,'') as hdcontext, COALESCE(aaa.user_id,'') as user_id, COALESCE(aaa.view_yn,'') as view_yn   "
					+ " from "
					+ " ("
					+ ""
					+ ""
					+ " SELECT "
					+ " a.id as hd_id,aa.id as view_id,COALESCE(a.div_id,0) as div_id,COALESCE(a.type_id,0) as type_id,COALESCE(a.class_id,0) as class_id, COALESCE(a.subject_id,0) as subject_id,  "
					+ " COALESCE(e.div_nm,'') as div_nm,COALESCE(b.type_nm,'') as type_nm,COALESCE(c.class_nm,'') as class_nm, COALESCE(d.subject_nm,'') as subject_nm,  "
					+ " COALESCE(a.update_dt,now()) as update_dt, "
					+ " COALESCE(aa.context,'') as context, COALESCE(aa.user_id,'') as user_id , COALESCE(e.view_yn,'') as view_yn "
					+ " FROM "
					+ " exm_mat_unit_view as aa "
					+ " left outer join exm_mat_unit_hd as a on (aa.id = a.view_id  )"
					+ " left outer join usy_type as b on (a.type_id = b.id ) "
					+ " left outer join usy_type_class as c on (a.class_id = c.id) "
					+ " left outer join usy_type_class_subject  as d on (a.subject_id = d.id  ) "
					+ " left outer join usy_type_class_subject_unit as f on (a.unit_id = f.id) "
					+ " left outer join usy_div_cd e  on (a.div_id = e.id) "
					+ " WHERE  "
					+ " COALESCE(aa.id,0) = ?  order by aa.id desc  "
					+ " ) as aaa "
					+ " order by aaa.view_id desc ,row_num  ";
			list = jdbc.query(
		        		sqlsyntax,
		        	    new ExmMatUnitHdMapper(), 
		        	    _id 
		        	);
	        //1개만 가져올때 	
			exm_mat_unit_hd result = list.isEmpty() ? null : list.get(0);
	        return result;
		}
		@Override
		public List<exm_mat_make_list> getExmMatMakeListList(long _class_id, long _subject_id, long _unit_id) {
			String sqlsyntax ="";
			List<exm_mat_make_list> list;
			sqlsyntax = "select "
					+ " ROW_NUMBER() OVER (ORDER BY aaa.view_id) AS row_num,COALESCE(aaa.hd_id,0) as hd_id,"
					+ " COALESCE(aaa.div_id,0) as div_id,COALESCE(aaa.type_id,0) as type_id,COALESCE(aaa.class_id,0) as class_id, COALESCE(aaa.subject_id,0) as subject_id,"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN COALESCE(aaa.div_nm,'') ELSE NULL END AS div_nm,"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN aaa.view_id ELSE NULL END AS id,\n"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN aaa.view_id ELSE aaa.view_id END AS view_id,\n"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN COALESCE(aaa.type_nm,'') ELSE NULL END AS type_nm,\n"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN COALESCE(aaa.class_nm,'') ELSE NULL END AS class_nm,\n"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN COALESCE(aaa.subject_nm,'') ELSE NULL END AS subject_nm,\n"
					+ "  COALESCE(aaa.update_dt,now()) as update_dt, "
					+ " COALESCE(aaa.context,'') as context, COALESCE(aaa.context,'') as hdcontext,  COALESCE(aaa.user_id,'') as user_id, COALESCE(aaa.view_yn,'') as view_yn   "
					+ " from "
					+ " ("
					+ ""
					+ ""
					+ " SELECT "
					+ " a.id as hd_id,aa.id as view_id,COALESCE(a.div_id,0) as div_id,COALESCE(a.type_id,0) as type_id,COALESCE(a.class_id,0) as class_id, COALESCE(a.subject_id,0) as subject_id,  "
					+ " COALESCE(e.div_nm,'') as div_nm,COALESCE(b.type_nm,'') as type_nm,COALESCE(c.class_nm,'') as class_nm, COALESCE(d.subject_nm,'') as subject_nm,  "
					+ " COALESCE(a.update_dt,now()) as update_dt, "
					+ " COALESCE(a.context,'') as context, COALESCE(aa.user_id,'') as user_id , COALESCE(e.view_yn,'') as view_yn "
					+ " FROM "
					+ " exm_mat_unit_view as aa "
					+ " left outer join exm_mat_unit_hd as a on (aa.id = a.view_id  )"
					+ " left outer join usy_type as b on (a.type_id = b.id ) "
					+ " left outer join usy_type_class as c on (a.class_id = c.id) "
					+ " left outer join usy_type_class_subject  as d on (a.subject_id = d.id  ) "
					+ " left outer join usy_type_class_subject_unit as f on (a.unit_id = f.id) "
					+ " left outer join usy_div_cd e  on (a.div_id = e.id) "
					+ " WHERE  "
					+ " COALESCE(a.class_id,0) = ?  order by aa.id desc  "
					+ " ) as aaa "
					+ " order by aaa.view_id desc ,row_num  ";
			list = jdbc.query(
		        		sqlsyntax,
		        	    new ExmMatMakeListMapper(), 
		        	    _class_id 
		        	);
	        //1개만 가져올때 	
	        //UsrMaster result = list.isEmpty() ? null : list.get(0);
	        return list;
		}
		@Override
		public List<exm_mat_make_list> getExmMatMakeListList(long _subject_id, long _unit_id) {
			String sqlsyntax ="";
			List<exm_mat_make_list> list;
			sqlsyntax = "select "
					+ " ROW_NUMBER() OVER (ORDER BY aaa.view_id) AS row_num,COALESCE(aaa.hd_id,0) as hd_id,"
					+ " COALESCE(aaa.div_id,0) as div_id,COALESCE(aaa.type_id,0) as type_id,COALESCE(aaa.class_id,0) as class_id, COALESCE(aaa.subject_id,0) as subject_id,"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN COALESCE(aaa.div_nm,'') ELSE NULL END AS div_nm,"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN aaa.view_id ELSE NULL END AS id,\n"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN aaa.view_id ELSE aaa.view_id END AS view_id,\n"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN COALESCE(aaa.type_nm,'') ELSE NULL END AS type_nm,\n"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN COALESCE(aaa.class_nm,'') ELSE NULL END AS class_nm,\n"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN COALESCE(aaa.subject_nm,'') ELSE NULL END AS subject_nm,\n"
					+ "  COALESCE(aaa.update_dt,now()) as update_dt, "
					+ " COALESCE(aaa.context,'') as context,  COALESCE(aaa.context,'') as hdcontext, COALESCE(aaa.user_id,'') as user_id, COALESCE(aaa.view_yn,'') as view_yn   "
					+ " from "
					+ " ("
					+ ""
					+ ""
					+ " SELECT "
					+ " a.id as hd_id,aa.id as view_id,COALESCE(a.div_id,0) as div_id,COALESCE(a.type_id,0) as type_id,COALESCE(a.class_id,0) as class_id, COALESCE(a.subject_id,0) as subject_id,  "
					+ " COALESCE(e.div_nm,'') as div_nm,COALESCE(b.type_nm,'') as type_nm,COALESCE(c.class_nm,'') as class_nm, COALESCE(d.subject_nm,'') as subject_nm,  "
					+ " COALESCE(a.update_dt,now()) as update_dt, "
					+ " COALESCE(a.context,'') as context, COALESCE(aa.user_id,'') as user_id , COALESCE(e.view_yn,'') as view_yn "
					+ " FROM "
					+ " exm_mat_unit_view as aa "
					+ " left outer join exm_mat_unit_hd as a on (aa.id = a.view_id  )"
					+ " left outer join usy_type as b on (a.type_id = b.id ) "
					+ " left outer join usy_type_class as c on (a.class_id = c.id) "
					+ " left outer join usy_type_class_subject  as d on (a.subject_id = d.id  ) "
					+ " left outer join usy_type_class_subject_unit as f on (a.unit_id = f.id) "
					+ " left outer join usy_div_cd e  on (a.div_id = e.id) "
					+ " WHERE  "
					+ " COALESCE(a.subject_id,0) = ?  order by aa.id desc  "
					+ " ) as aaa "
					+ " order by aaa.view_id desc ,row_num  ";
			list = jdbc.query(
		        		sqlsyntax,
		        	    new ExmMatMakeListMapper(), 
		        	    _subject_id 
		        	);
	        //1개만 가져올때 	
	        //UsrMaster result = list.isEmpty() ? null : list.get(0);
	        return list;
		}
		@Override
		public List<exm_mat_make_list> getExmMatMakeListList(long _class_id, long _subject_id, long _unit_id,int limit, int offset) {
			String sqlsyntax ="";
			List<exm_mat_make_list> list;
			sqlsyntax = "select "
					+ " ROW_NUMBER() OVER (ORDER BY aaa.view_id) AS row_num,COALESCE(aaa.hd_id,0) as hd_id,"
					+ " COALESCE(aaa.div_id,0) as div_id,COALESCE(aaa.type_id,0) as type_id,COALESCE(aaa.class_id,0) as class_id, COALESCE(aaa.subject_id,0) as subject_id,"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN COALESCE(aaa.div_nm,'') ELSE NULL END AS div_nm,"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN aaa.view_id ELSE NULL END AS id,\n"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN aaa.view_id ELSE aaa.view_id END AS view_id,\n"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN COALESCE(aaa.type_nm,'') ELSE NULL END AS type_nm,\n"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN COALESCE(aaa.class_nm,'') ELSE NULL END AS class_nm,\n"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN COALESCE(aaa.subject_nm,'') ELSE NULL END AS subject_nm,\n"
					+ "  COALESCE(aaa.update_dt,now()) as update_dt, "
					+ " COALESCE(aaa.context,'') as context,  COALESCE(aaa.context,'') as hdcontext, COALESCE(aaa.user_id,'') as user_id, COALESCE(aaa.view_yn,'') as view_yn   "
					+ " from "
					+ " ("
					+ ""
					+ ""
					+ " SELECT "
					+ " a.id as hd_id,aa.id as view_id,COALESCE(a.div_id,0) as div_id,COALESCE(a.type_id,0) as type_id,COALESCE(a.class_id,0) as class_id, COALESCE(a.subject_id,0) as subject_id,  "
					+ " COALESCE(e.div_nm,'') as div_nm,COALESCE(b.type_nm,'') as type_nm,COALESCE(c.class_nm,'') as class_nm, COALESCE(d.subject_nm,'') as subject_nm,  "
					+ " COALESCE(a.update_dt,now()) as update_dt, "
					+ " COALESCE(a.context,'') as context, COALESCE(aa.user_id,'') as user_id , COALESCE(e.view_yn,'') as view_yn "
					+ " FROM "
					+ " exm_mat_unit_view as aa "
					+ " left outer join exm_mat_unit_hd as a on (aa.id = a.view_id  )"
					+ " left outer join usy_type as b on (a.type_id = b.id ) "
					+ " left outer join usy_type_class as c on (a.class_id = c.id) "
					+ " left outer join usy_type_class_subject  as d on (a.subject_id = d.id  ) "
					+ " left outer join usy_type_class_subject_unit as f on (a.unit_id = f.id) "
					+ " left outer join usy_div_cd e  on (a.div_id = e.id) "
					+ " WHERE  "
					+ " COALESCE(a.class_id,0) = ?  order by aa.id desc  limit ? offset ? "
					+ " ) as aaa "
					+ " order by aaa.view_id desc ,row_num  ";
			list = jdbc.query(
		        		sqlsyntax,
		        	    new ExmMatMakeListMapper(), 
		        	    _class_id,limit,offset 
		        	);
	        //1개만 가져올때 	
	        //UsrMaster result = list.isEmpty() ? null : list.get(0);
	        return list;
		}
		@Override
		public List<exm_mat_make_unit> getExmMatMakeUnitById(long _hd_id) {
			String sqlsyntax ="";
			List<exm_mat_make_unit> list;
			sqlsyntax = "SELECT COALESCE(view_id,0) as view_id,COALESCE(hd_id,0) as hd_id,COALESCE(id,0) as row_id,COALESCE(id,0) as id, "
					+ "  COALESCE(context,'') as context  "
					+ " FROM exm_mat_unit_row WHERE hd_id = ?";
			list = jdbc.query(
		        		sqlsyntax,
		        	    new ExmMatMakeUnitMapper(), 
		        	    _hd_id 
		        	);
	        //1개만 가져올때 	
			//exm_mat_unit_hd result = list.isEmpty() ? null : list.get(0);
	        return list;
		}
		@Override
		public List<exm_mat_make_hd> getExmMatMakeHdById(long _unit_id) {
			String sqlsyntax ="";
			List<exm_mat_make_hd> list;
			sqlsyntax = "SELECT COALESCE(a.type_id,0) as type_id,COALESCE(a.view_id,0) as view_id,COALESCE(a.id,0) as id,"
					+ "  COALESCE(a.class_id,0) as class_id, COALESCE(a.subject_id,0) as subject_id, COALESCE(a.unit_id,0) as unit_id , "
					+ " COALESCE(a.div_id,0) as div_id, COALESCE(a.context,'') as context,"
					+ " CASE WHEN ROW_NUMBER() OVER (PARTITION BY a.view_id ORDER BY a.view_id) = 1 and e.view_yn ='Y' THEN 'Y' ELSE 'N' END AS visible_yn"
					+ " FROM "
					+ " exm_mat_unit_hd as a left outer join exm_mat_unit_view as b on(a.view_id = b.id ) "
					+ " left outer join usy_div_cd e  on (a.div_id = e.id)"
					+ " WHERE COALESCE(a.view_id,0) = ? order by a.view_id,a.id  ";
			list = jdbc.query(
		        		sqlsyntax,
		        	    new ExmMatMakeHdMapper(), 
		        	    _unit_id 
		        	);
	        //1개만 가져올때 	
			//exm_mat_unit_hd result = list.isEmpty() ? null : list.get(0);
	        return list;
		}
	    
	    @Override
	    public Long wExmMatUnitViewSave(exm_mat_unit_view viewitem) {
	        if (viewitem == null) return 0L;
	        String upsjson = comm.convertListToJson(viewitem);
	        String tableName = "exm_mat_unit_view";
	        Long hd_id = viewitem.getId();
	        // INSERT 처리 (ID 생성)
	        if (hd_id == 0) {

				// 1) CallableStatementCreator + 파라미터 선언
				List<SqlParameter> paramList = List.of(
				    new SqlParameter("v_table_name", Types.VARCHAR),
				    new SqlParameter("v_json_object", Types.OTHER),
				    new SqlOutParameter("v_pk_value", Types.INTEGER)
				);
				
				// 2) 프로시저 호출 및 OUT 파라미터 조회
				Map<String, Object> result = jdbc.call(con -> {
				    CallableStatement cs = con.prepareCall("CALL insert_dynamic_proc(?, ?, ?)");
				    cs.setString(1, tableName);
				
				    // PGobject를 통해 JSON 타입 안전 바인딩
				    org.postgresql.util.PGobject jsonObj = new org.postgresql.util.PGobject();
				    jsonObj.setType("json");
				    jsonObj.setValue(upsjson);
				    cs.setObject(2, jsonObj);
				
				    cs.registerOutParameter(3, Types.INTEGER);
				    return cs;
				}, paramList);  // :contentReference[oaicite:7]{index=7}
				
				// 3) OUT 파라미터 값 꺼내기
				hd_id = ((Number) result.get("v_pk_value")).longValue();
				System.out.println("생성된 VIEW PK 값: " + hd_id);
	        		
	 	        } 
	        // UPDATE 처리
	        else {
	            jdbc.update(
	                "CALL update_dynamic(?::varchar, ?::json)",
	                tableName,
	                upsjson
	            );
	        }
	        return hd_id;
	    }

	    @Override
	    public Long wExmMatUnitHdSave(exm_mat_unit_hd hditem) {
	        if (hditem == null) return 0L;
	        String upsjson = comm.convertListToJson(hditem);
	        String tableName = "exm_mat_unit_hd";
	        Long hd_id = hditem.getId();
	        // INSERT 처리 (ID 생성)
	        if (hd_id == null || Objects.equals(hd_id, 0L)) {
	        	

				// 1) CallableStatementCreator + 파라미터 선언
				List<SqlParameter> paramList = List.of(
				    new SqlParameter("v_table_name", Types.VARCHAR),
				    new SqlParameter("v_json_object", Types.OTHER),
				    new SqlOutParameter("v_pk_value", Types.INTEGER)
				);
				
				// 2) 프로시저 호출 및 OUT 파라미터 조회
				Map<String, Object> result = jdbc.call(con -> {
				    CallableStatement cs = con.prepareCall("CALL insert_dynamic_proc(?, ?, ?)");
				    cs.setString(1, tableName);
				
				    // PGobject를 통해 JSON 타입 안전 바인딩
				    org.postgresql.util.PGobject jsonObj = new org.postgresql.util.PGobject();
				    jsonObj.setType("json");
				    jsonObj.setValue(upsjson);
				    cs.setObject(2, jsonObj);
				
				    cs.registerOutParameter(3, Types.INTEGER);
				    return cs;
				}, paramList);  // :contentReference[oaicite:7]{index=7}
				
				// 3) OUT 파라미터 값 꺼내기
				hd_id = ((Number) result.get("v_pk_value")).longValue();
				System.out.println("생성된 HDPK 값: " + hd_id);
	        } 
	        // UPDATE 처리
	        else {
	            jdbc.update(
	                "CALL update_dynamic(?::varchar, ?::json)",
	                tableName,
	                upsjson
	            );
	        }
	        return hd_id;
	    }
	    @Override
	    public Long wExmMatUnitRowSave(exm_mat_unit_row hditem) {
	        if (hditem == null) return 0L;
	        String upsjson = comm.convertListToJson(hditem);
	        String tableName = "exm_mat_unit_row";
	        Long hd_id = hditem.getId();
	        // INSERT 처리 (ID 생성)
	        if (hd_id == null || Objects.equals(hd_id, 0L)) {

				// 1) CallableStatementCreator + 파라미터 선언
				List<SqlParameter> paramList = List.of(
				    new SqlParameter("v_table_name", Types.VARCHAR),
				    new SqlParameter("v_json_object", Types.OTHER),
				    new SqlOutParameter("v_pk_value", Types.INTEGER)
				);
				
				// 2) 프로시저 호출 및 OUT 파라미터 조회
				Map<String, Object> result = jdbc.call(con -> {
				    CallableStatement cs = con.prepareCall("CALL insert_dynamic_proc(?, ?, ?)");
				    cs.setString(1, tableName);
				
				    // PGobject를 통해 JSON 타입 안전 바인딩
				    org.postgresql.util.PGobject jsonObj = new org.postgresql.util.PGobject();
				    jsonObj.setType("json");
				    jsonObj.setValue(upsjson);
				    cs.setObject(2, jsonObj);
				
				    cs.registerOutParameter(3, Types.INTEGER);
				    return cs;
				}, paramList);  // :contentReference[oaicite:7]{index=7}
				
				// 3) OUT 파라미터 값 꺼내기
				hd_id = ((Number) result.get("v_pk_value")).longValue();
				System.out.println("생성된 ROWPK 값: " + hd_id);
	        } 
	        // UPDATE 처리
	        else {
	            jdbc.update(
	                "CALL update_dynamic(?::varchar, ?::json)",
	                tableName,
	                upsjson
	            );
	        }
	        return hd_id;
	    }
		    
	    
		@Override
		public List<usy_div_cd> getDivCd(String _view_yn) {
			String sqlsyntax ="";
			List<usy_div_cd> list;
			sqlsyntax = "SELECT COALESCE(id,0) as id,COALESCE(div_nm,'') as div_nm,COALESCE(mat_cnt,0) as mat_cnt,COALESCE(view_yn,'Y') as view_yn "
					+ " FROM usy_div_cd WHERE view_yn = ?";
			list = jdbc.query(
		        		sqlsyntax,
		        	    new UsyDivCdMapper(), 
		        	    _view_yn 
		        	);
	        //1개만 가져올때 	
			//exm_mat_unit_hd result = list.isEmpty() ? null : list.get(0);
	        return list;
		}
		@Override
		public List<exm_mat_unit_row> getExmMatUnitRowById(long _hd_id) {
			String sqlsyntax ="";
			List<exm_mat_unit_row> list;
			sqlsyntax = "SELECT COALESCE(view_id,0) as view_id,COALESCE(hd_id,0) as hd_id,COALESCE(id,0) as row_id,COALESCE(id,0) as id, "
					+ "  COALESCE(context,'') as context  "
					+ " FROM exm_mat_unit_row WHERE hd_id = ?";
			list = jdbc.query(
		        		sqlsyntax,
		        	    new ExmMatUnitRowMapper(), 
		        	    _hd_id 
		        	);
	        //1개만 가져올때 	
			//exm_mat_unit_hd result = list.isEmpty() ? null : list.get(0);
	        return list;
		}
		@Override
		public List<exm_mat_unit_hd> getExmMatUnitHdById(long _unit_id) {
			String sqlsyntax ="";
			List<exm_mat_unit_hd> list;
			sqlsyntax = "SELECT COALESCE(a.type_id,0) as type_id,COALESCE(a.view_id,0) as view_id,COALESCE(a.id,0) as id,"
					+ "  COALESCE(a.class_id,0) as class_id, COALESCE(a.subject_id,0) as subject_id, COALESCE(a.unit_id,0) as unit_id , "
					+ " COALESCE(a.div_id,0) as div_id, COALESCE(a.context,'') as context,"
					+ " CASE WHEN ROW_NUMBER() OVER (PARTITION BY a.view_id ORDER BY a.view_id) = 1 and e.view_yn ='Y' THEN 'Y' ELSE 'N' END AS visible_yn"
					+ " FROM "
					+ " exm_mat_unit_hd as a left outer join exm_mat_unit_view as b on(a.view_id = b.id ) "
					+ " left outer join usy_div_cd e  on (a.div_id = e.id)"
					+ " WHERE COALESCE(a.view_id,0) = ? order by a.view_id,a.id  ";
			list = jdbc.query(
		        		sqlsyntax,
		        	    new ExmMatUnitHdMapper(), 
		        	    _unit_id 
		        	);
	        //1개만 가져올때 	
			//exm_mat_unit_hd result = list.isEmpty() ? null : list.get(0);
	        return list;
		}
	    @Override
	    public usy_div_cd getUsyCdById(long _id) {
			String sqlsyntax ="";
			List<usy_div_cd> list;
				sqlsyntax = "SELECT  * "
						+ " FROM usy_div_cd as a "
						+ " WHERE COALESCE(a.id,0) = ?  ";

			        list = jdbc.query(
			        		sqlsyntax,
			        	    new UsyDivCdMapper(), 
			        	    _id 
			        	);
		    //1개만 가져올때 	
		        usy_div_cd result = list.isEmpty() ? null : list.get(0);
	        return result;
	    }
	    @Override
	    public List<usy_div_cd> getUsyCdList(long _id) {
			String sqlsyntax ="";
			List<usy_div_cd> list;
			if(_id==0) {
				sqlsyntax = "SELECT  * "
						+ " FROM usy_div_cd as a ";

			        list = jdbc.query(
			        		sqlsyntax,
			        	    new UsyDivCdMapper()
			        	);
			}else {
				sqlsyntax = "SELECT  * "
						+ " FROM usy_div_cd as a "
						+ " WHERE COALESCE(a.id,0) = ?  ";

			        list = jdbc.query(
			        		sqlsyntax,
			        	    new UsyDivCdMapper(), 
			        	    _id 
			        	);
			}
		    //1개만 가져올때 	
	        //UsrMaster result = list.isEmpty() ? null : list.get(0);
	        return list;
	    }
		@Override
		public exm_mat_unit_view getExmMatUnitViewById(long _unit_id) {
			String sqlsyntax ="";
			List<exm_mat_unit_view> list;
			sqlsyntax = "select "
					+ " ROW_NUMBER() OVER (ORDER BY aaa.view_id) AS row_num,COALESCE(aaa.hd_id,0) as hd_id,"
					+ " COALESCE(aaa.div_id,0) as div_id,COALESCE(aaa.type_id,0) as type_id,COALESCE(aaa.class_id,0) as class_id, COALESCE(aaa.subject_id,0) as subject_id,"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN COALESCE(aaa.div_nm,'') ELSE NULL END AS div_nm,"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN aaa.view_id ELSE NULL END AS id,\n"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN aaa.view_id ELSE aaa.view_id END AS view_id,\n"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN COALESCE(aaa.type_nm,'') ELSE NULL END AS type_nm,\n"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN COALESCE(aaa.class_nm,'') ELSE NULL END AS class_nm,\n"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN COALESCE(aaa.subject_nm,'') ELSE NULL END AS subject_nm,\n"
					+ "  COALESCE(aaa.update_dt,now()) as update_dt, "
					+ " COALESCE(aaa.hdcontext,'') as hdcontext, COALESCE(aaa.context,'') as context, COALESCE(aaa.user_id,'') as user_id, COALESCE(aaa.view_yn,'') as view_yn   "
					+ " from "
					+ " ("
					+ ""
					+ ""
					+ " SELECT "
					+ " a.id as hd_id,aa.id as view_id,COALESCE(a.div_id,0) as div_id,COALESCE(a.type_id,0) as type_id,COALESCE(a.class_id,0) as class_id, COALESCE(a.subject_id,0) as subject_id,  "
					+ " COALESCE(e.div_nm,'') as div_nm,COALESCE(b.type_nm,'') as type_nm,COALESCE(c.class_nm,'') as class_nm, COALESCE(d.subject_nm,'') as subject_nm,  "
					+ " COALESCE(a.update_dt,now()) as update_dt, "
					+ " COALESCE(a.context,'') as hdcontext,  COALESCE(aa.context,'') as context, COALESCE(aa.user_id,'') as user_id , COALESCE(e.view_yn,'') as view_yn "
					+ " FROM "
					+ " exm_mat_unit_view as aa "
					+ " left outer join exm_mat_unit_hd as a on (aa.id = a.view_id  )"
					+ " left outer join usy_type as b on (a.type_id = b.id ) "
					+ " left outer join usy_type_class as c on (a.class_id = c.id) "
					+ " left outer join usy_type_class_subject  as d on (a.subject_id = d.id  ) "
					+ " left outer join usy_type_class_subject_unit as f on (a.unit_id = f.id) "
					+ " left outer join usy_div_cd e  on (a.div_id = e.id) "
					+ " WHERE  "
					+ " COALESCE(aa.id,0) = ?  order by aa.id desc  "
					+ " ) as aaa "
					+ " order by aaa.view_id desc ,row_num  ";
			list = jdbc.query(
		        		sqlsyntax,
		        	    new ExmMatUnitViewMapper(), 
		        	    _unit_id 
		        	);
	        //1개만 가져올때 	
			exm_mat_unit_view result = list.isEmpty() ? null : list.get(0);
	        return result;
		}
	    
		@Override
		public List<exm_mat_unit_view> getExmMatUnitViewList(long _unit_id) {
			String sqlsyntax ="";
			List<exm_mat_unit_view> list;
			sqlsyntax = "select "
					+ " ROW_NUMBER() OVER (ORDER BY aaa.view_id) AS row_num,COALESCE(aaa.hd_id,0) as hd_id,"
					+ " COALESCE(aaa.div_id,0) as div_id,COALESCE(aaa.type_id,0) as type_id,COALESCE(aaa.class_id,0) as class_id, COALESCE(aaa.subject_id,0) as subject_id,"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN COALESCE(aaa.div_nm,'') ELSE NULL END AS div_nm,"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN aaa.view_id ELSE NULL END AS id,\n"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN aaa.view_id ELSE aaa.view_id END AS view_id,\n"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN COALESCE(aaa.type_nm,'') ELSE NULL END AS type_nm,\n"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN COALESCE(aaa.class_nm,'') ELSE NULL END AS class_nm,\n"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN COALESCE(aaa.subject_nm,'') ELSE NULL END AS subject_nm,\n"
					+ "  COALESCE(aaa.update_dt,now()) as update_dt, "
					+ " COALESCE(aaa.context,'') as context, COALESCE(aaa.context,'') as hdcontext, COALESCE(aaa.user_id,'') as user_id, COALESCE(aaa.view_yn,'') as view_yn   "
					+ " from "
					+ " ("
					+ ""
					+ ""
					+ " SELECT "
					+ " a.id as hd_id,aa.id as view_id,COALESCE(a.div_id,0) as div_id,COALESCE(a.type_id,0) as type_id,COALESCE(a.class_id,0) as class_id, COALESCE(a.subject_id,0) as subject_id,  "
					+ " COALESCE(e.div_nm,'') as div_nm,COALESCE(b.type_nm,'') as type_nm,COALESCE(c.class_nm,'') as class_nm, COALESCE(d.subject_nm,'') as subject_nm,  "
					+ " COALESCE(a.update_dt,now()) as update_dt, "
					+ " COALESCE(a.context,'') as context, COALESCE(aa.user_id,'') as user_id , COALESCE(e.view_yn,'') as view_yn "
					+ " FROM "
					+ " exm_mat_unit_view as aa "
					+ " left outer join exm_mat_unit_hd as a on (aa.id = a.view_id  )"
					+ " left outer join usy_type as b on (a.type_id = b.id ) "
					+ " left outer join usy_type_class as c on (a.class_id = c.id) "
					+ " left outer join usy_type_class_subject  as d on (a.subject_id = d.id  ) "
					+ " left outer join usy_type_class_subject_unit as f on (a.unit_id = f.id) "
					+ " left outer join usy_div_cd e  on (a.div_id = e.id) "
					+ " WHERE  "
					+ " COALESCE(a.unit_id,0) = ?  order by aa.id desc  "
					+ " ) as aaa "
					+ " order by aaa.view_id desc ,row_num  ";
			list = jdbc.query(
		        		sqlsyntax,
		        	    new ExmMatUnitViewMapper(), 
		        	    _unit_id 
		        	);
	        //1개만 가져올때 	
	        //UsrMaster result = list.isEmpty() ? null : list.get(0);
	        return list;
		}
		@Override
		public List<exm_mat_unit_view> getExmMatUnitViewList(long _class_id, long _subject_id, long _unit_id) {
			String sqlsyntax ="";
			List<exm_mat_unit_view> list;
			sqlsyntax = "select "
					+ " ROW_NUMBER() OVER (ORDER BY aaa.view_id) AS row_num,COALESCE(aaa.hd_id,0) as hd_id,"
					+ " COALESCE(aaa.div_id,0) as div_id,COALESCE(aaa.type_id,0) as type_id,COALESCE(aaa.class_id,0) as class_id, COALESCE(aaa.subject_id,0) as subject_id,"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN COALESCE(aaa.div_nm,'') ELSE NULL END AS div_nm,"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN aaa.view_id ELSE NULL END AS id,\n"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN aaa.view_id ELSE aaa.view_id END AS view_id,\n"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN COALESCE(aaa.type_nm,'') ELSE NULL END AS type_nm,\n"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN COALESCE(aaa.class_nm,'') ELSE NULL END AS class_nm,\n"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN COALESCE(aaa.subject_nm,'') ELSE NULL END AS subject_nm,\n"
					+ "  COALESCE(aaa.update_dt,now()) as update_dt, "
					+ " COALESCE(aaa.context,'') as context, COALESCE(aaa.context,'') as hdcontext,  COALESCE(aaa.user_id,'') as user_id, COALESCE(aaa.view_yn,'') as view_yn   "
					+ " from "
					+ " ("
					+ ""
					+ ""
					+ " SELECT "
					+ " a.id as hd_id,aa.id as view_id,COALESCE(a.div_id,0) as div_id,COALESCE(a.type_id,0) as type_id,COALESCE(a.class_id,0) as class_id, COALESCE(a.subject_id,0) as subject_id,  "
					+ " COALESCE(e.div_nm,'') as div_nm,COALESCE(b.type_nm,'') as type_nm,COALESCE(c.class_nm,'') as class_nm, COALESCE(d.subject_nm,'') as subject_nm,  "
					+ " COALESCE(a.update_dt,now()) as update_dt, "
					+ " COALESCE(a.context,'') as context, COALESCE(aa.user_id,'') as user_id , COALESCE(e.view_yn,'') as view_yn "
					+ " FROM "
					+ " exm_mat_unit_view as aa "
					+ " left outer join exm_mat_unit_hd as a on (aa.id = a.view_id  )"
					+ " left outer join usy_type as b on (a.type_id = b.id ) "
					+ " left outer join usy_type_class as c on (a.class_id = c.id) "
					+ " left outer join usy_type_class_subject  as d on (a.subject_id = d.id  ) "
					+ " left outer join usy_type_class_subject_unit as f on (a.unit_id = f.id) "
					+ " left outer join usy_div_cd e  on (a.div_id = e.id) "
					+ " WHERE  "
					+ " COALESCE(a.class_id,0) = ?  order by aa.id desc  "
					+ " ) as aaa "
					+ " order by aaa.view_id desc ,row_num  ";
			list = jdbc.query(
		        		sqlsyntax,
		        	    new ExmMatUnitViewMapper(), 
		        	    _class_id 
		        	);
	        //1개만 가져올때 	
	        //UsrMaster result = list.isEmpty() ? null : list.get(0);
	        return list;
		}
		@Override
		public List<exm_mat_unit_view> getExmMatUnitViewList(long _subject_id, long _unit_id) {
			String sqlsyntax ="";
			List<exm_mat_unit_view> list;
			sqlsyntax = "select "
					+ " ROW_NUMBER() OVER (ORDER BY aaa.view_id) AS row_num,COALESCE(aaa.hd_id,0) as hd_id,"
					+ " COALESCE(aaa.div_id,0) as div_id,COALESCE(aaa.type_id,0) as type_id,COALESCE(aaa.class_id,0) as class_id, COALESCE(aaa.subject_id,0) as subject_id,"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN COALESCE(aaa.div_nm,'') ELSE NULL END AS div_nm,"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN aaa.view_id ELSE NULL END AS id,\n"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN aaa.view_id ELSE aaa.view_id END AS view_id,\n"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN COALESCE(aaa.type_nm,'') ELSE NULL END AS type_nm,\n"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN COALESCE(aaa.class_nm,'') ELSE NULL END AS class_nm,\n"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN COALESCE(aaa.subject_nm,'') ELSE NULL END AS subject_nm,\n"
					+ "  COALESCE(aaa.update_dt,now()) as update_dt, "
					+ " COALESCE(aaa.context,'') as context,  COALESCE(aaa.context,'') as hdcontext, COALESCE(aaa.user_id,'') as user_id, COALESCE(aaa.view_yn,'') as view_yn   "
					+ " from "
					+ " ("
					+ ""
					+ ""
					+ " SELECT "
					+ " a.id as hd_id,aa.id as view_id,COALESCE(a.div_id,0) as div_id,COALESCE(a.type_id,0) as type_id,COALESCE(a.class_id,0) as class_id, COALESCE(a.subject_id,0) as subject_id,  "
					+ " COALESCE(e.div_nm,'') as div_nm,COALESCE(b.type_nm,'') as type_nm,COALESCE(c.class_nm,'') as class_nm, COALESCE(d.subject_nm,'') as subject_nm,  "
					+ " COALESCE(a.update_dt,now()) as update_dt, "
					+ " COALESCE(a.context,'') as context, COALESCE(aa.user_id,'') as user_id , COALESCE(e.view_yn,'') as view_yn "
					+ " FROM "
					+ " exm_mat_unit_view as aa "
					+ " left outer join exm_mat_unit_hd as a on (aa.id = a.view_id  )"
					+ " left outer join usy_type as b on (a.type_id = b.id ) "
					+ " left outer join usy_type_class as c on (a.class_id = c.id) "
					+ " left outer join usy_type_class_subject  as d on (a.subject_id = d.id  ) "
					+ " left outer join usy_type_class_subject_unit as f on (a.unit_id = f.id) "
					+ " left outer join usy_div_cd e  on (a.div_id = e.id) "
					+ " WHERE  "
					+ " COALESCE(a.subject_id,0) = ?  order by aa.id desc  "
					+ " ) as aaa "
					+ " order by aaa.view_id desc ,row_num  ";
			list = jdbc.query(
		        		sqlsyntax,
		        	    new ExmMatUnitViewMapper(), 
		        	    _subject_id 
		        	);
	        //1개만 가져올때 	
	        //UsrMaster result = list.isEmpty() ? null : list.get(0);
	        return list;
		}
		@Override
		public List<exm_mat_unit_view> getExmMatUnitViewList(long _class_id, long _subject_id, long _unit_id,int limit, int offset) {
			String sqlsyntax ="";
			List<exm_mat_unit_view> list;
			sqlsyntax = "select "
					+ " ROW_NUMBER() OVER (ORDER BY aaa.view_id) AS row_num,COALESCE(aaa.hd_id,0) as hd_id,"
					+ " COALESCE(aaa.div_id,0) as div_id,COALESCE(aaa.type_id,0) as type_id,COALESCE(aaa.class_id,0) as class_id, COALESCE(aaa.subject_id,0) as subject_id,"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN COALESCE(aaa.div_nm,'') ELSE NULL END AS div_nm,"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN aaa.view_id ELSE NULL END AS id,\n"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN aaa.view_id ELSE aaa.view_id END AS view_id,\n"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN COALESCE(aaa.type_nm,'') ELSE NULL END AS type_nm,\n"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN COALESCE(aaa.class_nm,'') ELSE NULL END AS class_nm,\n"
					+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_id ORDER BY aaa.view_id) = 1 THEN COALESCE(aaa.subject_nm,'') ELSE NULL END AS subject_nm,\n"
					+ "  COALESCE(aaa.update_dt,now()) as update_dt, "
					+ " COALESCE(aaa.context,'') as context,  COALESCE(aaa.context,'') as hdcontext, COALESCE(aaa.user_id,'') as user_id, COALESCE(aaa.view_yn,'') as view_yn   "
					+ " from "
					+ " ("
					+ ""
					+ ""
					+ " SELECT "
					+ " a.id as hd_id,aa.id as view_id,COALESCE(a.div_id,0) as div_id,COALESCE(a.type_id,0) as type_id,COALESCE(a.class_id,0) as class_id, COALESCE(a.subject_id,0) as subject_id,  "
					+ " COALESCE(e.div_nm,'') as div_nm,COALESCE(b.type_nm,'') as type_nm,COALESCE(c.class_nm,'') as class_nm, COALESCE(d.subject_nm,'') as subject_nm,  "
					+ " COALESCE(a.update_dt,now()) as update_dt, "
					+ " COALESCE(a.context,'') as context, COALESCE(aa.user_id,'') as user_id , COALESCE(e.view_yn,'') as view_yn "
					+ " FROM "
					+ " exm_mat_unit_view as aa "
					+ " left outer join exm_mat_unit_hd as a on (aa.id = a.view_id  )"
					+ " left outer join usy_type as b on (a.type_id = b.id ) "
					+ " left outer join usy_type_class as c on (a.class_id = c.id) "
					+ " left outer join usy_type_class_subject  as d on (a.subject_id = d.id  ) "
					+ " left outer join usy_type_class_subject_unit as f on (a.unit_id = f.id) "
					+ " left outer join usy_div_cd e  on (a.div_id = e.id) "
					+ " WHERE  "
					+ " COALESCE(a.class_id,0) = ?  order by aa.id desc  limit ? offset ? "
					+ " ) as aaa "
					+ " order by aaa.view_id desc ,row_num  ";
			list = jdbc.query(
		        		sqlsyntax,
		        	    new ExmMatUnitViewMapper(), 
		        	    _class_id,limit,offset 
		        	);
	        //1개만 가져올때 	
	        //UsrMaster result = list.isEmpty() ? null : list.get(0);
	        return list;
		}
	    
	    @Override
	    public List<pds_file_row> getPdsRowList(long _id) {
			String sqlsyntax ="";
			List<pds_file_row> list;
			sqlsyntax = "SELECT ROW_NUMBER() OVER (ORDER BY a.row) AS row_num,COALESCE(a.row,0) as row,COALESCE(a.id,0) as id,"
					+ "  COALESCE(a.save_name,'') as save_name, COALESCE(a.save_path,'') as save_path, COALESCE(a.display_name,'') as display_name,"
					+ "  COALESCE(a.update_dt,now()) as update_dt, COALESCE(a.down_count,0) as down_count, COALESCE(b.cov_yn,'') as cov_yn  "
					+ " FROM pds_file_row as a,pds_file_hd as b  WHERE a.id = b.id and COALESCE(a.id,0) = ?  ";

		        list = jdbc.query(
		        		sqlsyntax,
		        	    new PdsFileRowMapper(), 
		        	    _id 
		        	);
		    //1개만 가져올때 	
	        //UsrMaster result = list.isEmpty() ? null : list.get(0);
	        return list;
	    }
	    @Override
	    public pds_file_hd getPdsHdListById(long _id) {
			String sqlsyntax ="";
			List<pds_file_hd> list;
				sqlsyntax = "SELECT COALESCE(c.type_nm,'') as type_nm,COALESCE(d.class_nm,'') as class_nm,COALESCE(e.subject_nm,'') as subject_nm,COALESCE(f.unit_nm,'') as unit_nm, "
						+ "ROW_NUMBER() OVER (ORDER BY a.id) AS row_num,COALESCE(a.type_id,0) as type_cd,COALESCE(a.id,0) as id,COALESCE(a.down_count,0) as down_count,COALESCE(a.file_count,0) as file_count,"
						+ "  COALESCE(a.grade_cd,'') as grade_cd,COALESCE(a.type_id,0) as type_id,COALESCE(a.class_id,0) as class_id, COALESCE(a.subject_id,0) as subject_id, COALESCE(a.unit_id,0) as unit_id,"
						+ "  COALESCE(a.update_dt,now()) as update_dt, COALESCE(a.memo,'') as memo, COALESCE(a.title,'') as title, "
						+ " COALESCE(a.user_id,'') as user_id, COALESCE(b.nick_name,'') as nick_name, COALESCE(a.cov_yn,'') as cov_yn "
						+ " FROM pds_file_hd as a "
						+ " left outer join usy_type as c on (a.type_id = c.id) \n"
						+ " left outer join usy_type_class as d on (a.class_id = d.id and a.type_id = d.type_id) \n"
						+ " left outer join usy_type_class_subject as e on (a.subject_id = e.id and a.class_id = e.class_id) \n"
						+ " left outer join usy_type_class_subject_unit as f on (a.unit_id = f.id and a.subject_id = f.subject_id)\n"
						+ " left outer join usr_master as b on(a.user_id = b.user_cd) "
						+ " WHERE "
						+ "  "
						+ " COALESCE(a.id,0) = ? order by a.update_dt desc ";
		        list = jdbc.query(
		        		sqlsyntax,
		        	    new PdsFileHdMapper(), 
		        	    _id 
		        	);
			
	        //1개만 가져올때 	
		        pds_file_hd result = list.isEmpty() ? null : list.get(0);
	        return result;
	    }
		@Override
		public List<pds_file_hd> getPdsHdList(long _class_id, long _subject_id, long _unit_id,int limit, int offset) {
			String sqlsyntax ="";
			List<pds_file_hd> list;
				sqlsyntax = "SELECT COALESCE(c.type_nm,'') as type_nm,COALESCE(d.class_nm,'') as class_nm,COALESCE(e.subject_nm,'') as subject_nm,COALESCE(f.unit_nm,'') as unit_nm, "
						+ "ROW_NUMBER() OVER (ORDER BY a.id) AS row_num,COALESCE(a.type_id,0) as type_cd,COALESCE(a.id,0) as id,COALESCE(a.down_count,0) as down_count,COALESCE(a.file_count,0) as file_count,"
						+ "  COALESCE(a.grade_cd,'') as grade_cd,COALESCE(a.type_id,0) as type_id,COALESCE(a.class_id,0) as class_id, COALESCE(a.subject_id,0) as subject_id, COALESCE(a.unit_id,0) as unit_id,"
						+ "  COALESCE(a.update_dt,now()) as update_dt, COALESCE(a.memo,'') as memo, COALESCE(a.title,'') as title, "
						+ " COALESCE(a.user_id,'') as user_id, COALESCE(b.nick_name,'') as nick_name, COALESCE(a.cov_yn,'') as cov_yn "
						+ " FROM pds_file_hd as a "
						+ " left outer join usy_type as c on (a.type_id = c.id) \n"
						+ " left outer join usy_type_class as d on (a.class_id = d.id and a.type_id = d.type_id) \n"
						+ " left outer join usy_type_class_subject as e on (a.subject_id = e.id and a.class_id = e.class_id) \n"
						+ " left outer join usy_type_class_subject_unit as f on (a.unit_id = f.id and a.subject_id = f.subject_id)\n"
						+ " left outer join usr_master as b on(a.user_id = b.user_cd) "
						+ " WHERE "
						+ "  "
						+ " COALESCE(a.class_id,0) = ? order by a.update_dt desc limit ? offset ? ";
		        list = jdbc.query(
		        		sqlsyntax,
		        	    new PdsFileHdMapper(), 
		        	    _class_id,limit,offset 
		        	);
	        //1개만 가져올때 	
	        //UsrMaster result = list.isEmpty() ? null : list.get(0);
	        return list;
		}
	    @Override
	    public List<pds_file_hd> getPdsHdList(long _class_id,long _subject_id,long _unit_id) {
			String sqlsyntax ="";
			List<pds_file_hd> list;
				sqlsyntax = "SELECT COALESCE(c.type_nm,'') as type_nm,COALESCE(d.class_nm,'') as class_nm,COALESCE(e.subject_nm,'') as subject_nm,COALESCE(f.unit_nm,'') as unit_nm, "
						+ "ROW_NUMBER() OVER (ORDER BY a.id) AS row_num,COALESCE(a.type_id,0) as type_cd,COALESCE(a.id,0) as id,COALESCE(a.down_count,0) as down_count,COALESCE(a.file_count,0) as file_count,"
						+ "  COALESCE(a.grade_cd,'') as grade_cd,COALESCE(a.type_id,0) as type_id,COALESCE(a.class_id,0) as class_id, COALESCE(a.subject_id,0) as subject_id, COALESCE(a.unit_id,0) as unit_id,"
						+ "  COALESCE(a.update_dt,now()) as update_dt, COALESCE(a.memo,'') as memo, COALESCE(a.title,'') as title, "
						+ " COALESCE(a.user_id,'') as user_id, COALESCE(b.nick_name,'') as nick_name, COALESCE(a.cov_yn,'') as cov_yn "
						+ " FROM pds_file_hd as a "
						+ " left outer join usy_type as c on (a.type_id = c.id) \n"
						+ " left outer join usy_type_class as d on (a.class_id = d.id and a.type_id = d.type_id) \n"
						+ " left outer join usy_type_class_subject as e on (a.subject_id = e.id and a.class_id = e.class_id) \n"
						+ " left outer join usy_type_class_subject_unit as f on (a.unit_id = f.id and a.subject_id = f.subject_id)\n"
						+ " left outer join usr_master as b on(a.user_id = b.user_cd) "
						+ " WHERE "
						+ "  "
						+ " COALESCE(a.class_id,0) = ? order by a.update_dt desc  ";
		        list = jdbc.query(
		        		sqlsyntax,
		        	    new PdsFileHdMapper(), 
		        	    _class_id 
		        	);
	        //1개만 가져올때 	
	        //UsrMaster result = list.isEmpty() ? null : list.get(0);
	        return list;
	    }
	    @Override
	    public List<pds_file_hd> getPdsHdList(long _subject_id,long _unit_id) {
			String sqlsyntax ="";
			List<pds_file_hd> list;
			if(_subject_id == 0) {
				sqlsyntax = "SELECT COALESCE(c.type_nm,'') as type_nm,COALESCE(d.class_nm,'') as class_nm,COALESCE(e.subject_nm,'') as subject_nm,COALESCE(f.unit_nm,'') as unit_nm, "
						+ "ROW_NUMBER() OVER (ORDER BY a.id) AS row_num,COALESCE(a.type_id,0) as type_cd,COALESCE(a.id,0) as id,COALESCE(a.down_count,0) as down_count,COALESCE(a.file_count,0) as file_count,"
						+ "  COALESCE(a.grade_cd,'') as grade_cd,COALESCE(a.type_id,0) as type_id,COALESCE(a.class_id,0) as class_id, COALESCE(a.subject_id,0) as subject_id, COALESCE(a.unit_id,0) as unit_id,"
						+ "  COALESCE(a.update_dt,now()) as update_dt, COALESCE(a.memo,'') as memo, COALESCE(a.title,'') as title, "
						+ " COALESCE(a.user_id,'') as user_id, COALESCE(b.nick_name,'') as nick_name, COALESCE(a.cov_yn,'') as cov_yn "
						+ " FROM pds_file_hd as a "
						+ " left outer join usy_type as c on (a.type_id = c.id) \n"
						+ " left outer join usy_type_class as d on (a.class_id = d.id and a.type_id = d.type_id) \n"
						+ " left outer join usy_type_class_subject as e on (a.subject_id = e.id and a.class_id = e.class_id) \n"
						+ " left outer join usy_type_class_subject_unit as f on (a.unit_id = f.id and a.subject_id = f.subject_id)\n"
						+ " left outer join usr_master as b on(a.user_id = b.user_cd) "
						+ " WHERE "
						+ "  "
						+ " COALESCE(a.subject_id,0) = ? order by a.update_dt desc  ";
		        list = jdbc.query(
		        		sqlsyntax,
		        	    new PdsFileHdMapper(), 
		        	    _subject_id 
		        	);
			}else {
				if(_unit_id ==0 ) {
					sqlsyntax = "SELECT COALESCE(c.type_nm,'') as type_nm,COALESCE(d.class_nm,'') as class_nm,COALESCE(e.subject_nm,'') as subject_nm,COALESCE(f.unit_nm,'') as unit_nm, "
							+ "ROW_NUMBER() OVER (ORDER BY a.id) AS row_num,COALESCE(a.type_id,0) as type_cd,COALESCE(a.id,0) as id,COALESCE(a.down_count,0) as down_count,COALESCE(a.file_count,0) as file_count,"
							+ "  COALESCE(a.grade_cd,'') as grade_cd,COALESCE(a.type_id,0) as type_id,COALESCE(a.class_id,0) as class_id, COALESCE(a.subject_id,0) as subject_id, COALESCE(a.unit_id,0) as unit_id,"
							+ "  COALESCE(a.update_dt,now()) as update_dt, COALESCE(a.memo,'') as memo, COALESCE(a.title,'') as title, "
							+ " COALESCE(a.user_id,'') as user_id, COALESCE(b.nick_name,'') as nick_name, COALESCE(a.cov_yn,'') as cov_yn "
							+ " FROM pds_file_hd as a "
							+ " left outer join usy_type as c on (a.type_id = c.id) \n"
							+ " left outer join usy_type_class as d on (a.class_id = d.id and a.type_id = d.type_id) \n"
							+ " left outer join usy_type_class_subject as e on (a.subject_id = e.id and a.class_id = e.class_id) \n"
							+ " left outer join usy_type_class_subject_unit as f on (a.unit_id = f.id and a.subject_id = f.subject_id)\n"
							+ " left outer join usr_master as b on(a.user_id = b.user_cd) "
							+ " WHERE "
							+ "  COALESCE(a.subject_id,0) = ? "
							+ "  order by a.update_dt desc  ";
			        list = jdbc.query(
			        		sqlsyntax,
			        	    new PdsFileHdMapper(), 
			        	    _subject_id             
			        	);
				}else {
					sqlsyntax = "SELECT COALESCE(c.type_nm,'') as type_nm,COALESCE(d.class_nm,'') as class_nm,COALESCE(e.subject_nm,'') as subject_nm,COALESCE(f.unit_nm,'') as unit_nm, "
							+ "ROW_NUMBER() OVER (ORDER BY a.id) AS row_num,COALESCE(a.type_id,0) as type_cd,COALESCE(a.id,0) as id,COALESCE(a.down_count,0) as down_count,COALESCE(a.file_count,0) as file_count,"
							+ "  COALESCE(a.grade_cd,'') as grade_cd,COALESCE(a.type_id,0) as type_id,COALESCE(a.class_id,0) as class_id, COALESCE(a.subject_id,0) as subject_id, COALESCE(a.unit_id,0) as unit_id,"
							+ "  COALESCE(a.update_dt,now()) as update_dt, COALESCE(a.memo,'') as memo, COALESCE(a.title,'') as title, "
							+ " COALESCE(a.user_id,'') as user_id, COALESCE(b.nick_name,'') as nick_name, COALESCE(a.cov_yn,'') as cov_yn "
							+ " FROM pds_file_hd as a "
							+ " left outer join usy_type as c on (a.type_id = c.id) \n"
							+ " left outer join usy_type_class as d on (a.class_id = d.id and a.type_id = d.type_id) \n"
							+ " left outer join usy_type_class_subject as e on (a.subject_id = e.id and a.class_id = e.class_id) \n"
							+ " left outer join usy_type_class_subject_unit as f on (a.unit_id = f.id and a.subject_id = f.subject_id)\n"
							+ " left outer join usr_master as b on(a.user_id = b.user_cd) "
							+ " WHERE "
							+ " COALESCE(a.unit_id,0) = ? order by a.update_dt desc  ";
			        list = jdbc.query(
			        		sqlsyntax,
			        	    new PdsFileHdMapper(), 
			        	    _unit_id                 
			        	);
					
				}
			}
	       return list;
	    }
	    
		@Override
		public List<pds_past_hd> getPdsHdPastList(long _class_id, long _subject_id, long _unit_id) {
			String sqlsyntax ="";
			List<pds_past_hd> list;
				sqlsyntax = "SELECT COALESCE(c.type_nm,'') as type_nm,COALESCE(d.class_nm,'') as class_nm,COALESCE(e.subject_nm,'') as subject_nm,COALESCE(f.unit_nm,'') as unit_nm, "
						+ "ROW_NUMBER() OVER (ORDER BY a.id) AS row_num,COALESCE(a.type_id,0) as type_cd,COALESCE(a.id,0) as id,COALESCE(a.down_count,0) as down_count,COALESCE(a.file_count,0) as file_count,"
						+ "  COALESCE(a.grade_cd,'') as grade_cd,COALESCE(a.type_id,0) as type_id,COALESCE(a.class_id,0) as class_id, COALESCE(a.subject_id,0) as subject_id, COALESCE(a.unit_id,0) as unit_id,"
						+ "  COALESCE(a.update_dt,now()) as update_dt, COALESCE(a.memo,'') as memo, COALESCE(a.title,'') as title, "
						+ " COALESCE(a.user_id,'') as user_id, COALESCE(b.nick_name,'') as nick_name, COALESCE(a.cov_yn,'') as cov_yn "
						+ " FROM pds_past_hd as a "
						+ " left outer join usy_type as c on (a.type_id = c.id) \n"
						+ " left outer join usy_type_class as d on (a.class_id = d.id and a.type_id = d.type_id) \n"
						+ " left outer join usy_type_class_subject as e on (a.subject_id = e.id and a.class_id = e.class_id) \n"
						+ " left outer join usy_type_class_subject_unit as f on (a.unit_id = f.id and a.subject_id = f.subject_id)\n"
						+ " left outer join usr_master as b on(a.user_id = b.user_cd) "
						+ " WHERE "
						+ "  "
						+ " COALESCE(a.class_id,0) = ? order by a.update_dt desc  ";
		        list = jdbc.query(
		        		sqlsyntax,
		        	    new PdsPastHdMapper(), 
		        	    _class_id 
		        	);
	        //1개만 가져올때 	
	        //UsrMaster result = list.isEmpty() ? null : list.get(0);
	        return list;
		}
		@Override
		public List<pds_past_hd> getPdsHdPastList(long _subject_id, long _unit_id) {
			String sqlsyntax ="";
			List<pds_past_hd> list;
			if(_subject_id == 0) {
				sqlsyntax = "SELECT COALESCE(c.type_nm,'') as type_nm,COALESCE(d.class_nm,'') as class_nm,COALESCE(e.subject_nm,'') as subject_nm,COALESCE(f.unit_nm,'') as unit_nm, "
						+ "ROW_NUMBER() OVER (ORDER BY a.id) AS row_num,COALESCE(a.type_id,0) as type_cd,COALESCE(a.id,0) as id,COALESCE(a.down_count,0) as down_count,COALESCE(a.file_count,0) as file_count,"
						+ "  COALESCE(a.grade_cd,'') as grade_cd,COALESCE(a.type_id,0) as type_id,COALESCE(a.class_id,0) as class_id, COALESCE(a.subject_id,0) as subject_id, COALESCE(a.unit_id,0) as unit_id,"
						+ "  COALESCE(a.update_dt,now()) as update_dt, COALESCE(a.memo,'') as memo, COALESCE(a.title,'') as title, "
						+ " COALESCE(a.user_id,'') as user_id, COALESCE(b.nick_name,'') as nick_name, COALESCE(a.cov_yn,'') as cov_yn "
						+ " FROM pds_past_hd as a "
						+ " left outer join usy_type as c on (a.type_id = c.id) \n"
						+ " left outer join usy_type_class as d on (a.class_id = d.id and a.type_id = d.type_id) \n"
						+ " left outer join usy_type_class_subject as e on (a.subject_id = e.id and a.class_id = e.class_id) \n"
						+ " left outer join usy_type_class_subject_unit as f on (a.unit_id = f.id and a.subject_id = f.subject_id)\n"
						+ " left outer join usr_master as b on(a.user_id = b.user_cd) "
						+ " WHERE "
						+ "  "
						+ " COALESCE(a.subject_id,0) = ? order by a.update_dt desc  ";
		        list = jdbc.query(
		        		sqlsyntax,
		        	    new PdsPastHdMapper(), 
		        	    _subject_id 
		        	);
			}else {
				if(_unit_id ==0 ) {
					sqlsyntax = "SELECT COALESCE(c.type_nm,'') as type_nm,COALESCE(d.class_nm,'') as class_nm,COALESCE(e.subject_nm,'') as subject_nm,COALESCE(f.unit_nm,'') as unit_nm, "
							+ "ROW_NUMBER() OVER (ORDER BY a.id) AS row_num,COALESCE(a.type_id,0) as type_cd,COALESCE(a.id,0) as id,COALESCE(a.down_count,0) as down_count,COALESCE(a.file_count,0) as file_count,"
							+ "  COALESCE(a.grade_cd,'') as grade_cd,COALESCE(a.type_id,0) as type_id,COALESCE(a.class_id,0) as class_id, COALESCE(a.subject_id,0) as subject_id, COALESCE(a.unit_id,0) as unit_id,"
							+ "  COALESCE(a.update_dt,now()) as update_dt, COALESCE(a.memo,'') as memo, COALESCE(a.title,'') as title, "
							+ " COALESCE(a.user_id,'') as user_id, COALESCE(b.nick_name,'') as nick_name, COALESCE(a.cov_yn,'') as cov_yn "
							+ " FROM pds_past_hd as a "
							+ " left outer join usy_type as c on (a.type_id = c.id) \n"
							+ " left outer join usy_type_class as d on (a.class_id = d.id and a.type_id = d.type_id) \n"
							+ " left outer join usy_type_class_subject as e on (a.subject_id = e.id and a.class_id = e.class_id) \n"
							+ " left outer join usy_type_class_subject_unit as f on (a.unit_id = f.id and a.subject_id = f.subject_id)\n"
							+ " left outer join usr_master as b on(a.user_id = b.user_cd) "
							+ " WHERE "
							+ "  COALESCE(a.subject_id,0) = ? "
							+ "  order by a.update_dt desc  ";
			        list = jdbc.query(
			        		sqlsyntax,
			        	    new PdsPastHdMapper(), 
			        	    _subject_id             
			        	);
				}else {
					sqlsyntax = "SELECT COALESCE(c.type_nm,'') as type_nm,COALESCE(d.class_nm,'') as class_nm,COALESCE(e.subject_nm,'') as subject_nm,COALESCE(f.unit_nm,'') as unit_nm, "
							+ "ROW_NUMBER() OVER (ORDER BY a.id) AS row_num,COALESCE(a.type_id,0) as type_cd,COALESCE(a.id,0) as id,COALESCE(a.down_count,0) as down_count,COALESCE(a.file_count,0) as file_count,"
							+ "  COALESCE(a.grade_cd,'') as grade_cd,COALESCE(a.type_id,0) as type_id,COALESCE(a.class_id,0) as class_id, COALESCE(a.subject_id,0) as subject_id, COALESCE(a.unit_id,0) as unit_id,"
							+ "  COALESCE(a.update_dt,now()) as update_dt, COALESCE(a.memo,'') as memo, COALESCE(a.title,'') as title, "
							+ " COALESCE(a.user_id,'') as user_id, COALESCE(b.nick_name,'') as nick_name, COALESCE(a.cov_yn,'') as cov_yn "
							+ " FROM pds_past_hd as a "
							+ " left outer join usy_type as c on (a.type_id = c.id) \n"
							+ " left outer join usy_type_class as d on (a.class_id = d.id and a.type_id = d.type_id) \n"
							+ " left outer join usy_type_class_subject as e on (a.subject_id = e.id and a.class_id = e.class_id) \n"
							+ " left outer join usy_type_class_subject_unit as f on (a.unit_id = f.id and a.subject_id = f.subject_id)\n"
							+ " left outer join usr_master as b on(a.user_id = b.user_cd) "
							+ " WHERE "
							+ " COALESCE(a.unit_id,0) = ? order by a.update_dt desc  ";
			        list = jdbc.query(
			        		sqlsyntax,
			        	    new PdsPastHdMapper(), 
			        	    _unit_id                 
			        	);
					
				}
			}
	       return list;
		}
		@Override
		public List<pds_past_row> getPdsPastRowList(long _id) {
			String sqlsyntax ="";
			List<pds_past_row> list;
			sqlsyntax = "SELECT ROW_NUMBER() OVER (ORDER BY a.row) AS row_num,COALESCE(a.row,0) as row,COALESCE(a.id,0) as id,"
					+ "  COALESCE(a.save_name,'') as save_name, COALESCE(a.save_path,'') as save_path, COALESCE(a.display_name,'') as display_name,"
					+ "  COALESCE(a.update_dt,now()) as update_dt, COALESCE(a.down_count,0) as down_count, COALESCE(b.cov_yn,'') as cov_yn  "
					+ " FROM pds_past_row as a,pds_past_hd as b  WHERE a.id = b.id and COALESCE(a.id,0) = ?  ";

		        list = jdbc.query(
		        		sqlsyntax,
		        	    new PdsPastRowMapper(), 
		        	    _id 
		        	);
		    //1개만 가져올때 	
	        //UsrMaster result = list.isEmpty() ? null : list.get(0);
	        return list;
		}
		@Override
		public pds_past_hd getPdsHdPastListById(long _id) {
			String sqlsyntax ="";
			List<pds_past_hd> list;
				sqlsyntax = "SELECT COALESCE(c.type_nm,'') as type_nm,COALESCE(d.class_nm,'') as class_nm,COALESCE(e.subject_nm,'') as subject_nm,COALESCE(f.unit_nm,'') as unit_nm, "
						+ "ROW_NUMBER() OVER (ORDER BY a.id) AS row_num,COALESCE(a.type_id,0) as type_cd,COALESCE(a.id,0) as id,COALESCE(a.down_count,0) as down_count,COALESCE(a.file_count,0) as file_count,"
						+ "  COALESCE(a.grade_cd,'') as grade_cd,COALESCE(a.type_id,0) as type_id,COALESCE(a.class_id,0) as class_id, COALESCE(a.subject_id,0) as subject_id, COALESCE(a.unit_id,0) as unit_id,"
						+ "  COALESCE(a.update_dt,now()) as update_dt, COALESCE(a.memo,'') as memo, COALESCE(a.title,'') as title, "
						+ " COALESCE(a.user_id,'') as user_id, COALESCE(b.nick_name,'') as nick_name, COALESCE(a.cov_yn,'') as cov_yn "
						+ " FROM pds_past_hd as a "
						+ " left outer join usy_type as c on (a.type_id = c.id) \n"
						+ " left outer join usy_type_class as d on (a.class_id = d.id and a.type_id = d.type_id) \n"
						+ " left outer join usy_type_class_subject as e on (a.subject_id = e.id and a.class_id = e.class_id) \n"
						+ " left outer join usy_type_class_subject_unit as f on (a.unit_id = f.id and a.subject_id = f.subject_id)\n"
						+ " left outer join usr_master as b on(a.user_id = b.user_cd) "
						+ " WHERE "
						+ "  "
						+ " COALESCE(a.id,0) = ? order by a.update_dt desc ";
		        list = jdbc.query(
		        		sqlsyntax,
		        	    new PdsPastHdMapper(), 
		        	    _id 
		        	);
			
	        //1개만 가져올때 	
		        pds_past_hd result = list.isEmpty() ? null : list.get(0);
	        return result;
		}
		@Override
		public List<pds_past_hd> getPdsHdPastList(long _class_id, long _subject_cd, long _unit_cd, int limit,	int offset) {
			String sqlsyntax ="";
			List<pds_past_hd> list;
				sqlsyntax = "SELECT COALESCE(c.type_nm,'') as type_nm,COALESCE(d.class_nm,'') as class_nm,COALESCE(e.subject_nm,'') as subject_nm,COALESCE(f.unit_nm,'') as unit_nm, "
						+ "ROW_NUMBER() OVER (ORDER BY a.id) AS row_num,COALESCE(a.type_id,0) as type_cd,COALESCE(a.id,0) as id,COALESCE(a.down_count,0) as down_count,COALESCE(a.file_count,0) as file_count,"
						+ "  COALESCE(a.grade_cd,'') as grade_cd,COALESCE(a.type_id,0) as type_id,COALESCE(a.class_id,0) as class_id, COALESCE(a.subject_id,0) as subject_id, COALESCE(a.unit_id,0) as unit_id,"
						+ "  COALESCE(a.update_dt,now()) as update_dt, COALESCE(a.memo,'') as memo, COALESCE(a.title,'') as title, "
						+ " COALESCE(a.user_id,'') as user_id, COALESCE(b.nick_name,'') as nick_name, COALESCE(a.cov_yn,'') as cov_yn "
						+ " FROM pds_past_hd as a "
						+ " left outer join usy_type as c on (a.type_id = c.id) \n"
						+ " left outer join usy_type_class as d on (a.class_id = d.id and a.type_id = d.type_id) \n"
						+ " left outer join usy_type_class_subject as e on (a.subject_id = e.id and a.class_id = e.class_id) \n"
						+ " left outer join usy_type_class_subject_unit as f on (a.unit_id = f.id and a.subject_id = f.subject_id)\n"
						+ " left outer join usr_master as b on(a.user_id = b.user_cd) "
						+ " WHERE "
						+ "  "
						+ " COALESCE(a.class_id,0) = ? order by a.update_dt desc limit ? offset ? ";
		        list = jdbc.query(
		        		sqlsyntax,
		        	    new PdsPastHdMapper(), 
		        	    _class_id,limit,offset 
		        	);
	        //1개만 가져올때 	
	        //UsrMaster result = list.isEmpty() ? null : list.get(0);
	        return list;
		}
	    
		@Override
		public Long getPdsHdMaxNo(String id) {
	        String sql = "select get_max_no(?) as id;";
	        List<usy_key_no> list = jdbc.query(
	        	    sql,
	        	    new KeyNoMapper(), 
	        	    id                             
	        	);
	        usy_key_no result = list.isEmpty() ? null : list.get(0);
	        return result.getId();
		}

		@Override
		public UsrMaster findByUserId(Long id) {
	        String sql = "SELECT "
	        		+ "	a.id,a.group_cd,a.password,a.user_cd,a.user_nm,b.type_nm,a.type_id "
	        		+ "	FROM "
	        		+ "	usr_master as a left outer join usy_type as b on (a.type_id = b.id) "
	        		+ " WHERE a.id = ?";
	        List<UsrMaster> list = jdbc.query(
	        	    sql,
	        	    new UsrMaterRowMapper(), 
	        	    id                             
	        	);
	        UsrMaster result = list.isEmpty() ? null : list.get(0);
	        return result;
		}
	    
	    @Override
	    public List<UsyConfigType> findAll() {
	        String sql = "SELECT "
	        		+ "	a.id, a.user_id, a.type_id,b.user_nm,c.type_nm "
	        		+ "	FROM "
	        		+ "	usy_config_type as a left outer join "
	        		+ "	usr_master as b on (a.user_id = b.id)"
	        		+ "	left outer join usy_type as c on(a.type_id = c.id)";
	        return jdbc.query(
	            sql,
	            (rs, rowNum) -> new UsyConfigType(
	                rs.getLong("id"),
	                rs.getLong("user_id"),
	                rs.getLong("type_id"),
	                rs.getString("user_nm"),
	                rs.getString("type_nm")
	            )
	        );
	    }
	    @Override
	    public List<UsyConfigType> findById(Long id) {
		        String sql = "SELECT "
		        		+ "	a.id, a.user_id, a.type_id,b.user_nm,c.type_nm "
		        		+ "	FROM "
		        		+ "	usy_config_type as a left outer join "
		        		+ "	usr_master as b on (a.user_id = b.id)"
		        		+ "	left outer join usy_type as c on(a.type_id = c.id)"
	                   + " WHERE a.user_id = ?";
		        List<UsyConfigType> list = jdbc.query(
		        	    sql,
		        	    new UsyConfigTypeRowMapper(), 
		        	    id                             
		        	);
		        return list;
	    }
	    
	    
		@Override
		public UsrMaster findByUserId(String id) {
	        String sql = "SELECT "
	        		+ "	a.id,a.group_cd,a.password,a.user_cd,a.user_nm,b.type_nm,a.type_id "
	        		+ "	FROM "
	        		+ "	usr_master as a left outer join usy_type as b on (a.type_id = b.id) "
	        		+ " WHERE a.user_cd = ?";
	        List<UsrMaster> list = jdbc.query(
	        	    sql,
	        	    new UsrMaterRowMapper(), 
	        	    id                             
	        	);
	        UsrMaster result = list.isEmpty() ? null : list.get(0);
	        return result;
		}	    
	    
	    @Override
	    public List<usy_type_cd> getTypeListById(Long id) {
		        String sql = "SELECT "
		        		+ "	c.id,c.type_cd,c.type_nm "
		        		+ "	FROM "
		        		+ "	usy_config_type as a left outer join "
		        		+ "	usr_master as b on (a.user_id = b.id)"
		        		+ "	left outer join usy_type as c on(a.type_id = c.id)"
	                   + " WHERE a.user_id = ?";
		        List<usy_type_cd> list = jdbc.query(
		        	    sql,
		        	    new UsyTypeCdRowMapper(), 
		        	    id                             
		        	);
		        return list;
	    }
	    @Override
	    public List<usy_type_cd> getTypeList() {
	        String sql = "SELECT "
	        		+ "	a.id, a.type_cd, a.type_nm, 0 as count "
	        		+ "	FROM "
	        		+ "	usy_type as a ";
	        return jdbc.query(
	            sql,
	            (rs, rowNum) -> new usy_type_cd(
	                rs.getLong("id"),
	                rs.getString("type_cd"),
	                rs.getString("type_nm"),
	                rs.getLong("count")
	            )
	        );
	    }
	    @Override
	    public List<usy_class_cd> getClassList(Long id) {
		        String sql = "SELECT "
		        		+ "	a.id,a.class_nm,b.id as type_id,b.type_nm,0 as count "
		        		+ "	FROM "
		        		+ "	usy_type_class as a left outer join "
		        		+ "	usy_type as b on (a.type_id = b.id)"
		        		+ "	WHERE b.id = ?";
		        List<usy_class_cd> list = jdbc.query(
		        	    sql,
		        	    new UsyClassCdRowMapper(), 
		        	    id                             
		        	);
		        return list;
	    }
	    @Override
	    public List<usy_subject_cd> getSubjectList(Long id) {
		        String sql = "SELECT 	\n"
		        		+ "a.id,a.subject_nm,b.id as class_id,b.class_nm,c.id as type_id,c.type_nm,COALESCE(d.count,0) as count 	\n"
		        		+ "FROM 	\n"
		        		+ "usy_type_class_subject as a \n"
		        		+ "left outer join usy_type_class as b on (a.class_id = b.id) \n"
		        		+ "left outer join usy_type as c on (b.type_id = c.id)	\n"
		        		+ "left outer join \n"
		        		+ "(\n"
		        		+ "select subject_id,count(subject_id) as count \n"
		        		+ "from \n"
		        		+ "exm_mat_unit_hd\n"
		        		+ "where class_id = ?  \n"
		        		+ "group by type_id,subject_id\n"
		        		+ ")as d on (d.subject_id = a.id)    \n"
		        		+ "WHERE b.id = ?\n"
		        		+ "";
		        List<usy_subject_cd> list = jdbc.query(
		        	    sql,
		        	    new UsySubjectCdRowMapper(), 
		        	    id,id                             
		        	);
		        return list;
	    }
		@Override
		public usy_subject_cd findBySubjectId(Long id) {
	        String sql = "SELECT "
	        		+ "	a.id,a.group_cd,a.password,a.user_cd,a.user_nm,b.type_nm,a.type_id "
	        		+ "	FROM "
	        		+ "	usr_master as a left outer join usy_type as b on (a.type_id = b.id) "
	        		+ " WHERE a.user_cd = ?";
	        List<usy_subject_cd> list = jdbc.query(
	        	    sql,
	        	    new UsySubjectCdRowMapper(), 
	        	    id                             
	        	);
	        usy_subject_cd result = list.isEmpty() ? null : list.get(0);
	        return result;
		}	    
	    
	    @Override
	    public List<usy_unit_cd> getUsyUnitCdList(Long _subject_id) {
		        String sql = "SELECT 	\n"
		        		+ "a.id,a.unit_nm,b.id as subject_id,b.subject_nm,c.id as class_id,c.class_nm,d.id as type_id ,d.type_nm,COALESCE(e.count,0) as count 	\n"
		        		+ "FROM 		\n"
		        		+ "usy_type_class_subject_unit as a left outer join 		\n"
		        		+ "usy_type_class_subject as b on(a.subject_id = b.id)  left outer join 	 	\n"
		        		+ "usy_type_class as c on (b.class_id = c.id) left outer join   	\n"
		        		+ "usy_type as d on (c.type_id = d.id)	\n"
		        		+ "left outer join \n"
		        		+ "(\n"
		        		+ "select unit_id,count(unit_id) as count \n"
		        		+ "from \n"
		        		+ "exm_mat_unit_hd\n"
		        		+ "where subject_id = ?  \n"
		        		+ "group by type_id,class_id,subject_id,unit_id\n"
		        		+ ")as e on (e.unit_id = a.id)    \n"
		        		+ "WHERE b.id = ?";
		        List<usy_unit_cd> list = jdbc.query(
		        	    sql,
		        	    new UsyUnitCdRowMapper(), 
		        	    _subject_id,_subject_id                             
		        	);
		        return list;
	    }
		public Long insertAndReturnId(String func,String tableName, String jsonObject) { 
			return jdbc.execute(func, (CallableStatementCallback<Long>) cs -> { 
			cs.setString(1, tableName); 
			cs.setString(2, jsonObject); 
			cs.registerOutParameter(3, Types.INTEGER); 
			cs.execute(); 
			return cs.getLong(3); 
			}); 
		}
}
