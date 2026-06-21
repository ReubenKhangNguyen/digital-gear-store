package com.phuckhang.digital_store.common.configuration;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorAware")
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // Lấy thông tin người đang đăng nhập từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Nếu không có ai đăng nhập, hoặc là luồng ẩn danh -> Trả về "SYSTEM"
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return Optional.of("SYSTEM"); // Hoặc Optional.empty()
        }

        // Nếu có đăng nhập, trả về Username
        return Optional.of(authentication.getName());
    }
}
