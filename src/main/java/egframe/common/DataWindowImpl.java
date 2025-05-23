package egframe.common;

import java.time.LocalDate;
import java.util.List;

import com.vaadin.data.provider.ListDataProvider;
//import egframe.data.entitycontrol.sys_column_dic;
//import egframe.data.service.DBServiceImpl;
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.data.binder.BeanValidationBinder;
public abstract interface DataWindowImpl {
	abstract public void retrieve();
	abstract public void insert();
	abstract public void delete();
	abstract public void update();
	//abstract public void setBinder(SysFreeForm freeform);
	abstract public BeanValidationBinder<?> getBinder();
    abstract public String itemChanged(ComponentValueChangeEvent<?, ?> event) ;
	abstract public void selectItem(Object  select);
	abstract public String getItemString(int row,String col);
	abstract public void setItem(int row,SysEntity obj);
	abstract public LocalDate getItemDate(int row,String col);
	abstract public Long getItemNumber(int row,String col);
	abstract public int getRow(SysEntity obj);
	abstract public void refreshAll();
	abstract public void retrieve(String a, String b);
	//public void retrieve(List<sys_column_dic> arg);
	//public void setTransObject(DBServiceImpl sqlca2);
	abstract public void setClassType(String type);
	abstract public String getClassType();
	void retrieve(String a);
	abstract public void _setDDDW(SysGridChild list);
	abstract public void retrieve(long subject_id,long unit_id);
	abstract public void retrieve(long class_id, long subject_id, long unit_id);
	void resetData();
	void setDataProvider(ListDataProvider<?> dataProvider);
	void setBinder(SysFree freeform);
	abstract public void retrieve(long id);
	void setBinder(BeanValidationBinder _bind);
	abstract public void insert(SysEntity object);
	void retrieve(long class_id, long subject_id, long unit_id, int offser, int limit);
	//void addretrieve(long class_id, long subject_id, long unit_id, int offser, int limit);
	abstract public void addretrieve();
	void retrieve(List<Long> args);
}
