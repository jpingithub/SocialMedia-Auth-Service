package com.rb.auth.client;

import com.rb.auth.dto.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "USER-MANAGEMENT-SERVICE")
public interface UserManagementClient {

    @GetMapping("/api/v1/users/login")
    ResponseEntity<User> getUserByUsernameForLogin(@RequestParam("username") String username);

}
