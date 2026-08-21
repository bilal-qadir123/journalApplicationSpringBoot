package com.project.journalProject.service;

import com.project.journalProject.entity.UserEntry;
import com.project.journalProject.repository.JournalEntryRepository;
import com.project.journalProject.repository.UserEntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserEntryService {

    private final UserEntryRepository userEntryRepository;
    private final JournalEntryRepository journalEntryRepository;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserEntryService(UserEntryRepository userEntryRepository, JournalEntryRepository journalEntryRepository) {
        this.userEntryRepository = userEntryRepository;
        this.journalEntryRepository = journalEntryRepository;
    }

    public UserEntry createNewUser(UserEntry userEntry) {
        userEntry.setPassword(passwordEncoder.encode(userEntry.getPassword()));
        userEntry.setRoles(List.of("USER"));
        return userEntryRepository.save(userEntry);
    }

    public UserEntry createNewAdmin(UserEntry userEntry) {
        userEntry.setPassword(passwordEncoder.encode(userEntry.getPassword()));
        userEntry.setRoles(List.of("USER", "ADMIN"));
        return userEntryRepository.save(userEntry);
    }

    public Optional<UserEntry> getEntryByUserName(String userName) {
        return userEntryRepository.findByUserName(userName);
    }

    public Optional<UserEntry> deleteByUserName(String userName) {
        Optional<UserEntry> entry = userEntryRepository.findByUserName(userName);

        if (entry.isPresent()) {
            journalEntryRepository.deleteAll(entry.get().getJournalEntryList());
            userEntryRepository.delete(entry.get());
            return entry;
        }

        log.info("User {} not found", userName);
        return Optional.empty();
    }

    public Optional<UserEntry> updateEntryByUserName(String userName, UserEntry newEntry) {
        Optional<UserEntry> entry = userEntryRepository.findByUserName(userName);

        if (entry.isPresent()) {
            UserEntry oldEntry = entry.get();

            if (!newEntry.getUserName().isBlank()) {
                oldEntry.setUserName(newEntry.getUserName());
            }

            if (!newEntry.getPassword().isBlank()) {
                oldEntry.setPassword(passwordEncoder.encode(newEntry.getPassword()));
            }

            if (newEntry.getEmail() != null && !newEntry.getEmail().isBlank()) {
                oldEntry.setEmail(newEntry.getEmail());
            }

            oldEntry.setSentimentAnalysis(newEntry.isSentimentAnalysis());

            return Optional.of(userEntryRepository.save(oldEntry));
        }

        log.info("User {} not found", userName);
        return Optional.empty();
    }
}