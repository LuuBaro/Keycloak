package org.example.asm_1.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.example.asm_1.dto.keycloak.Credential;
import org.example.asm_1.dto.keycloak.LoginRequestParam;
import org.example.asm_1.dto.keycloak.TokenExchangeParam;
import org.example.asm_1.dto.keycloak.UserCreationParam;
import org.example.asm_1.dto.request.LoginRequest;
import org.example.asm_1.dto.request.RegisterRequest;
import org.example.asm_1.dto.response.LoginResponse;
import org.example.asm_1.dto.response.ProfileResponse;
import org.example.asm_1.exception.AppException;
import org.example.asm_1.exception.ErrorCode;
import org.example.asm_1.exception.ErrorNornalizer;
import org.example.asm_1.mapper.ProfileMapper;
import org.example.asm_1.model.Profile;
import org.example.asm_1.repository.KeycloakRepository;
import org.example.asm_1.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ProfileService {

    ProfileRepository profileRepository;
    KeycloakRepository keycloakRepository;
    ProfileMapper profileMapper;
    ErrorNornalizer errorNornalizer;
    PasswordEncoder passwordEncoder;

//    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @Value("${idp.client-id}")
    @NonFinal
    String clientId;

    @Value("${idp.client-secret}")
    @NonFinal
    String clientSecret;

    //lấy tất cả thông tin profile
    @PreAuthorize("hasRole('ADMIN')")
    public List<ProfileResponse> getAllProfiles() {
        var profiles = profileRepository.findAll();
        return profiles.stream().map(profileMapper::toProfileResponse).toList();
    }

    //lấy thông tin profile của user hiện tại
    public ProfileResponse getMyProfile() {
      var authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        var profile = profileRepository.findById(Long.valueOf(userId)).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return profileMapper.toProfileResponse(profile);
    }

    //đăng ký tài khoản
    public ProfileResponse register(RegisterRequest request) {
        // Mã hóa mật khẩu trước khi lưu vào cơ sở dữ liệu
        String encryptedPassword = passwordEncoder.encode(request.getPassword());

        // Tạo người dùng mới và lưu vào cơ sở dữ liệu
        Profile user = new Profile();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(encryptedPassword);

        user = profileRepository.save(user);

        // Chuyển đổi thành response nếu cần
        return profileMapper.toProfileResponse(user);
    }

    //lấy userId từ response
    private String extractUserId(ResponseEntity<?> response) {
        // Extract userId from response
        String location = response.getHeaders().get("Location").get(0);
        String[] splitedStr = location.split("/");
        return splitedStr[splitedStr.length - 1];
    }

    public LoginResponse login(LoginRequest request) {
        try {
            var token = keycloakRepository.exchangeToken(LoginRequestParam.builder()
                    .grant_type("password")
                    .client_id(clientId)
                    .client_secret(clientSecret)
                    .username(request.getUsername())
                    .password(request.getPassword())
                    .scope("openid")
                    .build());
            System.out.println("Token Response: " + token);
            System.out.println("Access Token: " + token.getAccessToken());
            System.out.println("Refresh Token: " + token.getRefreshToken());

            return LoginResponse.builder()
//                    .preferredUsername(token.getPreferredUsername())
                    .accessToken(token.getAccessToken())
                    .refreshToken(token.getRefreshToken())
                    .build();
        } catch (FeignException e) {
            throw errorNornalizer.handleKeyCloakException(e);
        }
    }

//    public LoginResponse login(LoginRequest request) {
//        try {
//            // Lấy thông tin người dùng từ database
//            Optional<Profile> profileOptional = profileRepository.findByUsername(request.getUsername());
//
//            if (profileOptional.isEmpty() || !BCrypt.checkpw(request.getPassword(), profileOptional.get().getPassword())) {
//                throw new AppException(ErrorCode.INVALID_CREDENTIALS);
//            }
//
//            Profile profile = profileOptional.get();
//
//            // Lấy token từ Keycloak
//            var token = keycloakRepository.exchangeToken(LoginRequestParam.builder()
//                    .grant_type("password")
//                    .client_id(clientId)
//                    .client_secret(clientSecret)
//                    .username(request.getUsername())
//                    .password(request.getPassword())
//                    .scope("openid")
//                    .build());
//
//            System.out.println("Token Response: " + token);
//
//            // Trả về response với thông tin đăng nhập và token từ Keycloak
//            return LoginResponse.builder()
//                    .username(request.getUsername())
//                    .accessToken(token.getAccessToken())
//                    .refreshToken(token.getRefreshToken())
//                    .build();
//        } catch (FeignException e) {
//            // Xử lý lỗi từ Keycloak
//            log.error("Error occurred while fetching token from Keycloak", e);
//            throw errorNornalizer.handleKeyCloakException(e);
//        } catch (Exception e) {
//            // Xử lý lỗi chung
//            log.error("Login failed", e);
//            throw new RuntimeException("Login failed", e);
//        }
//    }



}
