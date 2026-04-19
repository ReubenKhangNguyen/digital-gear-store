package com.phuckhang.digital_store.iam.service;

import com.phuckhang.digital_store.iam.dto.request.UserCreateRequestDTO;
import com.phuckhang.digital_store.iam.dto.response.UserResponse;

public interface UserService {

    UserResponse createUser(UserCreateRequestDTO request);
}
