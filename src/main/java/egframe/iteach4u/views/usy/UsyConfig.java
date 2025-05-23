package egframe.iteach4u.views.usy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinResponse;

import egframe.common.SysWindow;
import egframe.frame.entity.UsrMaster;
import egframe.frame.freeform.f_usr_master;
import egframe.frame.service.AuthenticatedUser;
import egframe.frame.service.SysDBO;

import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexDirection;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextAreaVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.FileBuffer;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.component.upload.receivers.MultiFileBuffer;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.server.AppShellSettings;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.component.html.Anchor;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.vaadin.addons.f0rce.uploadhelper.UHReceiver;
import org.vaadin.addons.f0rce.uploadhelper.UploadHelper;
import org.vaadin.addons.f0rce.uploadhelper.receiver.UHMultiFileMemoryBuffer;
import org.vaadin.addons.thshsh.upload.UploadField;
import org.vaadin.addons.thshsh.upload.UploadFile;
/*
import egframe.common.SysDataWindowChildControl;
import egframe.common.SysDataWindowControl;
import egframe.common.SysGridLayout;
import egframe.common.SysShareControl;
import egframe.common.SysTabControl;
import egframe.common.SysWindow;
import egframe.common.SysWindowTab;
import egframe.common.WindowImpl;
import egframe.data.entitycontrol.sys_column_dic;
import egframe.data.entitycontrol.sys_user;
import egframe.data.freeformcontrol.d_sys_column_dic_free;
import egframe.data.gridcontrol.d_sys_column_dic;
import egframe.data.gridcontrol.d_sys_module;
import egframe.data.service.SysDBO;
import egframe.iteach4u.entitycontrol.pds_file_hd;
import egframe.iteach4u.entitycontrol.usy_class_cd;
import egframe.iteach4u.entitycontrol.usy_subject_cd;
import egframe.iteach4u.entitycontrol.usy_type_cd;
import egframe.iteach4u.entitycontrol.usy_unit_cd;
import egframe.iteach4u.freeformcontrol.d_file_upload_free;
import egframe.iteach4u.freeformcontrol.d_pds_file_hd_free;
import egframe.iteach4u.gridcontrol.d_file_upload;
import egframe.iteach4u.gridcontrol.d_pds_file_hd;
import egframe.iteach4u.gridcontrol.d_pds_file_row;
import egframe.security.AuthenticatedUser;
import egframe.views.sys.SysPGMEntControl;
import egframe.views.sys.SysPGMFreeControl;
import egframe.views.sys.SysPGMGridControl;
import egframe.views.sys.SysPGMMenu;
import egframe.views.sys.SysPGMSrchControl;
import egframe.views.sys.SysPGMTabControl;
import egframe.views.sys.SysPGMViewControl;
*/
import egframe.frame.views.MainLayout;
import egframe.iteach4u.common.TYPE_CODE;
import egframe.iteach4u.common.UNIT_CODE;
import egframe.iteach4u.entity.UsyConfigType;
import egframe.iteach4u.entity.usy_type_cd;
import egframe.iteach4u.service.Repository.Iteach4uService;
import egframe.iteach4u.views.pds.PdsLayout;
import jakarta.annotation.security.PermitAll;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;

@Route(value = "usy/usyconfig", layout = PdsLayout.class)
@PermitAll
@Uses(Icon.class)
@PreserveOnRefresh
public class UsyConfig extends SysWindow  {
	
	private String title = "";
	private final Grid<UsyConfigType> grid = new Grid<>(UsyConfigType.class);
	private Iteach4uService jdbc;
	private AuthenticatedUser securityservice;
	public  TYPE_CODE type ;
	private FlexLayout unitlayout = new FlexLayout();
	private Button check = new Button("체크");
	private Button view = new Button("뷰");
	
	private FlexLayout masterlayout = new FlexLayout();
	private FlexLayout configlayout = new FlexLayout();
	private f_usr_master master ;
	
	@Autowired
	public UsyConfig(AuthenticatedUser securityService,Iteach4uService _jdbc) {
		securityservice = securityService;
		jdbc = _jdbc;
		/*
		grid.removeAllColumns();
       grid.addColumn(UsyConfigType::getId).setHeader("ID");
       grid.addColumn(UsyConfigType::getType_id).setHeader("분야ID");
       grid.addColumn(UsyConfigType::getUser_id).setHeader("유저Id");
       grid.addColumn(UsyConfigType::getType_nm).setHeader("분야명");
       grid.addColumn(UsyConfigType::getUser_nm).setHeader("유저명");
       Long user_id = (long) 1 ;
       grid.setItems(jdbc.findUser(user_id));

       add(grid);
       */
		initMain();
		//initUnit();
		addEvent();
		//add(view,check,unitlayout);
		wRetrieve();
    }
	
	public void initMain() {
		setFlexDirection(FlexDirection.COLUMN);
		//getStyle().set("border", "1px solid yellow");
	   	setHeight("100%");
	   	setMaster();
	   	setConfig();
	   	add(masterlayout,configlayout);
    }
    public void setMaster() {
    	UsrMaster data = jdbc.findByUserId(Long.valueOf(1));
    	master = new f_usr_master(data);
    	masterlayout.add(master);
    }
    public void setConfig() {
    	initUnit();
    	configlayout.add(unitlayout);
    }
    public void initUnit() {
       type = new TYPE_CODE(jdbc);
       type._setTypeData();
    	unitlayout.setSizeFull();
    	//unitlayout.getStyle().set("border", "1px solid blue");
    	List<usy_type_cd> list = jdbc.getTypeListById(Long.valueOf(1));
    	Set<usy_type_cd> set = new HashSet<>(list); // 기
    	type.setCheckedTypes(set);    	
    	
    	unitlayout.add(type);
    }
    public void addEvent() {

    }
    public void wRetrive() {
    	
    	List<usy_type_cd> list = jdbc.getTypeListById(Long.valueOf(1));
    	Set<usy_type_cd> set = new HashSet<>(list); // 기
    	type.setCheckedTypes(set);   	
    }
}
