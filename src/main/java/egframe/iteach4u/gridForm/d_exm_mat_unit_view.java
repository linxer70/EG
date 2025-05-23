package egframe.iteach4u.gridForm;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import egframe.common.SysGrid;
import egframe.common.SysGridChild;
import egframe.common.SysGridChildLayout;
import egframe.iteach4u.entity.exm_mat_unit_view;
import egframe.iteach4u.entity.pds_file_hd;
import egframe.iteach4u.entity.usy_type_cd;
import egframe.iteach4u.service.Repository.Iteach4uService;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.component.popover.PopoverPosition;
import com.vaadin.flow.component.popover.PopoverVariant;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexDirection;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Binder.Binding;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.QueryParameters;
public class d_exm_mat_unit_view extends SysGrid<exm_mat_unit_view> {//implements DataWindowImpl { 

	public Column<exm_mat_unit_view> div_nm;
	public Column<exm_mat_unit_view> user_id;
	public Column<exm_mat_unit_view> context;
	public Column<exm_mat_unit_view> typenm;
	public Column<exm_mat_unit_view> classnm;
	public Column<exm_mat_unit_view> subjectnm;
	private FlexLayout popupText = new FlexLayout();
	private FlexLayout popuplayout = new FlexLayout();
	public Div popup = new Div();
	public boolean chk = false;
	
	public Long type_id;
	public Long class_id;
	public Long subject_id;
	public Long unit_id;

	
	public  PersonFilter personFilter ;
	public HeaderRow headerRow;
	public Iteach4uService sqlca;
	public d_exm_mat_unit_view() {
    	___init();
 	} 
 	
    public d_exm_mat_unit_view(@Autowired Iteach4uService dbo) {
    	sqlca = dbo;
    	___init();
    }
    public void ___init(){
    	setSizeFull();
        //this.getElement().getStyle().set("z-index", "99");
		setSelectionMode(Grid.SelectionMode.SINGLE);
	    addColumn(item -> item.getValue("id")).setHeader("순번").setAutoWidth(true).setSortable(true).setKey("id").setVisible(true);
        addColumn(item -> item.getValue("row_num")).setHeader("순번").setAutoWidth(true).setSortable(true).setKey("row_num").setVisible(false);
        div_nm = addColumn(item -> item.getValue("div_nm")).setHeader("문제유형").setAutoWidth(true).setSortable(true).setKey("div_nm");
        typenm = addColumn(item -> item.getValue("type_nm")).setHeader("분류").setAutoWidth(true).setSortable(true).setKey("type_nm");
        classnm = addColumn(item -> item.getValue("class_nm")).setHeader("학년").setAutoWidth(true).setSortable(true).setKey("class_nm");
        subjectnm = addColumn(item -> item.getValue("subject_nm")).setHeader("과목").setAutoWidth(true).setSortable(true).setKey("subject_nm");
        context= addColumn(item -> item.getValue("context")).setHeader("지문").setAutoWidth(true).setSortable(true).setKey("context");
        user_id = addColumn(item -> item.getValue("user_id")).setHeader("작성자").setAutoWidth(true).setSortable(true).setKey("user_id");//.setVisible(false);
        addColumn(item -> item.getValue("type_id")).setHeader("분류").setAutoWidth(true).setSortable(true).setKey("type_id").setVisible(false);
        addColumn(item -> item.getValue("subject_id")).setHeader("과목").setAutoWidth(true).setSortable(true).setKey("subject_id").setVisible(false);
        addColumn(item -> item.getValue("class_id")).setHeader("학년").setAutoWidth(true).setSortable(true).setKey("class_id").setVisible(false);
        addColumn(item -> item.getValue("div_cd")).setHeader("단원").setAutoWidth(true).setSortable(true).setKey("div_cd").setVisible(false);
        addColumn(item -> item.getValue("update_dt")).setHeader("등록일자").setAutoWidth(true).setSortable(true).setKey("update_dt").setVisible(false);
        addColumn(item -> item.getValue("modify")).setHeader("modify_yn").setAutoWidth(true).setSortable(true).setKey("modify").setVisible(false);
        getColumnByKey("id").setRenderer(new ComponentRenderer<>(item -> {
            Long value = item.getId();
            if (value == 0) {
                return new Span("");  // null일 때는 빈 문자열로 표시
            }
            return new Span(String.valueOf(value));  // null이 아닌 경우, 값을 그대로 표시
        }));        
        personFilter = new PersonFilter(dataView);
        headerRow = appendHeaderRow();

		addItemDoubleClickListener(event -> {
			exm_mat_unit_view clickedItem = event.getItem(); // 더블 클릭된 아이템
			insertForm(clickedItem);
        });		
        
		addItemClickListener(event -> {
			exm_mat_unit_view clickedItem = event.getItem(); // 더블 클릭된 아이템
			//insertForm(clickedItem);
        });		
    	
    }
    public Set<String> seenCategories = new HashSet<>();
    public void _setDDDW(SysGridChild list) {
        addColumn(new ComponentRenderer<>(exam_mat_unit_view -> {
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
    	   		hd_control.retrieve(exam_mat_unit_view);
    	   		popover.setOpened(true);
    	   	});
        	return span;
        })).setHeader("단원내역").setAutoWidth(true).setSortable(true).setKey("unit_cd").setVisible(true);       
    	
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

    private Span createStatusIcon(exm_mat_unit_view item) {
        Span titleSpan = new Span(item.get_title());
        titleSpan.getElement().addEventListener("click", e -> {
        	exm_mat_unit_view clickedItem = item; // 더블 클릭된 아이템
			insertForm(clickedItem);
        });
        return titleSpan;
    }
    
    private void insertForm(exm_mat_unit_view _item) {
		if (this.getUI().isPresent()) {
			
		    UI ui = this.getUI().get();
		    
		    
		    Map<String, List<String>> parametersMap = new HashMap<>();
		    parametersMap.put("id", Collections.singletonList( String.valueOf(_item.getId()) != null ?  String.valueOf(_item.getId()) : "0"));
		    parametersMap.put("type_id", Collections.singletonList(String.valueOf(_item.get_type_id()) != null ? String.valueOf(_item.get_type_id()) : "0"));
		    parametersMap.put("class_id", Collections.singletonList(String.valueOf(_item.get_class_id()) != null ? String.valueOf(_item.get_class_id()) : "0"));
		    parametersMap.put("subject_id", Collections.singletonList(String.valueOf(_item.get_subject_id()) != null ? String.valueOf(_item.get_subject_id()) : "0"));
		    parametersMap.put("unit_id", Collections.singletonList(String.valueOf(_item.get_unit_id()) != null ? String.valueOf(_item.get_unit_id()) : "0"));
		    parametersMap.put("div_cd", Collections.singletonList(String.valueOf(_item.get_div_id()) != null ? String.valueOf(_item.get_div_id()) : "0"));
		    parametersMap.put("type_nm", Collections.singletonList(_item.get_type_nm() != null ? _item.get_type_nm() : ""));
		    parametersMap.put("class_nm", Collections.singletonList(_item.get_class_nm() != null ? _item.get_class_nm() : ""));
		    parametersMap.put("subject_nm", Collections.singletonList(_item.get_subject_nm() != null ? _item.get_subject_nm() : ""));
		    parametersMap.put("unit_nm", Collections.singletonList(_item.get_unit_nm() != null ? _item.get_unit_nm() : ""));
		    parametersMap.put("div_nm", Collections.singletonList(_item.get_div_nm() != null ? _item.get_div_nm() : ""));
		    parametersMap.put("user_id", Collections.singletonList(_item.get_user_id() != null ? _item.get_user_id() : ""));
		    parametersMap.put("view_yn", Collections.singletonList(_item.get_view_yn() != null ? _item.get_view_yn() : ""));
		    parametersMap.put("visible_yn", Collections.singletonList(_item.get_visible_yn() != null ? _item.get_visible_yn() : ""));
		    parametersMap.put("title", Collections.singletonList(_item.get_title() != null ? _item.get_title() : ""));
		    parametersMap.put("update_dt", Collections.singletonList(_item.get_update_dt() != null ? _item.get_update_dt().toString() : ""));
		    Location location = UI.getCurrent().getInternals().getActiveViewLocation();
		    String currentRoute = location.getPath();
		    parametersMap.put("beforeroute", Collections.singletonList(currentRoute != null ? currentRoute.toString() : ""));

		    QueryParameters queryParameters = new QueryParameters(parametersMap);
		    String navi = "exm/unitview/"+String.valueOf(_item.get_type_id()) +"/"+String.valueOf(_item.get_type_nm())+"/"+
		    		String.valueOf(_item.get_class_id())+"/"+String.valueOf(_item.get_class_nm());
		    if(currentRoute.equals(navi)) {
			    ui.navigate("exm/unitviewinsert/"+String.valueOf(_item.getId()), queryParameters);
		    }else {
		    	
		    }
		} else {
		    // UI가 존재하지 않을 때의 처리 로직
		}
    }
    
 	private static class PersonFilter {
        private final GridListDataView<exm_mat_unit_view> dataView;

        private String _user_id;
        private String _div_nm;
        private String _context;
        private String _type_nm;
        private String _class_nm;
        private String _subject_nm;

        public PersonFilter(GridListDataView<exm_mat_unit_view> dataView) {
            this.dataView = dataView;
            if(dataView==null) {
            	
            }else {
                this.dataView.addFilter(this::test);
            }
        }

        public void set_user_id(String user_id) {
            if(dataView==null) {
            	
            }else {
                this._user_id = user_id;
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

        public void set_context(String context) {
            if(dataView==null) {
            	
            }else {
                this._context = context;
                this.dataView.refreshAll();
            }
        }

	        public boolean test(exm_mat_unit_view person) {
	        boolean matchesDiv = matches(person.get_div_nm(), _div_nm);
	        boolean matchesUser = matches(person.get_user_id(), _user_id);
            boolean matchesTitle = matches(person.get_context(),_context);
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
	
	public void  retrieve(long _class_id,long _subject_id,long _unit_id) {
		readBuffer= new ArrayList<>();
		deleteBuffer = new ArrayList<>();
		dataView = null;
		selectedrow = -1;
		readBuffer = sqlca.getExmMatUnitViewList(_class_id, _subject_id,_unit_id);
		originalBuffer = new ArrayList<>();
		originalBuffer = new ArrayList<>(readBuffer);//추가 
		if(readBuffer.size()!=0) {
			dataView = setItems((List<exm_mat_unit_view>)  readBuffer);//수정
	    	selectRow(0);
		}else {
			dataView = setItems((List<exm_mat_unit_view>)  readBuffer);//수정
	    }
	}
	public void  retrieve(long _subject_id,long _unit_id) {
		readBuffer= new ArrayList<>();
		deleteBuffer = new ArrayList<>();
		dataView = null;
		selectedrow = -1;
		readBuffer = sqlca.getExmMatUnitViewList(_subject_id,_unit_id);
		originalBuffer = new ArrayList<>();
		originalBuffer = new ArrayList<>(readBuffer);//추가 
		if(readBuffer.size()!=0) {
			dataView = setItems((List<exm_mat_unit_view>)  readBuffer);//수정
	    	selectRow(0);
		}else {
			dataView = setItems((List<exm_mat_unit_view>)  readBuffer);//수정
	    }
		
	}
	public void  retrieve(long _unit_id) {
		readBuffer= new ArrayList<>();
		deleteBuffer = new ArrayList<>();
		dataView = null;
		selectedrow = -1;
		readBuffer = sqlca.getExmMatUnitViewList(_unit_id);
		originalBuffer = new ArrayList<>();
		originalBuffer = new ArrayList<>(readBuffer);//추가 
		if(readBuffer.size()!=0) {
			dataView = setItems((List<exm_mat_unit_view>)  readBuffer);//수정
	    	selectRow(0);
		}else {
			dataView = setItems((List<exm_mat_unit_view>)  readBuffer);//수정
	    }
		
	}

	public void retrieve(long _class_cd, long _subject_cd,long _unit_cd,int limit,int offset) {
		readBuffer= new ArrayList<>();
		deleteBuffer = new ArrayList<>();
		dataView = null;
		selectedrow = -1;
		List<exm_mat_unit_view> readBuffer1 = new ArrayList<>();
		readBuffer1 =sqlca.getExmMatUnitViewList(_class_cd, _subject_cd,_unit_cd,limit,offset);
		
		originalBuffer = new ArrayList<>();
		originalBuffer = new ArrayList<>(readBuffer);//추가 
		if(readBuffer.size()!=0) {
			dataView = setItems((List<exm_mat_unit_view>)  readBuffer);//수정
				int cnt = readBuffer1.size();
				for (int i=0 ; cnt > i ;i++) {
					exm_mat_unit_view row = (exm_mat_unit_view) readBuffer1.get(i);
					dataView.addItem(row);
				}
	    	selectRow(0);
		}else {
			dataView = setItems((List<exm_mat_unit_view>)  readBuffer);//수정
	    		int cnt = readBuffer1.size();
				for (int i=0 ; cnt > i ;i++) {
					exm_mat_unit_view row = (exm_mat_unit_view) readBuffer1.get(i);
					dataView.addItem(row);
			}
	    }
	}
	
	public void addretrieve() {
		readBuffer= new ArrayList<>();
		deleteBuffer = new ArrayList<>();
		dataView = null;
		selectedrow = -1;
		List<exm_mat_unit_view> readBuffer1 = new ArrayList<>();
//		readBuffer1 =sqlca.getPdsHdList(class_id, subject_id,unit_id,pagelimit,pageoffset);
		readBuffer1 =sqlca.getExmMatUnitViewList(class_id, subject_id,unit_id,pagelimit,currentPage * pagelimit);
		
		originalBuffer = new ArrayList<>();
		originalBuffer = new ArrayList<>(readBuffer);//추가 
		if(readBuffer.size()!=0) {
			dataView = setItems((List<exm_mat_unit_view>)  readBuffer);//수정
				int cnt = readBuffer1.size();
				for (int i=0 ; cnt > i ;i++) {
					exm_mat_unit_view row = (exm_mat_unit_view) readBuffer1.get(i);
					dataView.addItem(row);
				}
	    	selectRow(0);
		}else {
			dataView = setItems((List<exm_mat_unit_view>)  readBuffer);//수정
	    		int cnt = readBuffer1.size();
				for (int i=0 ; cnt > i ;i++) {
					exm_mat_unit_view row = (exm_mat_unit_view) readBuffer1.get(i);
					dataView.addItem(row);
			}
	    }
    	
        personFilter = new PersonFilter(dataView);
 
        headerRow.getCell(div_nm).setComponent(createFilterHeader("", personFilter::set_div_nm));
        headerRow.getCell(user_id).setComponent(createFilterHeader("", personFilter::set_user_id));
        headerRow.getCell(context).setComponent(createFilterHeader("", personFilter::set_context));
        headerRow.getCell(typenm).setComponent(createFilterHeader("", personFilter::set_type_nm));
        headerRow.getCell(classnm).setComponent(createFilterHeader("", personFilter::set_class_nm));
        headerRow.getCell(subjectnm).setComponent(createFilterHeader("", personFilter::set_subject_nm));
		
	}
	

	
	public void delete() {
		exm_mat_unit_view copy = new exm_mat_unit_view(selecteditem);
		((List<exm_mat_unit_view>) deleteBuffer).add(copy);
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
