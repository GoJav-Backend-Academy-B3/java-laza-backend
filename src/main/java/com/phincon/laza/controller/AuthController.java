package com.phincon.laza.controller;

import com.phincon.laza.model.dto.request.*;
import com.phincon.laza.model.dto.response.DataResponse;
import com.phincon.laza.model.dto.response.TokenResponse;
import com.phincon.laza.model.dto.response.UserResponse;
import com.phincon.laza.model.entity.User;
import com.phincon.laza.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<DataResponse<TokenResponse>> login(@Valid @RequestBody LoginRequest request) {
        TokenResponse token = authService.login(request);
        DataResponse<TokenResponse> dataResponse = new DataResponse<>(HttpStatus.OK.value(), HttpStatus.OK.name(), token, null);
        return ResponseEntity.status(dataResponse.getStatusCode()).body(dataResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<DataResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest request) throws Exception {
        User user = authService.register(request);
        UserResponse result = new UserResponse(user);
        DataResponse<UserResponse> dataResponse = new DataResponse<>(HttpStatus.CREATED.value(), HttpStatus.CREATED.name(), result, null);
        return ResponseEntity.status(dataResponse.getStatusCode()).body(dataResponse);
    }

    @PostMapping("/register/resend")
    public ResponseEntity<DataResponse<?>> registerResend(@Valid @RequestBody RecoveryRequest request) throws Exception {
        authService.registerResend(request);
        DataResponse<UserResponse> dataResponse = new DataResponse<>(HttpStatus.OK.value(), HttpStatus.OK.name(), null, null);
        return ResponseEntity.status(dataResponse.getStatusCode()).body(dataResponse);
    }

    @GetMapping("/register/confirm")
    public ResponseEntity<DataResponse<?>> registerConfirm(@RequestParam String token) {
        authService.registerConfirm(token);
        DataResponse<UserResponse> dataResponse = new DataResponse<>(HttpStatus.OK.value(), HttpStatus.OK.name(), null, null);
        return ResponseEntity.status(dataResponse.getStatusCode()).body(dataResponse);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<DataResponse<?>> forgotPassword(@Valid @RequestBody RecoveryRequest request) throws Exception {
        authService.forgotPassword(request);
        DataResponse<UserResponse> dataResponse = new DataResponse<>(HttpStatus.OK.value(), HttpStatus.OK.name(), null, null);
        return ResponseEntity.status(dataResponse.getStatusCode()).body(dataResponse);
    }

    @PostMapping("/forgot-password/confirm")
    public ResponseEntity<DataResponse<?>> forgotPasswordConfirm(@Valid @RequestBody VerificationCodeRequest request) {
        authService.forgotPasswordConfirm(request);
        DataResponse<UserResponse> dataResponse = new DataResponse<>(HttpStatus.OK.value(), HttpStatus.OK.name(), null, null);
        return ResponseEntity.status(dataResponse.getStatusCode()).body(dataResponse);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<DataResponse<?>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        DataResponse<UserResponse> dataResponse = new DataResponse<>(HttpStatus.OK.value(), HttpStatus.OK.name(), null, null);
        return ResponseEntity.status(dataResponse.getStatusCode()).body(dataResponse);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<DataResponse<TokenResponse>> refreshToken(@RequestHeader("X-AUTH-REFRESH") String refreshToken) {
        TokenResponse token = authService.refreshToken(refreshToken);
        DataResponse<TokenResponse> dataResponse = new DataResponse<>(HttpStatus.OK.value(), HttpStatus.OK.name(), token, null);
        return ResponseEntity.status(dataResponse.getStatusCode()).body(dataResponse);
    }
}
