package com.phuckhang.digital_store.iam.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordRequestDTO {
    @NotBlank(message = "Email không được để trống")
    String email;
    @NotBlank(message = "Mã OTP không được để trống")
    String otpCode;
    @Size(min = 6, message = "Mật khẩu mới phải từ 6 ký tự")
    String newPassword;
}
