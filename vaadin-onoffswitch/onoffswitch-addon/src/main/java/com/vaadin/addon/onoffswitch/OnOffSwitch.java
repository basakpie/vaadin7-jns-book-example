package com.vaadin.addon.onoffswitch;

import com.vaadin.addon.onoffswitch.client.OnOffSwitchClientRpc;
import com.vaadin.addon.onoffswitch.client.OnOffSwitchServerRpc;
import com.vaadin.addon.onoffswitch.client.OnOffSwitchState;

import com.vaadin.shared.MouseEventDetails;

// This is the server-side UI component that provides public API 
// for OnOffSwitch
public class OnOffSwitch extends com.vaadin.ui.AbstractComponent {

	private int clickCount = 0;

	// To process events from the client, we implement ServerRpc
	private OnOffSwitchServerRpc rpc = new OnOffSwitchServerRpc() {

		// Event received from client - user clicked our widget
		public void clicked(MouseEventDetails mouseDetails) {
			
			// Send nag message every 5:th click with ClientRpc
			if (++clickCount % 5 == 0) {
				getRpcProxy(OnOffSwitchClientRpc.class)
						.alert("Ok, that's enough!");
			}
			
			// Update shared state. This state update is automatically 
			// sent to the client. 
			getState().text = "You have clicked " + clickCount + " times";
		}
	};

	public OnOffSwitch() {

		// To receive events from the client, we register ServerRpc
		registerRpc(rpc);
	}

	// We must override getState() to cast the state to OnOffSwitchState
	@Override
	protected OnOffSwitchState getState() {
		return (OnOffSwitchState) super.getState();
	}
}
