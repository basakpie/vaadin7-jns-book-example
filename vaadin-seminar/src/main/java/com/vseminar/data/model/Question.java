package com.vseminar.data.model;

import java.util.Date;

public class Question {
	
	private Long id;
	private Long sessionId;   // 세션ID
	private String message;   // 질문메시지
	private Long createdBy;   // 생성자
	private Date createdDate; // 생성일시

	public Question() {
		this.createdDate = new Date();
	}
	
	public Question(Long sessionId, String message, Long createdBy) {
		this.sessionId = sessionId;		
		this.message = message;
		this.createdBy = createdBy;
		this.createdDate = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public String toString() {
		return "Question [id=" + id + ", sessionId=" + sessionId + ", message=" + message + ", createdBy=" + createdBy
				+ ", createdDate=" + createdDate + "]";
	}
	
}
