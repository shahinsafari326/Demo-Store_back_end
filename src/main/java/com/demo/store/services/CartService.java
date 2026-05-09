package com.demo.store.services;

import com.demo.store.dtos.CartDto;
import com.demo.store.dtos.CartItemDto;
import com.demo.store.entities.Cart;
import com.demo.store.entities.Product;
import com.demo.store.exceptions.CartItemNotFoundException;
import com.demo.store.exceptions.CartNotFoundException;
import com.demo.store.exceptions.ProductNotFoundException;
import com.demo.store.mappers.CartMapper;
import com.demo.store.repositories.CartRepository;
import com.demo.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
