package com.demo.store.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class CartDto {
    private UUID id;
    // these names should match names used in Cart, otherwise we would need custom mapping in mapper class!
    private List<CartItemDto> items = new ArrayList<>();
    private BigDecimal totalPrice = new BigDecimal(0);
}