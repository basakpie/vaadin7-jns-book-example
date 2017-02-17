package com.vaadin.addon.onoffswitch.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.vaadin.addon.onoffswitch.OnOffSwitch;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

// OnOffSwitch 서버 Component에 클라이언트 Widget 바인딩
@Connect(OnOffSwitch.class)
@SuppressWarnings("serial")
public class OnOffSwitchConnector extends AbstractComponentConnector {

	// 클라이언트 서버간 RPC Proxy 생성
	OnOffSwitchServerRpc serverRpc = RpcProxy.create(OnOffSwitchServerRpc.class, this);
	
	// 클라이언트 RPC Receiver 구현 
	OnOffSwitchClientRpc clientRpc = new OnOffSwitchClientRpc() {
		public void alert(String message) {
			//com.google.gwt.user.client.Window.alert("gwt alert: "+message);
			//jsniAlert("jsni alert: "+message);
		}
	};
	
	public static native void jsniAlert(String msg) /*-{
	  $wnd.alert(msg);
	}-*/;

	public OnOffSwitchConnector() {
		// 클라이언트 RPC Receiver 등록
		registerRpc(OnOffSwitchClientRpc.class, clientRpc);
		// GWT Widget 클릭 핸들러 추가
		getWidget().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				boolean checked = !getWidget().getValue();
				// 서버 RPC clicked 메서드 호출
				serverRpc.clicked(checked);
			}
		});
	}
	
	@Override
	public OnOffSwitchWidget getWidget() {
		return (OnOffSwitchWidget) super.getWidget();
	}

	@Override
	public OnOffSwitchState getState() {
		return (OnOffSwitchState) super.getState();
	}
	
	@Override
	public void onStateChanged(StateChangeEvent stateChangeEvent) {
		super.onStateChanged(stateChangeEvent);
		// 서버 측에서 변경 요청한 checked value
        final boolean checked = getState().checked;
        // 클라이언트 Widget의 Value 변경
        getWidget().setValue(checked, true);
	}

}
