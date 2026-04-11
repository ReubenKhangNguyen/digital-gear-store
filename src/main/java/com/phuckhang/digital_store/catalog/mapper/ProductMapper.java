package com.phuckhang.digital_store.catalog.mapper;

import com.phuckhang.digital_store.catalog.dto.request.product.ProductCreateRequestDTO;
import com.phuckhang.digital_store.catalog.dto.request.product.ProductUpdateRequestDTO;
import com.phuckhang.digital_store.catalog.dto.response.product.ProductDetailResponseDTO;
import com.phuckhang.digital_store.catalog.dto.response.product.ProductImageResponseDTO;
import com.phuckhang.digital_store.catalog.dto.response.product.ProductListResponseDTO;
import com.phuckhang.digital_store.catalog.entity.Product;
import com.phuckhang.digital_store.catalog.entity.ProductImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "categoryId", target = "category.id")
    @Mapping(source = "brandId", target = "brand.id")
    @Mapping(target = "productImages", ignore = true)
    Product toEntity(ProductCreateRequestDTO dto);


    @Mapping(source = "categoryId", target = "category.id")
    @Mapping(source = "brandId", target = "brand.id")
    @Mapping(target = "productImages", ignore = true)
    void updateProduct(@MappingTarget Product product, ProductUpdateRequestDTO dto);


    @Mapping(source = "productImages", target = "images")
    ProductDetailResponseDTO toDetailResponseDTO(Product product);


    ProductImageResponseDTO toImageResponseDTO(ProductImage image);


    @Mapping(target = "thumbnailUrl", source = "productImages", qualifiedByName = "mapThumbnailUrl")
    ProductListResponseDTO toListResponseDTO(Product product);


    @Named("mapThumbnailUrl")
    default String mapThumbnailUrl(List<ProductImage> images) {
        if (images == null || images.isEmpty()) return null;

        return images.stream()
                .filter(ProductImage::getIsThumbnail)
                .map(ProductImage::getImageUrl)
                .findFirst()
                .orElse(null);
    }
}