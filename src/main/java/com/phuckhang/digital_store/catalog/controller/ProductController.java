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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

//    Test thôi chưa làm các nghiệp vụ nặng
//    @DeleteMapping("/{id}")
//    public ApiResponse<String> deleteProductById(@PathVariable Long id) {
//        productService.deleteProduct(id);
//        return ApiResponse.<String>builder()
//                .message("Xóa sản phẩm thành công")
//                .build();
//    }

}