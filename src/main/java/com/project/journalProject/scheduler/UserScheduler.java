package com.project.journalProject.scheduler;

import com.project.journalProject.entity.JournalEntry;
import com.project.journalProject.entity.UserEntry;
import com.project.journalProject.repository.UserRepositoryImplementation;
import com.project.journalProject.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserScheduler {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepositoryImplementation userRepositoryImplementation;

    @Scheduled(cron = "0 0 9 * * SUN")
    public void fetchUsersAndSendEmail() {
        List<UserEntry> allUsers = userRepositoryImplementation.getAllUserForSentimentAnalysis();
        for (UserEntry entries : allUsers) {
            List<JournalEntry> journalEntryList = entries.getJournalEntryList();

            LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);

            List<String> recentContents = journalEntryList.stream()
                    .filter(x -> x.getDate().isAfter(sevenDaysAgo))
                    .map(JournalEntry::getContent)
                    .collect(Collectors.toList());

            String allEntries = String.join("\n---\n", recentContents);
        }
    }
}
