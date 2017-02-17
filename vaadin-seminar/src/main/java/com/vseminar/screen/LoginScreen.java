package com.vseminar.screen;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.validator.EmailValidator;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vseminar.data.UserNotFoundException;
import com.vseminar.data.UserSession;

@SpringComponent
@UIScope
@SuppressWarnings("serial")
public class LoginScreen extends VerticalLayout {

	final UserSession userSession;
	final VerticalLayout loginForm = new VerticalLayout();		
	final VerticalLayout buildLabels = new VerticalLayout();	
	final HorizontalLayout buildFields = new HorizontalLayout();
		
	final Label titleLabel = new Label("welcome to vaadin serminar");
	final Label errorLabel = new Label();
		
	final TextField email = new TextField("Email"); 
	final PasswordField password = new PasswordField("Password");
	final Button signin = new Button("Sign In");
					
	public VerticalLayout getLoginForm() { 
		return loginForm; 
	}
	
	public VerticalLayout getBuildLabels() { 
		return buildLabels; 
	}
	
	public HorizontalLayout getBuildFields() { 
		return buildFields; 
	}
	
	public Label getTitleLabel() { 
		return titleLabel; 
	}
	
	public Label getErrorLabel() { 
		return errorLabel; 
	}
	
	public TextField getEmail() { 
		return email; 
	}
	
	public PasswordField getPassword() { 
		return password; 
	}
	
	public Button getSignin() { 
		return signin; 
	}
	
	public UserSession getUserSession() { 
		return userSession; 
	}

    @Autowired
    public LoginScreen(UserSession userSession) {
    	this.userSession = userSession;
    }

    @PostConstruct
	public void init() {
		// Layout에 CSS Style 추가
		addStyleName("login-screen"); 
        // 1. 유저 세션 처리를 위한 객체 선언
		// userSession = new UserSession();		
        // Layout에 CSS Sytle 추가
		addStyleName("login-screen");
	    // 레이아웃사이즈를 화면 전체로 설정 한다.
        setSizeFull();
        
        // 로그인에 필요한 컴포넌트(s) 구성
        buildForm();
        buildLabels();
        buildFields();
        
        // 로그인폼 컴포넌트를 Layout에 추가(add)해 준다.
        addComponent(loginForm);
        // 로그인폼 컴포넌트의 정렬 위치를 중앙으로 설정 한다.
        setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);
	}
	
	private void buildForm() {
        //final VerticalLayout loginPanel = new VerticalLayout();
		//… loginPanel -> loginForm rename
		this.loginForm.addStyleName("login-panel"); // 스타일 추가
		this.loginForm.setSizeUndefined(); // setWidth,setHeight(-1, Unit.PIXELS)
		this.loginForm.setSpacing(true);   // Margin or Spacing을 통해 여백처리
		this.loginForm.addComponent(buildLabels);  // 타이틀s(welcome to vaadin seminar)
		this.loginForm.addComponent(buildFields); // 필드(username, password, sign in)        
    }
	
	private void buildLabels() {		
		this.titleLabel.setId("title_label"); 
		this.titleLabel.addStyleName(ValoTheme.LABEL_H4);        // 스타일추가
		this.titleLabel.addStyleName(ValoTheme.LABEL_COLORED);   // 스타일추가(color bule)
        
		this.errorLabel.setId("error_label");
        this.errorLabel.addStyleName(ValoTheme.LABEL_FAILURE);
        this.errorLabel.setVisible(false);

        this.buildLabels.addComponent(titleLabel);
        this.buildLabels.addComponent(errorLabel);  
	}

	private void buildFields() {        
		this.email.setId("email_field");
		this.email.setIcon(FontAwesome.USER);                     // FontAwesome Icon
		this.email.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON); // Icon을 Field 안쪽에 표현
		this.email.addValidator(new EmailValidator("Invalid e-mail address {0}"));//Email패턴검증

        this.password.setId("password_field");
        this.password.setIcon(FontAwesome.LOCK);
        this.password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        this.signin.setId("signin_button");
        this.signin.addStyleName(ValoTheme.BUTTON_PRIMARY); // 버튼컬러(블루)
        this.signin.setClickShortcut(KeyCode.ENTER);        // Enter키지원
        this.signin.focus();                                // 포커싱
        this.signin.addClickListener(new ClickListener() {  // 버튼 클릭시 이벤트처리
            @Override
            public void buttonClick(final ClickEvent event) {            	
                try {
            		// 2. 로그인처리(확인)
            		userSession.signin(email.getValue(), password.getValue());
            		// 3. 로그인처리후 화면 리로드
            		Page.getCurrent().reload(); 
            	} catch (UserNotFoundException ex) {
            		// 실패한 이유를 사용자에게 알려 주기
            		// Notification.show("SignIn Failed :", ex.getMessage(), Notification.Type.ERROR_MESSAGE);
            		errorLabel.setValue(String.format("Login Failed: %s", ex.getMessage()));
            	    errorLabel.setVisible(true);
            	}
            }
        });

        this.buildFields.setSpacing(true);
        this.buildFields.addComponents(email, password, signin); // Layout에 모든 필드 추가(담기)
        this.buildFields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT); // 버튼 정렬        
    }

}
