package com.codewithmosh.store.mappers;

import com.codewithmosh.store.dtos.CartDto;
import com.codewithmosh.store.dtos.ProductDto;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

// used as interface as map strut automatically implements this class at run time!
@Mapper (componentModel = "spring")
public interface CartMapper {
    CartDto toDto(Cart cart);

}
