package com.phuckhang.digital_store.iam.dto.request;

import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.FieldDefaults;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateRequestDTO {
    @NotBlank(message = "INVALID_USERNAME")
    @Size(min = 4, message = "INVALID_USERNAME")
    String username;

    @NotBlank(message = "INVALID_PASSWORD")
    @Size(min = 6, message = "INVALID_PASSWORD")
    String password;

    @NotBlank(message = "INVALID_FULLNAME")
    String fullName;

    @Email(message = "INVALID_EMAIL")
    String email;

    String phone;

    String avatar;

}
