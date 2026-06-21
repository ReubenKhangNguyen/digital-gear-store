package com.phuckhang.digital_store.iam.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequestDTO {

    @NotBlank(message = "Vui lòng nhập họ và tên") // Bạn có thể chuyển thành Error Code nếu có file Validator riêng
    String fullName;
    String phone;
}