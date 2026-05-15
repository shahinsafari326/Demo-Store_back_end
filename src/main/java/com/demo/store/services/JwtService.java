package com.demo.store.services;

import com.demo.store.config.JwtConfig;
import com.demo.store.entities.Role;
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


    private Jwt getToken(User user, long tokenExpiration) {
        long expirationTime = System.currentTimeMillis() + (tokenExpiration * 1000);

        var claimsBuilder =  Jwts.claims()
                .subject(user.getId().toString())
                .add("role", user.getRole().toString())
                .add("email", user.getEmail())
                .add("name", user.getName())
                .issuedAt(new Date())
                .expiration(new Date(expirationTime))
                .build();
        return new Jwt(claimsBuilder, jwtConfig.getSecretKey());
    }

    public Jwt generateRefreshToken(User user) {
        final long tokenExpiration = jwtConfig.getRefreshTokenExpiration();
        return getToken(user, tokenExpiration);
    }

    public Jwt generateAccessToken(User user) {
        final long tokenExpiration = jwtConfig.getAccessTokenExpiration();
        return getToken(user, tokenExpiration);
    }


    public Jwt parseToken(String token) {
        final Claims claims;
        try {
            claims = getClaims(token);
            return new Jwt(claims, jwtConfig.getSecretKey());
        }  catch (JwtException e) {
            return null;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(jwtConfig.getSecretKey())
                .build().parseSignedClaims(token)
                .getPayload();

    }





}
