package com.vseminar.view;

import java.io.Serializable;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public abstract class AbstractForm<T> extends CustomComponent {

    public interface SaveHandler<T> extends Serializable {

        void onSave(T entity);
    }

    public interface DeleteHandler<T> extends Serializable {

        void onDelete(T entity);
    }
    
	private SaveHandler<T> saveHandler;
	
    private DeleteHandler<T> deleteHandler;
	
    public SaveHandler<T> getSaveHandler() {
        return saveHandler;
    }

    public DeleteHandler<T> getDeleteHandler() {
        return deleteHandler;
    }
    
    public void setSaveHandler(SaveHandler<T> saveHandler) {
        this.saveHandler = saveHandler;
    }

    public void setDeleteHandler(DeleteHandler<T> deleteHandler) {
        this.deleteHandler = deleteHandler;
    }
    
    protected abstract void save(Button.ClickEvent e);

    protected abstract void delete(Button.ClickEvent e);
    
    private Window window;
    
	public Window openPopup(String title) {
		window = new Window(title, this); // 현재 객체를 윈도우로 띄운다.		
		window.setModal(true); // 모달로 띄우기
		window.setResizable(true); // 리사이즈 버튼 추가
		window.center();           // 창 위치는 가운데
        UI.getCurrent().addWindow(window); // 현재 UI를 부모로 해서 새창 띄우기
        return window;
    }

    public void closePopup() {
        if(window != null) {
        	window.close(); // 현재 윈도우 창 닫기
        	window = null;
        }
    }
        
}
