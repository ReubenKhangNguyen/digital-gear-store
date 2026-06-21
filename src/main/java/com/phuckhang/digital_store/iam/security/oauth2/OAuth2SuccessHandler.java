package com.phuckhang.digital_store.iam.security.oauth2;


import com.phuckhang.digital_store.iam.service.AuthenticationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    // Nhớ đổi tên JwtService/JwtTokenProvider tùy theo project của bạn
    private final AuthenticationService jwtTokenProvider;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // Lấy thông tin user vừa đăng nhập thành công
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        // Sinh JWT Token (Thay hàm generateToken tùy thuộc code hiện tại của bạn)
        // Nếu hàm sinh Token của bạn cần User entity thì truyền oAuth2User.getUser()
        String token = jwtTokenProvider.generateToken(oAuth2User.getUser());
        // Địa chỉ trang ReactJS của bạn (Có thể để trong application.yml)
        String frontendUrl = "http://localhost:5173/oauth2/redirect";
        // Gắn Token vào sau dấu thăng (#) để bảo mật chống lộ History URL
        String targetUrl = frontendUrl + "#token=" + token;
        // Bắn ngược khách hàng về Frontend cùng với Token
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}