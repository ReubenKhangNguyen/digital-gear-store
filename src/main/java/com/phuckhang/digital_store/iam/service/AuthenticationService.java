package com.phuckhang.digital_store.iam.service;

import com.phuckhang.digital_store.iam.dto.request.AuthenticationRequest;
import com.phuckhang.digital_store.iam.dto.request.IntrospectRequest;
import com.phuckhang.digital_store.iam.dto.response.AuthenticationResponse;
import com.phuckhang.digital_store.iam.dto.response.IntrospectResponse;

public interface AuthenticationService {

    AuthenticationResponse authenticate(AuthenticationRequest request);

    IntrospectResponse introspect(IntrospectRequest request);
}
