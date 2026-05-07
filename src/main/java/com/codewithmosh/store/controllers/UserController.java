package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.RegisterUserRequest;
import com.codewithmosh.store.dtos.UpdateUserPasswordRequest;
import com.codewithmosh.store.dtos.UpdateUserRequest;
import com.codewithmosh.store.dtos.UserDto;
import com.codewithmosh.store.entities.User;
import com.codewithmosh.store.mappers.UserMapper;
import com.codewithmosh.store.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private UserRepository userRepository;
    private UserMapper userMapper;

    @GetMapping
    public List<UserDto> getAllUsers (@RequestParam (required = false, defaultValue = "", name = "sort") String sort) {
        // validate sort param, only name and email is allowed, name is default

        if(!Set.of("name", "email").contains(sort)){
            sort = "name";
        }
        return userRepository.findAll(Sort.by(Sort.Direction.DESC, sort))
                .stream()
                .map(user -> userMapper.toDto(user))
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(userMapper.toDto(user));
        }
    }

    @PostMapping
    public ResponseEntity<?> registerUser(
            UriComponentsBuilder  uriComponentsBuilder,
            @Valid @RequestBody RegisterUserRequest request) {

        // validate business rules, does email exist
        if(userRepository.existsByEmail(request.getEmail())){
            return ResponseEntity.badRequest().body(
                    Map.of("email","email already registered")
            );
        }

        // convert request body to user
        User user = userMapper.toEntity(request);
        // save to database
        User result = userRepository.save(user);
        // convert to dto for returning
        var userDto = userMapper.toDto(result);
        var uri = uriComponentsBuilder.path("/users/{id}").buildAndExpand(result.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);

    }


    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable (name = "id") Long id,  @RequestBody UpdateUserRequest request) {

        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        } else {
            userMapper.updateUser(request,user);
            userRepository.save(user);
            return ResponseEntity.ok(userMapper.toDto(user));

        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDto> deleteUser(@PathVariable (name = "id") Long id) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        } else {
            userRepository.delete(user);
            return ResponseEntity.noContent().build(); //204 no content
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> updateUserPassword(@PathVariable (name = "id") Long id,
            @RequestBody UpdateUserPasswordRequest request) {

        var  user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        // validate old pass
        if (!user.getPassword().equals(request.getOldPassword())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        }

        user.setPassword(request.getNewPassword());
        userRepository.save(user);
        return ResponseEntity.noContent().build();

    }


}
