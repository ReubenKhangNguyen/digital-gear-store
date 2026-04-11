package com.phuckhang.digital_store.catalog.entity;

import com.phuckhang.digital_store.catalog.enums.BrandStatus;
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
@Entity
@Table(name = "brands") // Bảng dưới DB là số nhiều
public class Brand extends BaseEntity { // Tên Class là số ít

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    String name;

    @Column(name = "logo")
    String logo; // Bổ sung cột lưu link ảnh logo

    @Column(name = "description", columnDefinition = "TEXT")
    String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    BrandStatus status; // Đổi thành status cho gọn
}