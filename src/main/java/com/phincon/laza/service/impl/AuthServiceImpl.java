package com.phincon.laza.service.impl;

import com.phincon.laza.model.dto.request.LoginRequest;
import com.phincon.laza.model.dto.request.RegisterRequest;
import com.phincon.laza.model.dto.response.TokenResponse;
import com.phincon.laza.model.entity.ERole;
import com.phincon.laza.model.entity.Role;
import com.phincon.laza.model.entity.User;
import com.phincon.laza.repository.RoleRepository;
import com.phincon.laza.repository.UserRepository;
import com.phincon.laza.security.jwt.JwtService;
import com.phincon.laza.security.userdetails.SysUserDetails;
import com.phincon.laza.service.AuthService;
import com.phincon.laza.validator.RoleValidator;
import com.phincon.laza.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    public TokenResponse login(LoginRequest request) {
        Optional<User> findByUsername = userRepository.findByUsername(request.getUsername());
        userValidator.validateUserNotFound(findByUsername);

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
    public User register(RegisterRequest request) {
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
        return user;
    }
}
