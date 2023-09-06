package com.phincon.laza.service;

import com.phincon.laza.model.dto.request.LoginRequest;
import com.phincon.laza.model.dto.request.RegisterRequest;
import com.phincon.laza.model.dto.response.TokenResponse;
import com.phincon.laza.model.entity.User;

public interface AuthService {
    TokenResponse login(LoginRequest request);
    User register(RegisterRequest request) throws Exception;
    void registerConfirm(String token);
}
