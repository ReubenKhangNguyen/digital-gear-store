package com.phuckhang.digital_store.iam.mapper;

import com.phuckhang.digital_store.iam.dto.request.UserCreateRequestDTO;
import com.phuckhang.digital_store.iam.dto.response.UserResponse;
import com.phuckhang.digital_store.iam.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreateRequestDTO requestDTO);

    UserResponse toUserResponse(User user);

    void updateUser(@MappingTarget User user, UserCreateRequestDTO requestDTO);
}
