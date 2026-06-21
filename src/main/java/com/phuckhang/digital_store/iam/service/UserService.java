package com.phuckhang.digital_store.iam.service;

import com.phuckhang.digital_store.iam.dto.request.ChangePasswordRequestDTO;
import com.phuckhang.digital_store.iam.dto.request.UserCreateRequestDTO;
import com.phuckhang.digital_store.iam.dto.request.UserUpdateRequestDTO;
import com.phuckhang.digital_store.iam.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse createUser(UserCreateRequestDTO request);

    List<UserResponse> getUsers();

    UserResponse getMyInfo();

    void deleteUser(String id);

    UserResponse updateMyInfo(UserUpdateRequestDTO request);

    void changePassword(ChangePasswordRequestDTO request);

    void updateUserStatus(String userId, boolean isActive);
}
