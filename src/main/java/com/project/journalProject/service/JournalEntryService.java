package com.project.journalProject.service;

import com.project.journalProject.repository.JournalEntryRepository;
import com.project.journalProject.entity.JournalEntry;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {

    private final JournalEntryRepository journalEntryRepository;

    @Autowired
    public JournalEntryService(JournalEntryRepository journalEntryRepository) {
        this.journalEntryRepository = journalEntryRepository;
    }

    public JournalEntry createEntry(JournalEntry journalEntry) {
        return journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAll() {
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> getEntryByID(ObjectId id) {
        return journalEntryRepository.findById(id);
    }

    public Optional<JournalEntry> deleteByID(ObjectId id) {
        Optional<JournalEntry> entry = journalEntryRepository.findById(id);

        if(entry.isPresent()) {
            journalEntryRepository.deleteById(id);
            return entry;
        }
        return Optional.empty();
    }

    public Optional<JournalEntry> updateEntryByID(ObjectId id, JournalEntry newEntry) {
        Optional<JournalEntry> entry = journalEntryRepository.findById(id);

        if(entry.isPresent()) {
            JournalEntry oldEntry = entry.get();

            if (!newEntry.getTitle().isBlank()) {
                oldEntry.setTitle(newEntry.getTitle());
            }
            if (!newEntry.getContent().isBlank()) {
                oldEntry.setContent(newEntry.getContent());
            }
            if (newEntry.getDate() != null) {
                oldEntry.setDate(newEntry.getDate());
            }

            return Optional.of(journalEntryRepository.save(oldEntry));
        }
        return Optional.empty();
    }
}
