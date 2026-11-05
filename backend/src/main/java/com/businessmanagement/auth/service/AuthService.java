package com.businessmanagement.auth.service;

import com.businessmanagement.auth.dto.LoginRequest;
import com.businessmanagement.auth.dto.LoginResponse;
import com.businessmanagement.master.entity.AppUser;
import com.businessmanagement.master.repository.AppUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final AppUserRepository users;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(AppUserRepository users, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {
        AppUser user = users.findByUsername(request.username())
                .orElseThrow(() -> invalidCredentials());
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw invalidCredentials();
        }
        String role = user.getRole().getName();
        return new LoginResponse(
                jwtService.issueToken(user.getUsername(), role),
                "Bearer",
                jwtService.expiresInSeconds(),
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                role);
    }

    private ResponseStatusException invalidCredentials() {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Tên đăng nhập hoặc mật khẩu không đúng");
    }
}
