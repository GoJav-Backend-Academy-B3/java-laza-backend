package com.phincon.laza.service;

import com.phincon.laza.model.dto.request.*;
import com.phincon.laza.model.dto.response.TokenResponse;
import com.phincon.laza.model.entity.*;
import com.phincon.laza.repository.*;
import com.phincon.laza.security.jwt.JwtService;
import com.phincon.laza.security.userdetails.SysUserDetails;
import com.phincon.laza.service.impl.AuthServiceImpl;
import com.phincon.laza.utils.GenerateRandom;
import com.phincon.laza.validator.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private AuthValidator authValidator;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserValidator userValidator;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleValidator roleValidator;

    @Mock
    private ProviderRepository providerRepository;

    @Mock
    private ProviderValidator providerValidator;

    @Mock
    private VerificationTokenRepository verificationTokenRepository;

    @Mock
    private VerificationTokenValidator verificationTokenValidator;

    @Mock
    private VerificationCodeRepository verificationCodeRepository;

    @Mock
    private VerificationCodeValidator verificationCodeValidator;

    @Mock
    private SenderMailService senderMailService;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    public void init() {
        Role role = new Role();
        role.setName(ERole.USER);

        Set<Role> listRole = new HashSet<>();
        listRole.add(role);

        Provider provider = new Provider();
        provider.setName(EProvider.LOCAL);

        Set<Provider> listProvider = new HashSet<>();
        listProvider.add(provider);

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setName("John Doe");
        user.setUsername("johndoe");
        user.setEmail("johndoe@mail.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRoles(listRole);
        user.setProviders(listProvider);

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(GenerateRandom.token());
        verificationToken.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        verificationToken.setUser(user);

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setCode(GenerateRandom.code());
        verificationCode.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        verificationCode.setUser(user);

        lenient().when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        lenient().when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        lenient().when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        lenient().when(userRepository.save(any())).thenReturn(user);

        lenient().when(roleRepository.findByName(any())).thenReturn(Optional.of(role));
        lenient().when(providerRepository.findByName(any())).thenReturn(Optional.of(provider));
        lenient().when(verificationTokenRepository.save(any())).thenReturn(verificationToken);
        lenient().when(verificationTokenRepository.findByToken(anyString())).thenReturn(Optional.of(verificationToken));
        lenient().when(verificationCodeRepository.save(any())).thenReturn(verificationCode);
        lenient().when(verificationCodeRepository.findByCodeAndUserId(anyString(), any())).thenReturn(Optional.of(verificationCode));
    }

    @Test
    public void testLoginToAuth_thenCorrect() {
        LoginRequest request = new LoginRequest();
        request.setUsername("johndoe");
        request.setPassword("password");

        TokenResponse tokenResponse = authService.login(request);

        assertNotNull(tokenResponse);

        verify(userRepository, times(1)).findByUsername(anyString());

        log.info("[COMPLETE] testing service auth login then correct");
    }

    @Test
    public void testRegisterToAuth_thenCorrect() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setName("John Doe");
        request.setUsername("johndoe");
        request.setEmail("johndoe@mail.com");
        request.setPassword("password");

        User user = authService.register(request);

        assertNotNull(user);
        assertEquals(request.getName(), user.getName());
        assertEquals(request.getUsername(), user.getUsername());
        assertEquals(request.getEmail(), user.getEmail());
        assertEquals(1, user.getRoles().size());
        assertEquals(1, user.getProviders().size());

        verify(userRepository, times(1)).findByUsername(anyString());
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(userRepository, times(1)).save(any());
        verify(roleRepository, times(1)).findByName(any());
        verify(providerRepository, times(1)).findByName(any());
        verify(verificationTokenRepository, times(1)).save(any());

        log.info("[COMPLETE] testing service auth register then correct");
    }

    @Test
    public void testRegisterResendToAuth_thenCorrect() throws Exception {
        RecoveryRequest request = new RecoveryRequest();
        request.setEmail("johndoe@mail.com");

        authService.registerResend(request);

        verify(userRepository, times(1)).findByEmail(anyString());
        verify(verificationTokenRepository, times(1)).save(any());

        log.info("[COMPLETE] testing service auth registerResend then correct");
    }

    @Test
    public void testRegisterConfirmToAuth_thenCorrect() {
        String token = GenerateRandom.token();

        authService.registerConfirm(token);

        verify(userRepository, times(1)).save(any());
        verify(verificationTokenRepository, times(1)).findByToken(anyString());
        verify(verificationTokenRepository, times(1)).save(any());

        log.info("[COMPLETE] testing service auth registerConfirm then correct");
    }

    @Test
    public void testForgotPasswordToAuth_thenCorrect() throws Exception {
        RecoveryRequest request = new RecoveryRequest();
        request.setEmail("johndoe@mail.com");

        authService.forgotPassword(request);

        verify(userRepository, times(1)).findByEmail(anyString());
        verify(verificationCodeRepository, times(1)).save(any());

        log.info("[COMPLETE] testing service auth forgotPassword then correct");
    }

    @Test
    public void testForgotPasswordConfirmToAuth_thenCorrect() {
        VerificationCodeRequest request = new VerificationCodeRequest();
        request.setCode(GenerateRandom.code());
        request.setEmail("johndoe@mail.com");

        authService.forgotPasswordConfirm(request);

        verify(userRepository, times(1)).findByEmail(anyString());
        verify(verificationCodeRepository, times(1)).findByCodeAndUserId(anyString(), any());
        verify(verificationCodeRepository, times(1)).save(any());

        log.info("[COMPLETE] testing service auth forgotPasswordConfirm then correct");
    }

    @Test
    public void testResetPasswordToAuth_thenCorrect() {
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setCode(GenerateRandom.code());
        request.setEmail("johndoe@mail.com");
        request.setNewPassword("password");
        request.setConfirmPassword("password");

        authService.resetPassword(request);

        verify(userRepository, times(1)).findByEmail(anyString());
        verify(userRepository, times(1)).save(any());
        verify(verificationCodeRepository, times(1)).findByCodeAndUserId(anyString(), any());

        log.info("[COMPLETE] testing service auth resetPassword then correct");
    }

    @Test
    public void testRefreshTokenToAuth_thenCorrect() {
        String authHeader = "Bearer valid_refresh_token";

        User user = new User();
        user.setUsername("johndoe");

        Role role = new Role();
        role.setName(ERole.USER);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        UserDetails userDetails = SysUserDetails.create(user);

        when(jwtService.extractUsername("valid_refresh_token")).thenReturn(user.getUsername());
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("new_access_token");

        TokenResponse tokenResponse = authService.refreshToken(authHeader);

        assertNotNull(tokenResponse);
        assertEquals("new_access_token", tokenResponse.getAccessToken());
        assertEquals("valid_refresh_token", tokenResponse.getRefreshToken());

        verify(userRepository, times(1)).findByUsername(anyString());

        log.info("[COMPLETE] testing service auth refreshToken then correct");
    }

    @Test
    public void testTokenToAuth_thenCorrect() {
        User user = new User();
        user.setUsername("johndoe");

        Role role = new Role();
        role.setName(ERole.USER);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        UserDetails userDetails = SysUserDetails.create(user);

        TokenResponse token = authService.token(userDetails);

        assertNotNull(token);

        verify(userRepository, times(1)).findByUsername(anyString());

        log.info("[COMPLETE] testing service auth token oauth2 then correct");
    }
}
