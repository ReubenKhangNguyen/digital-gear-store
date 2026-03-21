package com.phuckhang.digital_store.catalog.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

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

    // Ở đây mình cố tình giấu đi createdAt, updatedAt, createdBy vì khách hàng xem danh mục không cần biết ai tạo nó vào lúc nào (Tránh Data Leakage)
}