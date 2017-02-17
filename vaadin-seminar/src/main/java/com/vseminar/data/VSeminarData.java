package com.vseminar.data;

import java.util.List;

public interface VSeminarData<T> {
	
	T findOne(long id);
	
	List<T> findAll();
	
	int count();
		
	T save(T entity);
	
	void delete(long id);

}
