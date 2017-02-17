package com.vseminar.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class ErrorView extends VerticalLayout implements View {

	private Label explanation;
	
	public ErrorView() {
		removeAllComponents();
		setSizeFull();
		setMargin(true);
	    setSpacing(true);
	
	    Label header = new Label("404 NOT FOUND");
	    header.addStyleName(ValoTheme.LABEL_H1);
	    addComponent(header);
	    addComponent(explanation = new Label());
	    Image image = new Image(null, new ThemeResource("img/vaadin-icon.jpg"));
	    addComponent(image);
	    setComponentAlignment(image, Alignment.MIDDLE_CENTER);
	}
	
	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		explanation.setValue(String.format("You tried to navigate to a view ('%s') that does not exist.",event.getViewName()));
    }
}

