package com.project.journalProject.controller;

import com.project.journalProject.entity.UserEntry;
import com.project.journalProject.service.UserEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserEntryController {

    private final UserEntryService userEntryService;

    @Autowired
    public UserEntryController(UserEntryService userEntryService) {
        this.userEntryService = userEntryService;
    }

    @PostMapping
    public ResponseEntity<UserEntry> createEntry(@RequestBody UserEntry myEntry) {
        return ResponseEntity.ok(userEntryService.saveEntry(myEntry));
    }

    @GetMapping
    public ResponseEntity<List<UserEntry>> getAll() {
        return ResponseEntity.ok(userEntryService.getAll());
    }

    @GetMapping("{userName}")
    public ResponseEntity<UserEntry> findEntryByUserName(@PathVariable String userName) {
        return userEntryService.getEntryByUserName(userName)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{userName}")
    public ResponseEntity<UserEntry> deleteByUserName(@PathVariable String userName) {
        return userEntryService.deleteByUserName(userName)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("{userName}")
    public ResponseEntity<UserEntry> updateByUserName(@PathVariable String userName, @RequestBody UserEntry entry) {

        return userEntryService.updateEntryByUserName(userName, entry)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}