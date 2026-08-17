package com.project.journalProject.repository;

import com.project.journalProject.entity.UserEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserEntryRepository extends MongoRepository<UserEntry, ObjectId> {

    Optional<UserEntry> findByUserName(String userName);

    void deleteByUserName(String userName);
}
