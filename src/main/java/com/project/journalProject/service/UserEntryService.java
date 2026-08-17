package com.project.journalProject.service;

import com.project.journalProject.entity.UserEntry;
import com.project.journalProject.repository.UserEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserEntryService {

    private final UserEntryRepository userEntryRepository;

    @Autowired
    public UserEntryService(UserEntryRepository userEntryRepository) {
        this.userEntryRepository = userEntryRepository;
    }

    public UserEntry saveEntry(UserEntry userEntry) {
        return userEntryRepository.save(userEntry);
    }

    public List<UserEntry> getAll() {
        return userEntryRepository.findAll();
    }

    public Optional<UserEntry> getEntryByUserName(String userName) {
        return userEntryRepository.findByUserName(userName);
    }

    public Optional<UserEntry> deleteByUserName(String userName) {
        Optional<UserEntry> entry = userEntryRepository.findByUserName(userName);

        if (entry.isPresent()) {
            userEntryRepository.deleteByUserName(userName);
            return entry;
        }

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
                oldEntry.setPassword(newEntry.getPassword());
            }

            return Optional.of(userEntryRepository.save(oldEntry));
        }

        return Optional.empty();
    }
}