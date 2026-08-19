package com.project.journalProject.service;

import com.project.journalProject.entity.JournalEntry;
import com.project.journalProject.entity.UserEntry;
import com.project.journalProject.repository.JournalEntryRepository;
import com.project.journalProject.repository.UserEntryRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class JournalEntryServiceTest {

    @Mock
    private JournalEntryRepository journalEntryRepository;

    @Mock
    private UserEntryRepository userEntryRepository;

    @InjectMocks
    private JournalEntryService journalEntryService;

    private UserEntry testUser;
    private ObjectId journalId;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        journalId = new ObjectId();

        JournalEntry testJournal = new JournalEntry("My First Entry");
        testJournal.setId(journalId);
        testJournal.setContent("Hello world");

        testUser = new UserEntry("bilal", "password");
        testUser.setRoles(List.of("USER"));
        testUser.setJournalEntryList(new ArrayList<>(List.of(testJournal)));
    }


    @Test
    public void createEntry_shouldSaveEntryAndLinkToUser_whenUserExists() {
        JournalEntry newEntry = new JournalEntry("New Entry");
        when(userEntryRepository.findByUserName("bilal")).thenReturn(Optional.of(testUser));
        when(journalEntryRepository.save(any(JournalEntry.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userEntryRepository.save(any(UserEntry.class))).thenAnswer(invocation -> invocation.getArgument(0));

        JournalEntry result = journalEntryService.createEntry(newEntry, "bilal");

        assertNotNull(result);
        assertNotNull(result.getDate(), "Date should be set automatically");
        verify(journalEntryRepository, times(1)).save(newEntry);
        verify(userEntryRepository, times(1)).save(testUser);
    }

    @Test
    public void createEntry_shouldReturnNull_whenUserDoesNotExist() {
        JournalEntry newEntry = new JournalEntry("New Entry");
        when(userEntryRepository.findByUserName("ghost")).thenReturn(Optional.empty());

        JournalEntry result = journalEntryService.createEntry(newEntry, "ghost");

        assertNull(result);
        verify(journalEntryRepository, never()).save(any());
    }


    @Test
    public void getAllJournalEntriesForUser_shouldReturnEntries_whenUserExists() {
        when(userEntryRepository.findByUserName("bilal")).thenReturn(Optional.of(testUser));

        List<JournalEntry> result = journalEntryService.getAllJournalEntriesForUser("bilal");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("My First Entry", result.get(0).getTitle());
    }

    @Test
    public void getAllJournalEntriesForUser_shouldReturnNull_whenUserDoesNotExist() {
        when(userEntryRepository.findByUserName("ghost")).thenReturn(Optional.empty());

        List<JournalEntry> result = journalEntryService.getAllJournalEntriesForUser("ghost");

        assertNull(result);
    }


    @Test
    public void deleteByUserNameAndID_shouldDeleteEntry_whenEntryBelongsToUser() {
        when(userEntryRepository.findByUserName("bilal")).thenReturn(Optional.of(testUser));

        Optional<JournalEntry> result = journalEntryService.deleteByUserNameAndID("bilal", journalId);

        assertTrue(result.isPresent());
        assertEquals("My First Entry", result.get().getTitle());
        verify(journalEntryRepository, times(1)).deleteById(journalId);
        verify(userEntryRepository, times(1)).save(testUser);
    }

    @Test
    public void deleteByUserNameAndID_shouldReturnEmpty_whenEntryIdDoesNotMatch() {
        ObjectId wrongId = new ObjectId();
        when(userEntryRepository.findByUserName("bilal")).thenReturn(Optional.of(testUser));

        Optional<JournalEntry> result = journalEntryService.deleteByUserNameAndID("bilal", wrongId);

        assertTrue(result.isEmpty());
        verify(journalEntryRepository, never()).deleteById(any());
    }


    @Test
    public void updateByUserNameAndID_shouldUpdateFields_whenEntryExists() {
        when(userEntryRepository.findByUserName("bilal")).thenReturn(Optional.of(testUser));
        when(journalEntryRepository.save(any(JournalEntry.class))).thenAnswer(invocation -> invocation.getArgument(0));

        JournalEntry updatedData = new JournalEntry("Updated Title");
        updatedData.setContent("Updated content");

        Optional<JournalEntry> result = journalEntryService.updateByUserNameAndID("bilal", journalId, updatedData);

        assertTrue(result.isPresent());
        assertEquals("Updated Title", result.get().getTitle());
        assertEquals("Updated content", result.get().getContent());
    }

    @Test
    public void updateByUserNameAndID_shouldReturnEmpty_whenUserDoesNotExist() {
        when(userEntryRepository.findByUserName("ghost")).thenReturn(Optional.empty());

        JournalEntry updatedData = new JournalEntry("Updated Title");

        Optional<JournalEntry> result = journalEntryService.updateByUserNameAndID("ghost", journalId, updatedData);

        assertTrue(result.isEmpty());
        verify(journalEntryRepository, never()).save(any());
    }
}
