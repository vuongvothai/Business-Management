package com.businessmanagement.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.businessmanagement.auth.dto.LoginRequest;
import com.businessmanagement.auth.dto.LoginResponse;
import com.businessmanagement.master.entity.AppUser;
import com.businessmanagement.master.entity.Role;
import com.businessmanagement.master.repository.AppUserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AppUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    private AuthService authService;
    private AppUser user;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userRepository, passwordEncoder, jwtService);
        user = new AppUser("testuser", "Test user", "encodedPassword", new Role("ADMIN"));
    }

    @Test
    void loginWithValidCredentialsReturnsBearerResponse() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("validPassword", "encodedPassword")).thenReturn(true);
        when(jwtService.issueToken("testuser", "ADMIN")).thenReturn("testToken");
        when(jwtService.expiresInSeconds()).thenReturn(28800L);

        LoginResponse response = authService.login(new LoginRequest("testuser", "validPassword"));

        assertThat(response.accessToken()).isEqualTo("testToken");
        assertThat(response.tokenType()).isEqualTo("Bearer");
        assertThat(response.expiresInSeconds()).isEqualTo(28800L);
        assertThat(response.username()).isEqualTo("testuser");
        assertThat(response.role()).isEqualTo("ADMIN");
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void loginWithUnknownUsernameRejectsCredentials() {
        when(userRepository.findByUsername("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(new LoginRequest("missing", "password")))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Tên đăng nhập hoặc mật khẩu không đúng");
        verifyNoInteractions(passwordEncoder, jwtService);
    }

    @Test
    void loginWithWrongPasswordRejectsCredentials() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        assertThatThrownBy(() -> authService.login(new LoginRequest("testuser", "wrongPassword")))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Tên đăng nhập hoặc mật khẩu không đúng");
        verifyNoInteractions(jwtService);
    }
}