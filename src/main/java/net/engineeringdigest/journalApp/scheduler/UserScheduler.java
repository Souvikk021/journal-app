package net.engineeringdigest.journalApp.scheduler;

import net.engineeringdigest.journalApp.cache.AppCache;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.enums.Sentiment;
import net.engineeringdigest.journalApp.repository.UserRepositoryImpl;
import net.engineeringdigest.journalApp.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserScheduler {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private AppCache appCache;

    // Run every Sunday at 9:00 AM
    @Scheduled(cron = "0 0 9 * * SUN")
    public void fetchUserAndSendSaMail() {

        List<User> users = userRepository.getUserForSA();

        for (User user : users) {

            List<JournalEntry> journalEntries =
                    user.getJournalEntries();

            if (journalEntries == null || journalEntries.isEmpty()) {
                continue;
            }

            LocalDateTime sevenDaysAgo =
                    LocalDateTime.now().minusDays(7);

            List<Sentiment> sentiments = journalEntries.stream()
                    .filter(entry -> entry.getDate() != null
                            && entry.getDate().isAfter(sevenDaysAgo))
                    .map(JournalEntry::getSentiment)
                    .collect(Collectors.toList());

            Map<Sentiment, Integer> sentimentCounts =
                    new HashMap<>();

            for (Sentiment sentiment : sentiments) {
                if (sentiment != null) {
                    sentimentCounts.put(
                            sentiment,
                            sentimentCounts.getOrDefault(sentiment, 0) + 1
                    );
                }
            }

            Sentiment mostFrequentSentiment = null;
            int maxCount = 0;

            for (Map.Entry<Sentiment, Integer> entry
                    : sentimentCounts.entrySet()) {

                if (entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    mostFrequentSentiment = entry.getKey();
                }
            }

            if (mostFrequentSentiment != null) {
                emailService.sendEmail(
                        user.getEmail(),
                        "Sentiment Report - Last 7 Days",
                        mostFrequentSentiment.toString()
                );
            }
        }
    }

    // Refresh the application cache every 10 minutes
    @Scheduled(cron = "0 0/10 * ? * *")
    public void clearAppCache() {
        appCache.init();
    }
}
