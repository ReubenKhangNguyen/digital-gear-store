package com.phuckhang.digital_store.order.entity;


import com.phuckhang.digital_store.catalog.entity.Product;
import com.phuckhang.digital_store.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.math.BigDecimal;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "order_details")
public class OrderDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    Order order;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    Product product;
    @Column(nullable = false)
    Integer quantity;
    // snapshot
    @Column (name = "product_name", nullable = false)
    String productName;
    // RẤT QUAN TRỌNG: Giá của sản phẩm chốt ngay tại thời điểm mua
    @Column(name = "price_at_purchase", nullable = false)
    BigDecimal priceAtPurchase;
}