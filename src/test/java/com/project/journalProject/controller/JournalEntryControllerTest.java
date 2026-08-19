package com.project.journalProject.controller;

import com.project.journalProject.entity.JournalEntry;
import com.project.journalProject.service.JournalEntryService;
import org.bson.types.ObjectId;
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

public class JournalEntryControllerTest {

    @Mock
    private JournalEntryService journalEntryService;

    @InjectMocks
    private JournalEntryController journalEntryController;

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
    public void createEntry_shouldReturn200() {
        JournalEntry savedEntry = new JournalEntry("My Journal");
        savedEntry.setContent("Today was a good day");
        savedEntry.setId(new ObjectId());

        when(journalEntryService.createEntry(any(JournalEntry.class), eq("bilal")))
                .thenReturn(savedEntry);

        JournalEntry inputEntry = new JournalEntry("My Journal");
        inputEntry.setContent("Today was a good day");

        ResponseEntity<JournalEntry> response = journalEntryController.createEntry(inputEntry);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("My Journal", response.getBody().getTitle());
        assertEquals("Today was a good day", response.getBody().getContent());
    }

    @Test
    public void getAllEntries_shouldReturnEntriesForAuthenticatedUser() {
        JournalEntry entry1 = new JournalEntry("Entry 1");
        JournalEntry entry2 = new JournalEntry("Entry 2");

        when(journalEntryService.getAllJournalEntriesForUser("bilal"))
                .thenReturn(List.of(entry1, entry2));

        ResponseEntity<List<JournalEntry>> response = journalEntryController.getAllJournalEntriesForUser();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(2, response.getBody().size());
        assertEquals("Entry 1", response.getBody().get(0).getTitle());
        assertEquals("Entry 2", response.getBody().get(1).getTitle());
    }

    @Test
    public void getEntryById_shouldReturn200_whenEntryExists() {
        ObjectId id = new ObjectId();
        JournalEntry entry = new JournalEntry("Found Entry");
        entry.setId(id);

        when(journalEntryService.getJournalEntriesByUser("bilal", id))
                .thenReturn(Optional.of(entry));

        ResponseEntity<JournalEntry> response = journalEntryController.findJournalEntriesByUser(id);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Found Entry", response.getBody().getTitle());
    }

    @Test
    public void getEntryById_shouldReturn404_whenEntryDoesNotExist() {
        ObjectId id = new ObjectId();

        when(journalEntryService.getJournalEntriesByUser("bilal", id))
                .thenReturn(Optional.empty());

        ResponseEntity<JournalEntry> response = journalEntryController.findJournalEntriesByUser(id);

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    public void deleteEntry_shouldReturn200_whenEntryExists() {
        ObjectId id = new ObjectId();
        JournalEntry entry = new JournalEntry("Deleted Entry");
        entry.setId(id);

        when(journalEntryService.deleteByUserNameAndID("bilal", id))
                .thenReturn(Optional.of(entry));

        ResponseEntity<JournalEntry> response = journalEntryController.deleteByUserNameAndID(id);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Deleted Entry", response.getBody().getTitle());
    }

    @Test
    public void deleteEntry_shouldReturn404_whenEntryDoesNotExist() {
        ObjectId id = new ObjectId();

        when(journalEntryService.deleteByUserNameAndID("bilal", id))
                .thenReturn(Optional.empty());

        ResponseEntity<JournalEntry> response = journalEntryController.deleteByUserNameAndID(id);

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    public void updateEntry_shouldReturn200_whenEntryExists() {
        ObjectId id = new ObjectId();
        JournalEntry updatedEntry = new JournalEntry("Updated Title");
        updatedEntry.setContent("Updated content");
        updatedEntry.setId(id);

        when(journalEntryService.updateByUserNameAndID(eq("bilal"), eq(id), any(JournalEntry.class)))
                .thenReturn(Optional.of(updatedEntry));

        JournalEntry requestBody = new JournalEntry("Updated Title");
        requestBody.setContent("Updated content");

        ResponseEntity<JournalEntry> response = journalEntryController.updateByNameAndID(id, requestBody);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Updated Title", response.getBody().getTitle());
        assertEquals("Updated content", response.getBody().getContent());
    }

    @Test
    public void updateEntry_shouldReturn404_whenEntryDoesNotExist() {
        ObjectId id = new ObjectId();

        when(journalEntryService.updateByUserNameAndID(eq("bilal"), eq(id), any(JournalEntry.class)))
                .thenReturn(Optional.empty());

        JournalEntry requestBody = new JournalEntry("Updated Title");

        ResponseEntity<JournalEntry> response = journalEntryController.updateByNameAndID(id, requestBody);

        assertEquals(404, response.getStatusCode().value());
    }
}
