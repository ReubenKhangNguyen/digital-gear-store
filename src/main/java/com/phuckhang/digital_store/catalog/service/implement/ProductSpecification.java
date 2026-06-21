package com.phuckhang.digital_store.catalog.service.implement;


import com.phuckhang.digital_store.catalog.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;

public class ProductSpecification {
    // 1. Tìm theo tên (Không phân biệt hoa thường)
    public static Specification<Product> hasNameLike(String keyword) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + keyword.toLowerCase() + "%");
    }

    //    // 2. Tìm theo Danh mục
//    public static Specification<Product> hasCategoryId(Integer categoryId) {
//        return (root, query, criteriaBuilder) ->
//                criteriaBuilder.equal(root.get("category").get("id"), categoryId);
//    }
//
//    // 3. Tìm theo Thương hiệu
//    public static Specification<Product> hasBrandId(Integer brandId) {
//        return (root, query, criteriaBuilder) ->
//                criteriaBuilder.equal(root.get("brand").get("id"), brandId);
//    }

    // 2. Tìm theo Nhiều Danh mục (IN Clause)
    public static Specification<Product> hasCategoryIds(List<Integer> categoryIds) {
        return (root, query, criteriaBuilder) ->
                root.get("category").get("id").in(categoryIds);
    }

    // 3. Tìm theo Nhiều Thương hiệu (IN Clause)
    public static Specification<Product> hasBrandIds(List<Integer> brandIds) {
        return (root, query, criteriaBuilder) ->
                root.get("brand").get("id").in(brandIds);
    }

    // 4. Tìm trong Khoảng giá
    public static Specification<Product> priceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
    }

    public static Specification<Product> priceGreaterThanEqual(BigDecimal minPrice) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<Product> priceLessThanEqual(BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

    // 5. [QUAN TRỌNG] Chốt chặn: Luôn luôn chỉ lấy sản phẩm đang bán (isActive = true)
    public static Specification<Product> isActiveTrue() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), com.phuckhang.digital_store.catalog.enums.ProductStatus.ACTIVE);
    }
}
