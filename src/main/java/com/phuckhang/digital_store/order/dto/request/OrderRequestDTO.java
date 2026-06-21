package com.phuckhang.digital_store.order.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequestDTO {
    @NotNull(message = "ADDRESS_ID_REQUIRED")
    Long addressId;
    @NotBlank(message = "PAYMENT_METHOD_REQUIRED")
    String paymentMethod;
    String customerNote;
}
