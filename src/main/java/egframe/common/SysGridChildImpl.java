package egframe.common;

public abstract interface SysGridChildImpl {
	abstract public void setTitle(String title);
	abstract public void retrieve();
	abstract public void insert();
	abstract public void delete();
	abstract public void update();
	abstract public int getRow(SysEntity obj);
	abstract public void retrieve(int list_no);
	abstract public <T> void retrieve(T entity);
	abstract public String getTitle();
}
