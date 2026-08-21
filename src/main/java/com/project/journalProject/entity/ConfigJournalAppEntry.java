package com.project.journalProject.entity;

import lombok.Data;
import lombok.NonNull;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "config_journal_app")
@Data
public class ConfigJournalAppEntry {

    @NonNull
    private String key;

    @NonNull
    private String value;
}
