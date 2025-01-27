package com.rb.auth.controller;

import com.rb.auth.dto.JWTToken;
import com.rb.auth.dto.MessageResponse;
import com.rb.auth.dto.UserDto;
import com.rb.auth.entity.UserEntity;
import com.rb.auth.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserEntity> registerUser(@RequestBody UserDto user) {
        return ResponseEntity.ok(userService.saveUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<JWTToken> login(@RequestBody UserDto userDto){
        final JWTToken jwtToken = new JWTToken();
        jwtToken.setJwtToken(userService.loginUser(userDto));
        return ResponseEntity.ok(jwtToken);
    }

    @GetMapping("/logout")
    public ResponseEntity<MessageResponse> logout(HttpServletRequest request){
        userService.logOut(request);
        MessageResponse response = new MessageResponse();
        response.setMessage("User logged out successfully.........");
        return ResponseEntity.ok(response);
    }

}
