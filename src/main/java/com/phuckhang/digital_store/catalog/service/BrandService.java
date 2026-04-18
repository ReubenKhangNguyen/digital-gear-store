package com.phuckhang.digital_store.catalog.service;

import com.phuckhang.digital_store.catalog.dto.request.brand.BrandCreateRequestDTO;
import com.phuckhang.digital_store.catalog.dto.request.brand.BrandUpdateRequestDTO;
import com.phuckhang.digital_store.catalog.dto.response.brand.BrandResponseDTO;

import java.util.List;

public interface BrandService {

    BrandResponseDTO createBrand(BrandCreateRequestDTO requestDTO);

    List<BrandResponseDTO> getAllBrands();

    BrandResponseDTO getBrandById(Long id);

    BrandResponseDTO updateBrand(Long id, BrandUpdateRequestDTO requestDTO);

    void deleteBrand(Long id); // Xóa mềm
}