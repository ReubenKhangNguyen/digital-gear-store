package com.phuckhang.digital_store.order.mapper;


import com.phuckhang.digital_store.catalog.entity.ProductImage;
import com.phuckhang.digital_store.order.dto.response.CartItemResponseDTO;
import com.phuckhang.digital_store.order.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import java.util.List;
@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "price", source = "product.price")
    @Mapping(target = "thumbnailUrl", source = "product.productImages", qualifiedByName = "mapCartThumbnailUrl")
    CartItemResponseDTO toCartItemResponseDTO(CartItem cartItem);
    @Named("mapCartThumbnailUrl")
    default String mapCartThumbnailUrl(List<ProductImage> images) {
        if (images == null || images.isEmpty()) return null;
        return images.stream()
                .filter(ProductImage::getIsThumbnail)
                .map(ProductImage::getImageUrl)
                .findFirst()
                .orElse(null);
    }
}
