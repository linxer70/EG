package egframe.common;

public interface  SysEntity {
		public boolean selected = false;
		public Object getValue(String col) ;
		public void setValue(String col,Object obj) ;
		public void setModify(String col) ;
		public String getModify() ;
		public String getPropertyValue(String propertyName);
		public boolean isSelected() ;
		public void setSelected(boolean selected);
		boolean equals(Object obj);
}

