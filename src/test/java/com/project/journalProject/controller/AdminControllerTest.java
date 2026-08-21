package com.project.journalProject.controller;

import com.project.journalProject.cache.AppCache;
import com.project.journalProject.entity.UserEntry;
import com.project.journalProject.repository.UserEntryRepository;
import com.project.journalProject.repository.UserRepositoryImplementation;
import com.project.journalProject.service.UserEntryService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AdminControllerTest {

    @Mock
    private UserEntryService userEntryService;

    @Mock
    private UserEntryRepository userEntryRepository;

    @Mock
    private UserRepositoryImplementation userRepositoryImplementation;

    @Mock
    private AppCache appCache;

    @InjectMocks
    private AdminController adminController;

    private AutoCloseable closeable;

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
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
    public void getAllUsers_shouldReturn200WithEmptyList_whenNoUsersExist() {
        when(userEntryRepository.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<List<UserEntry>> response = adminController.getAllUsers();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
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

    @Test
    public void createNewAdmin_shouldReturn409_whenUsernameAlreadyExists() {
        when(userEntryService.createNewAdmin(any(UserEntry.class)))
                .thenThrow(new RuntimeException("Duplicate key"));

        UserEntry requestBody = new UserEntry("existingAdmin", "password123");
        ResponseEntity<UserEntry> response = adminController.createNewAdmin(requestBody);

        assertEquals(409, response.getStatusCode().value());
    }

    @Test
    public void findEntryByUserName_shouldReturn200_whenUserExists() {
        UserEntry user = new UserEntry("bilal", "hashedPass");
        user.setRoles(List.of("USER"));

        when(userEntryService.getEntryByUserName("bilal")).thenReturn(Optional.of(user));

        ResponseEntity<UserEntry> response = adminController.findEntryByUserName("bilal");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("bilal", response.getBody().getUserName());
    }

    @Test
    public void findEntryByUserName_shouldReturn404_whenUserNotFound() {
        when(userEntryService.getEntryByUserName("nonexistent")).thenReturn(Optional.empty());

        ResponseEntity<UserEntry> response = adminController.findEntryByUserName("nonexistent");

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    public void clearAppCache_shouldCallInit() {
        adminController.clearAppCache();

        verify(appCache, times(1)).init();
    }
}
