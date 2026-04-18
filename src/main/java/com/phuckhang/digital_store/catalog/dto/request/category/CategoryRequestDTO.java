package com.phuckhang.digital_store.catalog.dto.request.category;

import lombok.*;
import lombok.experimental.FieldDefaults;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryRequestDTO {

    // Lớp giáp Validation đầu tiên: Không cho phép Admin để trống tên danh mục
    @NotBlank(message = "CATEGORY_NAME_BLANK")
    @Size(max = 100, message = "CATEGORY_NAME_TOO_LONG")
    String name;

    String description; // Mô tả có thể để trống nên không cần Validation

    Long parentId;
}