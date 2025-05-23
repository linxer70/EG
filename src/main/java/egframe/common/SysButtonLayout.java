package egframe.common;


import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;

public class SysButtonLayout extends HorizontalLayout {
	public Button wretrieve = new Button("조회");
	public Button wnewsheet = new Button("새문서");
	public Button winsert = new Button("입력");
	public Button wdelete = new Button("삭제");
	public Button wdeleteset = new Button("셑삭제");
	public Button wsave = new Button("저장");
	
	public SysButtonLayout() {
       wnewsheet.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
       wsave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
       wretrieve.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
       winsert.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
       wdelete.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
       wdeleteset.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
       setJustifyContentMode(JustifyContentMode.END);
    	setAlignItems(Alignment.AUTO);
        //getStyle().set("border", "1px solid black");
       add(wretrieve,wnewsheet,winsert,wdelete,wdeleteset,wsave);
		wretrieve.addClickListener(e->{});
		wsave.addClickListener(e->{});
		wretrieve.addClickListener(e->{});
		winsert.addClickListener(e->{});
		wdelete.addClickListener(e->{});
		wdeleteset.addClickListener(e->{});
	}
}