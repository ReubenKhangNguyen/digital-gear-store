package com.phuckhang.digital_store.iam.service.implement;

import com.phuckhang.digital_store.catalog.entity.Brand;
import com.phuckhang.digital_store.common.exception.AppException;
import com.phuckhang.digital_store.common.exception.ErrorCode;
import com.phuckhang.digital_store.iam.dto.request.ChangePasswordRequestDTO;
import com.phuckhang.digital_store.iam.dto.request.UserCreateRequestDTO;
import com.phuckhang.digital_store.iam.dto.request.UserUpdateRequestDTO;
import com.phuckhang.digital_store.iam.dto.response.UserResponse;
import com.phuckhang.digital_store.iam.entity.User;
import com.phuckhang.digital_store.iam.enums.AuthProvider;
import com.phuckhang.digital_store.iam.enums.Role;
import com.phuckhang.digital_store.iam.mapper.UserMapper;
import com.phuckhang.digital_store.iam.repository.UserRepository;
import com.phuckhang.digital_store.iam.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceIpml implements UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void updateUserStatus(String userId, boolean isActive) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        user.setIsActive(isActive);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequestDTO request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        // Kiểm tra xem Mật khẩu cũ nhập vào có khớp với mật khẩu trong DB không
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            // Nhớ tạo mã lỗi PASSWORD_NOT_MATCH trong file ErrorCode.java nhé
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
        }
        // Mã hóa mật khẩu mới và lưu lại
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserResponse updateMyInfo(UserUpdateRequestDTO request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        // Chỉ cho phép cập nhật 2 trường này (Không cho chạm vào Roles hay Username)
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponse createUser(UserCreateRequestDTO request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = userMapper.toUser(request);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setIsActive(true);

        HashSet<Role> roles = new HashSet<>();
        roles.add(Role.CUSTOMER);
        user.setRoles(roles);
        user.setAuthProvider(AuthProvider.LOCAL);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse getMyInfo() {
        var context =  SecurityContextHolder.getContext();

        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return userMapper.toUserResponse(user);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<UserResponse> getUsers() {

        List<User> allUsers = userRepository.findAll();

        return allUsers.stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
