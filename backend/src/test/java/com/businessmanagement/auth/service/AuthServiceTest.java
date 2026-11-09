package com.businessmanagement.auth.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.businessmanagement.auth.repository.AppUserRepository;
import com.businessmanagement.auth.service.impl.AuthServiceImpl;
import com.businessmanagement.auth.util.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private AppUserRepository userRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private JwtService jwtService;
    
    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    public void login_WithValidCredentials_ShouldReturnToken() {
        // Given
        String username = "testuser";
        String password = "validPassword";
        String encodedPassword = "encodedPassword";
        String expectedToken = "testToken";
        
        when(userRepository.existsByUsername(username)).thenReturn(true);
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
        when(jwtService.generateToken(username)).thenReturn(expectedToken);
        
        // When
        String actualToken = authService.login(username, password);
        
        // Then
        assertEquals(expectedToken, actualToken);
        verify(userRepository).existsByUsername(username);
        verify(passwordEncoder).matches(password, encodedPassword);
        verify(jwtService).generateToken(username);
    }
    
        @Test
        public void login_WithInvalidUsername_ShouldThrowException() {
            // Given
            String username = "nonexistent";
            String password = "anyPassword";
        
            when(userRepository.existsByUsername(username)).thenReturn(false);
        
            // When/Then
            assertThrows(RuntimeException.class, () -> {
                authService.login(username, password);
            });
        
            verify(userRepository).existsByUsername(username);
            verifyNoInteractions(passwordEncoder, jwtService);
        }
    
        @Test
        public void login_WithInvalidPassword_ShouldThrowException() {
            // Given
            String username = "testuser";
            String password = "wrongPassword";
            String encodedPassword = "encodedPassword";
        
            when(userRepository.existsByUsername(username)).thenReturn(true);
            when(passwordEncoder.matches(password, encodedPassword)).thenReturn(false);
        
            // When/Then
            assertThrows(RuntimeException.class, () -> {
                authService.login(username, password);
            });
        
            verify(userRepository).existsByUsername(username);
            verify(passwordEncoder).matches(password, encodedPassword);
            verifyNoInteractions(jwtService);
        }
}