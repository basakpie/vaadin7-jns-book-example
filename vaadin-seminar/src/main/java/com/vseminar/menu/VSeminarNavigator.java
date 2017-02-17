package com.vseminar.menu;

import java.util.LinkedHashMap;
import java.util.Map;

import com.vaadin.navigator.Navigator;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;
import com.vseminar.data.UserSession;
import com.vseminar.data.model.RoleType;

@SuppressWarnings("serial")
public class VSeminarNavigator extends Navigator {
	
	// Menu Mock Data(key : fragment)
	public static final Map<String, Navi> naviMaps = new LinkedHashMap<>();
	
	// 현재 접근 된 사용자의 메뉴 목록
	private Map<String, Navi> activeNaviMaps;
	
	public VSeminarNavigator(UI ui, ComponentContainer container, SpringViewProvider springViewProvider, UserSession userSession) {
		// 네비게이터가 처리 할 UI와, View 영역 넘겨주기
		super(ui, container);	
		// ViewProvider 등록
		super.addProvider(springViewProvider);
		
		// 현재 로그인한 사용자의 권한 가져오기
		final RoleType UserRoleType = userSession.getUser().getRole();
		
		activeNaviMaps = new LinkedHashMap<>();
		
		for(Navi item: naviMaps.values()) {
			// 현재 권한 >= 메뉴 권한이 높으면 등록 처리
			if(UserRoleType.ordinal() >= item.getRoleType().ordinal()) {
				// 네비게이터에 View 등록
				// super.addView(item.getFragment(), item.getViewClass());
				// 현재 사용자 메뉴 목록에 View 등록
				activeNaviMaps.put(item.getFragment(), item);
			}
		}
	}

	public Map<String, Navi> getActiveNaviMaps() {
		return activeNaviMaps;
	}
	
}
