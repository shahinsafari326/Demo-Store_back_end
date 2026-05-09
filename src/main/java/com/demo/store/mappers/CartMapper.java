package com.demo.store.mappers;

import com.demo.store.dtos.CartDto;
import com.demo.store.dtos.CartItemDto;
import com.demo.store.entities.Cart;
import com.demo.store.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

// used as interface as map strut automatically implements this class at run time!
@Mapper (componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "totalPrice", expression = "java(cart.getTotalPrice())")
    CartDto toDto(Cart cart);

    @Mapping(target = "totalPrice", expression = "java(cartItem.getTotalPrice())")
    CartItemDto toCartItemDto(CartItem cartItem);

}
