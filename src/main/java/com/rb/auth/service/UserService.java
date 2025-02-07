package com.rb.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import com.rb.auth.config.RsaConfigurationProperties;
import com.rb.auth.dto.TokenValidationResponse;
import com.rb.auth.dto.UserDto;
import com.rb.auth.entity.LoggedOutToken;
import com.rb.auth.entity.UserEntity;
import com.rb.auth.exception.TokenException;
import com.rb.auth.exception.UserException;
import com.rb.auth.repository.LoggedOutTokenRepository;
import com.rb.auth.repository.UserRepository;
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
    private UserRepository userRepository;
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

    public UserEntity saveUser(UserDto userDto) {
        final UserEntity user = objectMapper.convertValue(userDto, UserEntity.class);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        if (userRepository.findByUsername(userDto.getUsername()).isPresent())
            throw new UserException("User already exists");
        return userRepository.save(user);
    }

    public String loginUser(UserDto userDto) {
        final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
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

    public TokenValidationResponse validate(String token) throws ParseException, JOSEException {
        Optional<LoggedOutToken> byToken = loggedOutTokenRepository.findByToken(token);
        deleteExpiredTokens();
        if (byToken.isPresent()) throw new TokenException("Token already logged out");
        else {
            SignedJWT signedJWT = SignedJWT.parse(token);
            if(signedJWT.getJWTClaimsSet().getExpirationTime().getTime()<System.currentTimeMillis()){
                throw new TokenException("Token expired");
            }
            RSASSAVerifier verifier = new RSASSAVerifier(rsaConfigurationProperties.publicKey());
            TokenValidationResponse tokenValidationResponse = new TokenValidationResponse();
            tokenValidationResponse.setIsValid(signedJWT.verify(verifier));
            return tokenValidationResponse;
        }
    }

    private void deleteExpiredTokens(){
        loggedOutTokenRepository.deleteByExpiresAtLessThan(System.currentTimeMillis());
    }

}
