package com.phuckhang.digital_store.order.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemResponseDTO {
    Long id; // ID của CartItem (để có thể xóa khỏi giỏ)
    Long productId;
    String productName;
    String thumbnailUrl; // Ảnh đại diện sản phẩm
    BigDecimal price;    // Giá hiện tại
    Integer quantity;    // Số lượng trong giỏ
    Integer availableQuantity;
}
