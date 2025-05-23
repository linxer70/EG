package egframe.common;

public abstract interface SysGridChildLayoutImpl {
	abstract public void retrieve();
	abstract public void insert();
	abstract public void delete();
	abstract public void update();
	abstract public int getRow(SysEntity obj);
	abstract public void retrieve(int list_no);
	abstract public <T> void retrieve(T entity);
}
