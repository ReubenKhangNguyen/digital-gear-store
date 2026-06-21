package com.phuckhang.digital_store.iam.security.oauth2;


import com.phuckhang.digital_store.iam.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
@AllArgsConstructor
public class CustomOAuth2User implements OAuth2User {
    private User user; // Đóng gói Entity User của DB vào đây
    private Map<String, Object> attributes; // Dữ liệu thô từ Google

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Tùy vào cách phân quyền của bạn, ở đây trả về danh sách rỗng hoặc Role mặc định
        return Collections.emptyList();
    }

    @Override
    public String getName() {
        return user.getEmail();
    }
}