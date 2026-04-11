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
    @NotBlank(message = "Tên danh mục không được để trống")
    @Size(max = 100, message = "Tên danh mục không được vượt quá 100 ký tự")
    String name;

    String description; // Mô tả có thể để trống nên không cần Validation

    Long parentId;
}