package com.phuckhang.digital_store.iam.service;

import com.phuckhang.digital_store.iam.dto.request.AuthenticationRequest;
import com.phuckhang.digital_store.iam.dto.request.ForgotPasswordRequestDTO;
import com.phuckhang.digital_store.iam.dto.request.IntrospectRequest;
import com.phuckhang.digital_store.iam.dto.request.ResetPasswordRequestDTO;
import com.phuckhang.digital_store.iam.dto.response.AuthenticationResponse;
import com.phuckhang.digital_store.iam.dto.response.IntrospectResponse;
import com.phuckhang.digital_store.iam.entity.User;

public interface AuthenticationService {

    AuthenticationResponse authenticate(AuthenticationRequest request);

    IntrospectResponse introspect(IntrospectRequest request);

    void forgotPassword(ForgotPasswordRequestDTO request);

    void resetPassword(ResetPasswordRequestDTO request);

    String generateToken(User user);
}
