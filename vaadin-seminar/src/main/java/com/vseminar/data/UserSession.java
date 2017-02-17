package com.vseminar.data;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.WrappedSession;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.VaadinSessionScope;
import com.vseminar.data.model.User;

@VaadinSessionScope
@SpringComponent
@SuppressWarnings("serial")
public class UserSession implements Serializable {
	
	// Session Key
	public static final String SESSION_KEY = UserSession.class.getCanonicalName();
		
	private UserData userData;
	
	@Autowired
	public UserSession(UserData userData) {
		this.userData = userData;
	}
		
	/**
	 * // 현재섹션로그인유저정보가져오기
	 * @return
	 */
	public User getUser() {  
		User user = (User) getCurrentSession().getAttribute(SESSION_KEY);
		return user;
    }

	/**
	 * // 현재섹션로그인유저정보갱신
	 * @param user
	 */
    public void setUser(User user) { 
    	if (user == null) {
    		getCurrentSession().removeAttribute(SESSION_KEY);      
        } else {
        	getCurrentSession().setAttribute(SESSION_KEY, user);
        }
    }

    /**
     * // 현재섹션로그인유무확인
     * @return
     */
    public boolean isSignedIn() { 
        return getUser() != null;
    }

    /**
     * 로그인처리
     * @param email
     * @param password
     */
    public void signin(String email, String password) {    
    	User user = userData.findByEmailAndPassword(email, password);
    	if(user.getId()==null) {
			throw new UserNotFoundException("user not found");
		}
    	// 현재섹션에로그인유저정보담기
		setUser(user);		
    }
    
    /**
     * 로그아웃
     */
    public void signout() { 
    	getCurrentSession().invalidate(); // 현재섹션무효화처리
    	com.vaadin.server.Page.getCurrent().reload();  // 현재페이지리로딩
    }
    
    /**
     * Vaadin의 Request/Session 정보 객체
     * @return
     */
    private WrappedSession getCurrentSession() {
        VaadinRequest request = VaadinService.getCurrentRequest();        
        if (request == null) {
            throw new IllegalStateException("No request bound to current thread");
        }
        WrappedSession session = request.getWrappedSession();
        if (session == null) {
            throw new IllegalStateException("No Session bound to current thread");
        }
        return session;
    }

}