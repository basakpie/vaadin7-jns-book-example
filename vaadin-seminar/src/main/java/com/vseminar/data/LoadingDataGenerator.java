package com.vseminar.data;

import com.vaadin.server.FontAwesome;
import com.vseminar.data.model.RoleType;
import com.vseminar.data.model.User;
import com.vseminar.menu.Navi;
import com.vseminar.menu.VSeminarNavigator;
import com.vseminar.view.AboutView;
import com.vseminar.view.DashboardView;
import com.vseminar.view.SessionView;
import com.vseminar.view.UserView;

public final class LoadingDataGenerator {
	
	static {
		createUsers(); // User Mock Data
		createNavis(); // Navi Mock Data
	}
	
	private static void createUsers() {
		UserData userData = UserData.getInstance();
		userData.save(new User("user1", "user1@vseminar.com", "1234", "img/upload/1.jpg", RoleType.User));
		userData.save(new User("user2", "user2@vseminar.com", "1234", null, RoleType.User));
		userData.save(new User("user3", "user3@vseminar.com", "1234", "img/upload/3.jpg", RoleType.User));
		userData.save(new User("admin", "admin@vseminar.com", "1234", null, RoleType.Admin));
	}	
	
	private static void createNavis() {
		VSeminarNavigator.naviMaps.put("",        new Navi(DashboardView.VIEW_NAME, "Dashboard", DashboardView.class, FontAwesome.HOME,  RoleType.User));
		VSeminarNavigator.naviMaps.put("session", new Navi(SessionView.VIEW_NAME,   "Session",   SessionView.class,   FontAwesome.CUBE,  RoleType.User));
		VSeminarNavigator.naviMaps.put("about",   new Navi(AboutView.VIEW_NAME,     "About",     AboutView.class,     FontAwesome.INFO,  RoleType.User));
		VSeminarNavigator.naviMaps.put("user",    new Navi(UserView.VIEW_NAME,      "User",      UserView.class,      FontAwesome.USERS, RoleType.Admin));		
	}

}
