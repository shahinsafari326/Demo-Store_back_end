package com.demo.store.services;

import com.demo.store.config.JwtConfig;
import com.demo.store.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class JwtService {

    private final JwtConfig jwtConfig;


    private String getToken(User user, long tokenExpiration) {
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("name", user.getName())
                .claim("email", user.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(tokenExpiration))
                .signWith(jwtConfig.getSecretKey())
                .compact();
    }

    public String generateRefreshToken(User user) {
        final long tokenExpiration = jwtConfig.getRefreshTokenExpiration();
        return getToken(user, tokenExpiration);
    }

    public String generateAccessToken(User user) {
        final long tokenExpiration = jwtConfig.getAccessTokenExpiration();
        return getToken(user, tokenExpiration);
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
                .verifyWith(jwtConfig.getSecretKey())
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
