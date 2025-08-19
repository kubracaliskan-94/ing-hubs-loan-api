package com.ing.hubs.loan.api.security;

import com.ing.hubs.loan.api.model.User;
import com.ing.hubs.loan.api.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User currentUser = userService.findByUsername(username);
        System.out.println("Username: " + currentUser.getUsername());
        System.out.println("Stored password: " + currentUser.getPassword());

        String[] authorities = new ArrayList<>(currentUser.getRoles())
                .stream()
                .map(role -> role.getName().name())
                .toArray(String[]::new);
        log.info("User roles: {}", Arrays.stream(authorities).toArray());


        return org.springframework.security.core.userdetails.User
                .withUsername(currentUser.getUsername())
                .password(currentUser.getPassword())
                .authorities(authorities)
                .build();
    }
}
