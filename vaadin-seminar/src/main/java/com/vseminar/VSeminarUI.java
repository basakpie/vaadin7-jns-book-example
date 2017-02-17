package com.vseminar;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import com.vseminar.data.LoadingDataGenerator;
import com.vseminar.data.UserSession;
import com.vseminar.screen.LoginScreen;
import com.vseminar.screen.MainScreen;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("vseminar")
@Widgetset("com.vseminar.VSeminarWidgetset")
@SuppressWarnings("serial")
public class VSeminarUI extends UI {

	// Mock Data를 생성.
	@SuppressWarnings("unused")
	private static final LoadingDataGenerator dataGenerator = new LoadingDataGenerator();
	
	@Override
    protected void init(VaadinRequest vaadinRequest) {        
    	if(UserSession.isSignedIn()) { // UserSession은 이어서 구현 예정…
    		// Session에 값이 있으면 메인 스크린으로
    	    setContent(new MainScreen(this));         
    		return;
    	}
    	// Session에 값이 없으면 로그인 스크린으로
    	setContent(new LoginScreen());

    }

    @WebServlet(urlPatterns = "/*", name = "VSeminarUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = VSeminarUI.class, productionMode = false)
    public static class VSeminarUIServlet extends VaadinServlet {
    }
}
