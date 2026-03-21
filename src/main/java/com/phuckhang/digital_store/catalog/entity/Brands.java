package com.phuckhang.digital_store.catalog.entity;

import com.phuckhang.digital_store.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity // Đánh dấu đây là một Thực thể để Hibernate nhận diện
@Table(name = "brands") // Tên bảng sẽ tạo dưới MySQL (luôn để số nhiều)
public class Brands extends BaseEntity {

    @Id // Đánh dấu cột Khóa chính (Primary Key)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tự động tăng (Auto Increment: 1, 2, 3...)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name; // Ví dụ: "Sony", "Apple". Bắt buộc nhập (nullable=false) và Không được trùng nhau (unique=true)

    @Column(name = "description", columnDefinition = "TEXT")
    private String description; // Đoạn văn bản dài mô tả về thương hiệu


}
