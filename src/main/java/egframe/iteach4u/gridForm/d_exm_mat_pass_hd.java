package egframe.iteach4u.gridForm;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;

import egframe.common.DataWindowImpl;
import egframe.common.SysGrid;
import egframe.common.SysGridChild;
import egframe.common.SysGridChildLayout;
import egframe.common.SysGridLayout;
import egframe.iteach4u.entity.exm_mat_pass_hd;
import egframe.iteach4u.entity.exm_mat_pass_list;
import egframe.iteach4u.service.Repository.Iteach4uService;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.component.popover.PopoverPosition;
import com.vaadin.flow.component.popover.PopoverVariant;
import com.vaadin.flow.component.shared.Tooltip;
import com.vaadin.flow.component.shared.Tooltip.TooltipPosition;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexDirection;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Binder.Binding;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.QueryParameters;

import dev.mett.vaadin.tooltip.Tooltips;
public class d_exm_mat_pass_hd extends SysGrid<exm_mat_pass_hd> implements DataWindowImpl {
	public Long type_id;
	public Long class_id;
	public Long subject_id;
	public Long unit_id;
	
	public Column<exm_mat_pass_hd> update_id;
	public Column<exm_mat_pass_hd> unit_cd;
	public Column<exm_mat_pass_hd> tot_num;
	public Column<exm_mat_pass_hd> passed_nm;
	private FlexLayout popupText = new FlexLayout();
	private FlexLayout popuplayout = new FlexLayout();
	public Div popup = new Div();
    public Set<String> seenCategories = new HashSet<>();

	public Iteach4uService sqlca;
	public  PersonFilter personFilter ;
	public HeaderRow headerRow;
	public int zindex = 9999 ;
	

	public d_exm_mat_pass_hd() {
    	___init();
 	} 
 	
    public d_exm_mat_pass_hd(@Autowired Iteach4uService dbo) {
    	sqlca = dbo;
    	___init();
    }
    public void ___init(){
    	setSizeFull();
		setSelectionMode(Grid.SelectionMode.SINGLE);
		Set<exm_mat_pass_hd> selectedItems = new HashSet<>();
		addColumn(item -> item.getValue("no")).setHeader("순번").setAutoWidth(true).setSortable(true).setKey("no").setVisible(true);
	   unit_cd = addColumn(item -> item.getValue("exam_type")).setHeader("처음 컬럼").setAutoWidth(true).setSortable(true).setKey("unit_cd");
		passed_nm =addColumn(item -> item.getValue("passed_nm")).setHeader("문제지명").setAutoWidth(true).setSortable(true).setKey("passed_nm");
       update_id = addColumn(item -> item.getValue("update_id")).setHeader("작성자").setAutoWidth(true).setSortable(true).setKey("update_id");//.setVisible(false);
		addColumn(item -> item.getValue("exam_type")).setHeader("문제지유형").setAutoWidth(true).setSortable(true).setKey("exam_type").setVisible(true);
		tot_num = addColumn(item -> item.getValue("tot_num")).setHeader("문항수").setAutoWidth(true).setSortable(true).setKey("tot_num");
		addColumn(item -> item.getValue("time_limit")).setHeader("제한시간").setAutoWidth(true).setSortable(true).setKey("time_limit").setVisible(true);
       addColumn(item -> item.getValue("update_dt")).setHeader("등록일자").setAutoWidth(true).setSortable(true).setKey("update_dt").setVisible(true);
       addColumn(item -> item.getValue("modify")).setHeader("modify_yn").setAutoWidth(true).setSortable(true).setKey("modify").setVisible(false);

     
        personFilter = new PersonFilter(dataView);
        headerRow = appendHeaderRow();

		addItemDoubleClickListener(event -> {
			exm_mat_pass_hd clickedItem = event.getItem(); // 더블 클릭된 아이템
			insertForm(clickedItem);
        });		
        
		addItemClickListener(event -> {
			exm_mat_pass_hd clickedItem = event.getItem(); // 더블 클릭된 아이템
			//insertForm(clickedItem);
        });	
    	
    }
    public void _setDDDW(SysGridChild list) {
    	//SysGridChildLayout hd_control = new SysGridChildLayout(this,list);
    	
        addColumn(new ComponentRenderer<>(pass_mat_subject_list -> {
         	Span span = new Span();
        	 Popover popover = new Popover();
             popover.setTarget(span);
             popover.setWidth("300px");
             popover.addThemeVariants(PopoverVariant.ARROW,
                     PopoverVariant.LUMO_NO_PADDING);
             popover.setPosition(PopoverPosition.BOTTOM);
             popover.setModal(true);
             popover.setAriaLabelledBy("notifications-heading");
             popover.setOpened(false);
         	SysGridChildLayout hd_control = new SysGridChildLayout(this,list);

         	hd_control.setHeight("400px");
         	popover.add(hd_control);
         	Button button = new Button("단원내역"); 
         	span.add(button,popover);
         	
         	button.getElement().executeJs("""
         		    this.addEventListener('click', function(e) {
         		        e.stopPropagation();
         		        e.preventDefault();
         		        $0.$server.handleButtonClick();
         		    });
         		""", getElement());
         	
         	button.addClickListener(e->{
         		UI.getCurrent().getPage().executeJs("""
         			    const grid = $0;
         			    const popover = $1;
         			    popover.style.width = grid.offsetWidth + 'px';
         			""", hd_control.getElement(), popover.getElement());       		
    	   		//int list_no = pass_mat_subject_list.get_list_no();
    	   		hd_control.retrieve(pass_mat_subject_list);
    	   		//System.out.println(list_no);
    	   		popover.setOpened(true);
    	   	});
        	return span;
        })).setHeader("단원내역").setAutoWidth(true).setSortable(true).setKey("unit_cd").setVisible(true);       
    	
    }
    private Checkbox createCheckBox(exm_mat_pass_hd item) {
	    Checkbox checkbox = new Checkbox();
	    checkbox.addValueChangeListener(event -> {
	        if (event.getValue()) {
	        	item.set_chk(true);
	        } else {
	        	item.set_chk(false);
	        }
	    });
	    return checkbox;
    	
    }

	public void  DialogPreView() {
		HorizontalLayout layout = new HorizontalLayout();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        Button delbutton = new Button("삭제");
        Button exitbutton = new Button("취소");
        Button msg = new Button("데이터를 삭제하시겠습니까?");
        
        popuplayout.setFlexDirection(FlexDirection.COLUMN);
        popuplayout.getStyle().set("border", "1px solid black");
		delbutton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		exitbutton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delbutton.addClickListener(event -> {
        	layout.remove(delbutton);
        	layout.remove(exitbutton);
        	popupText.remove(msg);
        	//remove(popup);
        });
        exitbutton.addClickListener(event -> {
        	layout.remove(delbutton);
        	layout.remove(exitbutton);
        	popupText.remove(msg);
        	//remove(popup);
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

    
    private void insertForm(exm_mat_pass_hd _item) {
		if (this.getUI().isPresent()) {
			
		    UI ui = this.getUI().get();
		    
		    
		    Map<String, List<String>> parametersMap = new HashMap<>();
		    parametersMap.put("List_id", Collections.singletonList( String.valueOf(_item.getId()) != null ?  String.valueOf(_item.getId()) : ""));
		    parametersMap.put("tot_num", Collections.singletonList( String.valueOf(_item.get_tot_num()) != null ?  String.valueOf(_item.get_tot_num()) : ""));
		    parametersMap.put("list_time", Collections.singletonList( String.valueOf(_item.get_time_limit()) != null ?  String.valueOf(_item.get_time_limit()) : ""));
		    parametersMap.put("passed_nm", Collections.singletonList(_item.get_passed_nm() != null ? _item.get_passed_nm() : ""));
		    parametersMap.put("type_id", Collections.singletonList(String.valueOf(_item.get_type_id()) != null ? String.valueOf(_item.get_type_id()) : ""));
		    parametersMap.put("class_id", Collections.singletonList(String.valueOf(_item.get_class_id()) != null ? String.valueOf(_item.get_class_id()) : ""));
		    parametersMap.put("subject_id", Collections.singletonList(String.valueOf(_item.get_subject_id()) != null ? String.valueOf(_item.get_subject_id()) : ""));
		    parametersMap.put("unit_id", Collections.singletonList(String.valueOf(_item.get_unit_id()) != null ? String.valueOf(_item.get_unit_id()) : ""));
		    parametersMap.put("type_nm", Collections.singletonList(_item.get_type_nm() != null ? _item.get_type_nm() : ""));
		    parametersMap.put("exam_type", Collections.singletonList(_item.get_exam_type() != null ? _item.get_exam_type() : ""));
		    parametersMap.put("class_nm", Collections.singletonList(_item.get_class_nm() != null ? _item.get_class_nm() : ""));
		    parametersMap.put("subject_nm", Collections.singletonList(_item.get_subject_nm() != null ? _item.get_subject_nm() : ""));
		    parametersMap.put("update_id", Collections.singletonList(_item.get_update_id() != null ? _item.get_update_id() : ""));
		    parametersMap.put("update_dt", Collections.singletonList(_item.get_update_dt() != null ? _item.get_update_dt().toString() : ""));
		    Location location = UI.getCurrent().getInternals().getActiveViewLocation();
		    String currentRoute = location.getPath();
		    parametersMap.put("beforeroute", Collections.singletonList(currentRoute != null ? currentRoute.toString() : ""));

		    QueryParameters queryParameters = new QueryParameters(parametersMap);
		    String navi = "exm/examview/"+String.valueOf(_item.get_type_id()) +"/"+String.valueOf(_item.get_type_nm())+"/"+
		    		String.valueOf(_item.get_class_id())+"/"+String.valueOf(_item.get_class_nm());
		    if(currentRoute.equals(navi)) {
			    ui.navigate("exm/examviewinsert/"+String.valueOf(_item.getId()), queryParameters);
		    }else {
		    	
		    }
		} else {
		    // UI가 존재하지 않을 때의 처리 로직
		}
    }
    
 	private static class PersonFilter {
        private final GridListDataView<exm_mat_pass_hd> dataView;

        private String _update_id;
        private String _div_nm;
        private String _passed_nm;
        private String _type_nm;
        private String _class_nm;
        private String _subject_nm;

        public PersonFilter(GridListDataView<exm_mat_pass_hd> dataView) {
            this.dataView = dataView;
            if(dataView==null) {
            	
            }else {
                this.dataView.addFilter(this::test);
            }
        }

        public void set_update_id(String user_id) {
            if(dataView==null) {
            	
            }else {
                this._update_id = user_id;
                this.dataView.refreshAll();
            }
        }
        public void set_div_nm(String div_nm) {
            if(dataView==null) {
            	
            }else {
                this._div_nm = div_nm;
                this.dataView.refreshAll();
            }
        }
        public void set_type_nm(String class_nm) {
            if(dataView==null) {
            	
            }else {
                this._type_nm = class_nm;
                this.dataView.refreshAll();
            }
        }
        public void set_class_nm(String class_nm) {
            if(dataView==null) {
            	
            }else {
                this._class_nm = class_nm;
                this.dataView.refreshAll();
            }
        }
        public void set_subject_nm(String subject_nm) {
            if(dataView==null) {
            	
            }else {
                this._subject_nm = subject_nm;
                this.dataView.refreshAll();
            }
        }

        public void set_passed_nm(String passed_nm) {
            if(dataView==null) {
            	
            }else {
                this._passed_nm = passed_nm;
                this.dataView.refreshAll();
            }
        }

	        public boolean test(exm_mat_pass_hd person) {
	        boolean matchesDiv = matches(person.get_div_nm(), _div_nm);
	        boolean matchesUser = matches(person.get_update_id(), _update_id);
            boolean matchesTitle = matches(person.get_passed_nm(),_passed_nm);
            boolean matchesType = matches(person.get_type_nm(),_type_nm);
            boolean matchesClass = matches(person.get_class_nm(),_class_nm);
            boolean matchesSubject = matches(person.get_subject_nm(),_subject_nm);

            return matchesUser && matchesDiv&& matchesTitle&& matchesType&& matchesClass&& matchesSubject ;
        }

        private boolean matches(String value, String searchTerm) {
            boolean val = searchTerm == null || searchTerm.isEmpty()|| value.toLowerCase().contains(searchTerm.toLowerCase());
            return val;
        }
	}

private static Component createFilterHeader(String labelText,Consumer<String> filterChangeConsumer) {
    NativeLabel label = new NativeLabel(labelText);
    label.getStyle().set("padding-top", "var(--lumo-space-m)").set("font-size", "var(--lumo-font-size-xs)");
    TextField textField = new TextField();
    textField.setValueChangeMode(ValueChangeMode.EAGER);
    textField.setClearButtonVisible(true);
    textField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
    textField.setWidthFull();
    textField.getStyle().set("max-width", "100%");
    textField.addValueChangeListener(
    		e -> filterChangeConsumer.accept(e.getValue())
    );
    VerticalLayout layout = new VerticalLayout(label, textField);
    layout.getThemeList().clear();
    layout.getThemeList().add("spacing-xs");

    return layout;
}	  
    
	public void retrieve(String _type_cd, String _class_cd, String _subject_cd) {
		readBuffer= new ArrayList<>();
		deleteBuffer = new ArrayList<>();
		dataView = null;
		selectedrow = -1;
		readBuffer = new ArrayList<exm_mat_pass_hd>(getList(_type_cd, _class_cd, _subject_cd));
		originalBuffer = new ArrayList<>();
		originalBuffer = new ArrayList<>(readBuffer);//추가 
    	if(readBuffer.size()!=0) {
			dataView = setItems((List<exm_mat_pass_hd>)  readBuffer);//수정
        	selectRow(0);
    	}else {
    		dataView = setItems((List<exm_mat_pass_hd>)  readBuffer);//수정
        }
    	
        personFilter = new PersonFilter(dataView);
 
        headerRow.getCell(update_id).setComponent(createFilterHeader("", personFilter::set_update_id));
        headerRow.getCell(passed_nm).setComponent(createFilterHeader("", personFilter::set_passed_nm));
	}
	public List<exm_mat_pass_hd> getList(String _type_cd, String _class_cd, String _subject_cd ) {
		final String sqlsyntax = "select a.no,\n"
				+ "a.no as list_no,a.passed_nm,a.exam_type,a.tot_num,a.time_limit, a.update_id,a.update_dt,\n"
				+ "bbb.type_cd ,bbb.class_cd,bbb.subject_cd,b.name as type_nm,c.name as class_nm,d.name as subject_nm\n"
				+ "from \n"
				+ "pass_mat_make_list as a \n"
				+ "left outer join pass_mat_make_hd as bb on (a.no = bb.list_no)\n"
				+ "left outer join exam_mat_unit_hd as bbb on (bb.hd_no = bbb.no)\n"
				+ "left outer join codeb as b on (bbb.type_cd = b.no )  \n"
				+ "left outer join codem as c on (bbb.type_cd = c.bno and bbb.class_cd = c.no)  \n"
				+ "left outer join codes as d on (bbb.class_cd = d.mno and bbb.subject_cd = d.no)  \n"
				+ "left outer join codess as f on (f.sno = d.no and bbb.unit_cd = f.no)\n"
				+ "left outer join usy_div_cd e  on (bbb.div_cd = e.div_cd)\n"
				+ "where \n"
				+ "COALESCE(bbb.type_cd,'') like ?  and COALESCE(bbb.class_cd,'') like ?  and COALESCE(bbb.subject_cd,'') like ?  \n"
				+ " group by  a.no ,a.passed_nm,a.exam_type,a.tot_num,a.time_limit, a.update_id,a.update_dt,\n"
				+ " bbb.type_cd ,bbb.class_cd,bbb.subject_cd,b.name ,c.name ,d.name \n"
				+ "order by a.no desc";
		
		List<exm_mat_pass_hd> list = new ArrayList<>();
		ResultSet rs = null;
		PreparedStatement preparedStatement= null;
		Connection connection = null;
		try {
			//connection = sqlca.getDataSource().getConnection();
			preparedStatement = connection.prepareStatement(sqlsyntax);
			preparedStatement.setString(1,_type_cd);
			preparedStatement.setString(2,_class_cd);
			preparedStatement.setString(3,_subject_cd);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				exm_mat_pass_hd module = new exm_mat_pass_hd();
				module.setId(rs.getLong("id"));
				module.set_list_no(rs.getInt("list_no"));
				module.set_tot_num(rs.getInt("tot_num"));
				module.set_time_limit(rs.getInt("time_limit"));
				module.set_passed_nm(rs.getString("passed_nm"));
				module.set_exam_type(rs.getString("exam_type"));
				module.set_type_id(rs.getLong("type_id"));
				module.set_class_id(rs.getLong("class_id"));
				module.set_subject_id(rs.getLong("subject_id"));
				module.set_type_nm(rs.getString("type_nm"));
				module.set_class_nm(rs.getString("class_nm"));
				module.set_subject_nm(rs.getString("subject_nm"));
				module.set_update_dt(rs.getDate("update_dt").toLocalDate());
				module.set_update_id(rs.getString("update_id"));
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
	public void retrieve(String _type_cd, String _class_cd, String _subject_cd,List<String> _unit_cd) {
		readBuffer= new ArrayList<>();
		deleteBuffer = new ArrayList<>();
		dataView = null;
		selectedrow = -1;
//		readBuffer = new ArrayList<exm_mat_pass_hd>(getList(_type_cd, _class_cd, _subject_cd,_unit_cd));
		originalBuffer = new ArrayList<>();
		originalBuffer = new ArrayList<>(readBuffer);//추가 
    	if(readBuffer.size()!=0) {
			dataView = setItems((List<exm_mat_pass_hd>)  readBuffer);//수정
        	selectRow(0);
    	}else {
    		dataView = setItems((List<exm_mat_pass_hd>)  readBuffer);//수정
        }
    	
        personFilter = new PersonFilter(dataView);
 
        headerRow.getCell(update_id).setComponent(createFilterHeader("", personFilter::set_update_id));
        headerRow.getCell(passed_nm).setComponent(createFilterHeader("", personFilter::set_passed_nm));
	}

	public void delete() {
		exm_mat_pass_hd copy = new exm_mat_pass_hd(selecteditem);
		((List<exm_mat_pass_hd>) deleteBuffer).add(copy);
		dataView.removeItem(selecteditem);
		originalBuffer.remove(selecteditem);
		dataView.refreshAll();
		if(readBuffer.size()!=0) {
			selectRow(selectedrow);
		}else {
			selectedrow = -1;
		}
		
	}
}
