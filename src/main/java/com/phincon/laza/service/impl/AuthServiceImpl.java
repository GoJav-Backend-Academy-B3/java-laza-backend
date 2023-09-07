package com.phincon.laza.service.impl;

import com.phincon.laza.model.dto.request.*;
import com.phincon.laza.model.dto.response.TokenResponse;
import com.phincon.laza.model.entity.*;
import com.phincon.laza.repository.RoleRepository;
import com.phincon.laza.repository.UserRepository;
import com.phincon.laza.repository.VerificationCodeRepository;
import com.phincon.laza.repository.VerificationTokenRepository;
import com.phincon.laza.security.jwt.JwtService;
import com.phincon.laza.security.userdetails.SysUserDetails;
import com.phincon.laza.service.AuthService;
import com.phincon.laza.service.SenderMailService;
import com.phincon.laza.utils.GenerateRandom;
import com.phincon.laza.validator.RoleValidator;
import com.phincon.laza.validator.UserValidator;
import com.phincon.laza.validator.VerificationCodeValidator;
import com.phincon.laza.validator.VerificationTokenValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final RoleRepository roleRepository;
    private final RoleValidator roleValidator;
    private final VerificationTokenRepository verificationTokenRepository;
    private final VerificationTokenValidator verificationTokenValidator;
    private final VerificationCodeRepository verificationCodeRepository;
    private final VerificationCodeValidator verificationCodeValidator;
    private final SenderMailService senderMailService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    public TokenResponse login(LoginRequest request) {
        Optional<User> findByUsername = userRepository.findByUsername(request.getUsername());
        userValidator.validateUserNotFound(findByUsername);
        userValidator.validateUserBadCredentials(findByUsername, request.getPassword());
        userValidator.validateUserNotIsVerfied(findByUsername);

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        SysUserDetails user = new SysUserDetails();
        user.setUsername(findByUsername.get().getUsername());
        user.setPassword(findByUsername.get().getPassword());
        user.setRoles(findByUsername.get().getRoles());

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken);

        log.info("{} has been login", user.getUsername());
        return tokenResponse;
    }

    @Override
    public User register(RegisterRequest request) throws Exception {
        Optional<User> findByUsername = userRepository.findByUsername(request.getUsername());
        userValidator.validateUsernameIsExists(findByUsername);

        Optional<User> findByEmail = userRepository.findByEmail(request.getEmail());
        userValidator.validateEmailIsExists(findByEmail);

        List<Role> listRole = new ArrayList<>();
        Optional<Role> findRole = roleRepository.findByName(ERole.USER.toString());
        roleValidator.validateRoleNotFound(findRole);
        listRole.add(findRole.get());

        User user = new User();
        user.setFullName(request.getFullName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(listRole);

        userRepository.save(user);
        log.info("User id={} is saved", user.getId());

        String token = GenerateRandom.token();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(5);
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setExpiresAt(expiresAt);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        senderMailService.confirmRegister(user, token);

        log.info("User id={} success send mail confirm account", user.getId());
        return user;
    }

    @Override
    public void registerResend(RecoveryRequest request) throws Exception {
        Optional<User> findUser = userRepository.findByEmail(request.getEmail());
        userValidator.validateUserNotFound(findUser);

        String token = GenerateRandom.token();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(5);
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setExpiresAt(expiresAt);
        verificationToken.setUser(findUser.get());

        verificationTokenRepository.save(verificationToken);
        senderMailService.confirmRegister(findUser.get(), token);

        log.info("User id={} success resend mail confirm account", findUser.get().getId());
    }

    @Override
    public void registerConfirm(String token) {
        Optional<VerificationToken> findToken = verificationTokenRepository.findByToken(token);
        verificationTokenValidator.validateVerificationTokenNotFound(findToken);
        verificationTokenValidator.validateVerificationTokenAlreadyConfirm(findToken);
        verificationTokenValidator.validateVerificationTokenAlreadyExpire(findToken);

        findToken.get().setConfirmedAt(LocalDateTime.now());
        verificationTokenRepository.save(findToken.get());

        findToken.get().getUser().setVerified(true);
        userRepository.save(findToken.get().getUser());

        log.info("User id={} success verification token", findToken.get().getUser().getId());
    }

    @Override
    public void forgotPassword(RecoveryRequest request) throws Exception {
        Optional<User> findUser = userRepository.findByEmail(request.getEmail());
        userValidator.validateUserNotFound(findUser);
        userValidator.validateUserNotIsVerfied(findUser);

        String code = GenerateRandom.code();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(5);
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setCode(code);
        verificationCode.setExpiresAt(expiresAt);
        verificationCode.setUser(findUser.get());

        verificationCodeRepository.save(verificationCode);

        senderMailService.forgotPassword(findUser.get(), code);
        log.info("User id={} success send mail forgot password", findUser.get().getId());
    }

    @Override
    public void forgotPasswordConfirm(VerificationCodeRequest request) {
        Optional<User> findUser = userRepository.findByEmail(request.getEmail());
        userValidator.validateUserNotFound(findUser);
        userValidator.validateUserNotIsVerfied(findUser);

        Optional<VerificationCode> findCode = verificationCodeRepository.findByCodeAndUserId(request.getCode(), findUser.get().getId());
        verificationCodeValidator.validateVerificationCodeNotFound(findCode);
        verificationCodeValidator.validateVerificationCodeAlreadyConfirm(findCode);
        verificationCodeValidator.validateVerificationCodeAlreadyExpire(findCode);

        findCode.get().setConfirmedAt(LocalDateTime.now());
        verificationCodeRepository.save(findCode.get());
        log.info("User id={} success verification code", findCode.get().getUser().getId());
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        Optional<User> findUser = userRepository.findByEmail(request.getEmail());
        userValidator.validateUserNotFound(findUser);
        userValidator.validateUserNotIsVerfied(findUser);

        Optional<VerificationCode> findCode = verificationCodeRepository.findByCodeAndUserId(request.getCode(), findUser.get().getId());
        verificationCodeValidator.validateVerificationCodeNotFound(findCode);
        verificationCodeValidator.validateVerificationCodeNotAlreadyConfirm(findCode);
        verificationCodeValidator.validateVerificationCodeAlreadyExpire(findCode);

        findUser.get().setPassword(passwordEncoder.encode(request.getConfirmPassword()));
        userRepository.save(findUser.get());
        log.info("User id={} is updated password", findUser.get().getId());

        verificationCodeRepository.deleteById(findCode.get().getId());
        log.info("User id={} is deleted code", findUser.get().getId());
    }
}
