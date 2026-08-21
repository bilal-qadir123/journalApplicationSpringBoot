package com.project.journalProject.service;

import com.project.journalProject.entity.UserEntry;
import com.project.journalProject.repository.JournalEntryRepository;
import com.project.journalProject.entity.JournalEntry;
import com.project.journalProject.repository.UserEntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class JournalEntryService {

    private final JournalEntryRepository journalEntryRepository;
    private final UserEntryRepository userEntryRepository;

    @Autowired
    public JournalEntryService(JournalEntryRepository journalEntryRepository, UserEntryRepository userEntryRepository) {
        this.journalEntryRepository = journalEntryRepository;
        this.userEntryRepository = userEntryRepository;
    }

    @Transactional
    public Optional<JournalEntry> createEntry(JournalEntry journalEntry, String userName) {
        try {
            Optional<UserEntry> userEntry = userEntryRepository.findByUserName(userName);

            if (userEntry.isPresent()) {
                journalEntry.setDate(LocalDateTime.now());

                JournalEntry savedEntry = journalEntryRepository.save(journalEntry);
                userEntry.get().getJournalEntryList().add(savedEntry);
                userEntryRepository.save(userEntry.get());

                return Optional.of(savedEntry);
            }
        } catch (Exception e) {
            log.error("An error occurred while saving the journal", e);
        }
        return Optional.empty();
    }

    public List<JournalEntry> getAllJournalEntriesForUser(String userName) {
        Optional<UserEntry> userEntry = userEntryRepository.findByUserName(userName);
        return userEntry.map(UserEntry::getJournalEntryList).orElse(Collections.emptyList());
    }

    public Optional<JournalEntry> getJournalEntriesByUser(String userName, ObjectId id) {
        Optional<UserEntry> entry = userEntryRepository.findByUserName(userName);

        if(entry.isPresent()) {
            JournalEntry journalEntry = entry.get().getJournalEntryList().stream().
                    filter(x -> x.getId().equals(id)).findFirst().orElse(null);
            if (journalEntry != null) {
                return Optional.of(journalEntry);
            }
        }
        log.info("Journal not found for user {}", userName);
        return Optional.empty();
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
            log.info("Journal not found for user {}", userName);
            return Optional.empty();
        }
        log.info("User {} not found", userName);
        return Optional.empty();
    }

    public Optional<JournalEntry> updateByUserNameAndID(String userName, ObjectId id, JournalEntry newEntry) {
        Optional<UserEntry> entry = userEntryRepository.findByUserName(userName);

        if(entry.isPresent()) {
            JournalEntry journalEntry = entry.get().getJournalEntryList().stream().
                    filter(x -> x.getId().equals(id)).findFirst().orElse(null);

            if (journalEntry == null) {
                log.info("Journal {} not found for user {}", id, userName);
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
        log.info("User {} not found", userName);
        return Optional.empty();
    }
}
