package com.phuckhang.digital_store.iam.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordRequestDTO {
    @NotBlank(message = "Vui lòng nhập mật khẩu cũ")
    String oldPassword;
    @Size(min = 6, message = "INVALID_PASSWORD") // Độ dài tối thiểu tuỳ bạn quy định
    String newPassword;
}
