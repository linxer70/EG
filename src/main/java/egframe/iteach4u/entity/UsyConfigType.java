package egframe.iteach4u.entity;

public class UsyConfigType {
	private Long id;
	private Long user_id;
	private Long type_id;
	private String user_nm;
	private String type_nm;
	public UsyConfigType(long _id, long _user_id, long _type_id, String _user_nm, String _type_nm) {
		id = _id;
		user_id = _user_id;
		type_id = _type_id;
		user_nm = _user_nm;
		type_nm = _type_nm;
	}
	public UsyConfigType() {
		// TODO Auto-generated constructor stub
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUser_id() {
		return user_id;
	}
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}
	public Long getType_id() {
		return type_id;
	}
	public void setType_id(Long type_id) {
		this.type_id = type_id;
	}
	public String getUser_nm() {
		return user_nm;
	}
	public void setUser_nm(String user_nm) {
		this.user_nm = user_nm;
	}
	public String getType_nm() {
		return type_nm;
	}
	public void setType_nm(String type_nm) {
		this.type_nm = type_nm;
	}

}
