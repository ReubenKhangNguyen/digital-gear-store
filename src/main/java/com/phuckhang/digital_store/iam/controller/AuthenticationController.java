package com.phuckhang.digital_store.iam.controller;

import com.nimbusds.jose.JOSEException;
import com.phuckhang.digital_store.common.dto.response.ApiResponse;
import com.phuckhang.digital_store.iam.dto.request.AuthenticationRequest;
import com.phuckhang.digital_store.iam.dto.request.ForgotPasswordRequestDTO;
import com.phuckhang.digital_store.iam.dto.request.IntrospectRequest;
import com.phuckhang.digital_store.iam.dto.request.ResetPasswordRequestDTO;
import com.phuckhang.digital_store.iam.dto.response.AuthenticationResponse;
import com.phuckhang.digital_store.iam.dto.response.IntrospectResponse;
import com.phuckhang.digital_store.iam.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
    @RequestMapping("/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        return ApiResponse.<AuthenticationResponse>builder()
                .result(authenticationService.authenticate(request))
                .build();
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request){
        return ApiResponse.<IntrospectResponse>builder()
                .result(authenticationService.introspect(request))
                .build();
    }

    @PostMapping("/forgot-password")
    public ApiResponse<String> forgotPassword(@RequestBody @Valid ForgotPasswordRequestDTO request) {
        authenticationService.forgotPassword(request);
        return ApiResponse.<String>builder()
                .result("Mã OTP đã được gửi vào email của bạn!")
                .build();
    }
    @PostMapping("/reset-password")
    public ApiResponse<String> resetPassword(@RequestBody @Valid ResetPasswordRequestDTO request) {
        authenticationService.resetPassword(request);
        return ApiResponse.<String>builder()
                .result("Đổi mật khẩu thành công!")
                .build();
    }
}
