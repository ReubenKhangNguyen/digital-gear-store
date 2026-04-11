package com.phuckhang.digital_store.catalog.dto.response.brand;

import com.phuckhang.digital_store.catalog.enums.BrandStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrandResponseDTO {
    Long id;

    String name;

    String description;

    String logo;

    BrandStatus status;
}
