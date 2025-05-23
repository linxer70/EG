package egframe.iteach4u.service.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;

import egframe.frame.entity.UsrMaster;
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
import egframe.iteach4u.entity.usy_subject_cd;
import egframe.iteach4u.entity.usy_type_cd;
import egframe.iteach4u.entity.usy_unit_cd;

public interface Iteach4uRepository {
	JdbcTemplate getJdbc();
	List<UsyConfigType> findAll();
	List<UsyConfigType> findById(Long id);
	UsrMaster findByUserId(Long id);
	UsrMaster findByUserId(String id);
    /*
    int save(User user);
    int update(User user);
    int deleteById(Long id);
*/
	List<usy_type_cd> getTypeList();
	List<usy_type_cd> getTypeListById(Long id);
	List<usy_class_cd> getClassList(Long id);
	List<usy_subject_cd> getSubjectList(Long id);
	usy_subject_cd findBySubjectId(Long id);
	List<usy_div_cd> getDivCd(String _view_yn);
	Long getPdsHdMaxNo(String id);
	List<usy_unit_cd> getUsyUnitCdList(Long _subjectid);
	List<pds_file_hd> getPdsHdList(long _class_id, long _subject_id, long _unit_id);
	List<pds_file_hd> getPdsHdList(long _subject_id, long _unit_id);
	List<pds_file_row> getPdsRowList(long subject_id);
	pds_file_hd getPdsHdListById(long _id);
	List<pds_file_hd> getPdsHdList(long _class_cd, long _subject_cd, long _unit_cd, int limit,int offset);

	List<pds_past_hd> getPdsHdPastList(long _class_id, long _subject_id, long _unit_id);
	List<pds_past_hd> getPdsHdPastList(long _subject_id, long _unit_id);
	List<pds_past_row> getPdsPastRowList(long subject_id);
	pds_past_hd getPdsHdPastListById(long _id);
	List<pds_past_hd> getPdsHdPastList(long _class_cd, long _subject_cd, long _unit_cd, int limit,int offset);
	List<usy_div_cd> getUsyCdList(long _id);
	usy_div_cd getUsyCdById(long _id);

	List<exm_mat_unit_view> getExmMatUnitViewList(long _class_id, long _subject_id, long _unit_id, int limit, int offset);
	List<exm_mat_unit_view> getExmMatUnitViewList(long _class_id, long _subject_id, long _unit_id);
	List<exm_mat_unit_view> getExmMatUnitViewList(long _subject_id, long _unit_id);
	List<exm_mat_unit_view> getExmMatUnitViewList(long _unit_id);
	exm_mat_unit_view getExmMatUnitViewById(long _unit_id);
	List<exm_mat_unit_hd> getExmMatUnitHdById(long _unit_id);
	List<exm_mat_unit_row> getExmMatUnitRowById(long _hd_id);
	
	Long wExmMatUnitHdSave(exm_mat_unit_hd hditem);
	Long wExmMatUnitViewSave(exm_mat_unit_view viewitem);
	Long wExmMatUnitRowSave(exm_mat_unit_row viewitem);

	
	List<exm_mat_make_list> getExmMatMakeListList(long _class_id, long _subject_id, long _unit_id, int limit, int offset);
	List<exm_mat_make_list> getExmMatMakeListList(long _subject_id, long _unit_id);
	List<exm_mat_make_list> getExmMatMakeListList(long _class_id, long _subject_id, long _unit_id);
	List<exm_mat_make_list> getExmMatMakeListList(long _unit_id);
	List<exm_mat_make_list> getExmMatMakeListById(long _list_id);
	List<exm_mat_make_hd> getExmMatMakeHdById(long _unit_id);
	List<exm_mat_make_unit> getExmMatMakeUnitById(long _hd_id);
	List<exm_mat_make_list> getExmMatMakeListList(List<Long> args);
	List<exm_mat_unit_hd> getRandomExmMatUnitHd(exm_mat_make_list list);						//랜덤한 exm_mat_unit_hd 데이터 추출
	List<exm_mat_unit_hd> getRandomExmMatUnitHd(exm_mat_make_list listitem, List<Long> unitCodes);
	List<exm_mat_unit_hd> wPassUnitHdMake(exm_mat_make_list list);
	List<Long> getExamMatUnitHdUnitAllList(Long type_id, Long class_id, Long subject_id);
	List<Long> getMatHDAllList(Long type_id, Long class_id, Long subject_id, Long unit_id);
	List<exm_mat_unit_hd> getExmMatUnitHdList(List<Long> keydata);
	List<Long> getExamMatUnitHdUnitAllList(Long type_id, Long class_id, Long subject_id, List<Long> unitCodes);
	exm_mat_unit_view getExmMatMakeById(long _id);
	List<exm_mat_unit_hd> getMatHDList(Long list_id);
	exm_mat_unit_hd getExmMatUnitViewHdById(long _id);
	Long wExmMatMakeListSave(exm_mat_make_list viewitem);
	List<exm_mat_make_hd> getExmMatMakeHDList(Long list_id);
	Long wExmMatMakeHdSave(exm_mat_make_hd viewitem);
	UsrMaster getUserInfo(String username);

}
