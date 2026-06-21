package com.phuckhang.digital_store.order.entity;


import com.phuckhang.digital_store.common.entity.BaseEntity;
import com.phuckhang.digital_store.order.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "order_histories")
public class OrderHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    Order order;
    @Enumerated(EnumType.STRING)
    @Column(name = "old_status")
    OrderStatus oldStatus;
    @Enumerated(EnumType.STRING)
    @Column(name = "new_status", nullable = false)
    OrderStatus newStatus;
    @Column(name = "changed_by", nullable = false)
    String changedBy;
    @Column(length = 500)
    String note; // VD: "Khách hủy do đặt nhầm"
}
