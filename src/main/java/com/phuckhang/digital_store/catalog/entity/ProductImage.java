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
@Table(name = "product_images")
@Entity
public class ProductImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "image_url", nullable = false, length = 500)
    String imageUrl; // Đường dẫn URL của bức ảnh

    @Column(name = "is_thumbnail", nullable = false)
    Boolean isThumbnail; // True nếu là ảnh bìa chính, False nếu là ảnh phụ

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    Product product; // Bức ảnh này thuộc về Sản phẩm nào?
}