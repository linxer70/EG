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
import egframe.iteach4u.entity.exm_mat_make_list;
import egframe.iteach4u.entity.exm_mat_pass_list;
import egframe.iteach4u.entity.pds_file_hd;
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
public class d_exm_mat_make_list extends SysGrid<exm_mat_make_list> implements DataWindowImpl {
	public Long type_id;
	public Long class_id;
	public Long subject_id;
	public Long unit_id;
	
	public Column<exm_mat_make_list> listid;
	public Column<exm_mat_make_list> examtype;
	public Column<exm_mat_make_list> typeid;
	public Column<exm_mat_make_list> classid;
	public Column<exm_mat_make_list> subjectid;
	public Column<exm_mat_make_list> unitid;
	public Column<exm_mat_make_list> typenm;
	public Column<exm_mat_make_list> classnm;
	public Column<exm_mat_make_list> subjectnm;
	public Column<exm_mat_make_list> unitnm;
	public Column<exm_mat_make_list> totnum;
	public Column<exm_mat_make_list> updateid;
	public Column<exm_mat_make_list> passednm;
	public Column<exm_mat_make_list> timelimit;
	public Column<exm_mat_make_list> updatedt;
	private FlexLayout popupText = new FlexLayout();
	private FlexLayout popuplayout = new FlexLayout();
	public Div popup = new Div();
    public Set<String> seenCategories = new HashSet<>();

	public Iteach4uService sqlca;
	public  PersonFilter personFilter ;
	public HeaderRow headerRow;
	public int zindex = 9999 ;
	

	public d_exm_mat_make_list() {
    	init();
    	unvisibleColumn();
 	} 
 	
    public d_exm_mat_make_list(@Autowired Iteach4uService dbo) {
    	sqlca = dbo;
    	init();
    	unvisibleColumn();
    }
    public void init(){
    	setSizeFull();
		setSelectionMode(Grid.SelectionMode.SINGLE);
		Set<exm_mat_make_list> selectedItems = new HashSet<>();
		listid = addColumn(item -> item.getValue("id")).setHeader("순번").setAutoWidth(true).setSortable(true).setKey("id");
		examtype = addColumn(item -> item.getValue("exam_type")).setHeader("문제지유형").setAutoWidth(true).setSortable(true).setKey("exam_type");
		typeid = addColumn(item -> item.getValue("type_id")).setHeader("분류").setAutoWidth(true).setSortable(true).setKey("type_id");
		classid = addColumn(item -> item.getValue("class_id")).setHeader("학년").setAutoWidth(true).setSortable(true).setKey("class_id");
		subjectid = addColumn(item -> item.getValue("subject_id")).setHeader("과목").setAutoWidth(true).setSortable(true).setKey("subject_id");
		unitid = addColumn(item -> item.getValue("unit_id")).setHeader("단원").setAutoWidth(true).setSortable(true).setKey("unit_id");
		typenm = addColumn(item -> item.getValue("type_nm")).setHeader("분류").setAutoWidth(true).setSortable(true).setKey("type_nm");
		classnm = addColumn(item -> item.getValue("class_nm")).setHeader("학년").setAutoWidth(true).setSortable(true).setKey("class_nm");
		subjectnm = addColumn(item -> item.getValue("subject_nm")).setHeader("과목").setAutoWidth(true).setSortable(true).setKey("subject_nm");
		unitnm = addColumn(item -> item.getValue("exam_type")).setHeader("단원").setAutoWidth(true).setSortable(true).setKey("unit_nm");
		passednm =addColumn(item -> item.getValue("passed_nm")).setHeader("문제지명").setAutoWidth(true).setSortable(true).setKey("passed_nm");
       updateid = addColumn(item -> item.getValue("update_id")).setHeader("작성자").setAutoWidth(true).setSortable(true).setKey("update_id");//.setVisible(false);
		totnum = addColumn(item -> item.getValue("tot_num")).setHeader("문항수").setAutoWidth(true).setSortable(true).setKey("tot_num");
		timelimit = addColumn(item -> item.getValue("time_limit")).setHeader("제한시간").setAutoWidth(true).setSortable(true).setKey("time_limit");
       updatedt = addColumn(item -> item.getValue("update_dt")).setHeader("등록일자").setAutoWidth(true).setSortable(true).setKey("update_dt");
       addColumn(item -> item.getValue("modify")).setHeader("modify_yn").setAutoWidth(true).setSortable(true).setKey("modify").setVisible(false);
     
       binder = new Binder<>(exm_mat_make_list.class);
       editor = getEditor();
       editor.setBinder(binder);
        personFilter = new PersonFilter(dataView);
        headerRow = appendHeaderRow();

		addItemDoubleClickListener(event -> {
			exm_mat_make_list clickedItem = event.getItem(); // 더블 클릭된 아이템
			insertForm(clickedItem);
        });		
        
		addItemClickListener(event -> {
			exm_mat_make_list clickedItem = event.getItem(); // 더블 클릭된 아이템
			//insertForm(clickedItem);
        });	
    	
    }
    public void unvisibleColumn() {
    	typeid.setVisible(false);
    	classid.setVisible(false);
    	subjectid.setVisible(false);
    	unitid.setVisible(false);
    	typenm.setVisible(false);
    	classnm.setVisible(false);
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
    private Checkbox createCheckBox(exm_mat_pass_list item) {
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

    
    private void insertForm(exm_mat_make_list _item) {
		if (this.getUI().isPresent()) {
			
		    UI ui = this.getUI().get();
		    
		    
		    Map<String, List<String>> parametersMap = new HashMap<>();
		    parametersMap.put("List_id", Collections.singletonList( String.valueOf(_item.getId()) != null ?  String.valueOf(_item.getId()) : ""));
		    parametersMap.put("id", Collections.singletonList( String.valueOf(_item.getId()) != null ?  String.valueOf(_item.getId()) : ""));
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
		    String navi = "exm/makeview/"+String.valueOf(_item.get_type_id()) +"/"+String.valueOf(_item.get_type_nm())+"/"+
		    		String.valueOf(_item.get_class_id())+"/"+String.valueOf(_item.get_class_nm());
		    if(currentRoute.equals(navi)) {
			    ui.navigate("exm/makeviewinsert/"+String.valueOf(_item.getId()), queryParameters);
		    }else {
		    	
		    }
		} else {
		    // UI가 존재하지 않을 때의 처리 로직
		}
    }
    
 	private static class PersonFilter {
        private final GridListDataView<exm_mat_make_list> dataView;

        private String _update_id;
        private String _div_nm;
        private String _passed_nm;
        private String _type_nm;
        private String _class_nm;
        private String _subject_nm;

        public PersonFilter(GridListDataView<exm_mat_make_list> dataView) {
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

	        public boolean test(exm_mat_make_list person) {
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
    

public void retrieve(Long args ) {
	readBuffer= new ArrayList<>();
	deleteBuffer = new ArrayList<>();
	dataView = null;
	selectedrow = -1;
	List<exm_mat_make_list> items = sqlca.getExmMatMakeListById(args);
	readBuffer = items;
	originalBuffer = new ArrayList<>();
	originalBuffer = new ArrayList<>(readBuffer);//추가 
	if(readBuffer.size()!=0) {
		dataView = setItems((List<exm_mat_make_list>)  readBuffer);//수정
    	selectRow(0);
	}else {
		dataView = setItems((List<exm_mat_make_list>)  readBuffer);//수정
    }
	
    personFilter = new PersonFilter(dataView);

    headerRow.getCell(updateid).setComponent(createFilterHeader("", personFilter::set_update_id));
    headerRow.getCell(passednm).setComponent(createFilterHeader("", personFilter::set_passed_nm));
}

	public void retrieve(List<Long> args ) {
		readBuffer= new ArrayList<>();
		deleteBuffer = new ArrayList<>();
		dataView = null;
		selectedrow = -1;
		List<exm_mat_make_list> items = sqlca.getExmMatMakeListList(args);
		readBuffer = items;
		originalBuffer = new ArrayList<>();
		originalBuffer = new ArrayList<>(readBuffer);//추가 
    	if(readBuffer.size()!=0) {
			dataView = setItems((List<exm_mat_make_list>)  readBuffer);//수정
        	selectRow(0);
    	}else {
    		dataView = setItems((List<exm_mat_make_list>)  readBuffer);//수정
        }
    	
        personFilter = new PersonFilter(dataView);
 
        headerRow.getCell(updateid).setComponent(createFilterHeader("", personFilter::set_update_id));
        headerRow.getCell(passednm).setComponent(createFilterHeader("", personFilter::set_passed_nm));
	}
	public void retrieve(String _type_cd, String _class_cd, String _subject_cd,List<String> _unit_cd) {
		readBuffer= new ArrayList<>();
		deleteBuffer = new ArrayList<>();
		dataView = null;
		selectedrow = -1;
		//readBuffer = new ArrayList<exm_mat_make_list>(getList(_type_cd, _class_cd, _subject_cd,_unit_cd));
		originalBuffer = new ArrayList<>();
		originalBuffer = new ArrayList<>(readBuffer);//추가 
    	if(readBuffer.size()!=0) {
			dataView = setItems((List<exm_mat_make_list>)  readBuffer);//수정
        	selectRow(0);
    	}else {
    		dataView = setItems((List<exm_mat_make_list>)  readBuffer);//수정
        }
    	
        personFilter = new PersonFilter(dataView);
 
        headerRow.getCell(updateid).setComponent(createFilterHeader("", personFilter::set_update_id));
        headerRow.getCell(passednm).setComponent(createFilterHeader("", personFilter::set_passed_nm));
	}
 	
	public void delete() {
		exm_mat_make_list copy = new exm_mat_make_list(selecteditem);
		((List<exm_mat_make_list>) deleteBuffer).add(copy);
		dataView.removeItem(selecteditem);
		originalBuffer.remove(selecteditem);
		dataView.refreshAll();
		if(readBuffer.size()!=0) {
			selectRow(selectedrow);
		}else {
			selectedrow = -1;
		}
	}
	public void insert() {
		exm_mat_make_list select = new exm_mat_make_list();//수정
		 select.setModify("I");
		 if(selectedrow == -1) {
			 if(readBuffer.size()==0||readBuffer==null) {
					readBuffer= new ArrayList<>();
					deleteBuffer = new ArrayList<>();
					originalBuffer = new ArrayList<>();
					originalBuffer = new ArrayList<>(readBuffer);//추가 
					dataView = setItems((List<exm_mat_make_list>)  readBuffer);//수정
			 }
			dataView.addItem(select);
			((List<exm_mat_make_list>)originalBuffer).add(select);
		 }else {
			 dataView.addItemAfter(select, selecteditem);
			 ((List<exm_mat_make_list>)originalBuffer).add(selectedrow+1,select);
		 }
		 dataView.refreshAll();
		 selectRow(selectedrow+1);
	}
	
}
