package com.project.journalProject.repository;

import com.project.journalProject.entity.ConfigJournalAppEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfigJournalAppRepository extends MongoRepository<ConfigJournalAppEntry, ObjectId> {

}
