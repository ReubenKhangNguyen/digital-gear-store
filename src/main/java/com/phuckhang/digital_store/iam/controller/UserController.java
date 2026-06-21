package com.phuckhang.digital_store.iam.controller;

import com.cloudinary.Api;
import com.phuckhang.digital_store.catalog.dto.response.brand.BrandResponseDTO;
import com.phuckhang.digital_store.common.dto.response.ApiResponse;
import com.phuckhang.digital_store.iam.dto.request.ChangePasswordRequestDTO;
import com.phuckhang.digital_store.iam.dto.request.UserCreateRequestDTO;
import com.phuckhang.digital_store.iam.dto.request.UserUpdateRequestDTO;
import com.phuckhang.digital_store.iam.dto.response.UserResponse;
import com.phuckhang.digital_store.iam.entity.User;
import com.phuckhang.digital_store.iam.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserResponse> createUser(@Valid @RequestBody UserCreateRequestDTO request)
    {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request)).build();
    }

    @GetMapping
    public ApiResponse<List<UserResponse>> getAllUsers()
    {
        var authentication =  SecurityContextHolder.getContext().getAuthentication();
        log.info("UserName : {}", authentication.getName());
        authentication.getAuthorities().forEach(authority -> log.info("Authorities : {}", authority.getAuthority()));


        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .build();
    }

    @GetMapping("/myinfo")
    public ApiResponse<UserResponse> getMyInfo()
    {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }


    @PutMapping("/myinfo")
    public ApiResponse<UserResponse> updateMyInfo(@RequestBody @Valid UserUpdateRequestDTO request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateMyInfo(request))
                .build();
    }

    @PatchMapping("/password")
    public ApiResponse<String> changePassword(@RequestBody @Valid ChangePasswordRequestDTO request) {
        userService.changePassword(request);
        return ApiResponse.<String>builder()
                .result("Đổi mật khẩu thành công. Vui lòng đăng nhập lại với mật khẩu mới!")
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{userId}/status")
    public ApiResponse<String> updateUserStatus(
            @PathVariable String userId,
            @RequestParam boolean isActive) {

        userService.updateUserStatus(userId, isActive);

        String statusText = isActive ? "Mở khóa" : "Khóa";
        return ApiResponse.<String>builder()
                .result("Đã " + statusText + " tài khoản thành công")
                .build();
    }


}
