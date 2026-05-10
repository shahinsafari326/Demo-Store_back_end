package com.demo.store.controllers;

import com.demo.store.dtos.AddItemToCartRequest;
import com.demo.store.dtos.CartDto;
import com.demo.store.dtos.UpdateCartItemRequest;
import com.demo.store.entities.Cart;
import com.demo.store.mappers.CartMapper;
import com.demo.store.services.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/carts")
@Tag(name = "Carts") // used by swagger
public class CartController {

    private final CartMapper cartMapper;
    private final CartService cartService;


    @PostMapping
    @Operation(summary = "Create a new cart and return it") // swagger
    public ResponseEntity<?> createCart(UriComponentsBuilder uriComponentsBuilder) {
        var cartDto = cartService.createCart();
        var uri = uriComponentsBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri();
        return ResponseEntity.created(uri).body(cartDto);

    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<?> addProductToCart(@Parameter(description = "UUid of cart") @PathVariable UUID cartId,
                                              @Parameter(description = "Body containing productId")   @RequestBody AddItemToCartRequest addItemToCartRequest) {
        var cartItemDto = cartService.addProductToCart(cartId, addItemToCartRequest.getProductId());
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(@PathVariable UUID cartId) {
        Cart cart = cartService.getCart(cartId);
        return ResponseEntity.ok(cartMapper.toDto(cart));

    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> updateCartItem(@PathVariable UUID cartId,
                                            @PathVariable Long productId,
                                            @Valid @RequestBody UpdateCartItemRequest updateCartItemRequest) {
        var cartItemDto = cartService.updateCartItem(cartId, productId, updateCartItemRequest.getQuantity());
        return ResponseEntity.ok(cartItemDto);
    }

    @DeleteMapping ("/{cartId}/items/{productId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable UUID cartId, @PathVariable Long productId) {
        cartService.removeCartItem(cartId, productId);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping ("/{cartId}/items")
    public ResponseEntity<?> clearCart(@PathVariable UUID cartId) {
        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }

}
