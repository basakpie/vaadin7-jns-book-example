package com.vseminar.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;

import com.vseminar.data.model.Question;

@org.springframework.stereotype.Repository
public class QuestionData implements VSeminarData<Question> {

	private Map<Long, Question> questions = new LinkedHashMap<>();	
	private AtomicLong nextId = new AtomicLong(0);
		
	private SessionData sessionData;
	
	@Autowired
	public QuestionData(SessionData sessionData) {
		this.sessionData = sessionData;
	}
	
	@Override
	public Question findOne(long id) {
		Question question = questions.get(id);
		if(question!=null) return question;
        return new Question();
	}

	@Override
	public List<Question> findAll() {
		return Collections.unmodifiableList(new ArrayList<>(questions.values()));
	}
	
	@Override
	public int count() {		
		return questions.size();
	}

	@Override
	public Question save(Question question) {
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
	public void delete(long id) {
		Question question = findOne(id);
        if (question == null) {
            throw new IllegalArgumentException("Question with id " + id + " not found");
        }
        questions.remove(question);
	}
	
	public List<Question> findByIds(Set<Long> ids) {
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
