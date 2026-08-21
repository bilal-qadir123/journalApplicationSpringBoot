package com.project.journalProject.service;

import com.project.journalProject.entity.JournalEntry;
import com.project.journalProject.entity.UserEntry;
import com.project.journalProject.repository.JournalEntryRepository;
import com.project.journalProject.repository.UserEntryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserEntryRepository userEntryRepository;

    @Mock
    private JournalEntryRepository journalEntryRepository;

    @Spy
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @InjectMocks
    private UserEntryService userEntryService;

    private UserEntry testUser;

    private AutoCloseable closeable;

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        testUser = new UserEntry("bilal", "rawPassword123");
        testUser.setRoles(List.of("USER"));
        testUser.setJournalEntryList(new ArrayList<>());
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }


    @Test
    public void createNewUser_shouldHashPasswordAndSetUserRole() {
        when(userEntryRepository.save(any(UserEntry.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserEntry result = userEntryService.createNewUser(testUser);

        assertNotEquals("rawPassword123", result.getPassword(), "Password should be hashed, not stored as plain text");
        assertTrue(result.getPassword().startsWith("$2a$"), "Password should be hashed with BCrypt");
        assertEquals(List.of("USER"), result.getRoles());
        verify(userEntryRepository, times(1)).save(any(UserEntry.class));
    }

    @Test
    public void createNewAdmin_shouldHashPasswordAndSetAdminAndUserRoles() {
        when(userEntryRepository.save(any(UserEntry.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserEntry result = userEntryService.createNewAdmin(testUser);

        assertTrue(result.getPassword().startsWith("$2a$"), "Password should be hashed with BCrypt");
        assertEquals(List.of("USER", "ADMIN"), result.getRoles());
        verify(userEntryRepository, times(1)).save(any(UserEntry.class));
    }


    @Test
    public void getEntryByUserName_shouldReturnUser_whenUserExists() {
        when(userEntryRepository.findByUserName("bilal")).thenReturn(Optional.of(testUser));

        Optional<UserEntry> result = userEntryService.getEntryByUserName("bilal");

        assertTrue(result.isPresent());
        assertEquals("bilal", result.get().getUserName());
    }

    @Test
    public void getEntryByUserName_shouldReturnEmpty_whenUserDoesNotExist() {
        when(userEntryRepository.findByUserName("nonexistent")).thenReturn(Optional.empty());

        Optional<UserEntry> result = userEntryService.getEntryByUserName("nonexistent");

        assertTrue(result.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"bilal", "bilal@123", "bilal@12345"})
    public void getEntryByUserName_shouldReturnUser_forMultipleUsernames(String name) {
        UserEntry user = new UserEntry(name, "password");
        when(userEntryRepository.findByUserName(name)).thenReturn(Optional.of(user));

        Optional<UserEntry> result = userEntryService.getEntryByUserName(name);

        assertTrue(result.isPresent(), "Test failed for username: " + name);
        assertEquals(name, result.get().getUserName());
    }


    @Test
    public void deleteByUserName_shouldDeleteUserAndJournalEntries_whenUserExists() {
        JournalEntry journal1 = new JournalEntry("Entry 1");
        JournalEntry journal2 = new JournalEntry("Entry 2");
        testUser.setJournalEntryList(new ArrayList<>(List.of(journal1, journal2)));

        when(userEntryRepository.findByUserName("bilal")).thenReturn(Optional.of(testUser));

        Optional<UserEntry> result = userEntryService.deleteByUserName("bilal");

        assertTrue(result.isPresent());
        assertEquals("bilal", result.get().getUserName());
        verify(journalEntryRepository, times(1)).deleteAll(List.of(journal1, journal2));
        verify(userEntryRepository, times(1)).delete(testUser);
    }

    @Test
    public void deleteByUserName_shouldReturnEmpty_whenUserDoesNotExist() {
        when(userEntryRepository.findByUserName("ghost")).thenReturn(Optional.empty());

        Optional<UserEntry> result = userEntryService.deleteByUserName("ghost");

        assertTrue(result.isEmpty());
        verify(journalEntryRepository, never()).deleteAll(anyList());
        verify(userEntryRepository, never()).delete(any());
    }


    @Test
    public void updateEntryByUserName_shouldUpdateFields_whenUserExists() {
        when(userEntryRepository.findByUserName("bilal")).thenReturn(Optional.of(testUser));
        when(userEntryRepository.save(any(UserEntry.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserEntry updatedData = new UserEntry("bilalUpdated", "newPassword");
        updatedData.setEmail("bilal@example.com");
        updatedData.setSentimentAnalysis(true);

        Optional<UserEntry> result = userEntryService.updateEntryByUserName("bilal", updatedData);

        assertTrue(result.isPresent());
        assertEquals("bilalUpdated", result.get().getUserName());
        assertTrue(result.get().getPassword().startsWith("$2a$"), "Updated password should be hashed");
        assertEquals("bilal@example.com", result.get().getEmail());
        assertTrue(result.get().isSentimentAnalysis());
        assertEquals(List.of("USER"), result.get().getRoles(), "Roles should not be changed through update endpoint");
    }

    @Test
    public void updateEntryByUserName_shouldReturnEmpty_whenUserDoesNotExist() {
        when(userEntryRepository.findByUserName("ghost")).thenReturn(Optional.empty());

        UserEntry updatedData = new UserEntry("newName", "newPass");

        Optional<UserEntry> result = userEntryService.updateEntryByUserName("ghost", updatedData);

        assertTrue(result.isEmpty());
        verify(userEntryRepository, never()).save(any());
    }
}
