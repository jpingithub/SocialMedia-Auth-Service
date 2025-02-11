package com.rb.auth.service;

import com.rb.auth.client.UserManagementClient;
import com.rb.auth.dto.User;
import com.rb.auth.exception.UserException;
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
        if(userByUsernameForLogin.getStatusCode() == HttpStatus.OK && userByUsernameForLogin.hasBody()){
            log.info("User semi validated");
            return new AuthenticatedUser(userByUsernameForLogin.getBody());
        }else {
            log.info("No user found to validate login with user name : {}",username);
            throw new UserException("No user found with username : "+username);
        }
    }
}
