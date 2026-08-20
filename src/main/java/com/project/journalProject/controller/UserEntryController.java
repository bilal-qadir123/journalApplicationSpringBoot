package com.project.journalProject.controller;

import com.project.journalProject.api.response.WeatherResponse;
import com.project.journalProject.entity.UserEntry;
import com.project.journalProject.service.UserEntryService;
import com.project.journalProject.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserEntryController {

    private final UserEntryService userEntryService;
    private final WeatherService weatherService;

    @Autowired
    public UserEntryController(UserEntryService userEntryService, WeatherService weatherService) {
        this.userEntryService = userEntryService;
        this.weatherService = weatherService;
    }

    /* Kept for future use by admin */
    @GetMapping("{userName}")
    public ResponseEntity<UserEntry> findEntryByUserName(@PathVariable String userName) {
        return userEntryService.getEntryByUserName(userName)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping()
    public ResponseEntity<UserEntry> deleteByUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        return userEntryService.deleteByUserName(userName)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping
    public ResponseEntity<UserEntry> updateByUserName(@RequestBody UserEntry entry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        return userEntryService.updateEntryByUserName(userName, entry)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<String> getGreetings() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        try {
            WeatherResponse weatherResponse = weatherService.getWeather("Karachi");
            return ResponseEntity.ok(
                    "Hi " + userName + ", weather feels like " + weatherResponse.getMain().getFeelsLike()
            );
        } catch (Exception e) {
            return ResponseEntity.ok("Hi " + userName);
        }
    }
}