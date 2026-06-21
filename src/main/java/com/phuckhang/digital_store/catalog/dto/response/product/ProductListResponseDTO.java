package com.phuckhang.digital_store.catalog.dto.response.product;

import com.phuckhang.digital_store.catalog.enums.ProductStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductListResponseDTO {
    Long id;
    String name;
    BigDecimal price;

    String categoryName;
    Integer stockQuantity;
    ProductStatus status;

    // Ở trang chủ, ta chỉ cần 1 cái ảnh bìa (Thumbnail) duy nhất, không cần cả List ảnh
    String thumbnailUrl;
}