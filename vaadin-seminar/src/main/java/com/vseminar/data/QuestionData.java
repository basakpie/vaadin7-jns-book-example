package com.vseminar.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import com.vseminar.data.model.Question;

public class QuestionData implements VSeminarData<Question> {

	private static volatile QuestionData INSTANCE = null;
	
	private Map<Long, Question> questions;
	
	private AtomicLong nextId;
	
	private SessionData sessionData;
	
	private QuestionData() {
		sessionData = SessionData.getInstance();
		nextId = new AtomicLong();
		questions = new LinkedHashMap<>();
	}
	
	public synchronized static QuestionData getInstance() {		
		if(INSTANCE==null){
	        synchronized (QuestionData.class){
	            if(INSTANCE==null){
	            	INSTANCE = new QuestionData();
	            }
	        }
	    }
        return INSTANCE;
    }
	
	@Override
	public synchronized Question findOne(long id) {
		Question question = questions.get(id);
		if(question!=null) return question;
        return new Question();
	}

	@Override
	public synchronized List<Question> findAll() {
		return Collections.unmodifiableList(new ArrayList<>(questions.values()));
	}
	
	@Override
	public int count() {		
		return questions.size();
	}

	@Override
	public synchronized Question save(Question question) {
		if (question.getId()==null) {
			question.setId(nextId.incrementAndGet());
			questions.put(question.getId(), question);
			sessionData.addMessage(question);
            return question;
        }		
		if(questions.containsKey(question.getId())) {
			questions.put(question.getId(), question);
            return question;
		}		
        throw new IllegalArgumentException("No question with id " + question.getId() + " found");
	}
	
	@Override
	public synchronized void delete(long id) {
		Question question = findOne(id);
        if (question == null) {
            throw new IllegalArgumentException("Question with id " + id + " not found");
        }
        questions.remove(question);
	}
	
	public synchronized List<Question> findByIds(Set<Long> ids) {
		List<Question> reusts = new ArrayList<>();
		for(Long id: ids) {
			Question question = findOne(id);
			if(question.getId()!=null) {
				reusts.add(findOne(id));
			}
		}
    	return reusts;
	}
	
}
