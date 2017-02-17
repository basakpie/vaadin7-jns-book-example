package com.vseminar.data.model;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Session {
	
	private static final int MAX_ENTRIES = 100; // 최신질문최대사이즈
	
	private Long id;             	        
	private String title;        // 세션명
	private LevelType level;     // 난이도
	private String embeddedUrl;  // 세션발표자료
	private Date startDate;      // 시작시간
	private Date endDate;        // 종료시간		
	private Long ownerId;        // 개설자
	private String speaker;      // 스피커명
	private String description;  // 세션설명
	private Set<Long> questions; // 질문
	
	public Session() {
		this.level = LevelType.Junior;
		this.startDate = new Date();
		this.endDate = new Date();
		newMessages();
	}
	
	public Session(Long ownerId) {
		this.ownerId = ownerId;
		this.level = LevelType.Junior;
		this.startDate = new Date();
		this.endDate = new Date();
		newMessages();
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
		newMessages();
	}
	
	@SuppressWarnings("serial")
	private void newMessages() {
		// 메인화면에서의 질문 리스트는 전부 보여질 필요가 없으므로 MAX_ENTRIES 정도만 보관
		this.questions = Collections.newSetFromMap(new LinkedHashMap<Long, Boolean>(MAX_ENTRIES + 1, .90F, false){
		    protected boolean removeEldestEntry(Map.Entry<Long, Boolean> eldest) {
		        return size() > MAX_ENTRIES;
		    }
		});
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

	public Set<Long> getQuestions() {
		return questions;
	}

	public void setQuestions(Set<Long> questions) {
		this.questions = questions;
	}

	@Override
	public String toString() {
		return "Session [id=" + id + ", title=" + title + ", level=" + level + ", embeddedUrl=" + embeddedUrl
				+ ", startDate=" + startDate + ", endDate=" + endDate + ", ownerId=" + ownerId + ", speaker=" + speaker
				+ ", description=" + description + ", questions=" + questions + "]";
	}
	
}
