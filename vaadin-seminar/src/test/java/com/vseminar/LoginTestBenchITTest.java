package com.vseminar;

import org.junit.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.PasswordFieldElement;
import com.vaadin.testbench.elements.TextFieldElement;


public class LoginTestBenchITTest extends TestBenchTestCase {
	
	private static final String baseUrl = "http://127.0.0.1:8080/vaadin-seminar";
		
	@Before
	public void setUp() throws Exception {
	    	setDriver(new FirefoxDriver()); // 런타임에 파이어폭스 브라우저를 실행 시킨다.
	    	driver.get(baseUrl + "/"); // 브라우저의 주소를 이동 한다.
	    }
	
	@Test
	public void testLoginTestBenchIT () throws Exception {		
	    TextFieldElement email = $(TextFieldElement.class).id("email_field");
	    PasswordFieldElement password = $(PasswordFieldElement.class).id("password_field");
	    ButtonElement signin = $(ButtonElement.class).id("signin_button");
		    
	    email.clear();
	    email.setValue("user1@vseminar.com");
		    
	    password.clear();
	    password.setValue("1234");
		    
	    signin.click();
	}
	
	@After
	public void tearDown() throws Exception {	
		driver.quit();// 브라우저를 종료 시킨다.
	}

}

