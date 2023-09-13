package com.phincon.laza.service;

import com.phincon.laza.model.dto.request.*;
import com.phincon.laza.model.dto.response.TokenResponse;
import com.phincon.laza.model.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {
    TokenResponse login(LoginRequest request);
    User register(RegisterRequest request) throws Exception;
    void registerResend(RecoveryRequest request) throws Exception;
    void registerConfirm(String token);
    void forgotPassword(RecoveryRequest request) throws Exception;
    void forgotPasswordConfirm(VerificationCodeRequest request);
    void resetPassword(ResetPasswordRequest request);
    TokenResponse refreshToken(String authHeader);
    TokenResponse token(UserDetails request);
}
