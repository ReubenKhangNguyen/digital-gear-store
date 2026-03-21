package com.phuckhang.digital_store.catalog.dto.response;

import com.phuckhang.digital_store.catalog.enums.ProductStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDetailResponseDTO {
    Long id;
    String name;
    String sku;
    BigDecimal price;
    Integer stockQuantity;
    ProductStatus status;

    // Lồng DTO vào trong DTO: Thay vì trả về category_id = 1,
    // ta trả về {"id": 1, "name": "Máy ảnh", "description": "..."}
    CategoryResponseDTO category;
    BrandResponseDTO brand;

    // Trả về danh sách các link ảnh để Frontend làm hiệu ứng lướt qua lại
    List<String> imageUrls;

    Map<String, Object> specifications; // Cục JSON thông số chi tiết
}