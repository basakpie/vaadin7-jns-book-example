package com.vseminar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import com.vseminar.config.VSeminarSessionInitListener;
import com.vseminar.data.LoadingDataGenerator;
import com.vseminar.data.UserSession;
import com.vseminar.screen.LoginScreen;
import com.vseminar.screen.MainScreen;
import com.vaadin.annotations.Push;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.shared.ui.ui.Transport;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Title("Vaadin Seminar")
@Theme("vseminar")
@Widgetset("com.vseminar.VSeminarWidgetset")
@Push(value=PushMode.AUTOMATIC, transport=Transport.WEBSOCKET)
@SuppressWarnings({"serial", "unused"})
public class VSeminarUI extends UI {

	// Mock Data를 생성
	private static final LoadingDataGenerator dataGenerator = new LoadingDataGenerator();
	
	@Override
    protected void init(VaadinRequest vaadinRequest) {
		// 반응형 웹 디자인 적용 시키기
		Responsive.makeResponsive(this);
		// 로그인 확인		
    	if(UserSession.isSignedIn()) {
    		// Session에 값이 있으면 메인스크린으로
            setContent(new MainScreen());
            // 현재 요청된 주소(location) 값에 맞게 viewContainer의 View를 동적으로 교체
    		getNavigator().navigateTo(getNavigator().getState());
    		return;
    	}
    	// Session에 값이 없으면 로그인스크린으로
    	setContent(new LoginScreen());
    }
    
    @WebServlet(urlPatterns = "/*", name = "VSeminarUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = VSeminarUI.class, productionMode = false)
    public static class VSeminarUIServlet extends VaadinServlet {   
    	@Override
        protected final void servletInitialized() throws ServletException {
            super.servletInitialized();
            getService().addSessionInitListener(new VSeminarSessionInitListener());
        }
    }    
    
}
