package com.phuckhang.digital_store.order.service;

import com.phuckhang.digital_store.order.dto.request.CartItemRequestDTO;
import com.phuckhang.digital_store.order.dto.response.CartResponseDTO;

public interface CartService {
    CartResponseDTO getMyCart();

    CartResponseDTO addToCart(CartItemRequestDTO requestDTO);

    CartResponseDTO updateCartItem(Long cartItemId, Integer quantity);

    void removeCartItem(Long cartItemId);

    void clearCart();
}