package com.phuckhang.digital_store.catalog.repository;

import com.phuckhang.digital_store.catalog.entity.Brands;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Đánh dấu đây là Kho chứa dữ liệu
public interface BrandRepository extends JpaRepository<Brands, Long> {
    // JpaRepository<Thực_thể, Kiểu_dữ_liệu_của_Khóa_chính>

    // Ví dụ tính năng thêm: Spring Data JPA cho phép tự định nghĩa hàm bằng cách đặt tên chuẩn
    // Hàm này dùng để kiểm tra xem Tên hãng đã tồn tại chưa (phục vụ Validation)
    boolean existsByName(String name);
}