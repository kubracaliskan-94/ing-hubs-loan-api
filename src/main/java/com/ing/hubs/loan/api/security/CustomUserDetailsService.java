package com.ing.hubs.loan.api.security;

import com.ing.hubs.loan.api.model.entity.User;
import com.ing.hubs.loan.api.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User currentUser = userService.findByUsername(username);
        String[] authorities = new ArrayList<>(currentUser.getRoles())
                .stream()
                .map(role -> role.getName().name())
                .toArray(String[]::new);

        return org.springframework.security.core.userdetails.User
                .withUsername(currentUser.getUsername())
                .password(currentUser.getPassword())
                .authorities(authorities)
                .build();
    }
}
