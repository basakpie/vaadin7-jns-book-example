package com.vseminar.config;

import javax.servlet.ServletException;

import com.vaadin.spring.server.SpringVaadinServlet;

@SuppressWarnings("serial")
public class VSeminarServlet extends SpringVaadinServlet {	
	@Override
    protected final void servletInitialized() throws ServletException {
        super.servletInitialized();
        // VSeminarSessionInitListener -> VSpringSeminarSessionInitListener 클래스명 변경
        getService().addSessionInitListener(new VSeminarSessionInitListener());
    }	
}
