package com.phuckhang.digital_store.order.controller;

import com.phuckhang.digital_store.common.dto.response.ApiResponse;
import com.phuckhang.digital_store.order.dto.response.OrderResponseDTO;
import com.phuckhang.digital_store.order.enums.OrderStatus;
import com.phuckhang.digital_store.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    // 5. Admin xem toàn bộ hệ thống đơn hàng (Có phân trang)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ApiResponse<Page<OrderResponseDTO>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return ApiResponse.<Page<OrderResponseDTO>>builder()
                .result(orderService.getAllOrders(pageable))
                .build();
    }

    // 6. Admin cập nhật trạng thái đơn (Phê duyệt, Giao hàng...)
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{orderId}/status")
    public ApiResponse<OrderResponseDTO> updateOrderStatus(
            @PathVariable Long orderId, 
            @RequestParam OrderStatus newStatus) { 
        return ApiResponse.<OrderResponseDTO>builder()
                .result(orderService.updateOrderStatus(orderId, newStatus))
                .build();
    }
}
