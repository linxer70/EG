package egframe.iteach4u.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexDirection;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import egframe.common.SysToolBarControl;
import egframe.common.tinymce.TinyMce;
import egframe.frame.service.AuthenticatedUser;
import egframe.iteach4u.entity.exm_mat_make_hd;
import egframe.iteach4u.entity.exm_mat_make_list;
import egframe.iteach4u.entity.exm_mat_unit_hd;
import egframe.iteach4u.entity.exm_mat_unit_row;
import egframe.iteach4u.entity.exm_mat_unit_view;
import egframe.iteach4u.entity.usy_class_cd;
import egframe.iteach4u.entity.usy_div_cd;
import egframe.iteach4u.entity.usy_subject_cd;
import egframe.iteach4u.entity.usy_type_cd;
import egframe.iteach4u.entity.usy_unit_cd;
import egframe.iteach4u.freeForm.d_exm_mat_make_list_free;
import egframe.iteach4u.service.Repository.Iteach4uService;
import egframe.iteach4u.views.exm.ExmMakeInsert;
import jakarta.annotation.PreDestroy;
@Service
public class MAT_CONTROL extends FlexLayout{
	/*
	 * 문제 콘트롤 
	 */
	private static final long serialVersionUID = 1L;
	private FlexLayout toplayout = new FlexLayout();			//일반 버튼 
	private Button wadd = new Button("추가");
	public Iteach4uService sqlca;
	public int mcnt =0 ;		//문항 카운트 
	
	public Long id;
	public Long list_id;
	public Long view_id;

	public Long type_id;
	public Long class_id;
	public Long subject_id;
	public Long unit_id;
	
	public Long div_id = Long.valueOf(2);										//최초 생성시 기본 값 5지선다 
	public String view_yn="Y";													//최초 생성시 기본 값 지문 유 
	
	public List<MAT_UNIT> 			mat_unit_list = new ArrayList<>();		//문항들 목록 
	public MAT_UNIT 					mat_unit;									//문항 변수 선언 
	
	public List<exm_mat_unit_view>	viewlist= new ArrayList<>();			//문제지 생성시 다중 뷰을 위한 선언 
	public List<exm_mat_unit_hd>	hdlist= new ArrayList<>();				//뷰와 문항의 관계 1:다
	public List<exm_mat_unit_row>	rowlist= new ArrayList<>();				//문항과 예시의 관계 1:다

	public exm_mat_unit_view			viewitem = new exm_mat_unit_view();		//문항 생성과 조회시 키 데이터 선언 	
	public exm_mat_unit_hd 			hditem= new exm_mat_unit_hd();			//저장 시 변수 
	public exm_mat_unit_row 			rowitem= new exm_mat_unit_row();		//저장 시 변수 
	
	public List<usy_div_cd> div_combo_data;
	public usy_div_cd selecteddiv;

	public List<usy_unit_cd> unit_combo_data;
	public List<usy_subject_cd> subject_combo_data;
	public usy_unit_cd selectedunit;
	
	public exm_mat_make_list			listitem = new exm_mat_make_list();		//문항 생성과 조회시 키 데이터 선언 	
	public exm_mat_make_hd 			listhditem= new exm_mat_make_hd();			//저장 시 변수 
	public d_exm_mat_make_list_free listfree = new d_exm_mat_make_list_free() ;
	public List<exm_mat_make_hd> makehddata = new ArrayList<>();	

	public List<exm_mat_unit_hd> hddata = new ArrayList<>();	
	@Autowired
	public JdbcTemplate Jdbc;	
	public SysToolBarControl toolbar = new SysToolBarControl();
	
	public MAT_CONTROL() {//빈 문제지 추가 
		_initMatControl();
	}
	public MAT_CONTROL(AuthenticatedUser securityService,Iteach4uService _sqlca) {//빈 문제지 추가 
		sqlca = _sqlca;
		Jdbc = sqlca.getCon();
		_initMatControl();
	}
	public MAT_CONTROL(Iteach4uService _sqlca) {
		sqlca = _sqlca;
		Jdbc = sqlca.getCon();
		_initMatControl();
	}
	public MAT_CONTROL(Iteach4uService _sqlca,exm_mat_unit_view _view) {//Unit 에서 데이터 조회 view=1  hd 1 or 여러개  
		sqlca = _sqlca;
		Jdbc = sqlca.getCon();
		_initMatControl(_view);
	}
	public MAT_CONTROL(Iteach4uService _sqlca,exm_mat_make_list _list) {//Make에서 데이터 조회 
		sqlca = _sqlca;
		_init();
		mcnt = 0 ;
		listitem = _list;
		_initExamMake(listitem);
	}
	
	public void _init() {
		setFlexDirection(FlexDirection.COLUMN);
		setHeight("600px");
		//getStyle().set("border", "1px solid black");
		toplayout.setHeight("40px");
		//toplayout.getStyle().set("border", "2px solid white");
		toplayout.add(toolbar);
		add(toplayout);
	}
	
	public void _initExamMake(exm_mat_make_list _list) {
		listitem = _list;
		if(listitem.getId()==0) {//신규 
			_removeToolbar();
		}else {					//조회 
			_removeToolbar();
		}
		mcnt = 0;
		mat_unit = new MAT_UNIT(true);
	}
	public void _removeToolbar() {
		toplayout.remove(toolbar);
		remove(toplayout);
	}
	public void _addListHd() {
		listfree.setSizeFull();
		toplayout.add(listfree);
	}
	
	public void _initMatControl() {
		
	}
	public void _initMatControl(exm_mat_unit_view _view) {
		viewitem = _view;
		type_id = viewitem.get_type_id();
		class_id = viewitem.get_class_id();
		
		viewlist.add(viewitem);
		_init();
		if(viewitem.getId()==0) {								//신규 
			exm_mat_unit_hd hd = new exm_mat_unit_hd();
			hd.set_type_id(type_id);		
			hd.set_class_id(class_id);		
			hdlist.add(hd);
			_initExam(0);
			_initView(0);
			_initData(0);
		}else {													//조회			
			hdlist = sqlca.getExmMatUnitHdById(viewitem.getId());
			int k = hdlist.size();
			for(int i = 0 ;i < k ;i++) {
				_initExam(i);
				if(i==0) {
					_initView(i);
				}
				_initData(i);
			}
		}
		_addEvent();
		_setData(viewitem,hdlist);
	}
	private void _setData(exm_mat_unit_view _view, List<exm_mat_unit_hd> _hdlist) {
		
	}
	public void _initExam(int k ) {
		mat_unit = new MAT_UNIT(true);
		if(k==0) {
			mat_unit.setViewVisible(true);
		}else {
			mat_unit.setViewVisible(false);
		}
		mat_unit._setTitile(k);
		mat_unit_list.add(mat_unit);
		mat_unit._removeAdd();
		add(mat_unit_list.get(k));
		mat_unit_list.get(k)._insertAdd();
	}
	public void _initData(int k ) {
		exm_mat_unit_hd hd = hdlist.get(k); 
		
		MAT_UNIT matunit = mat_unit_list.get(k);
		EXAM_UNIT examunit = matunit.exam_unit;
		
		examunit.hd_mce.setPresentationValue(hd.get_context());
		
		subject_id = hd.get_subject_id();
		
		unit_combo_data = sqlca.getUsyUnitCdList(subject_id);
		examunit.unit_combo_data = unit_combo_data;
		examunit.unit_combo.setItems(unit_combo_data);
		
		Long divid = hd.get_div_id();
		
		selecteddiv = div_combo_data.stream().filter(usy_div_cd -> usy_div_cd.getId().equals(divid)).findFirst().orElse(null);
		
		if (selecteddiv != null) {
			examunit.div_combo.setItems(div_combo_data);
			examunit.div_combo.setValue(selecteddiv);
			//examunit.div_combo.setClassName("hide-dropdown-arrow-combobox");
			//examunit.div_combo.setEnabled(false);
		}    			
		
		Long unitid = hd.get_unit_id();
		
		selectedunit = unit_combo_data.stream().filter(usy_unit_cd -> usy_unit_cd.getId().equals(unitid)).findFirst().orElse(null);
		if (selectedunit != null) {
			examunit.unit_combo.setItems(unit_combo_data);
			examunit.unit_combo.setValue(selectedunit);
			//examunit.unit_combo.setClassName("hide-dropdown-arrow-combobox");
			//examunit.unit_combo.setEnabled(false);
		}
		
		List<exm_mat_unit_row> rows = new ArrayList() ;
		if(hd.getId()==null||hd.getId()==0) {
			int j = 5;
			for(int i = 0 ;i< j ;i++) {
				exm_mat_unit_row row = new exm_mat_unit_row();
				
				rows.add(row);
				if(i==0) {
					//examunit.row_01.setPresentationValue(row.get_context());
				}else if(i==1) {
					//examunit.row_02.setPresentationValue(row.get_context());
				}else if(i==2) {
					//examunit.row_03.setPresentationValue(row.get_context());
				}else if(i==3) {
					//examunit.row_04.setPresentationValue(row.get_context());
				}else if(i==4) {
					//examunit.row_05.setPresentationValue(row.get_context());
				}
			}			
		}else {
			rows = sqlca.getExmMatUnitRowById(hd.getId());
			int j = rows.size();
			if(j>0) {
				for(int i = 0 ;i< j ;i++) {
					exm_mat_unit_row row = rows.get(i);
					if(i==0) {
						examunit.row_01.setPresentationValue(row.get_context());
					}else if(i==1) {
						examunit.row_02.setPresentationValue(row.get_context());
					}else if(i==2) {
						examunit.row_03.setPresentationValue(row.get_context());
					}else if(i==3) {
						examunit.row_04.setPresentationValue(row.get_context());
					}else if(i==4) {
						examunit.row_05.setPresentationValue(row.get_context());
					}
				}			
			}else {//주관식 이거나 데이터 미입력 
				int jj = 5;
				for(int i = 0 ;i< jj ;i++) {
					exm_mat_unit_row row = new exm_mat_unit_row();
					row.set_hd_id(hd.getId());
					row.set_view_id(hd.get_view_id());
					
					rows.add(row);
					if(i==0) {
						//examunit.row_01.setPresentationValue(row.get_context());
					}else if(i==1) {
						//examunit.row_02.setPresentationValue(row.get_context());
					}else if(i==2) {
						//examunit.row_03.setPresentationValue(row.get_context());
					}else if(i==3) {
						//examunit.row_04.setPresentationValue(row.get_context());
					}else if(i==4) {
						//examunit.row_05.setPresentationValue(row.get_context());
					}
				}			
			}
		}
		matunit._setData(viewitem,hd,rows);
	}
	private void _getData(MAT_UNIT _matunit) {
		exm_mat_unit_view view = _matunit.viewitem;
		exm_mat_unit_hd hd = _matunit.hditem;
		List<exm_mat_unit_row> row = _matunit.rowlist;
	}
	public void _initView(int k ) {
		exm_mat_unit_hd hd = hdlist.get(k); 
		MAT_UNIT matunit = mat_unit_list.get(k);
		
		matunit.view_mce.setPresentationValue(viewitem.get_context());
		
		EXAM_UNIT examunit = matunit.exam_unit;
		
		class_id = viewitem.get_subject_id();
		
		subject_combo_data = sqlca.getSubjectList(class_id);
		examunit.subject_combo_data = subject_combo_data;
		
		view_yn = viewitem.get_view_yn();
		
		div_combo_data= sqlca.getDivCd(view_yn);
		examunit.div_combo_data = div_combo_data;
		examunit.div_combo.setItems(div_combo_data);
	}
	public void _subjectChanged(Long _subject) {
		if(listitem!=null) {
			listitem.set_subject_id(_subject);
		}
		viewitem.set_subject_id(_subject);
		subject_id = viewitem.get_subject_id();
		int k = hdlist.size();
		if(k>0) {
			for(int i = 0 ;i < k ;i++) {
				exm_mat_unit_hd hd = hdlist.get(i); 
				hd.set_subject_id(subject_id);
				
				MAT_UNIT matunit = mat_unit_list.get(i);
				EXAM_UNIT examunit = matunit.exam_unit;
				unit_combo_data= sqlca.getUsyUnitCdList(subject_id);
				examunit.unit_combo_data = unit_combo_data;
				examunit.unit_combo.setItems(unit_combo_data);
				
				Long unitid = hd.get_unit_id();
				selectedunit = unit_combo_data.stream().filter(usy_unit_cd -> usy_unit_cd.getId().equals(unitid)).findFirst().orElse(null);
				if (selectedunit != null) {
					examunit.unit_combo.setItems(unit_combo_data);
					examunit.unit_combo.setValue(selectedunit);
					//examunit.unit_combo.setClassName("hide-dropdown-arrow-combobox");
					//examunit.unit_combo.setEnabled(false);
				}
				
			}
		}
		
	}
	public void _viewYnChanged(String flag) {
		viewitem.set_view_yn(flag);
		view_yn = viewitem.get_view_yn();
		int k = hdlist.size();
		if(k>0) {
			for(int i = 0 ;i < k ;i++) {
				exm_mat_unit_hd hd = hdlist.get(i); 
				MAT_UNIT matunit = mat_unit_list.get(i);
				EXAM_UNIT examunit = matunit.exam_unit;
				div_combo_data= sqlca.getDivCd(view_yn);
				examunit.div_combo_data = div_combo_data;
				examunit.div_combo.setItems(div_combo_data);
				
				Long divid = hd.get_div_id();
				selecteddiv = div_combo_data.stream().filter(usy_div_cd -> usy_div_cd.getId().equals(divid)).findFirst().orElse(null);
				
				if (selecteddiv != null) {
					examunit.div_combo.setItems(div_combo_data);
					examunit.div_combo.setValue(selecteddiv);
					//examunit.div_combo.setClassName("hide-dropdown-arrow-combobox");
					//examunit.div_combo.setEnabled(false);
				}    			
				
			}
		}
		
	}
	
	public void _addEvent() {
	}
	
	public void _addMatControl() {
        getParentRecursively(this, ExmMakeInsert.class).ifPresent(parent -> {
//            ((ExmMakeInsert) parent)._addMatControl();
        });   
        	int k = hdlist.size();
			exm_mat_unit_hd hd = new exm_mat_unit_hd();
			hd.setId(Long.valueOf(0));
			hd.set_view_id(id);
			hd.set_type_id(type_id);
			hd.set_class_id(class_id);
			hd.set_subject_id(subject_id);
			hd.set_unit_id(unit_id);
			hd.set_div_id(div_id);
			hdlist.add(hd);
			_initExam(k);
			_initData(k);
	}
	
	private Optional<Component> getParentRecursively(Component component, Class<?> targetClass) {
	    Optional<Component> parent = component.getParent();
	    while (parent.isPresent()) {
	        if (targetClass.isInstance(parent.get())) {
	            return parent;
	        }
	        parent = parent.get().getParent();
	    }
	    return Optional.empty();
	}	
	
	public void setMce(TinyMce tiny) {
		toolbar.setMce(tiny);
	}
	public void wDelete(Long mat_num) {
	 	String callFunctionQuery;
	 	Long hdid = mat_num;
	 	callFunctionQuery = "delete from exm_mat_unit_hd where id = ?;";
       Jdbc.update(callFunctionQuery,hdid);
	 	callFunctionQuery = "delete from exm_mat_unit_row where hd_id = ?;";
	 	Jdbc.update(callFunctionQuery,hdid);
	 	
	}
	public boolean wUnitCreate(List<Long> unitCodes) {
		   if(listitem.getId()==null) {
			   listitem.setId(0L);
		   }
		   if(listitem.get_tot_num()==0) {
			   return false;
		   }
		   if(listitem.get_type_id()==null||listitem.get_type_id()==0) {
			   return false;
		   }
		   if(listitem.get_class_id()==null||listitem.get_class_id()==0) {
			   return false;
		   }
		   if(listitem.get_subject_id()==null||listitem.get_subject_id()==0) {
			   return false;
		   }
		   hddata = new ArrayList<>();
		   hddata = sqlca.getRandomExmMatUnitHd(listitem,unitCodes);		//랜덤한 hd데이터 추출
		   if(hddata==null) {
	  			Notification notification = new Notification("선택된 항목의 문항이 존재하지 않습니다.");
	  			notification.setDuration(2000);
	  			notification.setPosition(Position.MIDDLE);
	  		    notification.open();
			   return false;
		   }
		   for(int i = 0 ;i < mat_unit_list.size();i++) {
				remove(mat_unit_list.get(i));
		   }
		   for(int i = 0 ;i < hddata.size();i++) {
			   exm_mat_make_hd hd = new exm_mat_make_hd();
			   hd.set_hd_id(hddata.get(i).getId());
			   makehddata.add(hd);
		   }
		   
		   mat_unit_list = new ArrayList<>();
		   mat_unit_list.clear();

		   if (hddata.isEmpty()) return false; // 데이터가 없으면 바로 종료

		   Long prevViewNo = hddata.get(0).get_view_id(); // 첫 번째 view_no로 초기화
		   for (int i = 0; i < hddata.size(); i++) {
		       Long currentViewNo = hddata.get(i).get_view_id();
		       viewitem = sqlca.getExmMatMakeById(currentViewNo);
		       hditem = hddata.get(i);
		       rowlist = sqlca.getExmMatUnitRowById(hditem.getId());
		       
		       MAT_UNIT mat = new MAT_UNIT(hditem);
		       mat.setData(viewitem, hditem, rowlist);
		       mat._setTitile(i);
		       mat._resizeMce();

		       mat_unit_list.add(mat);
		       mat._removeAdd();
		       add(mat_unit_list.get(i));
		   }
		   
			return true;
	}
	public void wListView() {
		   if(listitem.getId()==null) {
			   listitem.setId(0L);
		   }
		   if(listitem.get_tot_num()==0) {
			   return ;
		   }
		   if(listitem.get_type_id()==null||listitem.get_type_id()==0) {
			   return ;
		   }
		   if(listitem.get_class_id()==null||listitem.get_class_id()==0) {
			   return ;
		   }
		   if(listitem.get_subject_id()==null||listitem.get_subject_id()==0) {
			   return ;
		   }
		   hddata = new ArrayList<>();
		   
		   hddata = sqlca.getMatHDList(listitem.getId());
		   makehddata = sqlca.getExmMatMakeHDList(listitem.getId());
		   for(int i = 0 ;i < mat_unit_list.size();i++) {
				remove(mat_unit_list.get(i));
		   }
		   
		   mat_unit_list = new ArrayList<>();
		   mat_unit_list.clear();

		   if (hddata.isEmpty()) return ; // 데이터가 없으면 바로 종료

		   Long prevViewNo = hddata.get(0).get_view_id(); // 첫 번째 view_no로 초기화
		   for (int i = 0; i < hddata.size(); i++) {
		       Long currentViewNo = hddata.get(i).get_view_id();
		       viewitem = sqlca.getExmMatMakeById(currentViewNo);
		       
		       hditem = hddata.get(i);
		       rowlist = sqlca.getExmMatUnitRowById(hditem.getId());
		       
		       MAT_UNIT mat = new MAT_UNIT(hditem);//여기서 인자로 문항넘버를 받아서 우츨으로 이동 
		       mat.setData(viewitem, hditem, rowlist);
		       mat._setTitile(i);
		       mat._resizeMce();
		       mat_unit_list.add(mat);
		       mat._removeAdd();
		       add(mat_unit_list.get(i));
		   }
	}
	public void wExamView() {
		   if(listitem.getId()==null) {
			   listitem.setId(0L);
		   }
		   if(listitem.get_tot_num()==0) {
			   return ;
		   }
		   if(listitem.get_type_id()==null||listitem.get_type_id()==0) {
			   return ;
		   }
		   if(listitem.get_class_id()==null||listitem.get_class_id()==0) {
			   return ;
		   }
		   if(listitem.get_subject_id()==null||listitem.get_subject_id()==0) {
			   return ;
		   }
		   hddata = new ArrayList<>();
		   
		   hddata = sqlca.getMatHDList(listitem.getId());
		   makehddata = sqlca.getExmMatMakeHDList(listitem.getId());
		   for(int i = 0 ;i < mat_unit_list.size();i++) {
				remove(mat_unit_list.get(i));
		   }
		   
		   mat_unit_list = new ArrayList<>();
		   mat_unit_list.clear();

		   if (hddata.isEmpty()) return ; // 데이터가 없으면 바로 종료

		   Long prevViewNo = hddata.get(0).get_view_id(); // 첫 번째 view_no로 초기화
		   for (int i = 0; i < hddata.size(); i++) {
		       Long currentViewNo = hddata.get(i).get_view_id();
		       viewitem = sqlca.getExmMatMakeById(currentViewNo);
		       
		       hditem = hddata.get(i);
		       rowlist = sqlca.getExmMatUnitRowById(hditem.getId());
		       
		       MAT_UNIT mat = new MAT_UNIT(hditem);//여기서 인자로 문항넘버를 받아서 우츨으로 이동 
		       mat.setData(viewitem, hditem, rowlist);
		       mat._setTitile(i);
		       mat._resizeMce();
		       mat.setViewMat();
		       mat_unit_list.add(mat);
		       mat._removeAdd();
		       add(mat_unit_list.get(i));
		   }
	}
}
