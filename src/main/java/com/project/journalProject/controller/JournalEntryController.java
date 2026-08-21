package com.project.journalProject.controller;

import com.project.journalProject.entity.JournalEntry;
import com.project.journalProject.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("journal")
public class JournalEntryController {

    private final JournalEntryService journalEntryService;

    @Autowired
    public JournalEntryController(JournalEntryService journalEntryService) {
        this.journalEntryService = journalEntryService;
    }

    @PostMapping()
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        return journalEntryService.createEntry(myEntry, userName)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.internalServerError().build());
    }

    @GetMapping()
    public ResponseEntity<List<JournalEntry>> getAllJournalEntriesForUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        return ResponseEntity.ok(journalEntryService.getAllJournalEntriesForUser(userName));
    }

    @GetMapping("{id}")
    public ResponseEntity<JournalEntry> findJournalEntriesByUser(@PathVariable ObjectId id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        return journalEntryService.getJournalEntriesByUser(userName, id)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<JournalEntry> deleteByUserNameAndID(@PathVariable ObjectId id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        return journalEntryService.deleteByUserNameAndID(userName, id)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    public ResponseEntity<JournalEntry> updateByNameAndID(@PathVariable ObjectId id, @RequestBody JournalEntry entry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        return journalEntryService.updateByUserNameAndID(userName, id, entry)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
