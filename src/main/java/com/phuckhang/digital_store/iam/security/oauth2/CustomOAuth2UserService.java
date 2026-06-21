package com.phuckhang.digital_store.iam.security.oauth2;


import com.phuckhang.digital_store.iam.entity.User;
import com.phuckhang.digital_store.iam.enums.AuthProvider;
import com.phuckhang.digital_store.iam.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 1. Kích hoạt gọi API sang Google để lấy dữ liệu Khách hàng
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        Optional<User> userOptional = userRepository.findByEmail(email);
        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            // CHỐNG HIJACKING TÀI KHOẢN (ACCOUNT COLLISION)
            if (user.getAuthProvider() != AuthProvider.GOOGLE) {
                // Quăng lỗi của OAuth2 để chặn lại (Lỗi này sẽ văng ra màn hình đăng nhập)
                throw new OAuth2AuthenticationException(new OAuth2Error("invalid_request", "Email này đã được đăng ký bằng mật khẩu truyền thống", ""));
            }
            // Nếu đăng nhập Google lại -> Update tên phòng khi Google họ đổi tên
            user.setFullName(name);
            user = userRepository.save(user);
        } else {
            // ĐĂNG KÝ ẨN TỰ ĐỘNG CHO KHÁCH HÀNG MỚI (AUTO-REGISTER)
            user = User.builder()
                    .email(email)
                    .username(email) // Lấy luôn email làm username
                    .fullName(name)
                    .authProvider(AuthProvider.GOOGLE)
                    .isActive(true)
                    .password(UUID.randomUUID().toString()) // Mật khẩu ảo dài ngoằng
                    .build();
            user = userRepository.save(user);
        }
        // Trả về lớp bọc CustomOAuth2User ở trên
        return new CustomOAuth2User(user, oAuth2User.getAttributes());
    }
}
