package com.phuckhang.digital_store.iam.service.implement;

import com.phuckhang.digital_store.common.exception.AppException;
import com.phuckhang.digital_store.common.exception.ErrorCode;
import com.phuckhang.digital_store.iam.dto.request.UserCreateRequestDTO;
import com.phuckhang.digital_store.iam.dto.response.UserResponse;
import com.phuckhang.digital_store.iam.entity.User;
import com.phuckhang.digital_store.iam.mapper.UserMapper;
import com.phuckhang.digital_store.iam.repository.UserRepository;
import com.phuckhang.digital_store.iam.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceIpml implements UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public UserResponse createUser(UserCreateRequestDTO request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = userMapper.toUser(request);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setIsActive(true);

        return userMapper.toUserResponse(userRepository.save(user));
    }
}
