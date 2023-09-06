package com.phincon.laza.controller;

import com.phincon.laza.model.dto.request.LoginRequest;
import com.phincon.laza.model.dto.request.RegisterRequest;
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

    @GetMapping("/register/confirm")
    public ResponseEntity<DataResponse<?>> registerConfirm(@RequestParam String token) {
        authService.registerConfirm(token);
        DataResponse<UserResponse> dataResponse = new DataResponse<>(HttpStatus.OK.value(), HttpStatus.OK.name(), null, null);
        return ResponseEntity.status(dataResponse.getStatusCode()).body(dataResponse);
    }
}
