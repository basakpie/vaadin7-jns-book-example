package com.vseminar.menu;

import java.util.Iterator;

import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.themes.ValoTheme;
import com.vseminar.data.UserSession;
import com.vseminar.data.model.RoleType;
import com.vseminar.data.model.User;
import com.vseminar.view.AbstractForm.SaveHandler;
import com.vseminar.view.DashboardView;
import com.vseminar.view.UserForm;

@SuppressWarnings("serial")
public class VSeminarMenu extends CssLayout {

	private static final String VALO_MENU_VISIBLE  = "valo-menu-visible";
	private static final String VALO_MENU_TOGGLE   = "valo-menu-toggle";	
    private static final String VALO_MENUITEMS     = "valo-menuitems";    
        
    private CssLayout menuPart; // 메뉴들을 담을 루트 레이아웃
    private CssLayout menuItems = new CssLayout(); // 메뉴아이템리스트 담을 레이아웃
	
    public VSeminarMenu(final VSeminarNavigator navigator, final UserForm userForm, final UserSession userSession) {
    	// ValoTheme.MENU** 는 이미 빌트인 되어 있는 Valo menu styles CSS이다.  
        setPrimaryStyleName(ValoTheme.MENU_ROOT);
        
        // 2. 메뉴파트 레이아웃
        menuPart = new CssLayout();
        menuPart.addStyleName(ValoTheme.MENU_PART);
		
        // 상단 타이틀
		final Label title = new Label("<h3>Vaadin <strong>Seminar</strong></h3>", ContentMode.HTML);
        title.setSizeUndefined();
		
        // 3. 타이틀 스타일 레이아웃
		final HorizontalLayout top = new HorizontalLayout();
        top.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        top.addStyleName(ValoTheme.MENU_TITLE);
        top.setSpacing(true);
        top.addComponent(title);
        
        // 타이틀을 메뉴파트에 추가하기
        menuPart.addComponent(top);
   
        // 4. 사용자메뉴 생성
		final MenuBar userMenu = new MenuBar();
		// ValoTheme Built-in user-menu Style 
		userMenu.addStyleName("user-menu");
		// 사용자메뉴에 아이템(프로필사진) 추가		
		Resource image = new ThemeResource(userSession.getUser().getImgPath());
		final MenuItem userMenuItem = userMenu.addItem(userSession.getUser().getName(), image, null);
		
		// 아이템에 회원정보 수정 버튼 추가 
		userMenuItem.addItem("Edit Profile", new MenuBar.Command() {
            @Override
            public void menuSelected(final MenuItem selectedItem) {
            	// 회원정보수정 폼(Form) 팝업(Window)으로 띄우기
            	// final UserForm userForm = new UserForm();
            	userForm.lazyInit(userSession.getUser()); // sub-window 설정
            	userForm.openPopup("Edit Profile"); // sub-Window 타이틀
            	// Form에서 데이터 저장 후 발생 할 이벤트를 등록 해 준다.
            	userForm.setSaveHandler(new SaveHandler<User>() {
					@Override
					public void onSave(User user) {						
						userForm.closePopup(); 	// 저장 후 sub-Window 닫기	
						Page.getCurrent().reload(); // 화면 리로드
					}
				});
            }
        });
		
		// 아이템에 로그아웃 버튼 추가
		userMenuItem.addItem("Sign Out", new MenuBar.Command(){
			@Override
			public void menuSelected(MenuItem selectedItem) {
				// 로그아웃처리
				userSession.signout();
			}
		});
		
		// 사용자메뉴를 메뉴파트에 다시 추가		
		menuPart.addComponent(userMenu);
        
		// 5. 반응형 웹 적용시 나타날 메뉴네비버튼
        final Button showMenu = new Button("Menu", new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
            	// 버튼을 클릭하면 toggle 형태로 메뉴파트레이아웃이 보였다 안 보였다 처리
                if (menuPart.getStyleName().contains(VALO_MENU_VISIBLE)) {
                	menuPart.removeStyleName(VALO_MENU_VISIBLE);
                } else {
                	menuPart.addStyleName(VALO_MENU_VISIBLE);
                }
            }
        });
        
        // 메뉴네비아이콘 스타일 적용
        showMenu.addStyleName(ValoTheme.BUTTON_PRIMARY);
        showMenu.addStyleName(ValoTheme.BUTTON_SMALL);
        showMenu.addStyleName(VALO_MENU_TOGGLE);
        showMenu.setIcon(FontAwesome.NAVICON);
        
        // 메뉴네비버튼을 메뉴파트에 다시 추가
        menuPart.addComponent(showMenu);
        
        // 6. 메뉴아이템리스트 레이아웃
        menuItems = new CssLayout();
        // ValoTheme Built-in CSS
        menuItems.setPrimaryStyleName(VALO_MENUITEMS);
        
        RoleType sectionType = null;
        // 현재 사용자의 권한에 맞는 메뉴만 아이템 목록으로 추가 하기 
        for(Navi item: navigator.getActiveNaviMaps().values()) {        	
        	final String fragment = item.getFragment();	 // vaadin-seminar/#!{fragment} 주소	
        	final String viewName = item.getViewName();	 // 메뉴명	
        	final Class<? extends View> viewClass = item.getViewClass(); // View 클래스    
        	final Resource icon = item.getIcon(); // 메뉴명 아이콘   
        	final RoleType roleType = item.getRoleType(); // 접근권한(USER, ADMIN)
        	
        	if(viewClass!=DashboardView.class && sectionType!=roleType) {
        		sectionType = roleType;
        		Label label = new Label("Role_"+sectionType.name(), ContentMode.HTML);
                label.setPrimaryStyleName(ValoTheme.MENU_SUBTITLE);
                label.addStyleName("h4");
                label.setSizeUndefined();
                // 메뉴 아이템 그룹(USER, ADMIN)별 언더라인스타일 추가
                menuItems.addComponent(label);
        	}
        	
        	// 네비버튼
			final Button naviBtn = new Button(viewName, new ClickListener() {
                @Override
                public void buttonClick(final ClickEvent event) {
                	// 메뉴아이템버튼 클릭시 ViewArea만 해당 View로 동적 교체 처리
                	navigator.navigateTo(fragment);
                }
            });
			
			// 네비버튼에 fragment값 넣기두기
			naviBtn.setData(fragment);
			
			// 네비버튼스타일추가
        	naviBtn.setPrimaryStyleName(ValoTheme.MENU_ITEM);
        	naviBtn.setIcon(icon); 
        	
        	// 네비버튼을 메뉴아이템에 추가
        	menuItems.addComponent(naviBtn);
        }
        
        // 권한에 맞게 정리된 메뉴아이템리스트를 메뉴파트에 추가
        menuPart.addComponent(menuItems);
        
        // 메뉴파트레이아웃을 VSeminarMenu에 추가
        addComponent(menuPart);
    }
    
    public void setSelectedItem(String viewName) {
    	if(menuItems.getComponentCount()<=0) {
    		return;
    	}    	
    	for (final Iterator<Component> it = menuItems.iterator(); it.hasNext();) {
    		final Component item = it.next();
    		if (item instanceof Button) {
    			final Button naviBtn = (Button)item;
            	// 메뉴아이템리스트의 버튼에 대해 selected 상태 해제
        		naviBtn.removeStyleName("selected");
        		String fragment = (String)naviBtn.getData();        	
        		if (fragment.equals(viewName)) {
                	// parameter로 넘어온 viewName의 메뉴 아이템 버튼만 selected 상태 처리
        			item.addStyleName("selected");
        		}
			}
        }
    	// 반응형 웹 적용시 모바일 크기에서는 메뉴클릭후 메뉴파트영역 숨기기 처리 
    	menuPart.removeStyleName(VALO_MENU_VISIBLE);    	
    }
}
