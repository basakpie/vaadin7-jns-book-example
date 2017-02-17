package com.vaadin.addon.onoffswitch.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HasValue;

public class OnOffSwitchWidget extends FocusWidget implements HasValue<Boolean> {

	private Element slider;
	private boolean value = false;
	
	public OnOffSwitchWidget() {
		/**
		 *
		  <div class="onoffswitch">
		    <label class="onoffswitch-slider off"></label>
		</div>
		 *
		 **/
		setElement(Document.get().createDivElement());
        setStyleName("onoffswitch");
        
        slider = Document.get().createLabelElement();        
        slider.setClassName("onoffswitch-slider");
        
        getElement().appendChild(slider);
        
        updateStyleName(false);
	}
	
	private void updateStyleName(boolean isChecked) {
        if (isChecked) {
        	slider.addClassName("on");
        	slider.removeClassName("off");
        } else {
        	slider.addClassName("off");
        	slider.removeClassName("on");
        }
    }

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Boolean> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public Boolean getValue() {
		return value;
	}

	@Override
	public void setValue(Boolean value) {
		setValue(value, false);
	}

	@Override
	public void setValue(Boolean value, boolean fireEvents) {
		if (value == null) {
        	value = Boolean.FALSE;
        }  
        if (this.value != value) {
            this.value = value;
            updateStyleName(value);
            if (fireEvents) {
                ValueChangeEvent.fire(this, value);
            }
        }
	}

}