package com.project.journalProject.controller;

import com.project.journalProject.entity.UserEntry;
import com.project.journalProject.repository.UserEntryRepository;
import com.project.journalProject.service.UserEntryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AdminControllerTest {

    @Mock
    private UserEntryService userEntryService;

    @Mock
    private UserEntryRepository userEntryRepository;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllUsers_shouldReturn200_whenUsersExist() {
        UserEntry user1 = new UserEntry("bilal", "hashedPass");
        user1.setRoles(List.of("USER"));
        UserEntry user2 = new UserEntry("admin", "hashedPass");
        user2.setRoles(List.of("USER", "ADMIN"));

        when(userEntryRepository.findAll()).thenReturn(List.of(user1, user2));

        ResponseEntity<List<UserEntry>> response = adminController.getAllUsers();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(2, response.getBody().size());
        assertEquals("bilal", response.getBody().get(0).getUserName());
        assertEquals("admin", response.getBody().get(1).getUserName());
    }

    @Test
    public void getAllUsers_shouldReturn404_whenNoUsersExist() {
        when(userEntryRepository.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<List<UserEntry>> response = adminController.getAllUsers();

        assertEquals(404, response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    public void createNewAdmin_shouldReturnCreatedAdmin() {
        UserEntry newAdmin = new UserEntry("newAdmin", "hashedPass");
        newAdmin.setRoles(List.of("USER", "ADMIN"));

        when(userEntryService.createNewAdmin(any(UserEntry.class))).thenReturn(newAdmin);

        UserEntry requestBody = new UserEntry("newAdmin", "password123");
        ResponseEntity<UserEntry> response = adminController.createNewAdmin(requestBody);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("newAdmin", response.getBody().getUserName());
        assertEquals(List.of("USER", "ADMIN"), response.getBody().getRoles());
        verify(userEntryService, times(1)).createNewAdmin(any(UserEntry.class));
    }
}
