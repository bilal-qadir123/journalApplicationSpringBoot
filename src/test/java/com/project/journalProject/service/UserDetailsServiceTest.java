package com.project.journalProject.service;

import com.project.journalProject.entity.UserEntry;
import com.project.journalProject.repository.UserEntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class UserDetailsServiceTest {

    @Mock
    private UserEntryRepository userEntryRepository;

    @InjectMocks
    private UserDetailsServiceImplementation userDetailsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void loadUserByUsername_shouldReturnUserDetails_whenUserExists() {
        UserEntry user = new UserEntry("bilal", "hashedPassword123");
        user.setRoles(List.of("USER"));

        when(userEntryRepository.findByUserName("bilal")).thenReturn(Optional.of(user));

        UserDetails result = userDetailsService.loadUserByUsername("bilal");

        assertEquals("bilal", result.getUsername());
        assertEquals("hashedPassword123", result.getPassword());
        assertTrue(
                result.getAuthorities().stream()
                        .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")),
                "Should contain ROLE_USER authority (Spring adds ROLE_ prefix automatically)"
        );
    }

    @Test
    public void loadUserByUsername_shouldIncludeAllRoles_forAdminUser() {
        UserEntry admin = new UserEntry("adminUser", "hashedPassword");
        admin.setRoles(List.of("USER", "ADMIN"));

        when(userEntryRepository.findByUserName("adminUser")).thenReturn(Optional.of(admin));

        UserDetails result = userDetailsService.loadUserByUsername("adminUser");

        assertEquals(2, result.getAuthorities().size());
        assertTrue(
                result.getAuthorities().stream()
                        .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")),
                "Should contain ROLE_ADMIN authority"
        );
    }

    @Test
    public void loadUserByUsername_shouldThrowUsernameNotFoundException_whenUserNotFound() {
        when(userEntryRepository.findByUserName("ghost")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("ghost")
        );

        assertTrue(exception.getMessage().contains("ghost"));
    }
}
