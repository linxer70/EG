package egframe.common;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;

import egframe.common.tinymce.TinyMce;
/*
tinymce.activeEditor.execCommand('Bold');
tinymce.activeEditor.execCommand('Italic');
tinymce.activeEditor.execCommand('Underline');
tinymce.activeEditor.execCommand('Strikethrough');
tinymce.activeEditor.execCommand('Superscript');
tinymce.activeEditor.execCommand('Subscript');
tinymce.activeEditor.execCommand('Cut');
tinymce.activeEditor.execCommand('Copy');
tinymce.activeEditor.execCommand('Paste');
tinymce.activeEditor.execCommand('JustifyLeft');
tinymce.activeEditor.execCommand('JustifyCenter');
tinymce.activeEditor.execCommand('JustifyRight');
tinymce.activeEditor.execCommand('JustifyFull');
tinymce.activeEditor.execCommand('ForeColor', false, '#FF0000');
tinymce.activeEditor.execCommand('BackColor', false, '#FF0000');
tinymce.activeEditor.execCommand('FontName', false, 'courier new');
tinymce.activeEditor.execCommand('FontSize', false, '30px');
tinymce.activeEditor.execCommand('FormatBlock', false, 'bold');
tinymce.activeEditor.execCommand('RemoveFormat');
tinymce.activeEditor.execCommand('Indent');
tinymce.activeEditor.execCommand('Outdent');
tinymce.activeEditor.execCommand('CreateLink', false, 'https://www.tiny.cloud');
tinymce.activeEditor.execCommand('Unlink');
tinymce.activeEditor.execCommand('InsertHorizontalRule');
tinymce.activeEditor.execCommand('InsertParagraph');
tinymce.activeEditor.execCommand('InsertText', false, 'My text content');
tinymce.activeEditor.execCommand('InsertHTML', false, 'My HTML content');
tinymce.activeEditor.execCommand('InsertImage', false, 'https://www.example.com/image.png');
tinymce.activeEditor.execCommand('SelectAll');
tinymce.activeEditor.execCommand('Delete');
tinymce.activeEditor.execCommand('ForwardDelete');
tinymce.activeEditor.execCommand('Redo');
tinymce.activeEditor.execCommand('Undo');
 */
public class SysToolBarControl  extends FlexLayout{
	private TinyMce target ;
	private Button fontname = new Button("서식");
	private Button latex = new Button("수식");
	private Button indent = new Button("들여쓰기");
	private Button outdent = new Button("내어쓰기");
	private Button table = new Button("표만들기");
	public SysToolBarControl() {
	    _initMain();
	    _initToolBar();
	    _initEvent();
	}
	public SysToolBarControl(TinyMce _target) {
		target = _target;
	    _initMain();
	    _initToolBar();
	    _initEvent();
	}
	public void _initMain() {
		//setHeight("40px");
		setSizeFull();
	   	getStyle().set("flex-wrap", "wrap");
	    setJustifyContentMode(JustifyContentMode.START);	//버튼을 우측부터 추가 
	    setAlignItems(FlexLayout.Alignment.CENTER); 
	    getStyle().set("gap", "5px");
	    getStyle().set("align-items", "start"); // 세로 정렬
	    getStyle().set("align-content", "start"); // 줄 정렬
	    getStyle().set("overflow-y", "auto"); // 세로 스크롤 활성화
	    getStyle().set("border", "1px solid yellow");
	}
	public void _initToolBar() {
		latex.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		indent.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		fontname.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		outdent.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		table.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		add(fontname,latex,indent,outdent,table);
	}
	public void _initEvent() {
		latex.addClickListener(e->{
			_executeLatex();
		});
		indent.addClickListener(e->{
			_executeCommand("indent");
		});
		outdent.addClickListener(e->{
			_executeCommand("outdent");
		});
		fontname.addClickListener(e->{
			_executeCommand("fontname","courier new");
		});
		table.addClickListener(e->{
			_executeCommand("table","courier new");
		});
	}
	public void _executeCommand(String command) {
        //System.out.println("BBBBBBBBBBBBBBBBBB"+viewmce.getID());
		UI.getCurrent().getPage().executeJs("window.manipulateElementById($0,$1);",target.getID(),command);
		
	}
	public void _executeLatex() {
		if(target!=null) {
	        //System.out.println("BBBBBBBBBBBBBBBBBB"+viewmce.getID());
			UI.getCurrent().getPage().executeJs("window.manipulateElementById('"+target.getID()+"');");
			
		}else {
			return ;
		}
	}
	public void _executeCommand(String cmd,String option) {
		if(target!=null) {
	        //System.out.println("BBBBBBBBBBBBBBBBBB"+viewmce.getID());
			UI.getCurrent().getPage().executeJs("window.manipulateElementById($0,$1,$2);",target.getID(),cmd,option);
			
		}else {
			return ;
		}
	}
	public void _executeLatex(TinyMce _target) {
		if(_target!=null) {
	        //System.out.println("BBBBBBBBBBBBBBBBBB"+viewmce.getID());
			UI.getCurrent().getPage().executeJs("window.manipulateElementById('"+_target.getID()+"');");
			
		}else {
			return ;
		}
	}
	public void setMce(TinyMce tinyMce) {
		target = tinyMce;
	}
}
