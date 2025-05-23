package egframe.common;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

public class SysSearchLayout extends FlexLayout {
	public List<FlexLayout> control= new ArrayList<>();//타이틀과 콘트롤을 합친 레이아웃을 저장 
	public List<Button> title= new ArrayList<>();
	public List<TextField> edit= new ArrayList<>();
	public List<TextArea> text= new ArrayList<>();
	public List<DatePicker> picker= new ArrayList<>();
	public List<FlexLayout> pan = new ArrayList<>();//순서대로 배치르 위한 판 
	public SysSearchLayout() {
		getElement().getStyle().set("overflow", "hidden");
		this.setWidthFull();
		this.setHeight("50px");
		this.addClassName("main-layout");
	}
}