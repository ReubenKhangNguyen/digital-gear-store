package com.phuckhang.digital_store.catalog.entity;

import com.phuckhang.digital_store.catalog.enums.ProductStatus;
import com.phuckhang.digital_store.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "products")
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, length = 255)
    String name;

    @Column(nullable = false, unique = true, length = 50)
    String sku; // Mã vạch quản lý kho

    @Column(nullable = false)
    BigDecimal price; // Dùng BigDecimal cho tiền tệ để không bị sai số thập phân

    @Column(name = "stock_quantity", nullable = false)
    Integer stockQuantity; // Số lượng tồn kho

    @Enumerated(EnumType.STRING) // Lưu chữ "ACTIVE" xuống DB thay vì lưu số 0, 1
    @Column(nullable = false, length = 20)
    ProductStatus status;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ProductImage> productImages = new ArrayList<>();

    // --- THIẾT LẬP KHÓA NGOẠI (FOREIGN KEYS) ---

    @ManyToOne(fetch = FetchType.LAZY) // Lazy: Khi nào cần mới query lấy Brand lên, giúp tăng tốc độ
    @JoinColumn(name = "brand_id", nullable = false) // Tên cột khóa ngoại dưới DB MySQL
    Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    Category category;

    // --- THÔNG SỐ KỸ THUẬT ĐỘNG (DYNAMIC SPECS) ---

    @JdbcTypeCode(SqlTypes.JSON) // "Phép thuật" của Hibernate 6 biến Map Java thành JSON MySQL
    @Column(name = "specifications", columnDefinition = "json")
    @ToString.Exclude
    Map<String, Object> specifications;
}