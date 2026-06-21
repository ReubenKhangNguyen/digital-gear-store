package com.phuckhang.digital_store.order.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailResponseDTO {
    Long id;
    Long productId;
    String productName;
    Integer quantity;
    BigDecimal priceAtPurchase;
}
