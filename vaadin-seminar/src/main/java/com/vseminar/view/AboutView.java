package com.vseminar.view;

import javax.annotation.PostConstruct;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringView(name = AboutView.VIEW_NAME)
@SuppressWarnings("serial")
public class AboutView extends VerticalLayout implements View {

	public static final String VIEW_NAME = "about";
	
	@PostConstruct
	public void init() {
		setHeight(100, Unit.PERCENTAGE);
		addComponent(createTopBar());
	}
	
	public HorizontalLayout createTopBar() {
		Label title = new Label("About");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
          
        HorizontalLayout topLayout = new HorizontalLayout(); 
        topLayout.addStyleName("top-bar");
        topLayout.setSpacing(true);
        topLayout.setWidth(100, Unit.PERCENTAGE);
        topLayout.addComponents(title);
        topLayout.setComponentAlignment(title, Alignment.MIDDLE_LEFT);        
        return topLayout;
    }
	
	@Override
	public void enter(ViewChangeEvent event) {
	}

}
