package com.vseminar.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.vseminar.data.model.User;

@org.springframework.stereotype.Repository
public class UserData implements VSeminarData<User> {

	private Map<Long, User> users = new LinkedHashMap<>();	
	private AtomicLong nextId = new AtomicLong(0);
	
	@Override
	public User findOne(long id) {
		User user = users.get(id);
		if(user!=null) return user;
        return new User();
	}

	@Override
	public List<User> findAll() {
		return Collections.unmodifiableList(new ArrayList<>(users.values()));
	}
	
	@Override
	public int count() {		
		return users.size();
	}

	@Override
	public User save(User user) {
		User checkUser;		
		if (user.getId()==null) {
			checkUser = findByNameOrEmail(user.getName(), user.getEmail());
			if(checkUser.getId()!=null) {
				throw new IllegalArgumentException("Duplicated user name or email");
			}
			user.setId(nextId.incrementAndGet());
			users.put(user.getId(), user);
            return user;
        }
		checkUser = findByName(user.getName());		
		if(users.containsKey(user.getId())) {
			if(user.getId()!=checkUser.getId() && user.getName().equals(checkUser.getName())) {
        		throw new IllegalArgumentException("Duplicated user name");
        	}
        	users.put(user.getId(), user);
            return user;
		}
        throw new IllegalArgumentException("No user with id " + user.getId() + " found");
	}
		
	@Override
	public void delete(long id) {
		User user = findOne(id);
        if (user == null) {
            throw new IllegalArgumentException("User with id " + id + " not found");
        }
        users.remove(user.getId());
	}
	
	public User findByName(String name) {
		List<User> users = findAll();
    	for(User user: users) {
    		if(user.getName().equals(name)) {
    			return user;
    		}
    	}
    	return new User();
	}
	
	public User findByNameOrEmail(String name, String email) {
		List<User> users = findAll();
    	for(User user: users) {
    		if(user.getName().equals(name) || user.getEmail().equals(email)) {
    			return user;
    		}
    	}
    	return new User();
	}
	
	public User findByEmailAndPassword(String email, String password) {
		List<User> users = findAll();
    	for(User user: users) {
    		if(user.getEmail().equals(email) && user.getPassword().equals(password)) {
    			return user;
    		}
    	}
    	return new User();
	}

}
