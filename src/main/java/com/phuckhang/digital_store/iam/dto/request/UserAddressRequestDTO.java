package com.phuckhang.digital_store.iam.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAddressRequestDTO {
    @NotBlank(message = "RECEIVER_NAME_REQUIRED")
    String receiverName;
    @NotBlank(message = "RECEIVER_PHONE_REQUIRED")
    String receiverPhone;
    @NotNull(message = "PROVINCE_REQUIRED")
    Integer provinceId;
    @NotNull(message = "DISTRICT_REQUIRED")
    Integer districtId;
    @NotBlank(message = "WARD_REQUIRED")
    String wardCode;
    @NotBlank(message = "PROVINCE_NAME_REQUIRED")
    String provinceName;
    @NotBlank(message = "DISTRICT_NAME_REQUIRED")
    String districtName;
    @NotBlank(message = "WARD_NAME_REQUIRED")
    String wardName;
    @NotBlank(message = "SPECIFIC_ADDRESS_REQUIRED")
    String specificAddress;
    Boolean isDefault; // Nếu Frontend không gửi, mặc định sẽ gán dưới Service
}
