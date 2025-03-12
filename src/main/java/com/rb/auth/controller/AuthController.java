package com.rb.auth.controller;

import com.nimbusds.jose.JOSEException;
import com.rb.auth.dto.JWTToken;
import com.rb.auth.dto.LoginRequest;
import com.rb.auth.dto.MessageResponse;
import com.rb.auth.dto.ValidTokenResponse;
import com.rb.auth.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<JWTToken> login(@RequestBody LoginRequest loginRequest) {
        log.info("Received login request : {}",loginRequest.toString());
        final JWTToken jwtToken = new JWTToken();
        jwtToken.setJwtToken(userService.loginUser(loginRequest));
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
    public ResponseEntity<ValidTokenResponse> validateToken(@RequestParam("token") String token) throws ParseException, JOSEException {
        return ResponseEntity.ok(userService.validate(token));
    }

}
