package com.phuckhang.digital_store.iam.controller;

import com.nimbusds.jose.JOSEException;
import com.phuckhang.digital_store.common.dto.response.ApiResponse;
import com.phuckhang.digital_store.iam.dto.request.AuthenticationRequest;
import com.phuckhang.digital_store.iam.dto.request.IntrospectRequest;
import com.phuckhang.digital_store.iam.dto.response.AuthenticationResponse;
import com.phuckhang.digital_store.iam.dto.response.IntrospectResponse;
import com.phuckhang.digital_store.iam.service.AuthenticationService;
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

    @PostMapping("/token")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        return ApiResponse.<AuthenticationResponse>builder()
                .result(authenticationService.authenticate(request))
                .build();
    }

    @PostMapping("/login")
    public ApiResponse<IntrospectResponse> login(@RequestBody IntrospectRequest request){
        return ApiResponse.<IntrospectResponse>builder()
                .result(authenticationService.introspect(request))
                .build();
    }
}
