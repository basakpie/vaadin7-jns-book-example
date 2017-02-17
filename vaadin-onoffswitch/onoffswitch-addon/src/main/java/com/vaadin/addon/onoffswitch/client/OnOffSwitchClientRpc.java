package com.vaadin.addon.onoffswitch.client;

import com.vaadin.shared.communication.ClientRpc;

public interface OnOffSwitchClientRpc extends ClientRpc {
	
    // 서버측에서의 클라이언트 호출 메서드 정의 
	public void alert(String message);
	
}