package com.phuckhang.digital_store.catalog.mapper;

import com.phuckhang.digital_store.catalog.dto.request.brand.BrandCreateRequestDTO;
import com.phuckhang.digital_store.catalog.dto.request.brand.BrandUpdateRequestDTO;
import com.phuckhang.digital_store.catalog.dto.response.brand.BrandResponseDTO;
import com.phuckhang.digital_store.catalog.entity.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper (componentModel = "spring")
public interface BrandMapper {

    Brand toEntity(BrandCreateRequestDTO brandCreateRequestDTO);

    BrandResponseDTO toBrandResponseDTO(Brand brand);

    void updateBrand(BrandUpdateRequestDTO brandUpdateRequestDTO, @MappingTarget Brand brand);
}
