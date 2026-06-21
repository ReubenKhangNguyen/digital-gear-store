package com.phuckhang.digital_store.order.service;


import com.phuckhang.digital_store.order.dto.request.OrderRequestDTO;
import com.phuckhang.digital_store.order.dto.response.OrderResponseDTO;
import com.phuckhang.digital_store.order.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    OrderResponseDTO checkout(OrderRequestDTO requestDTO);

    List<OrderResponseDTO> getMyOrders();

    OrderResponseDTO getOrderById(Long orderId);

    void cancelOrder(Long orderId);

    Page<OrderResponseDTO> getAllOrders(Pageable pageable);

    OrderResponseDTO updateOrderStatus(Long orderId, OrderStatus newStatus);
}