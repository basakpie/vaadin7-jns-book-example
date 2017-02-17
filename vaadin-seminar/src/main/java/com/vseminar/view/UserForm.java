package com.vseminar.view;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.ProgressListener;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vseminar.data.UserData;
import com.vseminar.data.UserSession;
import com.vseminar.data.model.RoleType;
import com.vseminar.data.model.User;
import com.vseminar.image.ImageUploader;

@SuppressWarnings("serial")
public class UserForm extends AbstractForm<User> {
	
	TextField name;
	TextField email;
	PasswordField password;
	ComboBox role;
	
	BeanFieldGroup<User> fieldGroup;

	Button save;
	Button delete;
	
	UserData userData;
	
	Image image;
	
	public UserForm(){
		userData = UserData.getInstance();
        fieldGroup = new BeanFieldGroup<User>(User.class);    
		
		VerticalLayout root = new VerticalLayout();		
		root.addComponent(createContent()); // 폼필드
		root.addComponent(createFooter());  // 저장, 삭제버튼
        setCompositionRoot(root); // CustomComponent 상속시 반드시 setCompositionRoot 최종 객체를 채워야 함.		
	}
	
	private Component createContent() {
		HorizontalLayout content = new HorizontalLayout();
		content.setSpacing(true);
		content.setMargin(new MarginInfo(true, true, false, true));

        FormLayout form = new FormLayout();
        form.setSizeUndefined();
        form.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);        
        form.addComponent(name     = new TextField("Name"));         // 이름
        form.addComponent(email    = new TextField("Email"));        // 이메일
        form.addComponent(password = new PasswordField("Password")); // 비밀번호
        form.addComponent(role     = new ComboBox("Role"));          // 권한(Admin, User)
		
        for(RoleType type : RoleType.values()) {
			role.addItem(type); // Admin, User 두개의 Role을 combox에 담는다.
		}
        
        content.addComponent(createUpload()); // 프로필 사진 업로드
        content.addComponent(form); // 폼 필드(s)를 레이아웃에 다시 담기
		return content;		
	}
	
	private Component createUpload() {
		image = new Image(); // 프로필 사진 보여주기
        image.setWidth(100, Unit.PIXELS);
        image.setSource(new ThemeResource("img/profile-pic-300px.jpg")); // 기본 이미지
        
		final Upload upload = new Upload();	// 파일 업로드 버튼
		upload.setButtonCaption("Change…");        
		upload.addStyleName(ValoTheme.BUTTON_TINY);
		upload.setImmediate(true); 
		
		final ImageUploader imageUploader = new ImageUploader();		
		upload.setReceiver(imageUploader);
		// 파일바이트출력스트림이 쓰여질때 마다 호출 되어짐.
		upload.addProgressListener(new ProgressListener() {
			@Override
			public void updateProgress(long readBytes, long contentLength) {
				int maxLength = 1024 * 500;
				// 파일 사이즈가 초과 하면 에러 처리
				if (readBytes > maxLength) {
					// 업로드 쓰레드에 인터럽트 보내기
					upload.interruptUpload();
					new Notification("Could not upload file", "file max size : 500kb ", Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());
				}
			}
        });
		
		upload.addSucceededListener(new SucceededListener() {
			@Override
			public void uploadSucceeded(SucceededEvent event) {
				if(imageUploader.getSuccessUploadFile()==null) return;
				// 이미지 업로드가 성공하면 프로필 화면 사진 변경
				image.setSource(new ThemeResource(imageUploader.getImgPath()));
				// User.setImgPath 업데이트
				fieldGroup.getItemDataSource().getBean().setImgPath(imageUploader.getImgPath());
			}
		});
        
        VerticalLayout imageLayout  = new VerticalLayout();
        imageLayout .setSpacing(true);
        imageLayout .setSizeUndefined();        
        imageLayout .addComponent(image); // 프로필 사진 영역
        imageLayout .addComponent(upload); // 파일 업로드 버튼
        return imageLayout ;
	}
	
	private Component createFooter() {
		HorizontalLayout footer = new HorizontalLayout(); // 버튼들을 담은 레이아웃
        footer.setSizeUndefined();
        footer.setSpacing(true);
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR); // 배경색을 valo 테마이용
        footer.setWidth(100.0f, Unit.PERCENTAGE);
        
        save = new Button("Save");                   // 저장 버튼
        save.addStyleName(ValoTheme.BUTTON_PRIMARY); // 파란색상
        save.addClickListener(new ClickListener() {  // 클릭이벤트
            @Override
            public void buttonClick(ClickEvent event) {
            	// Save Button 클릭시 save(Button.ClickEvent e) 메서드 호출
            	save(event);
            }
        });
        
        delete = new Button("Delete");                // 삭제 버튼
        delete.addStyleName(ValoTheme.BUTTON_DANGER); // 붉은색상
        delete.addClickListener(new ClickListener() { // 클릭이벤트
            @Override
            public void buttonClick(ClickEvent event) {
            	// Delete Button 클릭시 save(Button.ClickEvent e) 메서드 호출
            	delete(event);
            }
        });       
        
        footer.addComponents(save, delete); // 저장, 삭제 버튼 레이아웃에 추가
        footer.setExpandRatio(save, 1);
        footer.setComponentAlignment(save, Alignment.MIDDLE_RIGHT); // 버튼(s) 오른쪽 정렬
        return footer;
    }
	
	@Override
	protected void save(ClickEvent e) {
		try {
			fieldGroup.commit(); // 변경된 필드의 item의 property(s)의 value를 변경
			User item = fieldGroup.getItemDataSource().getBean(); // 변경된 item을 가져오기
			User entity = userData.save(item); // 변경된 item을 저장하기
			if(UserSession.getUser().getId()==entity.getId()) {  
				UserSession.setUser(entity); // 로그인한 사용자의 정보면 session도 갱신 처리
			}
			getSaveHandler().onSave(entity); // saveHandler 호출
		} catch (CommitException | IllegalArgumentException ex) {
			// 만약 validation 및 item 저장시 오류가 나면 사용자에게 알려주기
			Notification.show("Error while updating profile", ex.getMessage(), Type.ERROR_MESSAGE);
		}
	}

	@Override
	protected void delete(ClickEvent e) {
		try {
			// id만 있으면 됨으로 commit을 하지 않고 item 가져오기
			User item = fieldGroup.getItemDataSource().getBean();
			userData.delete(item.getId()); // 현재 user 삭제 처리
	        getDeleteHandler().onDelete(item); // deleteHandler 호출
		} catch (Exception ex) {
			Notification.show("Error while deleting profile", ex.getMessage(), Type.ERROR_MESSAGE);
		}		
	}
	
	public void lazyInit(User user) {
		User item = new User(user);
		// item 정보를 fieldGroup에 bind 처리
		//fieldGroup.bind(name, "name");
		//fieldGroup.bind(email, "email");
		//fieldGroup.bind(password, "password");
		//fieldGroup.bind(role, "role");
		fieldGroup.bindMemberFields(this); // 개별적 bind 처리를 한번에 적용
		fieldGroup.setItemDataSource(new BeanItem<User>(item)); // fieldGroup에 데이터 그룹화
		
		// 만약 신규 유저로 값이 없으면 null 대신 ""로 표시
		name.setNullRepresentation("");
        email.setNullRepresentation("");
        password.setNullRepresentation("");
        role.setNullSelectionItemId(RoleType.User);
        
        // fieldGroup.commit()시 validation 처리
		name.addValidator(new NullValidator("required name", false));
        email.addValidator(new EmailValidator("Invalid e-mail address {0}"));
        password.addValidator(new NullValidator("required password", false));
        
        // 수정금지처리
        email.setEnabled(item.getId()==null);
        role.setEnabled(UserSession.getUser().getRole()==RoleType.Admin);
        
        // 프로필이미지
        image.setSource(new ThemeResource(item.getImgPath()));
        
        // 로그인한 사용자 또는 신규 유저 생성시에는 삭제 버튼 감추기
        delete.setVisible(item.getId()!=UserSession.getUser().getId() && item.getId()!=null);
	}	
}
