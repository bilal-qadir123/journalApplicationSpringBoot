package com.project.journalProject.controller;

import com.project.journalProject.entity.UserEntry;
import com.project.journalProject.service.UserEntryService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class UserEntryControllerTest {

    @Mock
    private UserEntryService userEntryService;

    @InjectMocks
    private UserEntryController userEntryController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken("bilal", null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @AfterEach
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void getUser_shouldReturn200_whenUserExists() {
        UserEntry user = new UserEntry("bilal", "hashedPass");
        user.setRoles(List.of("USER"));

        when(userEntryService.getEntryByUserName("bilal")).thenReturn(Optional.of(user));

        ResponseEntity<UserEntry> response = userEntryController.findEntryByUserName("bilal");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("bilal", response.getBody().getUserName());
    }

    @Test
    public void getUser_shouldReturn404_whenUserNotFound() {
        when(userEntryService.getEntryByUserName("nonexistent")).thenReturn(Optional.empty());

        ResponseEntity<UserEntry> response = userEntryController.findEntryByUserName("nonexistent");

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    public void deleteUser_shouldDeleteAuthenticatedUser() {
        UserEntry user = new UserEntry("bilal", "hashedPass");
        user.setRoles(List.of("USER"));

        when(userEntryService.deleteByUserName("bilal")).thenReturn(Optional.of(user));

        ResponseEntity<UserEntry> response = userEntryController.deleteByUserName();

        assertEquals(200, response.getStatusCode().value());
        assertEquals("bilal", response.getBody().getUserName());
    }

    @Test
    public void deleteUser_shouldReturn404_whenUserNotFound() {
        when(userEntryService.deleteByUserName("bilal")).thenReturn(Optional.empty());

        ResponseEntity<UserEntry> response = userEntryController.deleteByUserName();

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    public void updateUser_shouldUpdateAuthenticatedUser() {
        UserEntry updatedUser = new UserEntry("bilalUpdated", "newHashedPass");
        updatedUser.setRoles(List.of("USER"));

        when(userEntryService.updateEntryByUserName(eq("bilal"), any(UserEntry.class)))
                .thenReturn(Optional.of(updatedUser));

        UserEntry requestBody = new UserEntry("bilalUpdated", "newPassword");
        ResponseEntity<UserEntry> response = userEntryController.updateByUserName(requestBody);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("bilalUpdated", response.getBody().getUserName());
    }

    @Test
    public void updateUser_shouldReturn404_whenUserNotFound() {
        when(userEntryService.updateEntryByUserName(eq("bilal"), any(UserEntry.class)))
                .thenReturn(Optional.empty());

        UserEntry requestBody = new UserEntry("bilalUpdated", "newPassword");
        ResponseEntity<UserEntry> response = userEntryController.updateByUserName(requestBody);

        assertEquals(404, response.getStatusCode().value());
    }
}
