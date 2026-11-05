package com.example.blog.security;

import com.example.blog.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class JwtService {
    private final JwtEncoder jwtEncoder;
    private final long expirationMinutes;
    private final String issuer;
    public JwtService(
            JwtEncoder jwtEncoder, @Value("${app.jwt.expiration-minutes}")
            long expirationMinutes, @Value("${app.jwt.issuer}") String issuer) {

        this.jwtEncoder = jwtEncoder;
        this.expirationMinutes = expirationMinutes;
        this.issuer = issuer;
    }
    public String generateToken(User user){
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(now.plus(expirationMinutes, ChronoUnit.MINUTES))
                .subject(user.getUsername())
                .claim("role", user.getRole().name())
                .claim("userId", user.getId())
                .build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }
}


