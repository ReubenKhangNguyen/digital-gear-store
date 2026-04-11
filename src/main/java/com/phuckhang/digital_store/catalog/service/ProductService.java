package com.phuckhang.digital_store.catalog.service;

import com.phuckhang.digital_store.catalog.dto.request.product.ProductCreateRequestDTO;
import com.phuckhang.digital_store.catalog.dto.response.product.ProductDetailResponseDTO;
import com.phuckhang.digital_store.catalog.dto.response.product.ProductListResponseDTO;

import java.util.List;

public interface ProductService {
    ProductDetailResponseDTO createProduct(ProductCreateRequestDTO requestDTO);

    ProductDetailResponseDTO getProductById(Long id);

    List<ProductListResponseDTO> getAllProducts();
}