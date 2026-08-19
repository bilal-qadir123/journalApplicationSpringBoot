package com.project.journalProject.service;

import com.project.journalProject.entity.UserEntry;
import com.project.journalProject.repository.UserEntryRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDetailsServiceImplementation implements UserDetailsService {

    private final UserEntryRepository userEntryRepository;

    public UserDetailsServiceImplementation(UserEntryRepository userEntryRepository) {
        this.userEntryRepository = userEntryRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntry> userEntry = userEntryRepository.findByUserName(username);

        if (userEntry.isPresent()) {
            return User.builder()
                    .username(userEntry.get().getUserName())
                    .password(userEntry.get().getPassword())
                    .roles(userEntry.get().getRoles().toArray(new String[0]))
                    .build();
        }
        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}