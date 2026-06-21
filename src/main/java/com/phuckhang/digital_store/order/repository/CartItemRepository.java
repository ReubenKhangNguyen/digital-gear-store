package com.phuckhang.digital_store.order.repository;

import com.phuckhang.digital_store.order.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    // Tìm xem 1 sản phẩm đã có trong giỏ hàng cụ thể chưa (Để gộp số lượng)
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);
}
