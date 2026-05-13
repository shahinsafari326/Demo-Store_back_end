package com.demo.store.services;

import com.demo.store.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    @Value("${spring.jwt}")
    private String secret;

    public String generateToken(User user) {
        final long tokenExpiration = System.currentTimeMillis() + 1000 * 60 * 60 * 24; // 1 day in millis
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("name", user.getName())
                .claim("email", user.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(tokenExpiration))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            var claims = getClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (JwtException e) {
            return false;
        }

    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build().parseSignedClaims(token)
                .getPayload();

    }

    /**
     * Subject is id as added in generateToken method.
     * @param token
     * @return
     */
    public Long getUserIdFromToken(String token) {
        return Long.valueOf(getClaims(token).getSubject()) ;
    }

}
