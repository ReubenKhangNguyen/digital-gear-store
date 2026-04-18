package com.phuckhang.digital_store.catalog.dto.request.brand;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrandCreateRequestDTO {

    @NotBlank(message = "BRAND_NAME_BLANK")
    @Size(max = 100, message = "BRAND_NAME_TOO_LONG")
    String name;
    String description;
    String logo;

}
