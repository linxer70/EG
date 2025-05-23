package egframe.common;

public interface WindowImpl  {
    public abstract void active() ;
    public abstract void open() ;
    public abstract void close() ;
    public abstract void destory() ;
    public abstract void wRetrieve() ;
    public abstract void wInsert() ;
    public abstract void wDelete() ;
    public abstract boolean wChkSave() ;
    public abstract void wSave() ;
    public abstract void wNewsheet() ;
    public abstract void wDeleteset() ;
	public abstract void addretrieve();
}