package com.demo.store.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateCartItemRequest {
    @NotNull(message = "Quantity must be provided")
    @Min(value = 1, message = "Min value is 1")
    private Integer quantity;
}
