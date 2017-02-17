package com.vseminar;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.util.CurrentInstance;
import com.vseminar.data.UserSession;
import com.vseminar.screen.LoginScreen;

public class LoginScreenJUnitTest {

	VSeminarUI ui;	
	LoginScreen loginScreen;

	@Mock 
	ApplicationContext context;
	
	@Mock 
	Page page;
	
	@Mock 
	VaadinSession vaadinSession;
	
    @Mock 
    UserSession userSession;    	
		
	@Before
	public void setUp() throws Exception {		
		initMock();
		this.loginScreen = new LoginScreen(userSession);
		this.loginScreen.init();		
	}
		
	private void initMock() throws NoSuchFieldException, IllegalAccessException {
		MockitoAnnotations.initMocks(this); // 인스턴스 변수를 Mock 처리		
        ui = new VSeminarUI(context, userSession);
        CurrentInstance.setInheritable(UI.class, ui);
        
        Field pageField = UI.class.getDeclaredField("page");
        pageField.setAccessible(true);
        pageField.set(ui, page);
        
        Field sessionField = UI.class.getDeclaredField("session");
        sessionField.setAccessible(true);
        sessionField.set(ui, vaadinSession);
    }

	
	@Test
	public void isComponentNPropsTest1() {
		// 1. VerticalLayout(buildForm)
	    final VerticalLayout buildForm = (VerticalLayout)loginScreen.getComponent(0);
			
	        // 2. VerticalLayout(buildLabels)
		final Component buildLabels = buildForm.getComponent(0);
			
		// 3. HorizontalLayout(buildFields)
		final Component buildFields = buildForm.getComponent(1);
									
		assertTrue(buildLabels instanceof VerticalLayout);
		assertTrue(buildFields instanceof HorizontalLayout);
				
		VerticalLayout labels = (VerticalLayout)buildLabels;
			
		assertEquals(2, labels.getComponentCount());		
		assertTrue(labels.getComponent(0) instanceof Label);
		assertTrue(labels.getComponent(1) instanceof Label);	
			
		Label title = (Label)labels.getComponent(0);		
		Label error = (Label)labels.getComponent(1);
					
		assertEquals(title.getValue(), "welcome to vaadin serminar");		
		assertTrue(title.getStyleName().contains(ValoTheme.LABEL_H4));
		assertTrue(title.getStyleName().contains(ValoTheme.LABEL_COLORED));
			
		assertTrue(error.getStyleName().contains(ValoTheme.LABEL_FAILURE));
		assertFalse(error.isVisible());		
		//... buildFields 검증 생략
	}
	
	@Test
	public void isComponentNPropsTest2() {		
		// VerticalLayout(buildForm)
		int loginFormIndex = loginScreen.getComponentIndex(this.loginScreen.getLoginForm());	
		VerticalLayout buildForm = (VerticalLayout)loginScreen.getComponent(loginFormIndex);
			
		// VerticalLayout(buildLabels)
		int labelsIndex = buildForm.getComponentIndex(this.loginScreen.getBuildLabels());
		VerticalLayout buildLabels = (VerticalLayout)buildForm.getComponent(labelsIndex);
			
		// HorizontalLayout(buildFields)
		int fieldsIndex = buildForm.getComponentIndex(this.loginScreen.getBuildFields());
		
		@SuppressWarnings("unused")
		final HorizontalLayout buildFields = (HorizontalLayout)buildForm.getComponent(fieldsIndex);
					
		assertEquals(2, buildLabels.getComponentCount());		
			
		int titleIndex = buildLabels.getComponentIndex(this.loginScreen.getTitleLabel());
		Label title = (Label)buildLabels.getComponent(titleIndex);
			
		assertEquals(title.getValue(), "welcome to vaadin serminar");		
		assertTrue(title.getStyleName().contains(ValoTheme.LABEL_H4));
		assertTrue(title.getStyleName().contains(ValoTheme.LABEL_COLORED));
					
		int errorIndex = buildLabels.getComponentIndex(this.loginScreen.getErrorLabel());
		Label error = (Label)buildLabels.getComponent(errorIndex);
			
		assertTrue(error.getStyleName().contains(ValoTheme.LABEL_FAILURE));
		assertFalse(error.isVisible());
			
		//... buildFields 검증 생략
	}
	
	@Test
	public void isLoginTest() throws Exception {
		final TextField email = loginScreen.getEmail();
		final PasswordField password = loginScreen.getPassword();
		final Button signin = (Button) loginScreen.getSignin();
			
		email.setValue("user1@vseminar.com");
		password.setValue("1234");
			
		UserSession session = loginScreen.getUserSession();
			
		// stub: void 리턴 타입을 갖는 UserSession.signin 메서드의 행위 처리 
		doNothing().when(session).signin(email.getValue(), password.getValue());
		// run : 로그인 이벤트 실행
		signin.click();
		// assert: UserSession.signin 메서드가 한번 호출 되는지 확인
		verify(session, atLeastOnce()).signin(email.getValue(), password.getValue());
	}

}
