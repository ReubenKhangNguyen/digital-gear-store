package com.phuckhang.digital_store.catalog.dto.response.product;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductImageResponseDTO {

    Long id;

    String imageUrl;

    Boolean isThumbnail;
}
