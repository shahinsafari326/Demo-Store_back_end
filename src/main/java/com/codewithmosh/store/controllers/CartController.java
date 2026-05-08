package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.AddItemToCartRequest;
import com.codewithmosh.store.dtos.CartDto;
import com.codewithmosh.store.dtos.UpdateCartItemRequest;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.entities.CartItem;
import com.codewithmosh.store.entities.Product;
import com.codewithmosh.store.mappers.CartMapper;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
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

        var cartItem = cart.addItemToCart(product);
        cartRepository.save(cart);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartMapper.toCartItemDto(cartItem));
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(@PathVariable UUID cartId) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(cartMapper.toDto(cart));

    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> updateCartItem(@PathVariable UUID cartId,
                                            @PathVariable Long productId,
                                            @Valid @RequestBody UpdateCartItemRequest updateCartItemRequest) {
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "cart not found")
            );
        }
        // find cart item
        var cartItem = cart.getCartItem(productId);
        if (cartItem == null) {
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "cart item not found")
            );
        }

        cartItem.setQuantity(updateCartItemRequest.getQuantity());
        cartRepository.save(cart);
        return ResponseEntity.ok(cartMapper.toCartItemDto(cartItem));

    }

    @DeleteMapping ("/{cartId}/items/{productId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable UUID cartId, @PathVariable Long productId) {
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "cart not found")
            );
        }
        var cartItem = cart.getCartItem(productId);
        if (cartItem == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "cart item not found")
            );
        }

        cart.removeItemFromCart(productId);
        cartRepository.save(cart);
        return ResponseEntity.noContent().build();

    }


    @DeleteMapping ("/{cartId}/items")
    public ResponseEntity<?> deleteCartItems(@PathVariable UUID cartId) {
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "cart not found")
            );
        }

        cart.clearCart();
        cartRepository.save(cart);
        return ResponseEntity.noContent().build();
    }

}
