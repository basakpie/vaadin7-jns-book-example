package com.vseminar.view;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutAction.ModifierKey;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vseminar.data.QuestionData;
import com.vseminar.data.SessionData;
import com.vseminar.data.UserData;
import com.vseminar.data.UserSession;
import com.vseminar.data.model.LevelType;
import com.vseminar.data.model.Question;
import com.vseminar.data.model.Session;
import com.vseminar.data.model.User;
import com.vseminar.image.ImageUploader;
import com.vseminar.push.MessageEventBus;
import com.vseminar.push.MessageEventBus.EventBusListener;

@SuppressWarnings({"serial", "static-access"})
public class DashboardView extends VerticalLayout implements View, EventBusListener {	
	
	private static final MessageEventBus eventBus = new MessageEventBus();
	
	public static final String VIEW_NAME = "";
	
	SessionData sessionData;
	QuestionData questionData;
	UserData userData;
	
	Table sessionTable;
	Table questionTable;
	
	TextArea textArea;
	Button button;	
	
	AtomicBoolean hasNewItem;
	
	public DashboardView() {		
		sessionData = SessionData.getInstance();
		questionData = QuestionData.getInstance();
		userData = UserData.getInstance(); 
		
		setHeight(100, Unit.PERCENTAGE);
		
		Component createContent = createContent(); // Content 생성   
		findSessionBean(); // SessionTable에 데이터를 채워 준다.
		
		addComponent(createTopBar()); // title 추가
		addComponent(createContent); // content tabs 추가
		setExpandRatio(createContent, 1);
		
		hasNewItem = new AtomicBoolean(); // 신규 메시지 도착 여부
		new RefreshThread().start(); // 새로 고침 스레드 시작
	}
	
	public HorizontalLayout createTopBar() {
		Label title = new Label("Dashboard");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
          
        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.addStyleName("top-bar"); // title의 여백 추가
        topLayout.setSpacing(true);
        topLayout.setWidth(100, Unit.PERCENTAGE);
        topLayout.addComponents(title);
        topLayout.setComponentAlignment(title, Alignment.MIDDLE_LEFT);        
        return topLayout;
    }
	
	private Component createContent() {		
		// ① contentLayout = ② sessionLayout + ③ questionLayout
		HorizontalLayout contentLayout = new HorizontalLayout();
		contentLayout.setSizeFull();
		contentLayout.setSpacing(true);
		
		// ② sessionLayout
		VerticalLayout sessionLayout = new VerticalLayout();		
		sessionLayout.setSizeFull();
		sessionLayout.addComponent(createSessionTab());
		
		// ③ questionLayout
		VerticalLayout questionLayout = new VerticalLayout();
		questionLayout.setSizeFull();
		questionLayout.addComponent(createQuestionTab());
						
		contentLayout.addComponent(sessionLayout);
		contentLayout.addComponent(questionLayout);
	    return contentLayout;
	}
	
	private TabSheet createSessionTab() {
	    // BrowserFrame은 iframe에 element 기능으로 external URL을 연결 할 수 있다.
	    final BrowserFrame browserFrame = new BrowserFrame();
	    browserFrame.setSizeFull();

	    // ②-① sessionTabSheet = sessionTable + browserFrame
	    TabSheet sessionTabSheet = new TabSheet();
	    sessionTabSheet.setSizeFull();
	    // 첫 번째 탭은 세션리스트 테이블로 채우기
	    sessionTabSheet.addTab(sessionTable = createSessionTable(), "Sessions", FontAwesome.CUBES);
	    // 두 번째 탭은 세션 embeddedUrl로 연결해 주기
	    sessionTabSheet.addTab(browserFrame, "Presentation", FontAwesome.EXTERNAL_LINK);
	    
	    // 탭을 클릭 이벤트 기능 추가 
	    sessionTabSheet.addSelectedTabChangeListener(new SelectedTabChangeListener() {
	        @Override
		 	public void selectedTabChange(SelectedTabChangeEvent event) {
		 	    // 현재 선택된 sessionTable의 item 정보를 가져오기
		 	    Session session = (Session)sessionTable.getNullSelectionItemId();
		 	    // browserFrame의 주소를 설정해 주기
		 	    browserFrame.setSource(new ExternalResource(session.getEmbeddedUrl()));
	        }
	    });
	    
	    return sessionTabSheet;
	}
	
	private Table createSessionTable() {	
	    // 세션리스트 테이블 구성
	    Table table = new Table();
	    table.setSizeFull();
	    table.setSelectable(true);
	    table.setContainerDataSource(new BeanItemContainer<>(Session.class));
	    
	    table.addItemClickListener(new ItemClickListener() {
			@Override
			public void itemClick(ItemClickEvent event) {
				// Session Item이 클릭 되면 해당 Questions으로 QuestionTable데이터 변경 
				clickSession((Session)event.getItemId());
			}
		});
	    
	    // 추가요구사항 : level (junior, senior) 문자 앞에 구분할 수 있는 써클 아이콘 추가
	    table.addGeneratedColumn("level", new ColumnGenerator() {
	    	@Override
	    	public Component generateCell(Table source, Object itemId, Object columnId) {
	    	    Session session = (Session)itemId;				
	    	    String color = session.getLevel()==LevelType.Junior ? "#2dd085" : "#f54993";
	    	    String iconTag = "<span class=\"v-icon\" style=\"font-family: "
	    	                    + FontAwesome.CIRCLE.getFontFamily() + ";color:" + color
	    	                    + "\">&#x"
	    	                    + Integer.toHexString(FontAwesome.CIRCLE.getCodepoint())
	    	                    + ";</span>";
	    				
	    	    String html = iconTag + " "+  session.getLevel().name();
	    				
	    	    Label label = new Label(html, ContentMode.HTML);
	            label.setSizeUndefined();
	            return label;
	    	}
	    });
	    
	    // visibleColumns : title, level, speaker		
	    table.setVisibleColumns("title", "level", "speaker");
	    
	    // ColumnHeader : Session, Level, Speaker
	    table.setColumnHeaders("Session", "Level", "Speaker");
	    
	    // ColumnWidth : title의 폭의 비중을 가장 크게 설정
	    table.setColumnExpandRatio("title", 1);
	    
	    return table;

	}
	
	private TabSheet createQuestionTab() {
		// ③-① questionTabSheet = tabLayout
	    TabSheet questionTabSheet = new TabSheet();
	    questionTabSheet.setSizeFull();	    
	    
	    // ④ tabLayout = ④-① questionTable + ④-② sendLayout(textBox + button)
	    VerticalLayout tabLayout = new VerticalLayout();
	    tabLayout.setSizeFull();
	    tabLayout.setSpacing(true);
	    
	    // ④-① questionTable
	    tabLayout.addComponent(questionTable = createQuestionTable());
	   // ④-② sendLayout(textBox + button)
	    tabLayout.addComponent(createSendLayout());	    
	    tabLayout.setExpandRatio(questionTable, 1);
	    
	    // 첫번째 탭 = 질문테이블 + 질문 입력 텍스트박스 + 질문 추가 버튼
	    questionTabSheet.addTab(tabLayout, "Question", FontAwesome.QUESTION_CIRCLE);	    
	    return questionTabSheet;
	}

	private Table createQuestionTable() {		
		// ④-① 질문리스트 테이블 구성
		Table table = new Table();
		table.setSizeFull();
		table.addStyleName(ValoTheme.TABLE_NO_HEADER);
		table.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
		table.addStyleName(ValoTheme.TABLE_NO_VERTICAL_LINES);
		table.addStyleName(ValoTheme.TABLE_SMALL);
		
		table.setContainerDataSource(new BeanItemContainer<>(Question.class));
		
		// 추가요구사항1 : user 컬럼은 generatedColumn으로 프로필 사진 + 이름 + 메시지생성시간(hh:mm:ss)을 하나로 재구성 처리
		table.addGeneratedColumn("user", new ColumnGenerator() {
			@Override
			public Component generateCell(Table source, Object itemId, Object columnId) {
				Question message = (Question)itemId;
				User user = userData.findOne(message.getCreatedBy());
		        String iconTag = "<img class=\"v-image v-widget v-round-image\" src=\"" + ImageUploader.getUrl(user.getImgPath()) + "\" alt=\"\">";
		        
		        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
                String time =  dateFormat.format(message.getCreatedDate());
		        
                StringBuffer htmls = new StringBuffer();
                htmls.append(iconTag);
                htmls.append(" ");
                htmls.append("<div style=\"text-align:center;\"><b>"+user.getName()+"</b></div>");
                htmls.append("<div style=\"text-align:center;font-size:small\">"+time+"</div>");
                
				Label label = new Label(htmls.toString(), ContentMode.HTML);
                label.setSizeUndefined();
                return label;
			}
		});
		
		// 추가요구사항2 : message 컬럼 또한 TextArea Component로 표현
		table.addGeneratedColumn("message", new ColumnGenerator() {
			@Override
			public TextArea generateCell(Table source, Object itemId, Object columnId) {
				Question message = (Question)itemId;
				TextArea textArea = new TextArea();
				textArea.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				textArea.setHeight(70, Unit.PIXELS);
				textArea.setWidth(100, Unit.PERCENTAGE);
				textArea.setValue(message.getMessage());
                return textArea;
			}
		});
		
		// visibleColumns : user, message
		table.setVisibleColumns("user", "message");
		// ColumnHeader : Session, Level, Speaker
		table.setColumnHeaders("User", "Message");
		// message가 폭(ColumnWidth)의 비중을 가장 크게 설정 
		table.setColumnExpandRatio("message", 1);
		return table;
	}
	
	private Component createSendLayout() {
		// 질문 입력 텍스트박스
		textArea = new TextArea();		
		textArea.setWidth(100, Unit.PERCENTAGE);
		textArea.setHeight(50, Unit.PIXELS);
		// ShortcutListener를 통해 키보드 shift + enter 이벤트 발생 시 메시지 전송
		textArea.addShortcutListener(new ShortcutListener("Send", null, KeyCode.ENTER, ModifierKey.SHIFT) {
	        @Override
	        public void handleAction(Object sender, Object target) {
	            sendMessage();// 질문 메시지 전송
	        }
		}); 
		
		// 질문 추가 버튼
		button = new Button();
		button.setIcon(FontAwesome.SEND);
		button.setWidth(50, Unit.PIXELS);
		button.setHeight(50, Unit.PIXELS);
		button.setStyleName(ValoTheme.BUTTON_LARGE);
		button.setStyleName(ValoTheme.BUTTON_PRIMARY);
		button.setStyleName(ValoTheme.BUTTON_ICON_ONLY);
		button.setClickShortcut(KeyCode.ENTER);
		// 전송 버튼 클릭시 이벤트 발생
		button.addClickListener(new ClickListener() {			
			@Override
			public void buttonClick(ClickEvent event) {
			   sendMessage();// 질문 메시지 전송
			}			
		}); 
		
		HorizontalLayout sendLayout = new HorizontalLayout();
		sendLayout.setWidth(100, Unit.PERCENTAGE);
		sendLayout.setSpacing(true);
		sendLayout.addComponent(textArea);
		sendLayout.addComponent(button);
		sendLayout.setExpandRatio(textArea, 1);		
		return sendLayout;
	}

	private void sendMessage() {
		// 질문 입력 값 가져오기
		String textValue =  textArea.getValue();
		if(textValue.isEmpty()) return;
		// 현재 선택된 item(Session) 정보 가져오기
		Session session = (Session)sessionTable.getNullSelectionItemId(); 	
		// 질문 메시지 저장하기
		Question question = questionData.save(new Question(session.getId(), textValue, UserSession.getUser().getId()));
		// 테이블에 데이터 추가 하기
		questionTable.getContainerDataSource().addItem(question);
		// 입력필드 초기화
		textArea.setValue("");
		// 테이블스크롤이동
		scrollEnd();
		// 신규 메시지 보내기
		eventBus.send(question);
	}
	
	private void findSessionBean() {
		List<Session> sessions = sessionData.findAll();		
		if(sessions.size()<=0) return;
		sessionTable.getContainerDataSource().removeAllItems();
		for(Session session: sessions) {
			sessionTable.getContainerDataSource().addItem(session);
		}
		clickSession(sessions.get(0)); // 세션 데이터 조회시 0번째 item 선택 처리
	}

	private void clickSession(Session session) {
		// 현재 선택된 item(Session) 정보 담아두기
		sessionTable.setNullSelectionItemId(session); 		 
		Set<Long> questionIds = session.getQuestions();
		if(questionIds.size()<=0) return;		
		findByQuestionIds(session.getQuestions());
		button.focus(); 	
	}
	
	private void findByQuestionIds(Set<Long> ids) {		
		if(ids.size()<=0) return;
		List<Question> questions = questionData.findByIds(ids);
		questionTable.getContainerDataSource().removeAllItems();
		for(Question itemId: questions) {
			questionTable.getContainerDataSource().addItem(itemId);
		}
		// 처음 질문 리스트를 가져 왔을 때도 스크롤 맨 밑으로 이동 처리 추가
		scrollEnd();
	}
	
	private void scrollEnd() {
		int itemSize = questionTable.size();		
		if(itemSize<=0) return;		
		if(itemSize<questionTable.getPageLength()) {
			questionTable.setPageLength(itemSize);
		}
		// 스크롤을 밑으로 내려 주기
		questionTable.setCurrentPageFirstItemId(questionTable.lastItemId());
		// 아이템 선택 상태로 변경
		questionTable.select(questionTable.lastItemId());
	}
	
	@Override	
    public void attach() {
    	eventBus.register(this); // 수신 등록
        super.attach();
    }    
	
	@Override
	public void detach() {
	   	eventBus.unregister(this); // 수신 해제
	    super.detach();
	}
	
	@Override
	public void receive(Question question) {
		// 신규 메시지 수신하기
		Session selectedSession = (Session)sessionTable.getNullSelectionItemId();
		// 신규 메시지가 현재 선택된 세션과 동일한지
		if(selectedSession.getId()!=question.getSessionId()) return;
		// 신규 메시지가 중복된 메시지 인지.
		if(questionTable.getContainerDataSource().containsId(question)) return;
		// 신규 메시지 질문 테이블에 추가 하기
		questionTable.getContainerDataSource().addItem(question);
		// 신규 메시지 도착으로 상태가 변경
		hasNewItem.set(true);
	}	
	
	@Override
	public void enter(ViewChangeEvent event) {
	}
	
	class RefreshThread extends Thread {
		@Override
		public void run() {			
			while (true) {
				try {
					Thread.sleep(1000);					
					if(hasNewItem.get()) {
						// 신규 메시지가 도착 상태면 서버 푸쉬 진행
						UI.getCurrent().access(new Runnable(){
							@Override
							public void run() {
								scrollEnd();
							}
						});
						hasNewItem.set(false);
					}			
				} catch (InterruptedException e) {
					hasNewItem.set(false);
				}
			}
		}
	}
		
}
