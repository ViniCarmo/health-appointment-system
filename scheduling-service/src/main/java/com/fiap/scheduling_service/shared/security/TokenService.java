package com.fiap.scheduling_service.shared.security;

import com.fiap.scheduling_service.user.infrastructure.security.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    private static final long EXPIRATION_MS = 3600000; // 1 hora

public String generateToken(UserDetailsImpl userDetails) {
        SecretKey key = getSigningKey();

return Jwts.builder()
        .subject(userDetails.getUsername()) // email
        .claim("userId", userDetails.getDomainUser().getId().toString())
        .claim("userType", userDetails.getDomainUser().getRole().name())
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
        .signWith(key)
        .compact();
}

    public String validateTokenAndGetSubject(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}
