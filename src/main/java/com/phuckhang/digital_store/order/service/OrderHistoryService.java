package com.phuckhang.digital_store.order.service;

import com.phuckhang.digital_store.order.entity.Order;
import com.phuckhang.digital_store.order.enums.OrderStatus;

public interface OrderHistoryService {
    void saveHistory(Order order, OrderStatus oldStatus, OrderStatus newStatus, String note);
}
