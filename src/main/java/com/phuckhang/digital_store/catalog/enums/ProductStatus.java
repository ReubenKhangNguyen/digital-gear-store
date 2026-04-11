package com.phuckhang.digital_store.catalog.enums;

public enum ProductStatus {
    ACTIVE,
    OUT_OF_STOCK,    // Tạm thời hết hàng (vẫn hiển thị nhưng không cho mua)
    DISCONTINUED     // Ngừng kinh doanh (Xóa mềm - Ẩn khỏi hệ thống)
}
