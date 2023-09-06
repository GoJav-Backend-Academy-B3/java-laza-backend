package com.phincon.laza.controller;

import com.phincon.laza.model.dto.request.ChangePasswordRequest;
import com.phincon.laza.model.dto.request.UserRequest;
import com.phincon.laza.model.dto.response.DataResponse;
import com.phincon.laza.model.dto.response.PaginationMeta;
import com.phincon.laza.model.dto.response.UserResponse;
import com.phincon.laza.model.entity.User;
import com.phincon.laza.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<DataResponse<List<UserResponse>>> getAll(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "5") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userService.getAll(pageable);
        List<UserResponse> result = users.getContent().stream().map(user -> new UserResponse(user)).collect(Collectors.toList());
        PaginationMeta  paginationMeta = new PaginationMeta(users.getNumber(), users.getSize(), users.getTotalPages());
        DataResponse<List<UserResponse>> dataResponse = new DataResponse<>(HttpStatus.OK.value(), HttpStatus.OK.name(), result, paginationMeta);
        return ResponseEntity.status(dataResponse.getStatusCode()).body(dataResponse);
    }

    @GetMapping("/me")
    public ResponseEntity<DataResponse<UserResponse>> profile(@AuthenticationPrincipal UserDetails ctx) {
        User user = userService.getByUsername(ctx.getUsername());
        UserResponse result = new UserResponse(user);
        DataResponse<UserResponse> dataResponse = new DataResponse<>(HttpStatus.OK.value(), HttpStatus.OK.name(), result, null);
        return ResponseEntity.status(dataResponse.getStatusCode()).body(dataResponse);
    }

    @PutMapping("/update")
    public ResponseEntity<DataResponse<UserResponse>> update(@AuthenticationPrincipal UserDetails ctx, @Valid @RequestBody UserRequest request) {
        User user = userService.update(ctx.getUsername(), request);
        UserResponse result = new UserResponse(user);
        DataResponse<UserResponse> dataResponse = new DataResponse<>(HttpStatus.OK.value(), HttpStatus.OK.name(), result, null);
        return ResponseEntity.status(dataResponse.getStatusCode()).body(dataResponse);
    }

    @PatchMapping("/change-password")
    public ResponseEntity<DataResponse<?>> changePassword(@AuthenticationPrincipal UserDetails ctx, @Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(ctx.getUsername(), request);
        DataResponse<UserResponse> dataResponse = new DataResponse<>(HttpStatus.OK.value(), HttpStatus.OK.name(), null, null);
        return ResponseEntity.status(dataResponse.getStatusCode()).body(dataResponse);
    }
}
