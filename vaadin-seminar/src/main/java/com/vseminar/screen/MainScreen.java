package com.vseminar.screen;

import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vseminar.menu.VSeminarMenu;
import com.vseminar.menu.VSeminarNavigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vseminar.view.ErrorView;

@SuppressWarnings("serial")
public class MainScreen extends HorizontalLayout {
	
	public MainScreen() {
		// View가 동적으로 바뀌어 보이게 되는 빈 Layout
		CssLayout viewArea = new CssLayout();
		viewArea.setSizeFull();
	      
		// 네비게이터 생성(로그인한 권한에 맞게 View 리스트 등록)
		final VSeminarNavigator navigator = new VSeminarNavigator(UI.getCurrent(), viewArea);
	        
		// 메뉴 영역(로그인한 권한에 맞게 Menu Item 보여주기)
        final VSeminarMenu menuArea = new VSeminarMenu(navigator);
        
        // 뷰 변경시 발생되는 이벤트 리스너
 		ViewChangeListener viewChangeListener = new ViewChangeListener() {
 	        @Override
 	        public boolean beforeViewChange(ViewChangeEvent event) {
 	        	return true;
 	        }
 	        @Override
 	        public void afterViewChange(ViewChangeEvent event) {
 	        	// 선택된 메뉴 아이템 Background를 진하게 처리 하기
 	        	menuArea.setSelectedItem(event.getViewName());
 	        }
 	    };
 	    
 	    // 네비게이터 View 변경 이벤트 발생
        navigator.addViewChangeListener(viewChangeListener);        
        // 네비게이터 동작 중 오류가 나면 에러 페이지로 이동
        navigator.setErrorView(ErrorView.class);
	        
        // MainScreen에 메뉴 영역 + 동적 변경 뷰 영역 순서대로 추가
        addComponents(menuArea, viewArea);
        
        // 동적 변경 뷰 영역이 빈 영역 모두 사용하기
        setExpandRatio(viewArea, 1);
        setSizeFull();
	}	
}
