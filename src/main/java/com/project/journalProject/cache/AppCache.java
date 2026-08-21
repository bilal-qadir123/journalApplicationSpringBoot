package com.project.journalProject.cache;

import com.project.journalProject.entity.ConfigJournalAppEntry;
import com.project.journalProject.repository.ConfigJournalAppRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppCache {

    public enum keys {
        WEATHER_API;
    }

    private final ConfigJournalAppRepository configJournalAppRepository;

    public AppCache(ConfigJournalAppRepository configJournalAppRepository) {
        this.configJournalAppRepository = configJournalAppRepository;
    }

    private Map<String, String> appCache;

    @PostConstruct
    public void init() {
        appCache = new HashMap<>();
        List<ConfigJournalAppEntry> allValues = configJournalAppRepository.findAll();

        for (ConfigJournalAppEntry configJournalAppEntry : allValues) {
            appCache.put(
                    configJournalAppEntry.getKey(),
                    configJournalAppEntry.getValue()
            );
        }
    }

    public String get(String key) {
        return appCache.get(key);
    }
}
