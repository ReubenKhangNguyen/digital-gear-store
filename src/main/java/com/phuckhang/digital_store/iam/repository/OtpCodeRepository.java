package com.phuckhang.digital_store.iam.repository;

import com.phuckhang.digital_store.iam.entity.OtpCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpCodeRepository extends JpaRepository<OtpCode, Long> {
    Optional<OtpCode> findByUserIdAndOtpCode(String userId, String otpCode);
}
