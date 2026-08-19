package com.project.journalProject.controller;

import com.project.journalProject.entity.UserEntry;
import com.project.journalProject.service.UserEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserEntryController {

    private final UserEntryService userEntryService;

    @Autowired
    public UserEntryController(UserEntryService userEntryService) {
        this.userEntryService = userEntryService;
    }

    /* Kept for future use by admin */
    @GetMapping("{userName}")
    public ResponseEntity<UserEntry> findEntryByUserName(@PathVariable String userName) {
        return userEntryService.getEntryByUserName(userName)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping()
    public ResponseEntity<UserEntry> deleteByUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        return userEntryService.deleteByUserName(userName)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping
    public ResponseEntity<UserEntry> updateByUserName(@RequestBody UserEntry entry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        return userEntryService.updateEntryByUserName(userName, entry)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}