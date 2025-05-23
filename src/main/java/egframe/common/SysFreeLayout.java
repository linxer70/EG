package egframe.common;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;

public class SysFreeLayout  extends FormLayout{
	private SysFree freeform;
	private SysGrid gridform;
	private SysGrid gridrow;
	private Button maintitle = new Button();
	public SysFreeLayout() {
		this.setSizeFull();
		this.maintitle.setVisible(false);
		add(this.maintitle);
	}
	public SysFreeLayout(SysFree free) {
		this.freeform = free;
		add(this.maintitle,this.freeform);
	}
	public void setFreeForm(SysFree free) {
		if(this.freeform!= null) {
			remove(this.freeform);
		}
		this.freeform = free;
		add(this.freeform);
	}
	public void setFreeForm(SysFree free,SysGrid _grid) {
		if(this.freeform!= null) {
			remove(this.freeform);
		}
		this.freeform = free;
		if(this.gridrow!= null) {
			remove(this.gridrow);
		}
	}
	public void setGridForm(SysGrid grid) {
		if(this.gridform!= null) {
			remove(this.gridform);
		}
		this.gridform = grid;
		add(this.gridform);
	}
	public void setTitle(String title) {
		this.maintitle.setText(title);
		this.maintitle.setVisible(true);
	}
	public void setFreeWidth(String str) {
		this.setWidth(str);
	}
	public SysFree getFree() {
		return freeform;
	}
	public String getModify(){
		return freeform.getModify();
	}
	public void setModify(String str){
		freeform.setModify(str);
	}
	public void setBinder(BeanValidationBinder bind) {
		freeform.setBinder(bind);
	}
	public void setItem(SysEntity item) {
		freeform.setItem(item);
	}
}
