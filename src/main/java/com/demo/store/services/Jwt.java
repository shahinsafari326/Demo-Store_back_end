package com.demo.store.services;

import com.demo.store.entities.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Helper class for encapsulating jwt related fields.
 */
public class Jwt {
    private final Claims claims;
    private final SecretKey secretKey;


    public Jwt(Claims claims, SecretKey secretKey) {
        this.claims = claims;
        this.secretKey = secretKey;
    }


    public boolean isExpired() {
        return claims.getExpiration().before(new Date());
    }

    /**
     * Subject is id as added in generateToken method.
     * @return
     */
    public Long getUserId() {
        return Long.valueOf(claims.getSubject()) ;
    }

    public Role getRole() {
        return Role.valueOf(claims.get("role").toString()) ;
    }

    public String toString() {
        return Jwts.builder().claims(claims).signWith(secretKey).compact();
    }
}
