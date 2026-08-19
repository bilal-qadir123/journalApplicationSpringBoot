package com.project.journalProject.controller;

import com.project.journalProject.entity.UserEntry;
import com.project.journalProject.service.UserEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("public")
public class PublicController {

    private final UserEntryService userEntryService;

    @Autowired
    public PublicController(UserEntryService userEntryService) {
        this.userEntryService = userEntryService;
    }

    @PostMapping
    public ResponseEntity<UserEntry> createEntry(@RequestBody UserEntry myEntry) {
        return ResponseEntity.ok(userEntryService.createEntry(myEntry));
    }
}
