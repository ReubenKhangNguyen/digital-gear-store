package com.phuckhang.digital_store.iam.controller;

import com.phuckhang.digital_store.common.dto.response.ApiResponse;
import com.phuckhang.digital_store.iam.dto.request.UserCreateRequestDTO;
import com.phuckhang.digital_store.iam.dto.response.UserResponse;
import com.phuckhang.digital_store.iam.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
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

}
