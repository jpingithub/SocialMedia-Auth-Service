package com.rb.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import com.rb.auth.config.RsaConfigurationProperties;
import com.rb.auth.dto.LoginRequest;
import com.rb.auth.entity.LoggedOutToken;
import com.rb.auth.exception.TokenException;
import com.rb.auth.repository.LoggedOutTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private LoggedOutTokenRepository loggedOutTokenRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private RsaConfigurationProperties rsaConfigurationProperties;

    public String loginUser(LoginRequest loginRequest) {
        final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        return tokenService.generateToken(authentication);
    }

    public void logOut(String token) throws ParseException {
        token = token.substring(7);
        SignedJWT signedJWT = SignedJWT.parse(token);
        long expiration = signedJWT.getJWTClaimsSet().getExpirationTime().getTime();
        LoggedOutToken loggedOutToken = new LoggedOutToken();
        loggedOutToken.setToken(token);
        loggedOutToken.setExpiresAt(expiration);
        loggedOutTokenRepository.save(loggedOutToken);
    }

    public Boolean validate(String token) throws ParseException, JOSEException {
        Optional<LoggedOutToken> byToken = loggedOutTokenRepository.findByToken(token);
        deleteExpiredTokens();
        if (byToken.isPresent()) throw new TokenException("Token already logged out");
        else {
            SignedJWT signedJWT = SignedJWT.parse(token);
            if (signedJWT.getJWTClaimsSet().getExpirationTime().getTime() < System.currentTimeMillis()) {
                throw new TokenException("Token expired");
            }
            RSASSAVerifier verifier = new RSASSAVerifier(rsaConfigurationProperties.publicKey());
            return signedJWT.verify(verifier);
        }
    }

    private void deleteExpiredTokens() {
        loggedOutTokenRepository.deleteByExpiresAtLessThan(System.currentTimeMillis());
    }

}
