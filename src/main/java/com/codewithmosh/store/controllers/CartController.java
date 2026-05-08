package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.AddItemToCartRequest;
import com.codewithmosh.store.dtos.CartDto;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.entities.CartItem;
import com.codewithmosh.store.entities.Product;
import com.codewithmosh.store.mappers.CartMapper;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;


    @PostMapping
    public ResponseEntity<?> createCart(UriComponentsBuilder uriComponentsBuilder) {
        Cart cart = new Cart();
        cartRepository.save(cart);
        var cartDto = cartMapper.toDto(cart);
        var uri = uriComponentsBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri();
        return ResponseEntity.created(uri).body(cartDto);

    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<?> addProductToCart(@PathVariable UUID cartId,
                                                    @RequestBody AddItemToCartRequest addItemToCartRequest) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) return ResponseEntity.notFound().build();
        Product product = productRepository.findById(addItemToCartRequest.getProductId()).orElse(null);
        if (product == null) return ResponseEntity.badRequest().build();

        // find cart item
        var cartItem = cart.getItems().stream().filter(item ->
                item.getProduct().getId().equals(product.getId())).findFirst().orElse(null);
        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        } else {
            // create new cart item
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartItem.setCart(cart);
            cart.getItems().add(cartItem);
        }
        cartRepository.save(cart);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartMapper.toCartItemDto(cartItem));
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(@PathVariable UUID cartId) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(cartMapper.toDto(cart));

    }
}
