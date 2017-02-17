package com.vseminar.data;

import com.vseminar.data.model.RoleType;
import com.vseminar.data.model.User;

public final class LoadingDataGenerator {
	
	static {
		createUsers();
	}
	
	private static void createUsers() {
		UserData userData = UserData.getInstance();
		userData.save(new User("user1", "user1@vseminar.com", "1234", "img/upload/1.jpg", RoleType.User));
		userData.save(new User("user2", "user2@vseminar.com", "1234", null, RoleType.User));
		userData.save(new User("user3", "user3@vseminar.com", "1234", "img/upload/3.jpg", RoleType.User));
		userData.save(new User("admin", "admin@vseminar.com", "1234", null, RoleType.Admin));
	}	
	
}
