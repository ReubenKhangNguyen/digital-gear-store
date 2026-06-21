package com.phuckhang.digital_store.order.controller;


import com.phuckhang.digital_store.common.dto.response.ApiResponse;
import com.phuckhang.digital_store.order.dto.request.CartItemRequestDTO;
import com.phuckhang.digital_store.order.dto.response.CartResponseDTO;
import com.phuckhang.digital_store.order.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/my-cart")
    public ApiResponse<CartResponseDTO> getMyCart() {
        return ApiResponse.<CartResponseDTO>builder()
                .result(cartService.getMyCart())
                .build();
    }

    @PostMapping("/items")
    public ApiResponse<CartResponseDTO> addToCart(@RequestBody @Valid CartItemRequestDTO requestDTO) {
        return ApiResponse.<CartResponseDTO>builder()
                .result(cartService.addToCart(requestDTO))
                .build();
    }

    @PutMapping("/items/{cartItemId}")
    public ApiResponse<CartResponseDTO> updateCartItem(@PathVariable Long cartItemId, @RequestParam Integer quantity) {
        return ApiResponse.<CartResponseDTO>builder()
                .result(cartService.updateCartItem(cartItemId, quantity))
                .build();
    }

    @DeleteMapping("/items/{cartItemId}")
    public ApiResponse<String> removeCartItem(@PathVariable Long cartItemId) {
        cartService.removeCartItem(cartItemId);
        return ApiResponse.<String>builder()
                .result("Đã xóa sản phẩm khỏi giỏ hàng")
                .build();
    }

    @DeleteMapping("/items")
    public ApiResponse<String> clearCart() {
        cartService.clearCart();
        return ApiResponse.<String>builder()
                .result("Đã làm sạch giỏ hàng")
                .build();
    }

}
