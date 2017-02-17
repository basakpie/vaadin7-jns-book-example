package com.vseminar.data;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.server.FontAwesome;
import com.vseminar.data.model.LevelType;
import com.vseminar.data.model.Question;
import com.vseminar.data.model.RoleType;
import com.vseminar.data.model.Session;
import com.vseminar.data.model.User;
import com.vseminar.menu.Navi;
import com.vseminar.menu.VSeminarNavigator;
import com.vseminar.view.AboutView;
import com.vseminar.view.DashboardView;
import com.vseminar.view.SessionView;
import com.vseminar.view.UserView;

@org.springframework.stereotype.Component
public class LoadingDataGenerator {
	
	private UserData userData;
	
	private QuestionData questionData;
	
	private SessionData sessionData;
		
	@Autowired
	public LoadingDataGenerator(UserData userData, QuestionData questionData, SessionData sessionData) {
		this.userData = userData;
		this.questionData = questionData;
		this.sessionData = sessionData;
	}
	
	public void create() {
		createUsers();
		createSessions();
		createNavis();
	}

	
	private void createUsers() {
		userData.save(new User("user1", "user1@vseminar.com", "1234", "img/upload/1.jpg", RoleType.User));
		userData.save(new User("user2", "user2@vseminar.com", "1234", null, RoleType.User));
		userData.save(new User("user3", "user3@vseminar.com", "1234", "img/upload/3.jpg", RoleType.User));
		userData.save(new User("admin", "admin@vseminar.com", "1234", null, RoleType.Admin));
	}	
	
	private void createNavis() {
		VSeminarNavigator.naviMaps.put("",        new Navi(DashboardView.VIEW_NAME, "Dashboard", DashboardView.class, FontAwesome.HOME,  RoleType.User));
		VSeminarNavigator.naviMaps.put("session", new Navi(SessionView.VIEW_NAME,   "Session",   SessionView.class,   FontAwesome.CUBE,  RoleType.User));
		VSeminarNavigator.naviMaps.put("about",   new Navi(AboutView.VIEW_NAME,     "About",     AboutView.class,     FontAwesome.INFO,  RoleType.User));
		VSeminarNavigator.naviMaps.put("user",    new Navi(UserView.VIEW_NAME,      "User",      UserView.class,      FontAwesome.USERS, RoleType.Admin));		
	}
	
	private void createSessions() {		
		Long user1 = userData.findOne(1L).getId();
		Long user2 = userData.findOne(2L).getId();
		Long user3 = userData.findOne(3L).getId();
		
		String slideUrl = "http://www.slideshare.net/slideshow/embed_code/key/wcZuA4l1M1Fgwv";
		String vaadinUrl = "http://demo.vaadin.com/sampler/";
		
		createQuestions(sessionData.save(new Session("Vaadin Architecture", LevelType.Junior, slideUrl, "speaker_1", user1, "")), user1);		
		createQuestions(sessionData.save(new Session("Vaadin Writing a Server-Side Web Application", LevelType.Senior, vaadinUrl, "speaker_1", user1, "")), user1);
		createQuestions(sessionData.save(new Session("Vaadin User Interface Components", LevelType.Junior, slideUrl, "speaker_2", user2, "")), user2);
		createQuestions(sessionData.save(new Session("Vaadin Managing Layout", LevelType.Senior, vaadinUrl, "speaker_2", user2, "")), user2);
		createQuestions(sessionData.save(new Session("Vaadin Designer", LevelType.Junior, slideUrl, "speaker_2", user2, "")), user2);
		createQuestions(sessionData.save(new Session("Vaadin Themes", LevelType.Junior, vaadinUrl, "speaker_2", user2, "")), user2);
		createQuestions(sessionData.save(new Session("Vaadin Binding Components to Data", LevelType.Senior, slideUrl, "speaker_1", user1, "")), user2);
		createQuestions(sessionData.save(new Session("Vaadin Vaadin SQLContainer", LevelType.Senior, vaadinUrl, "speaker_3", user3, "")), user3);
		createQuestions(sessionData.save(new Session("Vaadin Advanced Web Application Topics", LevelType.Junior, slideUrl, "speaker_1", user1, "")), user3);
		createQuestions(sessionData.save(new Session("Vaadin Portal Integration", LevelType.Junior, vaadinUrl, "speaker_2", user2, "")), user3);
		createQuestions(sessionData.save(new Session("Vaadin Client-Side Vaadin Development", LevelType.Junior, slideUrl, "speaker_3", user3, "")), user3);
		createQuestions(sessionData.save(new Session("Vaadin Client-Side Applications", LevelType.Senior, vaadinUrl, "speaker_3", user2, "")), user3);
		createQuestions(sessionData.save(new Session("Vaadin Client-Side Widgets", LevelType.Senior, slideUrl, "speaker_3", user3, "")), user3);
		createQuestions(sessionData.save(new Session("Vaadin Integrating with the Server-Side", LevelType.Junior, vaadinUrl, "speaker_1", user1, "")), user3);
		createQuestions(sessionData.save(new Session("Vaadin Using Vaadin Add-ons", LevelType.Junior, slideUrl, "speaker_2", user2, "")), user3);		
	}
	
	private void createQuestions(Session session, Long userId) {
		for(int i=1; i <= 5; i++) {
			questionData.save(new Question(session.getId(), "test sample question " + i + " : " + session.getTitle(), userId));
		}
	}
	
}
