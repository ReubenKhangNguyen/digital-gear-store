package com.phuckhang.digital_store.iam.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAddressResponseDTO {
    Long id;
    String receiverName;
    String receiverPhone;
    Integer provinceId;
    Integer districtId;
    String wardCode;
    String provinceName;
    String districtName;
    String wardName;
    String specificAddress;
    Boolean isDefault;
}
