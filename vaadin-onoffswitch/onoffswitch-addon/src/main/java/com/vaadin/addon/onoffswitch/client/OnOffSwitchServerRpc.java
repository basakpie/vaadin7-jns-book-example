package com.vaadin.addon.onoffswitch.client;

import com.vaadin.shared.communication.ServerRpc;

public interface OnOffSwitchServerRpc extends ServerRpc {
	
	// Widget OnOffSwitch 클릭시 Widget.getValue를 서버측에 전달 한다.
	public void clicked(boolean checked);
	
}
