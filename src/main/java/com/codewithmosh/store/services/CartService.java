package com.codewithmosh.store.services;

import com.codewithmosh.store.dtos.CartDto;
import com.codewithmosh.store.dtos.CartItemDto;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.entities.Product;
import com.codewithmosh.store.exceptions.CartItemNotFoundException;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.ProductNotFoundException;
import com.codewithmosh.store.mappers.CartMapper;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;


    public CartDto createCart() {
        Cart cart = new Cart();
        cartRepository.save(cart);
        return cartMapper.toDto(cart);
    }

    public CartItemDto addProductToCart (UUID cartId, Long productId){
        Cart cart = getCart(cartId);
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) throw new ProductNotFoundException();

        var cartItem = cart.addItemToCart(product);
        cartRepository.save(cart);
        return cartMapper.toCartItemDto(cartItem);
    }

    public CartItemDto updateCartItem (UUID cartId, Long productId, Integer quantity){
        var cart = getCart(cartId);
        // find cart item
        var cartItem = cart.getCartItem(productId);
        if (cartItem == null) {
            throw new CartItemNotFoundException();
        }

        cartItem.setQuantity(quantity);
        cartRepository.save(cart);
        return cartMapper.toCartItemDto(cartItem);
    }

    public void removeCartItem (UUID cartId, Long productId){
        var cart = getCart(cartId);
        var cartItem = cart.getCartItem(productId);
        if (cartItem == null) {
            throw new CartItemNotFoundException();
        }

        cart.removeItemFromCart(productId);
        cartRepository.save(cart);
    }

    public void clearCart(UUID cartId){
        var cart = getCart(cartId);
        cart.clearCart();
        cartRepository.save(cart);
    }

    public Cart getCart(UUID cartId){
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }
        return cart;
    }
}
