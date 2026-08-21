package com.project.journalProject.controller;

import com.project.journalProject.cache.AppCache;
import com.project.journalProject.entity.EmailEntry;
import com.project.journalProject.entity.UserEntry;
import com.project.journalProject.repository.UserEntryRepository;
import com.project.journalProject.repository.UserRepositoryImplementation;
import com.project.journalProject.service.EmailService;
import com.project.journalProject.service.UserEntryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin")
public class AdminController {

    private final UserEntryService userEntryService;
    private final UserEntryRepository userEntryRepository;
    private final UserRepositoryImplementation userRepositoryImplementation;
    private final AppCache appCache;
    private final EmailService emailService;

    public AdminController(UserEntryService userEntryService, UserEntryRepository userEntryRepository, UserRepositoryImplementation userRepositoryImplementation, AppCache appCache, EmailService emailService) {
        this.userEntryService = userEntryService;
        this.userEntryRepository = userEntryRepository;
        this.userRepositoryImplementation = userRepositoryImplementation;
        this.appCache = appCache;
        this.emailService = emailService;
    }

    @GetMapping("all-users")
    public ResponseEntity<List<UserEntry>> getAllUsers() {
        return ResponseEntity.ok(userEntryRepository.findAll());
    }

    @PostMapping("add-new-admin")
    public ResponseEntity<UserEntry> createNewAdmin(@RequestBody UserEntry userEntry) {
        try {
            return ResponseEntity.ok(userEntryService.createNewAdmin(userEntry));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("user/{userName}")
    public ResponseEntity<UserEntry> findEntryByUserName(@PathVariable String userName) {
        return userEntryService.getEntryByUserName(userName)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("get-users-for-sentiment-analysis")
    public ResponseEntity<List<UserEntry>> getAllUsersForSentimentAnalysis() {
        return ResponseEntity.ok(userRepositoryImplementation.getAllUserForSentimentAnalysis());
    }

    @GetMapping("clear-app-cache")
    public ResponseEntity<Void> clearAppCache() {
        appCache.init();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("trigger-email")
    public ResponseEntity<Void> sendEmail(@RequestBody EmailEntry emailEntry) {
        emailService.sendEmail(emailEntry.getRecipientEmail(), emailEntry.getSubject(), emailEntry.getBody());
        return ResponseEntity.noContent().build();
    }
}
