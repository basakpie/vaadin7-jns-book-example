package com.vseminar.data.model;

import java.util.Date;

public class Session {
	
	private Long id;             	        
	private String title;        // 세션명
	private LevelType level;     // 난이도
	private String embeddedUrl;  // 세션발표자료
	private Date startDate;      // 시작시간
	private Date endDate;        // 종료시간		
	private Long ownerId;        // 개설자
	private String speaker;      // 스피커명
	private String description;  // 세션설명
	
	public Session() {
		this.level = LevelType.Junior;
		this.startDate = new Date();
		this.endDate = new Date();
	}
	
	public Session(Long ownerId) {
		this.ownerId = ownerId;
		this.level = LevelType.Junior;
		this.startDate = new Date();
		this.endDate = new Date();
	}
	
	public Session(String title, LevelType level, String embeddedUrl, String speaker, Long ownerId, String description) {		
		this.title = title;
		this.level = level;
		this.embeddedUrl = embeddedUrl;
		this.speaker = speaker;
		this.ownerId = ownerId;
		this.startDate = new Date();
		this.endDate = new Date();
		this.description = description;
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public LevelType getLevel() {
		return level;
	}

	public void setLevel(LevelType level) {
		this.level = level;
	}

	public String getEmbeddedUrl() {
		return embeddedUrl;
	}

	public void setEmbeddedUrl(String embeddedUrl) {
		this.embeddedUrl = embeddedUrl;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getSpeaker() {
		return speaker;
	}

	public void setSpeaker(String speaker) {
		this.speaker = speaker;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Session [id=" + id + ", title=" + title + ", level=" + level + ", embeddedUrl=" + embeddedUrl
				+ ", startDate=" + startDate + ", endDate=" + endDate + ", ownerId=" + ownerId + ", speaker=" + speaker
				+ ", description=" + description + "]";
	}
	
}
