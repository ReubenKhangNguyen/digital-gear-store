package com.phuckhang.digital_store.catalog.service;

import com.phuckhang.digital_store.catalog.dto.request.product.ProductCreateRequestDTO;
import com.phuckhang.digital_store.catalog.dto.request.product.ProductUpdateRequestDTO;
import com.phuckhang.digital_store.catalog.dto.response.product.ProductDetailResponseDTO;
import com.phuckhang.digital_store.catalog.dto.response.product.ProductListResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    ProductDetailResponseDTO createProduct(ProductCreateRequestDTO requestDTO);

    ProductDetailResponseDTO getProductById(Long id);

    List<ProductListResponseDTO> getAllProducts();

    ProductDetailResponseDTO updateProduct(Long id, ProductUpdateRequestDTO requestDTO);

    String deleteProduct(Long id);

    Page<ProductListResponseDTO> searchProducts(String keyword, List<Integer> categoryIds, List<Integer> brandIds, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);}