package com.project.journalProject.controller;

import com.project.journalProject.entity.UserEntry;
import com.project.journalProject.repository.UserEntryRepository;
import com.project.journalProject.repository.UserRepositoryImplementation;
import com.project.journalProject.service.UserEntryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin")
public class AdminController {

    private final UserEntryService userEntryService;
    private final UserEntryRepository userEntryRepository;
    private final UserRepositoryImplementation userRepositoryImplementation;

    public AdminController(UserEntryService userEntryService, UserEntryRepository userEntryRepository, UserRepositoryImplementation userRepositoryImplementation) {
        this.userEntryService = userEntryService;
        this.userEntryRepository = userEntryRepository;
        this.userRepositoryImplementation = userRepositoryImplementation;
    }

    @GetMapping("all-users")
    public ResponseEntity<List<UserEntry>> getAllUsers() {
        List<UserEntry> allUsers = userEntryRepository.findAll();
        if (!allUsers.isEmpty()) {
            return ResponseEntity.ok(allUsers);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("add-new-admin")
    public ResponseEntity<UserEntry> createNewAdmin(@RequestBody UserEntry userEntry) {
        return ResponseEntity.ok(userEntryService.createNewAdmin(userEntry));
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
}
