package com.demo.store.dtos;

import com.demo.store.validations.Lowercase;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterUserRequest {
    @NotBlank (message = "Name is required")
    @Size (max = 255, message = "Name must be less than 255 chars long")
    private String name;

    @NotBlank(message = "Email is required")
    @Email
    @Lowercase
    private String email;

    @NotBlank
    @Size(min =  8, max = 25, message = "Password must be between 8 and 25 characters long.")
    private String password;
}
