package com.phuckhang.digital_store.iam.controller;

import com.phuckhang.digital_store.common.dto.response.ApiResponse;
import com.phuckhang.digital_store.iam.dto.request.UserAddressRequestDTO;
import com.phuckhang.digital_store.iam.dto.response.UserAddressResponseDTO;
import com.phuckhang.digital_store.iam.service.UserAddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/addresses")
@RequiredArgsConstructor
public class UserAddressController {
    private final UserAddressService userAddressService;
    @GetMapping
    public ApiResponse<List<UserAddressResponseDTO>> getMyAddresses() {
        return ApiResponse.<List<UserAddressResponseDTO>>builder()
                .result(userAddressService.getMyAddresses())
                .build();
    }

    @PostMapping
    public ApiResponse<UserAddressResponseDTO> createAddress(@RequestBody @Valid UserAddressRequestDTO requestDTO) {
        return ApiResponse.<UserAddressResponseDTO>builder()
                .result(userAddressService.createAddress(requestDTO))
                .build();
    }

    @PutMapping("/{addressId}")
    public ApiResponse<UserAddressResponseDTO> updateAddress(
            @PathVariable Long addressId,
            @RequestBody @Valid UserAddressRequestDTO requestDTO) {
        return ApiResponse.<UserAddressResponseDTO>builder()
                .result(userAddressService.updateAddress(addressId, requestDTO))
                .build();
    }

    @DeleteMapping("/{addressId}")
    public ApiResponse<String> deleteAddress(@PathVariable Long addressId) {
        userAddressService.deleteAddress(addressId);
        return ApiResponse.<String>builder()
                .result("Đã xóa địa chỉ thành công")
                .build();
    }
    @PatchMapping("/{addressId}/default")
    public ApiResponse<String> setDefaultAddress(@PathVariable Long addressId) {
        userAddressService.setDefaultAddress(addressId);
        return ApiResponse.<String>builder()
                .result("Đã thiết lập địa chỉ mặc định")
                .build();
    }
}
