package com.phuckhang.digital_store.catalog.controller;


import com.phuckhang.digital_store.catalog.dto.request.product.ProductCreateRequestDTO;
import com.phuckhang.digital_store.catalog.dto.response.product.ProductDetailResponseDTO;
import com.phuckhang.digital_store.catalog.dto.response.product.ProductListResponseDTO;
import com.phuckhang.digital_store.catalog.service.ProductService;
import com.phuckhang.digital_store.common.dto.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import com.phuckhang.digital_store.catalog.dto.request.product.ProductUpdateRequestDTO;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {

    ProductService productService;

    @GetMapping
    public ApiResponse<List<ProductListResponseDTO>> getAllProducts() {
        return ApiResponse.<List<ProductListResponseDTO>>builder()
                .result(productService.getAllProducts())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductDetailResponseDTO> getProductById(@PathVariable Long id) {
        return ApiResponse.<ProductDetailResponseDTO>builder()
                .result(productService.getProductById(id))
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // HTTP 201 Created
    public ApiResponse<ProductDetailResponseDTO> createProduct(
            @Valid @RequestBody ProductCreateRequestDTO requestDTO) {
        return ApiResponse.<ProductDetailResponseDTO>builder()
                .message("Tạo sản phẩm thành công")
                .result(productService.createProduct(requestDTO))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductDetailResponseDTO> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductUpdateRequestDTO requestDTO) {
        return ApiResponse.<ProductDetailResponseDTO>builder()
                .message("Cập nhật sản phẩm thành công")
                .result(productService.updateProduct(id, requestDTO))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteProductById(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ApiResponse.<String>builder()
                .message("Xóa sản phẩm thành công")
                .build();
    }


    @GetMapping("/search")
    public ApiResponse<Page<ProductListResponseDTO>> searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) List<Integer> categoryIds,
            @RequestParam(required = false) List<Integer> brandIds,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        // --- CẤU HÌNH BẢO MẬT: WHITELIST SẮP XẾP CHỐNG SQL INJECTION LỖ HỔNG (Áp dụng đúng đề xuất của bạn) ---
        List<String> allowedSortFields = Arrays.asList("createdAt", "price", "name");
        if (!allowedSortFields.contains(sortBy)) {
            sortBy = "createdAt"; // Nếu truyền bậy bạ (vd: sortBy=password) -> Trả về mặc định
        }
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ApiResponse.<Page<ProductListResponseDTO>>builder()
                .result(productService.searchProducts(keyword, categoryIds, brandIds, minPrice, maxPrice, pageable))
                .build();
    }

}