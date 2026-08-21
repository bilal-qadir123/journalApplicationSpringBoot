package com.project.journalProject.repository;

import com.project.journalProject.entity.UserEntry;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserRepositoryImplementation {

    private final MongoTemplate mongoTemplate;

    public UserRepositoryImplementation(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<UserEntry> getAllUserForSentimentAnalysis() {
        Query query = new Query();

        query.addCriteria(Criteria.where("email").exists(true));
        query.addCriteria(Criteria.where("sentimentAnalysis").is(true));

        return mongoTemplate.find(query, UserEntry.class);
    }
}
