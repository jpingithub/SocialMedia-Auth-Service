package com.rb.auth.controller;

import com.nimbusds.jose.JOSEException;
import com.rb.auth.dto.JWTToken;
import com.rb.auth.dto.MessageResponse;
import com.rb.auth.dto.TokenValidationResponse;
import com.rb.auth.dto.UserDto;
import com.rb.auth.entity.UserEntity;
import com.rb.auth.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserEntity> registerUser(@RequestBody UserDto user) {
        return ResponseEntity.ok(userService.saveUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<JWTToken> login(@RequestBody UserDto userDto) {
        log.info("Received login request : {}",userDto.toString());
        final JWTToken jwtToken = new JWTToken();
        jwtToken.setJwtToken(userService.loginUser(userDto));
        return ResponseEntity.ok(jwtToken);
    }

    @GetMapping("/logout")
    public ResponseEntity<MessageResponse> logout(@RequestHeader("Authorization") String token) throws ParseException {
        userService.logOut(token);
        MessageResponse response = new MessageResponse();
        response.setMessage("User logged out successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/validate")
    public ResponseEntity<TokenValidationResponse> validateToken(@RequestParam("token") String token) throws ParseException, JOSEException {
        log.info("Received Token : {}",token);
        return ResponseEntity.ok(userService.validate(token));
    }

}
