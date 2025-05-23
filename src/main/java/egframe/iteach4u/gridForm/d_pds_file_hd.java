package egframe.iteach4u.gridForm;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import egframe.common.SysEntity;
import egframe.common.SysGrid;
import egframe.common.SysGridLayout;
//import egframe.data.entitycontrol.sys_column_dic;
//import egframe.data.service.SysDBO;
import egframe.iteach4u.entity.pds_file_hd;
import egframe.iteach4u.service.Repository.Iteach4uService;


import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.data.provider.SortDirection;

public class d_pds_file_hd extends SysGrid<pds_file_hd> {//implements DataWindowImpl { 

	public Column<pds_file_hd> user_id;
	public Column<pds_file_hd> title;
	public Column<pds_file_hd> nick_name;
	public Iteach4uService sqlca;
	public  PersonFilter personFilter ;
	public HeaderRow headerRow;
	public Long type_id;
	public Long class_id;
	public Long subject_id;
	public Long unit_id;
	public String type_nm;
	public String class_nm;
	public String subject_nm;
	public String unit_nm;
	public int currentpage;
	public int limit;
	public int offset;
	private AtomicBoolean isDoubleClickHandling = new AtomicBoolean(false);
	public d_pds_file_hd() {
    	___init();
 	} 
 	
    public d_pds_file_hd(@Autowired Iteach4uService dbo) {
    	sqlca = dbo;
    	___init();
    }
    public d_pds_file_hd(@Autowired Iteach4uService dbo,SysGridLayout _parent) {
    	sqlca = dbo;
    	___init();
    	sysgridlayout = _parent;
    }
	private static void addCloseHandler(Component textField,
            Editor<pds_file_hd> editor) {
        textField.getElement().addEventListener("keydown", e -> editor.cancel())
                .setFilter("event.code === 'Escape'");
    }	
    public void ___init(){
    	
    	
    	setSizeFull();
		setSelectionMode(Grid.SelectionMode.SINGLE);
        addColumn(item -> item.getValue("id")).setHeader("자료번호").setAutoWidth(true).setSortable(true).setKey("id").setVisible(true);
        Grid.Column<pds_file_hd> firstNameColumn = getColumnByKey("id");
        firstNameColumn.setWidth("7rem");
        firstNameColumn.setFlexGrow(0);
        addColumn(item -> item.getValue("row_num")).setHeader("순번").setAutoWidth(true).setSortable(true).setKey("row_num").setVisible(false);
        addColumn(item -> item.getValue("type_id")).setHeader("구분").setAutoWidth(true).setSortable(true).setKey("type_id").setVisible(false);
        addColumn(item -> item.getValue("class_nm")).setHeader("학년").setAutoWidth(true).setSortable(true).setKey("class_nm").setVisible(true);
        addColumn(item -> item.getValue("subject_nm")).setHeader("과목").setAutoWidth(true).setSortable(true).setKey("subject_nm").setVisible(true);
        addColumn(item -> item.getValue("unit_nm")).setHeader("단원").setAutoWidth(true).setSortable(true).setKey("unit_nm").setVisible(true);
        title= addColumn(item ->item.get_title()).setHeader("제목").setAutoWidth(true).setSortable(true).setKey("title");
        addColumn(item -> item.getValue("file_counter")).setHeader("첨부파일수").setAutoWidth(true).setSortable(true).setKey("file_counter");
        Grid.Column<pds_file_hd> NameColumn = getColumnByKey("file_counter");
        NameColumn.setWidth("7rem");
        NameColumn.setFlexGrow(0);
        user_id = addColumn(item -> item.getValue("user_id")).setHeader("작성자").setAutoWidth(true).setSortable(true).setKey("user_id");//.setVisible(false);
        addColumn(item -> item.getValue("nick_name")).setHeader("닉네임").setAutoWidth(true).setSortable(true).setKey("nick_name").setVisible(false);
        addColumn(item -> item.getValue("update_dt")).setHeader("등록일자").setAutoWidth(true).setSortable(true).setKey("update_dt").setVisible(false);
        addColumn(item -> item.getValue("memo")).setHeader("파일개요").setAutoWidth(true).setSortable(true).setKey("memo").setVisible(true);
        addColumn(item -> item.getValue("modify")).setHeader("modify_yn").setAutoWidth(true).setSortable(true).setKey("modify").setVisible(false);
//        bind = new BeanValidationBinder<>(pds_file_hd.class);
//        bind.setBean(new pds_file_hd()); 
        binder = new Binder<>(pds_file_hd.class);
        editor = getEditor();
        editor.setBinder(binder);
        
        TextField title_field = new TextField();
        addCloseHandler(title_field, editor);
        
        binder.forField(title_field).asRequired("program_nm must not be empty")
                //.withValidator(new EmailValidator("Enter a valid email address"))
                //.withStatusLabel(emailValidationMessage)
                .bind(pds_file_hd::get_title, pds_file_hd::set_title);
        title.setEditorComponent(title_field);              
        
        this.setPageSize(25);
        personFilter = new PersonFilter(dataView);
        headerRow = appendHeaderRow();
        
        addItemClickListener(e -> {
        	if (isDoubleClickHandling.get()) {
                return;
            }
          handleSingleClick(e);
            
        });
        
		addItemDoubleClickListener(event -> {
		   isDoubleClickHandling.set(true);
			handleDoubleClick(event);
		   isDoubleClickHandling.set(false);
        });

		addSortListener(event -> {
			sysgridlayout.sortEvent(event);
		});
		
    }

	private void handleSingleClick(ItemClickEvent<pds_file_hd> event) {
		System.out.println("Single Click Event: ");
		/*
     	editor.editItem(event.getItem());
        Component editorComponent = event.getColumn().getEditorComponent();
        if (editorComponent instanceof Focusable) {
            ((Focusable) editorComponent).focus();
            getSelectionModel().select(event.getItem());
        }
        */
	}
    private void handleDoubleClick(ItemDoubleClickEvent<pds_file_hd> event) {
		System.out.println("Double Click Event: ");
        pds_file_hd clickedItem = event.getItem(); // 더블 클릭된 아이템
        openDetail(clickedItem);
	}

	public void setParent(SysGridLayout _parent) {
		sysgridlayout = _parent;
	}

    public void openDetail(pds_file_hd clickedItem) {
        //Notification.show("Double-clicked on: " + clickedItem);
		if (this.getUI().isPresent()) {
			
		    UI ui = this.getUI().get();
		    
		    
		    Map<String, List<String>> parametersMap = new HashMap<>();
		    parametersMap.put("id", Collections.singletonList( String.valueOf(clickedItem.getId()) != null ?  String.valueOf(clickedItem.getId()) : ""));
		    parametersMap.put("type_id", Collections.singletonList(String.valueOf(clickedItem.get_type_id()) != null ? String.valueOf(clickedItem.get_type_id()) : ""));
		    parametersMap.put("class_id", Collections.singletonList(String.valueOf(clickedItem.get_class_id()) != null ? String.valueOf(clickedItem.get_class_id()) : ""));
		    parametersMap.put("subject_id", Collections.singletonList(String.valueOf(clickedItem.get_subject_id()) != null ? String.valueOf(clickedItem.get_subject_id()) : ""));
		    parametersMap.put("unit_id", Collections.singletonList(String.valueOf(clickedItem.get_unit_id()) != null ? String.valueOf(clickedItem.get_unit_id()) : ""));
		    parametersMap.put("type_nm", Collections.singletonList(String.valueOf(clickedItem.get_type_nm()) != null ? String.valueOf(clickedItem.get_type_nm()) : ""));
		    parametersMap.put("class_nm", Collections.singletonList(String.valueOf(clickedItem.get_class_nm()) != null ? String.valueOf(clickedItem.get_class_nm()) : ""));
		    parametersMap.put("subject_nm", Collections.singletonList(String.valueOf(clickedItem.get_subject_nm()) != null ? String.valueOf(clickedItem.get_subject_nm()) : ""));
		    parametersMap.put("unit_nm", Collections.singletonList(String.valueOf(clickedItem.get_unit_nm()) != null ? String.valueOf(clickedItem.get_unit_nm()) : ""));
		    parametersMap.put("user_id", Collections.singletonList(clickedItem.get_user_id() != null ? clickedItem.get_user_id() : ""));
		    parametersMap.put("title", Collections.singletonList(clickedItem.get_title() != null ? clickedItem.get_title() : ""));
		    parametersMap.put("update_dt", Collections.singletonList(clickedItem.get_update_dt() != null ? clickedItem.get_update_dt().toString() : ""));
		    
		    Location location = UI.getCurrent().getInternals().getActiveViewLocation();
		    String currentRoute = location.getPath();
		    parametersMap.put("beforeroute", Collections.singletonList(currentRoute != null ? currentRoute.toString() : ""));

		    QueryParameters queryParameters = new QueryParameters(parametersMap);
		    String navi = "pds/dataview/"+String.valueOf(clickedItem.get_type_id()) +"/"+String.valueOf(clickedItem.get_type_nm())+"/"+
		    		String.valueOf(clickedItem.get_class_id())+"/"+String.valueOf(clickedItem.get_class_nm());
		    if(currentRoute.equals(navi)) {
			    ui.navigate("pds/dataviewinsert/"+String.valueOf(clickedItem.getId()), queryParameters);
		    }else {
		    	
		    }
		} else {
		    // UI가 존재하지 않을 때의 처리 로직
		}
    }
    
    
 	private static class PersonFilter {
        private final GridListDataView<pds_file_hd> dataView;

        private String _user_id;
        private String _nick_name;
        private String _title;

        public PersonFilter(GridListDataView<pds_file_hd> dataView) {
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
        public void set_nick_name(String nick_name) {
            if(dataView==null) {
            	
            }else {
                this._nick_name = nick_name;
                this.dataView.refreshAll();
            }
        }

        public void set_title(String title) {
            if(dataView==null) {
            	
            }else {
                this._title = title;
                this.dataView.refreshAll();
            }
        }

	        public boolean test(pds_file_hd person) {
            boolean matchesUser = matches(person.get_user_id(), _user_id);
            boolean matchesTitle = matches(person.get_title(),_title);
            boolean matchesNick = matches(person.get_nick_name(),_nick_name);

            return matchesUser && matchesTitle && matchesNick;
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
	readBuffer = sqlca.getPdsHdList(_class_id,_subject_id,_unit_id);
	originalBuffer = new ArrayList<>();
	originalBuffer = new ArrayList<>(readBuffer);//추가 
	if(readBuffer.size()!=0) {
		dataView = setItems((List<pds_file_hd>)  readBuffer);//수정
    	selectRow(0);
	}else {
		dataView = setItems((List<pds_file_hd>)  readBuffer);//수정
    }
	
    personFilter = new PersonFilter(dataView);

    headerRow.getCell(user_id).setComponent(createFilterHeader("등록자", personFilter::set_user_id));
    headerRow.getCell(title).setComponent(createFilterHeader("제목", personFilter::set_title));
    //headerRow.getCell(nick_name).setComponent(createFilterHeader("닉네임", personFilter::set_nick_name));
    
}
public void  retrieve(long _subject_id,long _unit_id) {
	readBuffer= new ArrayList<>();
	deleteBuffer = new ArrayList<>();
	dataView = null;
	selectedrow = -1;
	readBuffer = sqlca.getPdsHdList(_subject_id,_unit_id);
	originalBuffer = new ArrayList<>();
	originalBuffer = new ArrayList<>(readBuffer);//추가 
	if(readBuffer.size()!=0) {
		dataView = setItems((List<pds_file_hd>)  readBuffer);//수정
    	selectRow(0);
	}else {
		dataView = setItems((List<pds_file_hd>)  readBuffer);//수정
    }
	
    personFilter = new PersonFilter(dataView);

    headerRow.getCell(user_id).setComponent(createFilterHeader("등록자", personFilter::set_user_id));
    headerRow.getCell(title).setComponent(createFilterHeader("제목", personFilter::set_title));
    //headerRow.getCell(nick_name).setComponent(createFilterHeader("닉네임", personFilter::set_nick_name));
    
}
public void  retrieve(long _unit_id) {
	readBuffer= new ArrayList<>();
	deleteBuffer = new ArrayList<>();
	dataView = null;
	selectedrow = -1;
	   List<pds_file_hd> items =  new ArrayList<>();
	   items.add(sqlca.getPdsHdListById(_unit_id));
	
	readBuffer = items;
	originalBuffer = new ArrayList<>();
	originalBuffer = new ArrayList<>(readBuffer);//추가 
	if(readBuffer.size()!=0) {
		dataView = setItems((List<pds_file_hd>)  readBuffer);//수정
    	selectRow(0);
	}else {
		dataView = setItems((List<pds_file_hd>)  readBuffer);//수정
    }
	
    personFilter = new PersonFilter(dataView);

    headerRow.getCell(user_id).setComponent(createFilterHeader("등록자", personFilter::set_user_id));
    headerRow.getCell(title).setComponent(createFilterHeader("제목", personFilter::set_title));
    //headerRow.getCell(nick_name).setComponent(createFilterHeader("닉네임", personFilter::set_nick_name));
    
}

public void retrieve(long _class_cd, long _subject_cd,long _unit_cd,int limit,int offset) {
	readBuffer= new ArrayList<>();
	deleteBuffer = new ArrayList<>();
	dataView = null;
	selectedrow = -1;
	List<pds_file_hd> readBuffer1 = new ArrayList<>();
	readBuffer1 =sqlca.getPdsHdList(_class_cd, _subject_cd,_unit_cd,limit,offset);
	
	originalBuffer = new ArrayList<>();
	originalBuffer = new ArrayList<>(readBuffer);//추가 
	if(readBuffer.size()!=0) {
		dataView = setItems((List<pds_file_hd>)  readBuffer);//수정
			int cnt = readBuffer1.size();
			for (int i=0 ; cnt > i ;i++) {
				pds_file_hd row = (pds_file_hd) readBuffer1.get(i);
				dataView.addItem(row);
			}
    	selectRow(0);
	}else {
		dataView = setItems((List<pds_file_hd>)  readBuffer);//수정
    		int cnt = readBuffer1.size();
			for (int i=0 ; cnt > i ;i++) {
				pds_file_hd row = (pds_file_hd) readBuffer1.get(i);
				dataView.addItem(row);
		}
    }
	
    personFilter = new PersonFilter(dataView);

    headerRow.getCell(user_id).setComponent(createFilterHeader("등록자", personFilter::set_user_id));
    headerRow.getCell(title).setComponent(createFilterHeader("제목", personFilter::set_title));
    
}
public void addretrieve() {
	readBuffer= new ArrayList<>();
	deleteBuffer = new ArrayList<>();
	dataView = null;
	selectedrow = -1;
	List<pds_file_hd> readBuffer1 = new ArrayList<>();
//	readBuffer1 =sqlca.getPdsHdList(class_id, subject_id,unit_id,pagelimit,pageoffset);
	readBuffer1 =sqlca.getPdsHdList(class_id, subject_id,unit_id,pagelimit,currentPage * pagelimit);
	
	originalBuffer = new ArrayList<>();
	originalBuffer = new ArrayList<>(readBuffer);//추가 
	if(readBuffer.size()!=0) {
		dataView = setItems((List<pds_file_hd>)  readBuffer);//수정
			int cnt = readBuffer1.size();
			for (int i=0 ; cnt > i ;i++) {
				pds_file_hd row = (pds_file_hd) readBuffer1.get(i);
				dataView.addItem(row);
			}
    	selectRow(0);
	}else {
		dataView = setItems((List<pds_file_hd>)  readBuffer);//수정
    		int cnt = readBuffer1.size();
			for (int i=0 ; cnt > i ;i++) {
				pds_file_hd row = (pds_file_hd) readBuffer1.get(i);
				dataView.addItem(row);
		}
    }
	
    personFilter = new PersonFilter(dataView);

    headerRow.getCell(user_id).setComponent(createFilterHeader("등록자", personFilter::set_user_id));
    headerRow.getCell(title).setComponent(createFilterHeader("제목", personFilter::set_title));
    
}
public void addretrieve(long _class_cd, long _subject_cd,long _unit_cd,int limit,int offset) {
	readBuffer= new ArrayList<>();
	deleteBuffer = new ArrayList<>();
	dataView = null;
	selectedrow = -1;
	List<pds_file_hd> readBuffer1 = new ArrayList<>();
	readBuffer1 =sqlca.getPdsHdList(_class_cd, _subject_cd,_unit_cd,limit,offset);
	
	originalBuffer = new ArrayList<>();
	originalBuffer = new ArrayList<>(readBuffer);//추가 
	if(readBuffer.size()!=0) {
		dataView = setItems((List<pds_file_hd>)  readBuffer);//수정
			int cnt = readBuffer1.size();
			for (int i=0 ; cnt > i ;i++) {
				pds_file_hd row = (pds_file_hd) readBuffer1.get(i);
				dataView.addItem(row);
			}
    	selectRow(0);
	}else {
		dataView = setItems((List<pds_file_hd>)  readBuffer);//수정
    		int cnt = readBuffer1.size();
			for (int i=0 ; cnt > i ;i++) {
				pds_file_hd row = (pds_file_hd) readBuffer1.get(i);
				dataView.addItem(row);
		}
    }
	
    personFilter = new PersonFilter(dataView);

    headerRow.getCell(user_id).setComponent(createFilterHeader("등록자", personFilter::set_user_id));
    headerRow.getCell(title).setComponent(createFilterHeader("제목", personFilter::set_title));
    
}
public List<pds_file_hd> getList(String _type_cd, String _class_cd, String _subject_cd,String _unit_cd,int limit,int offset) {
	final String sqlsyntax = "select  \n"
			+ "ROW_NUMBER() OVER (ORDER BY aaa.view_no) AS row_num,COALESCE(aaa.hd_no,0) as hd_no, COALESCE(aaa.div_cd,'') as div_cd,COALESCE(aaa.type_cd,'') as type_cd,\n"
			+ "COALESCE(aaa.class_cd,'') as class_cd, COALESCE(aaa.subject_cd,'') as subject_cd,  \n"
			+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_no ORDER BY aaa.view_no) = 1 THEN COALESCE(aaa.div_nm,'') ELSE NULL END AS div_nm,\n"
			+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_no ORDER BY aaa.view_no) = 1 THEN aaa.view_no ELSE NULL END AS no,\n"
			+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_no ORDER BY aaa.view_no) = 1 THEN aaa.view_no ELSE aaa.view_no END AS view_no,\n"
			+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_no ORDER BY aaa.view_no) = 1 THEN COALESCE(aaa.type_nm,'') ELSE NULL END AS type_nm,\n"
			+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_no ORDER BY aaa.view_no) = 1 THEN COALESCE(aaa.class_nm,'') ELSE NULL END AS class_nm,\n"
			+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_no ORDER BY aaa.view_no) = 1 THEN COALESCE(aaa.subject_nm,'') ELSE NULL END AS subject_nm,\n"
			+ "  COALESCE(aaa.update_dt,now()) as update_dt,  COALESCE(aaa.context,'') as context, COALESCE(aaa.user_id,'') as user_id, \n"
			+ "CASE WHEN ROW_NUMBER() OVER (PARTITION BY aaa.view_no ORDER BY aaa.view_no) = 1 THEN 	'Y' ELSE 'N' END AS   visible_yn,COALESCE(aaa.view_yn,'') as view_yn    \n"
			+ "	from  \n"
			+ "	( \n"
			+ "		SELECT  \n"
			+ "		a.no as hd_no,aa.no as view_no,COALESCE(a.div_cd,'') as div_cd,COALESCE(a.type_cd,'') as type_cd,COALESCE(a.class_cd,'') as class_cd, COALESCE(a.subject_cd,'') as subject_cd,   \n"
			+ "		COALESCE(e.div_nm,'') as div_nm,COALESCE(b.name,'') as type_nm,COALESCE(c.name,'') as class_nm, COALESCE(d.name,'') as subject_nm,  COALESCE(a.update_dt,now()) as update_dt,  \n"
			+ "		COALESCE(a.context,'') as context, COALESCE(aa.user_id,'') as user_id , COALESCE(e.view_yn,'') as view_yn  \n"
			+ "		FROM  \n"
			+ "		exam_mat_unit_view as aa  left outer join exam_mat_unit_hd as a on (aa.no = a.view_no  ) \n"
			+ "		left outer join codeb as b on (a.type_cd = b.no )  \n"
			+ "		left outer join codem as c on (a.type_cd = c.bno and a.class_cd = c.no)  \n"
			+ "		left outer join codes as d on (a.class_cd = d.mno and a.subject_cd = d.no)  \n"
			+ "		left outer join codess as f on (f.sno = d.no and a.unit_cd = f.no)  \n"
			+ "		left outer join usy_div_cd e  on (a.div_cd = e.div_cd)  \n"
			+ "		WHERE   \n"
			+ "		COALESCE(a.type_cd,'') like ?  \n"
			+ "		and COALESCE(a.class_cd,'') like ?  \n"
			+ "		and COALESCE(a.subject_cd,'') like ? \n"
			+ "		and COALESCE(a.unit_cd,'') like ? \n"
			+ "		order by aa.no desc   \n"
			+ "	) as aaa  \n"
			+ "	order by aaa.view_no desc ,row_num   limit ? offset ? ";
	
	List<pds_file_hd> list = new ArrayList<>();
	ResultSet rs = null;
	PreparedStatement preparedStatement= null;
	Connection connection = null;
	try {
//		connection = sqlca.getDataSource().getConnection();
		preparedStatement = connection.prepareStatement(sqlsyntax);
		preparedStatement.setString(1,_type_cd);
		preparedStatement.setString(2,_class_cd);
		preparedStatement.setString(3,_subject_cd);
		preparedStatement.setString(4,_unit_cd);
		preparedStatement.setInt(5,limit);
		preparedStatement.setInt(6,offset);
		rs = preparedStatement.executeQuery();
		while (rs.next()) {
			pds_file_hd module = new pds_file_hd();
			module.set_update_dt(rs.getDate("update_dt").toLocalDate());
			module.set_user_id(rs.getString("user_id"));
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

public void insert() {
	pds_file_hd select = new pds_file_hd();//수정
	 select.setModify("I");
	 if(selectedrow == -1) {
		 if(readBuffer.size()==0||readBuffer==null) {
				readBuffer= new ArrayList<>();
				deleteBuffer = new ArrayList<>();
				originalBuffer = new ArrayList<>();
				originalBuffer = new ArrayList<>(readBuffer);//추가 
				dataView = setItems((List<pds_file_hd>)  readBuffer);//수정
		 }
		dataView.addItem(select);
		((List<pds_file_hd>)originalBuffer).add(select);
	 }else {
		 dataView.addItemAfter(select, selecteditem);
		 ((List<pds_file_hd>)originalBuffer).add(selectedrow+1,select);
	 }
	 dataView.refreshAll();
	 selectRow(selectedrow+1);
}
public void insert(SysEntity object) {
	pds_file_hd select =(pds_file_hd)object;
	 select.setModify("I");
	 if(selectedrow == -1) {
		 if(readBuffer.size()==0||readBuffer==null) {
				readBuffer= new ArrayList<>();
				deleteBuffer = new ArrayList<>();
				originalBuffer = new ArrayList<>();
				originalBuffer = new ArrayList<>(readBuffer);//추가 
				dataView = setItems((List<pds_file_hd>)  readBuffer);//수정
		 }
		dataView.addItem(select);
		((List<pds_file_hd>)originalBuffer).add(select);
	 }else {
		 dataView.addItemAfter(select, selecteditem);
		 ((List<pds_file_hd>)originalBuffer).add(selectedrow+1,select);
	 }
	 dataView.refreshAll();
	 selectRow(selectedrow+1);
}
    public void resetData() {
		readBuffer= new ArrayList<>();
		deleteBuffer = new ArrayList<>();
		dataView = null;
		selectedrow = -1;
		originalBuffer = new ArrayList<>();
		originalBuffer = new ArrayList<>(readBuffer);//추가 
		dataView = setItems((List<pds_file_hd>)  readBuffer);//수정
    }
}
