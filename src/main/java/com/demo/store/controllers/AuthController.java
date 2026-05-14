package com.demo.store.controllers;

import com.demo.store.dtos.JwtResponse;
import com.demo.store.dtos.*;
import com.demo.store.repositories.UserRepository;
import com.demo.store.services.JwtService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;



    @PostMapping("/login")
    public ResponseEntity<JwtResponse> loginUser(
            @Valid @RequestBody LoginUserRequest request) {

        // verify login
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));


        // generate token
        var user  = userRepository.findByEmail(request.getEmail()).orElseThrow();

        var token = jwtService.generateAccessToken(user);
        return ResponseEntity.ok(new JwtResponse(token));

    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshToken(
            @CookieValue(value = "refreshToken") String tokenToRefresh) {

        if (!jwtService.validateToken(tokenToRefresh)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // generate new token if refreshToken still valid
        var userId =  jwtService.getUserIdFromToken(tokenToRefresh);
        var user = userRepository.findById(userId).orElseThrow();
        var accessToken = jwtService.generateAccessToken(user);
        return ResponseEntity.ok(new JwtResponse(accessToken));

    }

}
