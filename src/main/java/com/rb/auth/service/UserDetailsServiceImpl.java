package com.rb.auth.service;

import com.rb.auth.client.UserManagementClient;
import com.rb.auth.dto.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserManagementClient userManagementClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Received username : {}",username);
        ResponseEntity<User> userByUsernameForLogin = userManagementClient.getUserByUsernameForLogin(username);
        if(userByUsernameForLogin.getStatusCode() == HttpStatus.OK && userByUsernameForLogin.hasBody())
            return new AuthenticatedUser(userByUsernameForLogin.getBody());
        else throw new UsernameNotFoundException("No user found...");
    }
}
