package com.phuckhang.digital_store.order.dto.response;


import com.phuckhang.digital_store.order.enums.OrderStatus;
import com.phuckhang.digital_store.order.enums.PaymentStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponseDTO {
    Long id;
    String orderCode;
    String receiverName;
    String receiverPhone;
    String shippingAddress;
    BigDecimal totalAmount;
    BigDecimal shippingFee;
    OrderStatus orderStatus;
    PaymentStatus paymentStatus;
    String paymentMethod;
    List<OrderDetailResponseDTO> orderDetails;
}
