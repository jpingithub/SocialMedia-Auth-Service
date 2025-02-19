package com.rb.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
public class TokenService {

    @Autowired private JwtEncoder jwtEncoder;

    public String generateToken(Authentication authentication){
        final Instant now = Instant.now();
        String roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        final JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuer("RealBoy.org")
                .issuedAt(now)
                .claim("scope",roles)
                .subject(authentication.getName())
                .expiresAt(now.plus(10, ChronoUnit.MINUTES))
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
    }

}
