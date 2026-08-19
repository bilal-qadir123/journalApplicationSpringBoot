package com.project.journalProject.controller;

import com.project.journalProject.entity.UserEntry;
import com.project.journalProject.repository.UserEntryRepository;
import com.project.journalProject.service.UserEntryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin")
public class AdminController {

    private final UserEntryService userEntryService;
    private final UserEntryRepository userEntryRepository;

    public AdminController(UserEntryService userEntryService, UserEntryRepository userEntryRepository) {
        this.userEntryService = userEntryService;
        this.userEntryRepository = userEntryRepository;
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
}
