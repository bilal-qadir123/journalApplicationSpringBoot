package com.project.journalProject.scheduler;

import com.project.journalProject.entity.JournalEntry;
import com.project.journalProject.entity.UserEntry;
import com.project.journalProject.enums.Sentiment;
import com.project.journalProject.repository.UserRepositoryImplementation;
import com.project.journalProject.service.EmailService;
import com.project.journalProject.service.SentimentAnalysisService;
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

    @Autowired
    private SentimentAnalysisService sentimentAnalysisService;

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

            if (!allEntries.trim().isEmpty()) {
                Sentiment sentiment = sentimentAnalysisService.getSentiment(allEntries);
                String subject = "Your Weekly Journal Sentiment Analysis";
                String body = "Based on your recent entries from the past 7 days, your overall sentiment is: "
                        + sentiment.name();

                emailService.sendEmail(entries.getEmail(), subject, body);
            }
        }
    }
}
