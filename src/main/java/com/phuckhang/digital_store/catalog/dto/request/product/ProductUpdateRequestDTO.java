
package com.phuckhang.digital_store.catalog.dto.request.product;

import com.phuckhang.digital_store.catalog.enums.ProductStatus;
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
public class ProductUpdateRequestDTO {

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

    @NotNull(message = "PRODUCT_STATUS_NULL")
    ProductStatus status;


    Map<String, Object> specifications;
}