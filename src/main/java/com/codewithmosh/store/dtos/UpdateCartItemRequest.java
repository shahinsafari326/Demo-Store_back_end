package com.codewithmosh.store.dtos;

import com.codewithmosh.store.validations.Lowercase;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NonNull;

@Data
public class UpdateCartItemRequest {
    @NotNull(message = "Quantity must be provided")
    @Min(value = 1, message = "Min value is 1")
    private Integer quantity;
}
