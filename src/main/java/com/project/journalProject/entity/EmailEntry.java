package com.project.journalProject.entity;

import lombok.Data;
import lombok.NonNull;

@Data
public class EmailEntry {

    @NonNull
    private String recipientEmail;

    @NonNull
    private String subject;

    @NonNull
    private String body;

}
