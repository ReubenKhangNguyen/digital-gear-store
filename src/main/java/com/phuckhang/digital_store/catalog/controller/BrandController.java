package com.phuckhang.digital_store.catalog.controller;

import com.phuckhang.digital_store.catalog.dto.request.brand.BrandCreateRequestDTO;
import com.phuckhang.digital_store.catalog.dto.response.brand.BrandResponseDTO;
import com.phuckhang.digital_store.catalog.service.BrandService;
import com.phuckhang.digital_store.common.dto.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brands")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandController {

    BrandService brandService;

    @GetMapping
    public ApiResponse<List<BrandResponseDTO>> getAllBrands() {
        return ApiResponse.<List<BrandResponseDTO>>builder()
                .result(brandService.getAllBrands())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<BrandResponseDTO> getBrandById(@PathVariable Long id) {
        return ApiResponse.<BrandResponseDTO>builder()
                .result(brandService.getBrandById(id))
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Ép mã HTTP trả về là 201 Created
    public ApiResponse<BrandResponseDTO> createBrand(@Valid @RequestBody BrandCreateRequestDTO brandCreateRequestDTO){
        return ApiResponse.<BrandResponseDTO>builder()
                .message("Tạo thương hiệu thành công")
                .result(brandService.createBrand(brandCreateRequestDTO))
                .build();
    }
}