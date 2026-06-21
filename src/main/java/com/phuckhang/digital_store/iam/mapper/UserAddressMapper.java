package com.phuckhang.digital_store.iam.mapper;

import com.phuckhang.digital_store.iam.dto.request.UserAddressRequestDTO;
import com.phuckhang.digital_store.iam.dto.response.UserAddressResponseDTO;
import com.phuckhang.digital_store.iam.entity.UserAddress;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserAddressMapper {

    UserAddress toUserAddress(UserAddressRequestDTO requestDTO);
    UserAddressResponseDTO toUserAddressResponseDTO(UserAddress entity);
    // Ghi đè dữ liệu từ DTO vào Entity có sẵn (Bỏ qua ID và User)
    void updateAddressFromDTO(UserAddressRequestDTO dto, @MappingTarget UserAddress entity);
}
