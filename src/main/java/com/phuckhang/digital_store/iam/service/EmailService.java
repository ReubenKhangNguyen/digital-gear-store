package com.phuckhang.digital_store.iam.service;

public interface EmailService {

    void sendOtpEmail(String toEmail, String otpCode);
}
