package org.example.asm_1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    // Danh sách các endpoint công khai, không yêu cầu xác thực
    private final String[] PUBLIC_ENDPOINTS = {
            "/register",             // Đăng ký tài khoản
            "/swagger-ui/**",        // Tài liệu Swagger UI
            "/v3/api-docs/**",       // API Docs
            "/login"                 // Đăng nhập tài khoản
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // Cấu hình quyền truy cập
        httpSecurity.authorizeHttpRequests(request -> request
                // Cho phép truy cập không cần xác thực với các endpoint trong PUBLIC_ENDPOINTS
                .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                // Yêu cầu xác thực cho tất cả các endpoint khác
                .anyRequest().authenticated());

        // Cấu hình OAuth2 Resource Server với JWT
        httpSecurity.oauth2ResourceServer(oauth2 -> oauth2.jwt(
                        // Cấu hình cách chuyển đổi thông tin xác thực từ JWT
                        jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(
                                jwtAuthenticationConverter()))
                // Cấu hình xử lý lỗi xác thực
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint()));

        // Vô hiệu hóa CSRF (Cross-Site Request Forgery) để đơn giản hóa cho API REST
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        // Trả về cấu hình bảo mật đã hoàn tất
        return httpSecurity.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        // Tạo đối tượng JwtAuthenticationConverter
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();

        // Cài đặt bộ chuyển đổi để ánh xạ quyền từ JWT
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new CustomAuthoritiesConverter());

        return jwtAuthenticationConverter; // Trả về bộ chuyển đổi
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Sử dụng BCrypt cho mã hóa mật khẩu
    }
}
