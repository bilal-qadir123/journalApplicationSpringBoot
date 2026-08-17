package com.project.journalProject.controller;

import com.project.journalProject.entity.JournalEntry;
import com.project.journalProject.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("{userName}")
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry, @PathVariable String userName) {
        return ResponseEntity.ok(journalEntryService.createEntry(myEntry, userName));
    }

    @GetMapping("{userName}")
    public ResponseEntity<List<JournalEntry>> getAllJournalEntriesForUser(@PathVariable String userName) {
        return ResponseEntity.ok(journalEntryService.getAllJournalEntriesForUser(userName));
    }

    @DeleteMapping("{userName}/{id}")
    public ResponseEntity<JournalEntry> deleteByUserNameAndID(@PathVariable String userName, @PathVariable ObjectId id) {
        return journalEntryService.deleteByUserNameAndID(userName, id)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("{userName}/{id}")
    public ResponseEntity<JournalEntry> updateByNameAndID(@PathVariable String userName, @PathVariable ObjectId id, @RequestBody JournalEntry entry) {
        return journalEntryService.updateByNameAndID(userName, id, entry)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
