package com.vseminar.screen;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vseminar.VSeminarUI;
import com.vseminar.data.UserSession;

@SuppressWarnings("serial")
public class MainScreen extends HorizontalLayout {
	
	public MainScreen(VSeminarUI vseminarUI) {		
		// 로그인한 사용자의 이메일 정보
		Label label = new Label(UserSession.getUser().getEmail());	
		final Button signout = new Button("Sign Out");   // 로그아웃버튼		
		signout.addClickListener(new ClickListener() {
	            @Override
	      public void buttonClick(final ClickEvent event) {
	                // 로그아웃처리
	                UserSession.signout();
	            }
	        });
		addComponent(label);
		addComponent(signout);
	}
}

