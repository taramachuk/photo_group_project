package com.example.backend.service;

import com.example.backend.dto.LoginUserDto;
import com.example.backend.dto.RegisterUserDto;
import com.example.backend.dto.VerifyUserDto;
import com.example.backend.exception.EmailAlreadyUsedException;
import com.example.backend.exception.InvalidLoginException;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void signup_ShouldRegisterUser_WhenEmailIsUnique() throws MessagingException {
        RegisterUserDto inputDto = new RegisterUserDto();
        inputDto.setEmail("nowy@test.pl");
        inputDto.setPassword("haslo123");
        inputDto.setUsername("NowyUser");

        when(userRepository.existsByEmail(inputDto.getEmail())).thenReturn(false);

        when(passwordEncoder.encode(inputDto.getPassword())).thenReturn("zakodowane_haslo_xyz");

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = authenticationService.signup(inputDto);

        assertNotNull(result);
        assertEquals("nowy@test.pl", result.getEmail());
        assertEquals("zakodowane_haslo_xyz", result.getPassword());
        assertNotNull(result.getVerificationCode());

        verify(emailService, times(1)).sendVerificationEmail(anyString(), anyString(), anyString());
    }

    @Test
    void signup_ShouldThrowException_WhenEmailIsTaken() throws MessagingException {
        RegisterUserDto inputDto = new RegisterUserDto();
        inputDto.setEmail("zajety@test.pl");
        inputDto.setPassword("haslo123");

        when(userRepository.existsByEmail(inputDto.getEmail())).thenReturn(true);

        assertThrows(EmailAlreadyUsedException.class, () -> {
            authenticationService.signup(inputDto);
        });

        verify(userRepository, never()).save(any());
        verify(emailService, never()).sendVerificationEmail(any(), any(), any());
    }

    @Test
    void authenticate_ShouldReturnUser_WhenCredentialsAreValid() {
        LoginUserDto loginDto = new LoginUserDto();
        loginDto.setEmail("stary@test.pl");
        loginDto.setPassword("dobreHaslo");

        User existingUser = new User();
        existingUser.setEmail("stary@test.pl");
        existingUser.setPassword("zakodowane_haslo");
        existingUser.setEnabled(true);

        when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.of(existingUser));

        User result = authenticationService.authenticate(loginDto);

        assertNotNull(result);
        assertEquals("stary@test.pl", result.getEmail());

        verify(authenticationManager, times(1)).authenticate(any());
    }

    @Test
    void authenticate_ShouldThrowException_WhenUserNotFound() {
        LoginUserDto loginDto = new LoginUserDto();
        loginDto.setEmail("duch@test.pl");
        loginDto.setPassword("cokolwiek");

        when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.empty());

        assertThrows(InvalidLoginException.class, () -> {
            authenticationService.authenticate(loginDto);
        });

        verify(authenticationManager, never()).authenticate(any());
    }

    @Test
    void verifyUser_ShouldEnableUser_WhenCodeIsValid() {
        VerifyUserDto inputDto = new VerifyUserDto();
        inputDto.setEmail("weryfikacja@test.pl");
        inputDto.setVerificationCode("123456");

        User user = new User();
        user.setEmail("weryfikacja@test.pl");
        user.setVerificationCode("123456");
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(10));
        user.setEnabled(false);

        when(userRepository.findByEmail(inputDto.getEmail())).thenReturn(Optional.of(user));

        authenticationService.verifyUser(inputDto);

        assertTrue(user.isEnabled());
        assertNull(user.getVerificationCode());
        verify(userRepository, times(1)).save(user);
    }




}