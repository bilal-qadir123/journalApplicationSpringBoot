package com.project.journalProject.service;

import com.project.journalProject.entity.UserEntry;
import com.project.journalProject.repository.JournalEntryRepository;
import com.project.journalProject.entity.JournalEntry;
import com.project.journalProject.repository.UserEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {

    private final JournalEntryRepository journalEntryRepository;
    private final UserEntryRepository userEntryRepository;

    @Autowired
    public JournalEntryService(JournalEntryRepository journalEntryRepository, UserEntryRepository userEntryRepository) {
        this.journalEntryRepository = journalEntryRepository;
        this.userEntryRepository = userEntryRepository;
    }

    @Transactional
    public JournalEntry createEntry(JournalEntry journalEntry, String userName) {
        try {
            Optional<UserEntry> userEntry = userEntryRepository.findByUserName(userName);

            if (userEntry.isPresent()) {
                journalEntry.setDate(LocalDateTime.now());

                JournalEntry savedEntry = journalEntryRepository.save(journalEntry);
                userEntry.get().getJournalEntryList().add(savedEntry);
                userEntryRepository.save(userEntry.get());

                return savedEntry;
            }
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while saving the entry", e);
        }
        return null;
    }

    public List<JournalEntry> getAllJournalEntriesForUser(String userName) {
        Optional<UserEntry> userEntry = userEntryRepository.findByUserName(userName);
        return userEntry.map(UserEntry::getJournalEntryList).orElse(null);
    }

    public Optional<JournalEntry> deleteByUserNameAndID(String userName, ObjectId id) {
        Optional<UserEntry> entry = userEntryRepository.findByUserName(userName);

        if (entry.isPresent()) {
            JournalEntry journalEntry = entry.get().getJournalEntryList().stream()
                    .filter(x -> x.getId().equals(id)).findFirst().orElse(null);

            if (journalEntry != null) {
                entry.get().getJournalEntryList().remove(journalEntry);
                userEntryRepository.save(entry.get());
                journalEntryRepository.deleteById(id);
                return Optional.of(journalEntry);
            }
        }
        return Optional.empty();
    }

    public Optional<JournalEntry> updateByNameAndID(String userName, ObjectId id, JournalEntry newEntry) {
        Optional<UserEntry> entry = userEntryRepository.findByUserName(userName);

        if(entry.isPresent()) {
            JournalEntry journalEntry = entry.get().getJournalEntryList().stream().
                    filter(x -> x.getId().equals(id)).findFirst().orElse(null);

            if (journalEntry == null) {
                return Optional.empty();
            }
            if (!newEntry.getTitle().isBlank()) {
                journalEntry.setTitle(newEntry.getTitle());
            }
            if (newEntry.getContent() != null && !newEntry.getContent().isBlank()) {
                journalEntry.setContent(newEntry.getContent());
            }
            if (newEntry.getDate() != null) {
                journalEntry.setDate(newEntry.getDate());
            }

            return Optional.of(journalEntryRepository.save(journalEntry));
        }
        return Optional.empty();
    }
}
