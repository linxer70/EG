package egframe.common;

import java.util.List;

import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexDirection;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.RouterLayout;

public class SysWindow extends FlexLayout implements HasDynamicTitle, HasUrlParameter<Long>,AfterNavigationObserver,BeforeEnterObserver ,WindowImpl,RouterLayout {
	public String title = "";
	public SysButtonLayout buttonlayout = new SysButtonLayout();
	public SysTitleLayout titlelayout = new SysTitleLayout();
	public HorizontalLayout toplayout = new HorizontalLayout();
	public SysSearchLayout searchlayout = new SysSearchLayout();
	public FlexLayout hdlayout = new FlexLayout();
	public FlexLayout rowlayout = new FlexLayout();
	public FlexLayout seqlayout = new FlexLayout();
	public Long title_type_id ;
	public Long title_class_id;
	public String title_type_nm = "";
	public String title_class_nm = "";
	
	public SysWindow() {
    	//setSizeFull();
    	//setMargin(false);
    	//setSpacing(false);
    	this.addClassName("main-layout");
    	setFlexDirection(FlexDirection.COLUMN);
        getStyle().set("border", "1px solid black");		//메인 테두리
        titlelayout.setWidth("50%");
        buttonlayout.setWidth("50%");
        
        toplayout.setSpacing(false);						//콘트롤 간격 없음
        toplayout.setMargin(false);
        toplayout.add(titlelayout,buttonlayout);
        
        toplayout.getStyle().set("border", "1px solid blue");
        toplayout.setHeight("40px");
        toplayout.setWidth("100%");
        
        
        searchlayout.getStyle().set("border", "1px solid blue");
        searchlayout.setFlexDirection(FlexDirection.ROW);
        searchlayout.setHeight("50px");
        searchlayout.setWidth("100%");
        
        hdlayout.setFlexDirection(FlexDirection.COLUMN);
        rowlayout.setFlexDirection(FlexDirection.COLUMN);
        seqlayout.setFlexDirection(FlexDirection.COLUMN);
        
        add(toplayout,searchlayout,hdlayout,rowlayout,seqlayout);
        
        buttonlayout.wretrieve.addClickListener(e->{wRetrieve();});
        buttonlayout.winsert.addClickListener(e->{wInsert();});
        buttonlayout.wdelete.addClickListener(e->{wDelete();});
        buttonlayout.wsave.addClickListener(e->{wSave();});
        buttonlayout.wnewsheet.addClickListener(e->{wNewsheet();});
        buttonlayout.wdeleteset.addClickListener(e->{wDeleteset();});
	}
	public void setTitle(String title) {
		titlelayout.setTitle(title);
	}
	@Override
	public void beforeEnter(BeforeEnterEvent event) {
	}
	@Override
	public void active() {
	}
	@Override
	public void open() {
	}
	@Override
	public void close() {
	}
	@Override
	public void destory() {
	}
	@Override
	public void wRetrieve() {
	}
	@Override
	public void wInsert() {
	}
	@Override
	public void wDelete() {
	}
	@Override
	public void wSave() {
	}
	@Override
	public void wNewsheet() {
	}
	@Override
	public void wDeleteset() {
	}
	@Override
	public void afterNavigation(AfterNavigationEvent event) {
	}
	@Override
	public boolean wChkSave() {
		return false;
	}
	@Override
    public void setParameter(BeforeEvent event,@OptionalParameter Long id) {
        if (id != null) {
            title = "게시물 #" + id;
        } else {
            title = "환경설정";
            this.setTitle(title);
        }
    }
	@Override
	public String getPageTitle() {
		return title;
	}
	@Override
	public void addretrieve() {
	}
}