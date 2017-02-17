package com.vseminar.image;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.regex.Pattern;

import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload.Receiver;

@SuppressWarnings("serial")
public class ImageUploader implements Receiver {

	final static Pattern pattern = Pattern.compile("([^\\s]+(\\.(?i)(jpg|jpeg|gif|png))$)");
	
	private File file;
	
	private String imgPath;
	
	public File getSuccessUploadFile() {
		// 파일바이트출력스트림을 통해 업로드된 파일
		return this.file;
	}
	
	public String getImgPath() {
		// 업로드 이미지 파일 주소
		return this.imgPath;
	}
		
	/**
	 * 파일 업로드시 호출 된다.
	 */
	@Override
	public OutputStream receiveUpload(String sourceFileName, String mimeType) {
		
        FileOutputStream fos = null;        
        try {
        	// 업로드 허용 이미지 
        	boolean isAllowExt = pattern.matcher(sourceFileName).matches();
        	if(!isAllowExt) {
        		throw new IOException("allow extenssion *.jpg|jpeg|gif|png"); 			
    		}
        	
        	// 업로드 위치
        	imgPath = "img/upload/" + sourceFileName;
        	
        	// 디렉토리 및 파일 생성
            file = getFile(imgPath);
            if(file.exists()) {
            	file.delete();
            }
            file.getParentFile().mkdirs();
        	file.createNewFile();
        	
        	// 파일바이트출력스트림
            fos = new FileOutputStream(file);
        } catch(IOException e) {        	
        	new Notification("Could not open file", e.getMessage(), Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());        	
        	return null;
        } 
        return fos;
	}
	
	public static File getFile(String imgPath) {
		String baseDirectory = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
		return new File(baseDirectory + "/VAADIN/themes/" + UI.getCurrent().getTheme() + "/" + imgPath); 
	}
	
	public static String getUrl(String imgPath) {
		String contextPath = VaadinServlet.getCurrent().getServletContext().getContextPath();
		return contextPath + "/VAADIN/themes/" + UI.getCurrent().getTheme() + "/" + imgPath; 
	}
	
}
