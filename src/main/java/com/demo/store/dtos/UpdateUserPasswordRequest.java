package com.demo.store.dtos;

import lombok.Data;

@Data
public class UpdateUserPasswordRequest {
    private String oldPassword;
    private String newPassword;
}
