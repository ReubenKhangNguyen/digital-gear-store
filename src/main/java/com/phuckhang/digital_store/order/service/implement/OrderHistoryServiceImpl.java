package com.phuckhang.digital_store.order.service.implement;

import com.phuckhang.digital_store.order.entity.Order;
import com.phuckhang.digital_store.order.entity.OrderHistory;
import com.phuckhang.digital_store.order.enums.OrderStatus;
import com.phuckhang.digital_store.order.repository.OrderHistoryRepository;
import com.phuckhang.digital_store.order.service.OrderHistoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderHistoryServiceImpl implements OrderHistoryService {
    OrderHistoryRepository orderHistoryRepository;

    @Override
    public void saveHistory(Order order, OrderStatus oldStatus, OrderStatus newStatus, String note) {
        // Lấy tên User đang đăng nhập, nếu không có (hệ thống tự chạy) thì gán là SYSTEM
        String changedBy = "SYSTEM";
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            changedBy = authentication.getName();
        }
        OrderHistory history = OrderHistory.builder()
                .order(order)
                .oldStatus(oldStatus)
                .newStatus(newStatus)
                .changedBy(changedBy)
                .note(note)
                .build();
        orderHistoryRepository.save(history);
    }
}