package com.vseminar.view;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.data.fieldgroup.FieldGroup.CommitEvent;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.FieldGroup.CommitHandler;
import com.vaadin.data.sort.Sort;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.MultiSelectionModel;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vseminar.data.SessionData;
import com.vseminar.data.UserData;
import com.vseminar.data.UserSession;
import com.vseminar.data.model.RoleType;
import com.vseminar.data.model.Session;

@SuppressWarnings("serial")
public class SessionView extends VerticalLayout implements View {	
	
	public static final String VIEW_NAME = "session";
	
	SessionData sessionData;
	UserData userData;
	
	Grid grid;
	BeanItemContainer<Session> container;
	
	Button newBtn;
	Button delBtn;
	
	public SessionView() {
		sessionData = SessionData.getInstance();
		userData = UserData.getInstance();
		
		setHeight(100, Unit.PERCENTAGE); // VertiaclLayout의 Height 사이즈		
		grid = createGrid(); // 그리드 생성
		
		findBean(); // Grid에 BeanItemContainer에 데이터를 채워 준다.
		
		addComponent(createTopBar());
		addComponent(grid);  // 그리드 추가      
        setExpandRatio(grid, 1); // 화면에서 그리드가영역이 나머지 영역 모두 차지
	}
	
	public HorizontalLayout createTopBar() {
		Label title = new Label("Session");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        
        // 추가버튼
        newBtn = new Button("New");
        newBtn.addStyleName(ValoTheme.BUTTON_PRIMARY);
        newBtn.setIcon(FontAwesome.PLUS_CIRCLE);
        newBtn.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {        
            	// containr에 빈 session bean을 추가해 준다.
            	// 이후 Grid 수정시 처럼 row 더블 클릭하고 데이터 입력 후 저장한다.
            	container.addItemAt(0, new Session(UserSession.getUser().getId()));
        	    grid.scrollToStart(); // 스크롤을 맨 위로 이동 처리
            }
        });
        // 삭제버튼
        delBtn = new Button("Delete");
        delBtn.addStyleName(ValoTheme.BUTTON_DANGER);
        delBtn.setIcon(FontAwesome.MINUS_CIRCLE);
        delBtn.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	// 선택 된 grid의 row의 정보를 가져온다.
            	MultiSelectionModel selection = (MultiSelectionModel)grid.getSelectionModel();
            	for (Object itemId: selection.getSelectedRows()) {
            		Session session = (Session)itemId;            		
            		if(session.getId()!=null) {
            			sessionData.delete(session.getId()); // 데이터를 삭제
            		}
            		// 그리드에서도 제거
            		grid.getContainerDataSource().removeItem(session); // item 제거
                	grid.getSelectionModel().reset(); // 그리드 선택 기능 초기화                	
            	}
            	delBtn.setEnabled(false); // 삭제 버튼 비활성 처리
            }
        });
        
        delBtn.setEnabled(false);
        
        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.addStyleName("top-bar"); // title과 table간의 여백 추가
        topLayout.setSpacing(true);        
        topLayout.setWidth(100, Unit.PERCENTAGE);
        topLayout.addComponents(title);
        topLayout.addComponent(newBtn);
        topLayout.addComponent(delBtn);
        topLayout.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
        topLayout.setExpandRatio(title, 1);
        return topLayout;
    }
	
	private Grid createGrid() {
		final Grid grid = new Grid(); // 그리드생성
		grid.setSizeFull();		// 그리드사이즈변경
		grid.setEditorEnabled(true); // 수정모드활성화
		grid.setSelectionMode(SelectionMode.MULTI); // 그리드 선택 모드를 멀티로 활성화
		
		// 빈 BeanItemContainer를 생성 후 그리드에 넘겨줘 기본 정보를 그리게 한다
		grid.setContainerDataSource(container = new BeanItemContainer<>(Session.class, null));
		
		// 컬럼의순서를 나열한다.		
		grid.setColumnOrder("id", "title", "level", "startDate", "endDate", "embeddedUrl", "speaker", "description");
		
        // 컬럼명과 숨김, 가로폭사이즈를 조절 한다.
		grid.getColumn("id").setHeaderCaption("ID").setHidden(true).setEditable(false);
		grid.getColumn("title").setHeaderCaption("Title").setEditorField(textEditorField());
		grid.getColumn("level").setHeaderCaption("Level");
		grid.getColumn("startDate").setHeaderCaption("StartDate").setEditorField(dateEditorField()); 
		grid.getColumn("endDate").setHeaderCaption("EndDate").setEditorField(dateEditorField());		
		grid.getColumn("speaker").setHeaderCaption("Speaker").setEditorField(textEditorField());;
		grid.getColumn("embeddedUrl").setHeaderCaption("Presentation").setEditorField(textEditorField()).setMaximumWidth(200);
		grid.getColumn("description").setHeaderCaption("Description").setEditorField(textEditorField()).setMaximumWidth(200);
		grid.getColumn("ownerId").setHeaderCaption("Owner").setHidden(true).setEditable(false);
		grid.getColumn("questions").setHidden(true).setEditable(false);
		// 수정시 입력값을 검증
		grid.getColumn("title").getEditorField().addValidator(new StringLengthValidator("The name must be 1-50 letters (was {0})", 1, 50, true));
		
		// Editor를 통해 Save 저장시 이벤트 발생
		grid.getEditorFieldGroup().addCommitHandler(new CommitHandler(){
			@Override
			public void preCommit(CommitEvent event) throws CommitException {				
			}
			@Override
			public void postCommit(CommitEvent event) throws CommitException {
				Session session = (Session)grid.getEditedItemId(); // 수정된 item 가져오기			
				sessionData.save(session);				
			}
		});	
		
		// 그리드 row 선택 사이즈에 따라 삭제 버튼을 동적으로 활성/비활성 처리
		grid.addSelectionListener(new SelectionListener() {
			@Override
			public void select(SelectionEvent event) {
				delBtn.setEnabled(grid.getSelectedRows().size() > 0);
			}
		});
		
		return grid;
	}
	
	private void findBean() {
		List<Session> sessions = new ArrayList<>();
		
		if(UserSession.getUser().getRole()==RoleType.Admin) {
		    // 관리자 권한이면 전체 Session 을 보여준다.
		    sessions.addAll(sessionData.findAll()); 
		 } else {
		    // 일반 사용자면 자신이 개설한 세션만 보여준다.
		    sessions.addAll(sessionData.findByOwner(UserSession.getUser()));
		 }
		
		 if(sessions.size()<=0) return;
		 
	     container.removeAllItems();
		 container.addAll(sessions);
		 
		 // 세션 리스트를 시작일시로 정렬
		 grid.sort(Sort.by("startDate", SortDirection.ASCENDING));
	}
		
	private DateField dateEditorField() {
		DateField dateField = new DateField();
		dateField.setResolution(Resolution.MINUTE); // 달력 + 시:분
		return dateField;
	}
	
	private TextField textEditorField() {
		TextField textField = new TextField();
		textField.setNullRepresentation(""); // null 대신 empty로 출력
		return textField;
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
	}
	
}
