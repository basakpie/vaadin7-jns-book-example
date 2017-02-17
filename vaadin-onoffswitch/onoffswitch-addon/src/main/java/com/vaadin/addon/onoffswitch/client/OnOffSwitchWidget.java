package com.vaadin.addon.onoffswitch.client;

import com.google.gwt.user.client.ui.Label;

// Extend any GWT Widget
public class OnOffSwitchWidget extends Label {

	public OnOffSwitchWidget() {

		// CSS class-name should not be v- prefixed
		setStyleName("onoffswitch");

		// State is set to widget in OnOffSwitchConnector		
	}

}