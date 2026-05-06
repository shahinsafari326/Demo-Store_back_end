package com.codewithmosh.store.dtos;

import lombok.Data;

@Data
public class UpdateUserPasswordRequest {
    private String oldPassword;
    private String newPassword;
}
