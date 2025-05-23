package egframe.common;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.value.ValueChangeMode;

public class SysTitleLayout extends HorizontalLayout {
	private Button wtitle = new Button();
	public SysTitleLayout() {
       setJustifyContentMode(JustifyContentMode.START );
    	setAlignItems(Alignment.AUTO);
    	wtitle.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    	//setSizeFull();
    	//getStyle().set("border", "1px solid black");
        //setHeight("50px");
        //setWidth("100%");
        add(wtitle);
	}
	public void setTitle(String title) {
		wtitle.setText(title);
	}
}