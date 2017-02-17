package com.vseminar.screen;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vseminar.data.UserSession;
import com.vseminar.view.AboutView;
import com.vseminar.view.DashboardView;
import com.vseminar.view.ErrorView;
import com.vseminar.view.SessionView;
import com.vseminar.view.UserView;

@SuppressWarnings("serial")
public class MainScreen extends HorizontalLayout {
	
	public MainScreen(UI ui) {		
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
		
		// View가 동적으로 바뀌어 보이게 되는 빈 Layout
		CssLayout viewContainer = new CssLayout();

		// URI Fragment별로 View를 매칭 해서 관리하는 Navigator
		final Navigator navigator = new Navigator(ui, viewContainer);

		// addView를 통해 새로운 뷰를 등록 ("주소", "구현된 뷰클래스");
		navigator.addView(DashboardView.VIEW_NAME, new DashboardView());
		navigator.addView(SessionView.VIEW_NAME,   new SessionView());
		navigator.addView(AboutView.VIEW_NAME,     new AboutView());
		navigator.addView(UserView.VIEW_NAME,      new UserView());

		// 네비게이터 동작 중 오류가 나면 에러뷰를 표현
		navigator.setErrorView(ErrorView.class);
		
		// MainScreen의 HorizontalLayout에 viewContainer를 추가
		addComponent(viewContainer);

		// 현재 요청된 주소(location) 값에 맞게 viewContainer의 View를 동적으로 교체		
		navigator.navigateTo(UI.getCurrent().getNavigator().getState());
	}
}
