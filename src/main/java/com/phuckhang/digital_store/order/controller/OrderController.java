package com.phuckhang.digital_store.order.controller;

import com.phuckhang.digital_store.common.dto.response.ApiResponse;
import com.phuckhang.digital_store.order.dto.request.OrderRequestDTO;
import com.phuckhang.digital_store.order.dto.response.OrderResponseDTO;
import com.phuckhang.digital_store.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 1. Khách chốt đơn 
    @PostMapping("/checkout")
    public ApiResponse<OrderResponseDTO> checkout(@RequestBody @Valid OrderRequestDTO requestDTO) {
        return ApiResponse.<OrderResponseDTO>builder()
                .result(orderService.checkout(requestDTO))
                .build();
    }

    // 2. Khách xem lịch sử mua hàng 
    @GetMapping("/my-orders")
    public ApiResponse<List<OrderResponseDTO>> getMyOrders() {
        return ApiResponse.<List<OrderResponseDTO>>builder()
                .result(orderService.getMyOrders())
                .build();
    }

    // 3. Khách xem chi tiết 1 đơn hàng cụ thể
    @GetMapping("/{orderId}")
    public ApiResponse<OrderResponseDTO> getOrderById(@PathVariable Long orderId) {
        return ApiResponse.<OrderResponseDTO>builder()
                .result(orderService.getOrderById(orderId))
                .build();
    }

    // 4. Khách tự hủy đơn 
    @PutMapping("/{orderId}/cancel")
    public ApiResponse<String> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return ApiResponse.<String>builder()
                .result("Đã hủy đơn hàng thành công!")
                .build();
    }
}
