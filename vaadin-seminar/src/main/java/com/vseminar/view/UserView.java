package com.vseminar.view;

import java.util.List;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.CellStyleGenerator;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vseminar.data.UserData;
import com.vseminar.data.UserSession;
import com.vseminar.data.model.User;
import com.vseminar.view.AbstractForm.DeleteHandler;
import com.vseminar.view.AbstractForm.SaveHandler;

@SuppressWarnings("serial")
public class UserView extends VerticalLayout implements View {
	
	public static final String VIEW_NAME = "user";
	
	UserData userData;
	
	BeanItemContainer<User> container;
	
	UserForm userForm;
	
	public UserView() {		
		userData = UserData.getInstance();
		
		setHeight(100, Unit.PERCENTAGE); // VertiaclLayout의 Height 사이즈
		Table table = createTable(); // 테이블 생성
		
		createForm(); // UserForm의 Save,DeleteHandler를 구현
		findBean(); // Table의 BeanItemContainer에 데이터를 채워 준다.
		
		addComponent(createTopBar());
		addComponent(table); // 테이블 추가
        setExpandRatio(table, 1); // 화면에서 테이블이 영역이 나머지 영역 모두 차지
	}
	
	public HorizontalLayout createTopBar() {
		Label title = new Label("User");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
          
        Button newBtn = new Button("New"); // 신규 유저 추가 버튼
        newBtn.addStyleName(ValoTheme.BUTTON_PRIMARY);
        newBtn.setIcon(FontAwesome.PLUS_CIRCLE);
        newBtn.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {          
            	// UserForm을 여기서 sub-window로 팝업 연동
            	userForm.lazyInit(new User());
				userForm.openPopup("New User");
            }
        });
        
        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.addStyleName("top-bar"); // title과 table간의 여백 추가
        topLayout.setWidth(100, Unit.PERCENTAGE);
        topLayout.addComponents(title);
        topLayout.addComponent(newBtn); // 신규 유저 추가 버튼을 레이아웃에 추가
        topLayout.setComponentAlignment(title, Alignment.MIDDLE_LEFT);       
        topLayout.setExpandRatio(title, 1); // 버튼을 오른쪽으로 밀어준다.
        return topLayout;
    }
	
	private Table createTable() {
		Table table = new Table(); // 테이블생성
		table.setSizeFull(); // 테이블사이즈변경
		table.setSelectable(true); // 테이블 아이템 선택 기능 활성화
		// 빈 BeanItemContainer를 생성 후 테이블에 넘겨줘 기본 정보를 그리게 한다.
		table.setContainerDataSource(container = new BeanItemContainer<>(User.class));
		
		// user.getImgPath() 데이터를 Image Component로 구성
		table.addGeneratedColumn("picture", new ColumnGenerator() {
			@Override
			public Image generateCell(Table source, Object itemId, Object columnId) {
				User user = (User)itemId; // 해당 row의 item 데이터 가져오기
				Resource image = new ThemeResource(user.getImgPath());
				return new Image(null, image); // picture 컬럼은 Image로 표현
			}
		});
		
		// picture 컬럼에 v-round-image css 입히기
		table.setCellStyleGenerator(new CellStyleGenerator(){
			@Override
			public String getStyle(Table source, Object itemId, Object propertyId) {
				if(propertyId!=null) { 
					if(propertyId.equals("picture")) { // 컬럼명이 picture인 경우
						return "v-round-image"; // v-round-image 라는 css 적용
					}
				}
				return null;
			}
		});		
		
		// 컬럼의 순서를 setVisibleColumns으로 결정 한다.
        // setVisibleColumns의 imgPath를 위에서 추가한 picture 컬럼으로 변경해 준다.
		table.setVisibleColumns("picture", "name","email","password", "role");
		
		// 헤더 컬럼명을 setVisibleColumns과 동일한 순서로 변경 한다.
		table.setColumnHeaders("IMG", "Name","Email","Password", "Role");
		
		// 특정 컬럼의 가로폭을 지정한다. picture 컬럼의 가로폭 조정
		table.setColumnWidth("picture", 60);
		
		// 아이템 클릭 이벤트 처리
		table.addItemClickListener(new ItemClickListener() {
			@Override
			public void itemClick(ItemClickEvent event) {
				// UserForm을 여기서 sub-window로 팝업 연동
				userForm.lazyInit((User)event.getItemId());
				userForm.openPopup("Edit Profile");
			}
		});
		
		return table;
	}
	
	private void findBean() {
		List<User> users = userData.findAll();
		if(users.size()>0) {
			container.removeAllItems();	// BeanItemContainer에 데이터 지우기.		
		}
		// BeanItemContainer에 데이터 채우면 테이블이 동적 갱신 되어짐
		container.addAll(users);
	}
	
	private void createForm() {		
		userForm = new UserForm();
		userForm.setSaveHandler(new SaveHandler<User>() {
			@Override
			public void onSave(User entity) {
				userForm.closePopup();
				// 로그인한 유저이며 메뉴의 이름을 갱신하기 위해 화면 리로드
				if(UserSession.getUser().getId()==entity.getId()) {
					Page.getCurrent().reload();
					return;
				}
				findBean(); // 테이블의 데이터를 동적으로 갱신	
			}
		});
		userForm.setDeleteHandler(new DeleteHandler<User>() {
			@Override
			public void onDelete(User entity) {
				userForm.closePopup();
				findBean(); // 테이블의 데이터를 동적으로 갱신
			}
		});
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
	}
}
 
