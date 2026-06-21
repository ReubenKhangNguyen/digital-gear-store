package com.phuckhang.digital_store.order.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartResponseDTO {
    List<CartItemResponseDTO> cartItems;
    Integer totalQuantity; // Tổng số món hàng trong giỏ (dùng để hiện số lên icon giỏ hàng UI)
    BigDecimal totalPrice;
}
