package com.phuckhang.digital_store.catalog.controller;


import com.phuckhang.digital_store.catalog.dto.request.brand.BrandCreateRequestDTO;
import com.phuckhang.digital_store.catalog.dto.response.brand.BrandResponseDTO;
import com.phuckhang.digital_store.catalog.service.BrandService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<BrandResponseDTO>> getAllBrands() {

        List<BrandResponseDTO> brandResponseDTOS = brandService.getAllBrands();

        return ResponseEntity.ok(brandResponseDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandResponseDTO> getBrandById(@PathVariable Long id) {

        BrandResponseDTO brandResponseDTO = brandService.getBrandById(id);

        return ResponseEntity.ok(brandResponseDTO);
    }


    @PostMapping
    public ResponseEntity<BrandResponseDTO> createBrand(@Valid @RequestBody BrandCreateRequestDTO brandCreateRequestDTO){

        BrandResponseDTO brandResponseDTO = brandService.createBrand(brandCreateRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(brandResponseDTO);
    }

}
