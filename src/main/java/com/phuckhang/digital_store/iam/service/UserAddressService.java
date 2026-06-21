package com.phuckhang.digital_store.iam.service;

import com.phuckhang.digital_store.iam.dto.request.UserAddressRequestDTO;
import com.phuckhang.digital_store.iam.dto.response.UserAddressResponseDTO;

import java.util.List;

public interface UserAddressService {
    List<UserAddressResponseDTO> getMyAddresses();

    UserAddressResponseDTO createAddress(UserAddressRequestDTO requestDTO);

    UserAddressResponseDTO updateAddress(Long addressId, UserAddressRequestDTO requestDTO);

    void deleteAddress(Long addressId);

    void setDefaultAddress(Long addressId);
}