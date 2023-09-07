package com.phincon.laza.service.impl;

import com.phincon.laza.model.dto.other.CloudinaryUploadResult;
import com.phincon.laza.model.dto.request.ChangePasswordRequest;
import com.phincon.laza.model.dto.request.RoleRequest;
import com.phincon.laza.model.dto.request.UserRequest;
import com.phincon.laza.model.entity.Role;
import com.phincon.laza.model.entity.User;
import com.phincon.laza.repository.RoleRepository;
import com.phincon.laza.repository.UserRepository;
import com.phincon.laza.service.UserService;
import com.phincon.laza.validator.RoleValidator;
import com.phincon.laza.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final RoleRepository roleRepository;
    private final RoleValidator roleValidator;
    private final PasswordEncoder passwordEncoder;
    private final CloudinaryImageServiceImpl cloudinaryImageService;

    @Override
    public Page<User> getAll(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users;
    }

    @Override
    public User getByUsername(String username) {
        Optional<User> findUser = userRepository.findByUsername(username);
        userValidator.validateUserNotFound(findUser);
        return findUser.get();
    }

    @Override
    public User update(String username, UserRequest request) throws Exception {
        Optional<User> findUser = userRepository.findByUsername(username);
        userValidator.validateUserNotFound(findUser);

        Optional<User> findByUsername = userRepository.findByUsername(request.getUsername());
        if (!findUser.get().getUsername().equals(findByUsername.get().getUsername())) {
            userValidator.validateUsernameIsExists(findByUsername);
            findUser.get().setUsername(request.getUsername());
        }

        Optional<User> findByEmail = userRepository.findByEmail(request.getEmail());
        if (!findUser.get().getEmail().equals(findByEmail.get().getEmail())) {
            userValidator.validateEmailIsExists(findByEmail);

            findUser.get().setEmail(request.getEmail());
            findUser.get().setVerified(false);
        }

        if (Objects.nonNull(request.getImage())) {
            CloudinaryUploadResult image = cloudinaryImageService.upload(request.getImage(), "user");
            findUser.get().setImageUrl(image.secureUrl());
        }

        findUser.get().setFullName(request.getFullName());
        userRepository.save(findUser.get());
        log.info("User id={} is updated", findUser.get().getId());
        return findUser.get();
    }

    @Override
    public void changePassword(String username, ChangePasswordRequest request) {
        userValidator.validateUserPasswordNotMatch(request.getNewPassword(), request.getConfirmPassword());

        Optional<User> findUser = userRepository.findByUsername(username);
        userValidator.validateUserNotFound(findUser);
        userValidator.validateInvalidOldPassword(request.getOldPassword(), findUser.get().getPassword());

        findUser.get().setPassword(passwordEncoder.encode(request.getConfirmPassword()));
        userRepository.save(findUser.get());
        log.info("User id={} is updated password", findUser.get().getId());
    }

    @Override
    public void updateRole(String username, RoleRequest request) {
        Optional<User> findUser = userRepository.findByUsername(username);
        userValidator.validateUserNotFound(findUser);

        List<Role> listRole = new ArrayList<>();
        for (String v : request.getRoles()) {
            Optional<Role> findRole = roleRepository.findByName(v);
            roleValidator.validateRoleNotFound(findRole);
            listRole.add(findRole.get());
        }

        findUser.get().setRoles(listRole);
        userRepository.save(findUser.get());
        log.info("User id={} is updated roles", findUser.get().getId());
    }
}
