package com.demo.store.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDto {
    @JsonIgnore
    private Long id;

    @JsonProperty("user_name")
    private String name;

    private String description;

    private BigDecimal price;

    private byte categoryId;
}