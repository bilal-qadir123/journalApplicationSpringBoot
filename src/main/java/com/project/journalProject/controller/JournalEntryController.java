package com.project.journalProject.controller;

import com.project.journalProject.entity.JournalEntry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("journal")
public class JournalEntryController {

    private Map<Long, JournalEntry> journalEntries = new HashMap<>();

    @GetMapping
    public List<JournalEntry> getAll() {
        return new ArrayList<>(journalEntries.values());
    }

    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry) {
        if (!journalEntries.containsKey(myEntry.getId())) {
            journalEntries.put(myEntry.getId(), myEntry);
            return ResponseEntity.ok(myEntry);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("{id}")
    public ResponseEntity<JournalEntry> getJournalEntryByID(@PathVariable Long id) {
        if (journalEntries.containsKey(id)) {
            return ResponseEntity.ok(journalEntries.get(id));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    public Map<String, Object> deleteEntry(@PathVariable Long id) {

        Map<String, Object> response = new HashMap<>();
        JournalEntry removed = journalEntries.remove(id);

        if (removed == null) {
            response.put("message", "ID does not exist");
            response.put("id", id);
            return response;
        }

        response.put("success", true);
        response.put("removed", id);
        response.put("entries", new ArrayList<>(journalEntries.values()));

        return response;
    }

    @PutMapping("{id}")
    public ResponseEntity<JournalEntry> updateEntry (@PathVariable Long id, @RequestBody JournalEntry myEntry) {
        if (!Objects.equals(myEntry.getId(), id)) {
            return ResponseEntity.badRequest().build();
        }

        if (journalEntries.containsKey(id)) {
            journalEntries.put(id, myEntry);
            return ResponseEntity.ok(myEntry);
        }
        return ResponseEntity.notFound().build();
    }
}
