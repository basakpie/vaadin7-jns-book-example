package com.vseminar.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.vaadin.spring.annotation.EnableVaadin;
import com.vseminar.data.LoadingDataGenerator;

@Configuration
@EnableVaadin
public class VSeminarConfiguration {
	
	@Autowired
	LoadingDataGenerator loadingDataGenerator;
	
	@PostConstruct
	public void init() {
		loadingDataGenerator.create();    	
	}
		
}

