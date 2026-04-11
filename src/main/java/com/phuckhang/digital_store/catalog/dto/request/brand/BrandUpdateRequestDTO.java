package com.phuckhang.digital_store.catalog.dto.request.brand;

import com.phuckhang.digital_store.catalog.enums.BrandStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrandUpdateRequestDTO {

    @NotBlank(message = "Tên thương hiệu không được để trống")
    String name;

    String logo;
    String description;

    @NotNull(message = "Trạng thái không được để trống khi cập nhật")
    BrandStatus status;
}