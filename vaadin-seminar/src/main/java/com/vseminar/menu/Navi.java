package com.vseminar.menu;

import com.vaadin.navigator.View;
import com.vaadin.server.Resource;
import com.vseminar.data.model.RoleType;

public class Navi {
	
	private String fragment; // vaadin-seminar/#!{fragment} 주소	
	private String viewName; // 메뉴명	
    private Class<? extends View> viewClass; // View 클래스    
    private Resource icon;     // 메뉴명 아이콘
    private RoleType roleType; // 접근권한(USER, ADMIN)

	public Navi(String fragment, String viewName, Class<? extends View> viewClass, Resource icon,
			RoleType roleType) {
		super();
		this.fragment = fragment;
		this.viewName = viewName;
		this.viewClass = viewClass;
		this.icon = icon;
		this.roleType = roleType;
	}
	
	public String getFragment() {
		return fragment;
	}

	public void setFragment(String fragment) {
		this.fragment = fragment;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public Class<? extends View> getViewClass() {
		return viewClass;
	}

	public void setViewClass(Class<? extends View> viewClass) {
		this.viewClass = viewClass;
	}

	public Resource getIcon() {
		return icon;
	}

	public void setIcon(Resource icon) {
		this.icon = icon;
	}

	public RoleType getRoleType() {
		return roleType;
	}

	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fragment == null) ? 0 : fragment.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Navi other = (Navi) obj;
		if (fragment == null) {
			if (other.fragment != null)
				return false;
		} else if (!fragment.equals(other.fragment))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "VSeminarMenu [fragment=" + fragment + ", viewName=" + viewName + ", viewClass=" + viewClass + ", icon="
				+ icon + ", roleType=" + roleType + "]";
	}
    
}
