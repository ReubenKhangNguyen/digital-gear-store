package com.phuckhang.digital_store.catalog.controller;


import com.phuckhang.digital_store.catalog.dto.request.product.ProductCreateRequestDTO;
import com.phuckhang.digital_store.catalog.dto.response.product.ProductDetailResponseDTO;
import com.phuckhang.digital_store.catalog.dto.response.product.ProductListResponseDTO;
import com.phuckhang.digital_store.catalog.service.ProductService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<ProductListResponseDTO>> getAllProducts() {
        List<ProductListResponseDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailResponseDTO> getProductById(@PathVariable Long id) {
        ProductDetailResponseDTO productDetail = productService.getProductById(id);
        return ResponseEntity.ok(productDetail);
    }


    @PostMapping
    public ResponseEntity<ProductDetailResponseDTO> createProduct(
            @Valid @RequestBody ProductCreateRequestDTO requestDTO) {

        ProductDetailResponseDTO newProduct = productService.createProduct(requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }
}