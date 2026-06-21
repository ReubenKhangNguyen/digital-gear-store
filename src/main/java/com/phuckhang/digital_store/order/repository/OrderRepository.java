package com.phuckhang.digital_store.order.repository;

import com.phuckhang.digital_store.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    List<Order> findByUserIdOrderByCreatedAtDesc(String userId);
}
