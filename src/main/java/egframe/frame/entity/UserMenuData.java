package egframe.frame.entity;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserMenuData {
    private static List<UserMenu> departmentList = new ArrayList<>();
    
    public UserMenuData(List<UserMenu> departmentlist) {
    	departmentList = departmentlist;
	}

	private static List<UserMenu> createDepartmentList(List<UserMenu> departmentlist) {
    	departmentList = departmentlist;
        return departmentList;
    }

    private static List<UserMenu> createDepartmentList() {
        List<UserMenu> departmentList = new ArrayList<>();
        return departmentList;

    }
    
    
    public List<UserMenu> getDepartments() {
        return departmentList;
    }
    public List<UserMenu> getRootDepartments() {
    	List<UserMenu> filteredList = departmentList.stream()
    		    .filter(department -> department.getParentCd() == null)
    		    .collect(Collectors.toList());
    	
    	return filteredList;
    }

    public List<UserMenu> getChildDepartments(UserMenu parent) {
    	List<UserMenu> filteredList = departmentList.stream()
    		    .filter(department -> department.getParentCd() == parent)
    		    .collect(Collectors.toList());
        return filteredList;
    }
}

