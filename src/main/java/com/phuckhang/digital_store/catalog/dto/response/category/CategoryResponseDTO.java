package com.phuckhang.digital_store.catalog.dto.response.category;

import com.phuckhang.digital_store.catalog.enums.CategoryStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryResponseDTO {

    Long id; // Trả về ID để Frontend làm key hoặc link

    String name;

    String description;

    Long parentId;

    CategoryStatus categoryStatus;

    List<CategoryResponseDTO> categoryChild;

    // Ở đây mình cố tình giấu đi createdAt, updatedAt, createdBy vì khách hàng xem danh mục không cần biết ai tạo nó vào lúc nào (Tránh Data Leakage)
}