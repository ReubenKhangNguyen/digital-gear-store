package com.phuckhang.digital_store.catalog.dto.request.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class ProductCreateRequestDTO {

    @NotBlank(message = "PRODUCT_NAME_BLANK")
    String name;

    @NotBlank(message = "PRODUCT_SKU_BLANK")
    String sku;

    @NotNull(message = "PRODUCT_PRICE_NULL")
    @Min(value = 0, message = "PRODUCT_PRICE_NEGATIVE")
    BigDecimal price;

    @Min(value = 0, message = "PRODUCT_STOCK_NEGATIVE")
    Integer stockQuantity;

    @NotNull(message = "PRODUCT_CATEGORY_ID_NULL")
    Long categoryId; // Chỉ cần ID, không cần cả Object Category

    @NotNull(message = "PRODUCT_BRAND_ID_NULL")
    Long brandId;

    List<ProductImageDTO> images;


    // Đây là "thức ăn" cho thuật toán Gợi ý (Recommendation) của bạn sau này!
    // Frontend sẽ gửi lên cục JSON: {"ram": "16GB", "lens_mount": "E-mount"}
    Map<String, Object> specifications;
}