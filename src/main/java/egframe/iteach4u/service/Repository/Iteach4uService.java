package egframe.iteach4u.service.Repository;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

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

@Service
public class Iteach4uService {
	private final Iteach4uRepository userRepo;

    @Autowired
    public Iteach4uService(Iteach4uRepository userRepo) {
        this.userRepo = userRepo;
    }
    public Iteach4uRepository getRePo() {
    	return this.userRepo;
    }
    public UsrMaster getUserInfo(String viewitem){
    	return userRepo.getUserInfo(viewitem);
    }
    public Long wExmMatMakeHdSave(exm_mat_make_hd viewitem){
    	return userRepo.wExmMatMakeHdSave(viewitem);
    }
    public Long wExmMatMakeListSave(exm_mat_make_list viewitem){
    	return userRepo.wExmMatMakeListSave(viewitem);
    }
    public List<exm_mat_make_hd> getExmMatMakeHDList(Long list_id){
    	return userRepo.getExmMatMakeHDList(list_id);
    }
    
    public List<exm_mat_unit_hd> getMatHDList(Long list_id){
    	return userRepo.getMatHDList(list_id);
    }

    public exm_mat_unit_view getExmMatMakeById(long _id){
    	return userRepo.getExmMatMakeById(_id);
    }
    
	public List<exm_mat_unit_hd> getRandomExmMatUnitHd(exm_mat_make_list listitem, List<Long> unitCodes) {
    	return userRepo.getRandomExmMatUnitHd(listitem,unitCodes);
	}
    public List<exm_mat_unit_hd> getRandomExmMatUnitHd(exm_mat_make_list listitem){
    	return userRepo.getRandomExmMatUnitHd(listitem);
    }
    public List<exm_mat_unit_hd> wPassUnitHdMake(exm_mat_make_list viewitem){
    	return userRepo.wPassUnitHdMake(viewitem);
    }
    public Long wExmMatUnitViewSave(exm_mat_unit_view viewitem){
    	return userRepo.wExmMatUnitViewSave(viewitem);
    }
    public Long wExamHdSave(exm_mat_unit_hd viewitem){
    	return userRepo.wExmMatUnitHdSave(viewitem);
    }
    public Long wExamRowSave(exm_mat_unit_row viewitem){
    	return userRepo.wExmMatUnitRowSave(viewitem);
    }
    
    public List<usy_div_cd> getDivCd(String _div_cd){
    	return userRepo.getDivCd(_div_cd);
    }
    public usy_div_cd getUsyCdById(long _div_cd){
    	return userRepo.getUsyCdById(_div_cd);
    }
    public List<usy_div_cd> getUsyCdList(long _div_cd){
    	return userRepo.getUsyCdList(_div_cd);
    }
    
    
    public List<exm_mat_make_unit> getExmMatMakeUnitById(long _unit_id){
    	return userRepo.getExmMatMakeUnitById(_unit_id);
    }
    public List<exm_mat_make_hd> getExmMatMakeHdById(long _unit_id){
    	return userRepo.getExmMatMakeHdById(_unit_id);
    }
    public List<exm_mat_make_list> getExmMatMakeListById(long _unit_id){
    	return userRepo.getExmMatMakeListById(_unit_id);
    }
    public List<exm_mat_make_list> getExmMatMakeListList(long _class_id, long _subject_id, long _unit_id,int offset,int limit){
    	return userRepo.getExmMatMakeListList(_class_id,_subject_id, _unit_id,offset,limit);
    }
    public List<exm_mat_make_list> getExmMatUnitListList(long _subject_id, long _unit_id){
    	return userRepo.getExmMatMakeListList(_subject_id, _unit_id);
    }
    public List<exm_mat_make_list> getExmMatUnitListList(long _unit_id){
    	return userRepo.getExmMatMakeListList(_unit_id);
    }
    public List<exm_mat_make_list> getExmMatUnitListList(long _class_id, long _subject_id, long _unit_id){
    	return userRepo.getExmMatMakeListList(_class_id,_subject_id, _unit_id);
    }
    
    
    
    public List<exm_mat_unit_row> getExmMatUnitRowById(long _unit_id){
    	return userRepo.getExmMatUnitRowById(_unit_id);
    }
    public List<exm_mat_unit_hd> getExmMatUnitHdById(long _unit_id){
    	return userRepo.getExmMatUnitHdById(_unit_id);
    }
    public exm_mat_unit_view getExmMatUnitViewById(long _unit_id){
    	return userRepo.getExmMatUnitViewById(_unit_id);
    }
    public List<exm_mat_unit_view> getExmMatUnitViewList(long _class_id, long _subject_id, long _unit_id,int offset,int limit){
    	return userRepo.getExmMatUnitViewList(_class_id,_subject_id, _unit_id,offset,limit);
    }
    public List<exm_mat_unit_view> getExmMatUnitViewList(long _subject_id, long _unit_id){
    	return userRepo.getExmMatUnitViewList(_subject_id, _unit_id);
    }
    public List<exm_mat_unit_view> getExmMatUnitViewList(long _unit_id){
    	return userRepo.getExmMatUnitViewList(_unit_id);
    }
    public List<exm_mat_unit_view> getExmMatUnitViewList(long _class_id, long _subject_id, long _unit_id){
    	return userRepo.getExmMatUnitViewList(_class_id,_subject_id, _unit_id);
    }
    public List<pds_past_row> getPdsPastRowList(long _subject_id){
    	return userRepo.getPdsPastRowList(_subject_id);
    }
	public List<pds_past_hd> getPdsHdPastList(long _class_id, long _subject_id, long _unit_id,int offset,int limit) {
    	return userRepo.getPdsHdPastList(_class_id,_subject_id, _unit_id,offset,limit);
	}
    public List<pds_past_hd> getPdsHdPastList(long _subject_id, long _unit_id){
    	return userRepo.getPdsHdPastList(_subject_id, _unit_id);
    }
	public List<pds_past_hd> getPdsHdPastList(long _class_id, long _subject_id, long _unit_id) {
    	return userRepo.getPdsHdPastList(_class_id,_subject_id, _unit_id);
	}
    public pds_past_hd getPdsHdPastListById(long _id){
    	return userRepo.getPdsHdPastListById(_id);
    }
    public List<pds_file_row> getPdsRowList(long _subject_id){
    	return userRepo.getPdsRowList(_subject_id);
    }
	public List<pds_file_hd> getPdsHdList(long _class_id, long _subject_id, long _unit_id,int offset,int limit) {
    	return userRepo.getPdsHdList(_class_id,_subject_id, _unit_id,offset,limit);
	}
    public List<pds_file_hd> getPdsHdList(long _subject_id, long _unit_id){
    	return userRepo.getPdsHdList(_subject_id, _unit_id);
    }
	public List<pds_file_hd> getPdsHdList(long _class_id, long _subject_id, long _unit_id) {
    	return userRepo.getPdsHdList(_class_id,_subject_id, _unit_id);
	}
    public pds_file_hd getPdsHdListById(long _id){
    	return userRepo.getPdsHdListById(_id);
    }
    public Long getPdsHdMaxNo(String _id){
    	return userRepo.getPdsHdMaxNo(_id);
    }
    public List<UsyConfigType> findAllUsers() {
        return userRepo.findAll();
    }

    public List<UsyConfigType> findUser(Long id) {
        return userRepo.findById(id);
    }
    public List<usy_type_cd> getTypeList(){
        return userRepo.getTypeList();
    }
    public List<usy_class_cd> getClassList(Long id){
        return userRepo.getClassList(id);
    }
    public List<usy_subject_cd> getSubjectList(Long id){
        return userRepo.getSubjectList(id);
    }
	public List<usy_unit_cd> getUsyUnitCdList(Long id) {
		return userRepo.getUsyUnitCdList(id);
	}    
    public List<usy_type_cd> getTypeListById(Long id){
        return userRepo.getTypeListById(id);
    }
    public UsrMaster findByUserId(Long id) {
    	return userRepo.findByUserId(id);
    }
    public UsrMaster findByUserId(String id) {
    	return userRepo.findByUserId(id);
    }
    public JdbcTemplate getCon() {
    	return userRepo.getJdbc();
    }
    /*
    public boolean createUser(User user) {
        return userRepo.save(user) == 1;
    }

    public boolean updateUser(User user) {
        return userRepo.update(user) == 1;
    }

    public boolean deleteUser(Long id) {
        return userRepo.deleteById(id) == 1;
    }
*/
	public usy_subject_cd findBySubjectId(Long valueOf) {
		// TODO Auto-generated method stub
		return userRepo.findBySubjectId(valueOf);
	}
	public List<exm_mat_make_list> getExmMatMakeListList(List<Long> args) {
		return userRepo.getExmMatMakeListList(args);
	}
}
