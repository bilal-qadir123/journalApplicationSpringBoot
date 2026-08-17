package com.project.journalProject.controller;

import com.project.journalProject.entity.JournalEntry;
import com.project.journalProject.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("journal")
public class JournalEntryController {

    private final JournalEntryService journalEntryService;

    @Autowired
    public JournalEntryController(JournalEntryService journalEntryService) {
        this.journalEntryService = journalEntryService;
    }

    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry) {
        myEntry.setDate(LocalDateTime.now());
        return ResponseEntity.ok(journalEntryService.createEntry(myEntry));
    }

    @GetMapping
    public ResponseEntity<List<JournalEntry>> getAll() {
        return ResponseEntity.ok(journalEntryService.getAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<JournalEntry> findEntryByID(@PathVariable ObjectId id) {
        return journalEntryService.getEntryByID(id)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<JournalEntry> deleteByID(@PathVariable ObjectId id) {
        return journalEntryService.deleteByID(id)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    public ResponseEntity<JournalEntry> updateByID(@PathVariable ObjectId id, @RequestBody JournalEntry entry) {
        return journalEntryService.updateEntryByID(id, entry)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
