package com.phuckhang.digital_store.order.entity;


import com.phuckhang.digital_store.common.entity.BaseEntity;
import com.phuckhang.digital_store.iam.entity.User;
import com.phuckhang.digital_store.order.enums.OrderStatus;
import com.phuckhang.digital_store.order.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "order_code", nullable = false, unique = true, length = 50)
    String orderCode;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    // ---SNAPSHOT: CHỤP ĐỊA CHỈ NHẬN HÀNG LÚC CHỐT ĐƠN ---
    @Column(name = "receiver_name", nullable = false, length = 100)
    String receiverName;
    @Column(name = "receiver_phone", nullable = false, length = 20)
    String receiverPhone;
    @Column(name = "shipping_address", nullable = false, length = 500)
    String shippingAddress; // Chuỗi in hóa đơn: "Số nhà, Phường, Quận, Tỉnh"
    @Column(name = "to_district_id", nullable = false)
    Integer toDistrictId; // Nối API tính phí GHN
    @Column(name = "to_ward_code", nullable = false, length = 20)
    String toWardCode; // Nối API tính phí GHN

    // -----------------------------------------------------------
    @Column(name = "total_amount", nullable = false)
    BigDecimal totalAmount;
    @Column(name = "shipping_fee", nullable = false)
    BigDecimal shippingFee;
    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false, length = 30)
    OrderStatus orderStatus;
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, length = 30)
    PaymentStatus paymentStatus;
    @Column(name = "payment_method", nullable = false, length = 50)
    String paymentMethod; // "COD", "VNPAY"
    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderDetail> orderDetails = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderHistory> orderHistories = new ArrayList<>();

    @Column(length = 500)
    String customerNote;
}

