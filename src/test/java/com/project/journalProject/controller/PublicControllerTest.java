package com.project.journalProject.controller;

import com.project.journalProject.entity.UserEntry;
import com.project.journalProject.service.UserEntryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class PublicControllerTest {

    @Mock
    private UserEntryService userEntryService;

    @InjectMocks
    private PublicController publicController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createUser_shouldReturnSavedUser() {
        UserEntry savedUser = new UserEntry("bilal", "$2a$10$hashedPassword");
        savedUser.setRoles(List.of("USER"));

        when(userEntryService.createNewUser(any(UserEntry.class))).thenReturn(savedUser);

        UserEntry inputUser = new UserEntry("bilal", "password123");
        ResponseEntity<UserEntry> response = publicController.createEntry(inputUser);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("bilal", response.getBody().getUserName());
        assertEquals(List.of("USER"), response.getBody().getRoles());
    }
}
