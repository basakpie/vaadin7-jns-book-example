package com.vseminar;

import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

@SuppressWarnings("unused")
public class LoginSeleniumITTest {
	
    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    @Before
    public void setUp() throws Exception {
    	driver = new FirefoxDriver();
    	baseUrl = "http://127.0.0.1:8080/vaadin-seminar";
    	driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test
    public void testLoginSeleniumIT() throws Exception {
	    driver.get(baseUrl + "/");
	    driver.findElement(By.id("email_field")).clear();
	    driver.findElement(By.id("email_field")).sendKeys("user1@vseminar.com");
	    driver.findElement(By.id("password_field")).clear();
	    driver.findElement(By.id("password_field")).sendKeys("1234");
	    driver.findElement(By.id("signin_button")).click();
    }

    @After
    public void tearDown() throws Exception {
	    driver.quit();
	    String verificationErrorString = verificationErrors.toString();
	    if (!"".equals(verificationErrorString)) {
	        fail(verificationErrorString);
	    }
    }

	private boolean isElementPresent(By by) {
	    try {
	        driver.findElement(by);
	        return true;
	    } catch (NoSuchElementException e) {
	        return false;
	    }
    }

    private boolean isAlertPresent() {
        try {
	        driver.switchTo().alert();
	        return true;
	    } catch (NoAlertPresentException e) {
	        return false;
	    }
    }

    private String closeAlertAndGetItsText() {
	    try {
	        Alert alert = driver.switchTo().alert();
	        String alertText = alert.getText();
	        if (acceptNextAlert) {
	            alert.accept();
	        } else {
	            alert.dismiss();
	        }
	        return alertText;
	    } finally {
	        acceptNextAlert = true;
	    }
    }
}
