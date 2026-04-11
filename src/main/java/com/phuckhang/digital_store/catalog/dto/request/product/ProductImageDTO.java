package com.phuckhang.digital_store.catalog.dto.request.product;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductImageDTO {

    Boolean isThumbnail;

    String imageUrl;

}
