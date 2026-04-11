
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

    @NotBlank(message = "Tên sản phẩm không được trống")
    String name;

    @NotBlank(message = "Mã SKU không được trống")
    String sku;

    @NotNull(message = "Giá không được để trống")
    @Min(value = 0, message = "Giá sản phẩm không được âm")
    BigDecimal price;

    @Min(value = 0, message = "Số lượng tồn kho không được âm")
    Integer stockQuantity;

    @NotNull(message = "Thiếu ID Danh mục")
    Long categoryId; // Chỉ cần ID, không cần cả Object Category

    @NotNull(message = "Thiếu ID Thương hiệu")
    Long brandId;

    List<ProductImageDTO> images;

    @NotNull(message = "Trạng thái không được để trống khi cập nhật")
    ProductStatus status;


    Map<String, Object> specifications;
}