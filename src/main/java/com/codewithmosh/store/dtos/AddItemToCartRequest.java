package com.codewithmosh.store.dtos;

import com.codewithmosh.store.validations.Lowercase;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddItemToCartRequest {
    @NotBlank (message = "Product id is required")
    @Size (max = 25, message = "Product id must be less than 25 chars long")
    private Long productId;
}
