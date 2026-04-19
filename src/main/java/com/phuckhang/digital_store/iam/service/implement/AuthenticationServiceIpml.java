package com.phuckhang.digital_store.iam.service.implement;


import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.phuckhang.digital_store.common.exception.AppException;
import com.phuckhang.digital_store.common.exception.ErrorCode;
import com.phuckhang.digital_store.iam.dto.request.AuthenticationRequest;
import com.phuckhang.digital_store.iam.dto.request.IntrospectRequest;
import com.phuckhang.digital_store.iam.dto.response.AuthenticationResponse;
import com.phuckhang.digital_store.iam.dto.response.IntrospectResponse;
import com.phuckhang.digital_store.iam.entity.User;
import com.phuckhang.digital_store.iam.repository.UserRepository;
import com.phuckhang.digital_store.iam.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceIpml implements AuthenticationService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        String token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest request) {

        String token = request.getToken();
        boolean isValid = true;

        try {
            JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

            SignedJWT signedJWT = SignedJWT.parse(token);

            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

            boolean verified = signedJWT.verify(verifier);

            isValid = verified && expirationTime.after(new Date());

        } catch (Exception e) {
            isValid = false;
        }

        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }


    private String generateToken(User user)
    {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("digital-store.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader,payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Không thể tạo Token", e);
            throw new RuntimeException(e);
        }
    }
}
