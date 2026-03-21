package com.phuckhang.digital_store.catalog.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCreateRequestDTO {

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

    // Đây là "thức ăn" cho thuật toán Gợi ý (Recommendation) của bạn sau này!
    // Frontend sẽ gửi lên cục JSON: {"ram": "16GB", "lens_mount": "E-mount"}
    Map<String, Object> specifications;
}