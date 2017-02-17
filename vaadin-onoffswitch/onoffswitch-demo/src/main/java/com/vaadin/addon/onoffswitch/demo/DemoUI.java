package com.vaadin.addon.onoffswitch.demo;

import com.vaadin.addon.onoffswitch.OnOffSwitch;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("demo")
@Title("OnOffSwitch Add-on Demo")
@SuppressWarnings("serial")
public class DemoUI extends UI
{

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = DemoUI.class, widgetset = "com.vaadin.addon.onoffswitch.demo.DemoWidgetSet")
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {

    	final OnOffSwitch onOffSwitch = new OnOffSwitch(false);
        onOffSwitch.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				boolean checked = (boolean)event.getProperty().getValue();
				System.out.println("OnOffSwitch checked : " + checked);
			}
        });

        final VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.addComponent(onOffSwitch);
        layout.setComponentAlignment(onOffSwitch, Alignment.MIDDLE_CENTER);
        setContent(layout);

    }

}
