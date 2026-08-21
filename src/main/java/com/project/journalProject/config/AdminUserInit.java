package com.project.journalProject.config;

import com.project.journalProject.entity.UserEntry;
import com.project.journalProject.repository.UserEntryRepository;
import com.project.journalProject.service.UserEntryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class AdminUserInit implements CommandLineRunner {

    private final UserEntryService userEntryService;
    private final UserEntryRepository userEntryRepository;

    public AdminUserInit(UserEntryService userEntryService, UserEntryRepository userEntryRepository) {
        this.userEntryService = userEntryService;
        this.userEntryRepository = userEntryRepository;
    }

    @Override
    public void run(String... args) {
        List<UserEntry> allUsers = userEntryRepository.findAll();
        boolean adminExists = allUsers.stream()
                .anyMatch(user -> user.getRoles() != null && user.getRoles().contains("ADMIN"));

        if (!adminExists) {
            UserEntry admin = new UserEntry("admin", "admin123");
            userEntryService.createNewAdmin(admin);
            log.info("Default admin user created — username: admin, password: admin123");
            log.warn("Please change the default admin password!");
        } else {
            log.info("Admin user already exists, skipping initialization");
        }
    }
}
