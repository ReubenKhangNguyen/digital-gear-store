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

    @NotBlank(message = "Tên hãng không được để trống")
    @Size(max = 100, message = "Tên hãng không được vượt quá 100 ký tự")
    String name;
    String description;
    String logo;

}
