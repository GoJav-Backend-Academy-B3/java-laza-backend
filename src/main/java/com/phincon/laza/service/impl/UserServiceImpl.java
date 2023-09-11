package com.phincon.laza.service.impl;

import com.phincon.laza.model.dto.other.CloudinaryUploadResult;
import com.phincon.laza.model.dto.request.ChangePasswordRequest;
import com.phincon.laza.model.dto.request.RoleRequest;
import com.phincon.laza.model.dto.request.UserRequest;
import com.phincon.laza.model.entity.*;
import com.phincon.laza.repository.ProviderRepository;
import com.phincon.laza.repository.RoleRepository;
import com.phincon.laza.repository.UserRepository;
import com.phincon.laza.service.UserService;
import com.phincon.laza.validator.ProviderValidator;
import com.phincon.laza.validator.RoleValidator;
import com.phincon.laza.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final RoleRepository roleRepository;
    private final RoleValidator roleValidator;
    private final ProviderRepository providerRepository;
    private final ProviderValidator providerValidator;
    private final PasswordEncoder passwordEncoder;
    private final CloudinaryImageServiceImpl cloudinaryImageService;

    @Override
    public Page<User> getAll(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users;
    }

    @Override
    public User getById(String id) {
        Optional<User> findUser = userRepository.findById(id);
        userValidator.validateUserNotFound(findUser);
        return findUser.get();
    }

    @Override
    public User getByUsername(String username) {
        Optional<User> findUser = userRepository.findByUsername(username);
        userValidator.validateUserNotFound(findUser);
        return findUser.get();
    }

    @Override
    public User update(String id, UserRequest request) throws Exception {
        Optional<User> findUser = userRepository.findById(id);
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

        findUser.get().setName(request.getName());
        userRepository.save(findUser.get());
        log.info("User id={} is updated", findUser.get().getId());
        return findUser.get();
    }

    @Override
    public void changePassword(String id, ChangePasswordRequest request) {
        userValidator.validateUserPasswordNotMatch(request.getNewPassword(), request.getConfirmPassword());

        Optional<User> findUser = userRepository.findById(id);
        userValidator.validateUserNotFound(findUser);
        userValidator.validateUserInvalidOldPassword(request.getOldPassword(), findUser.get().getPassword());

        List<Provider> listProvider =  findUser.get().getProviders();
        Optional<Provider> findProvider = providerRepository.findByName(EProvider.LOCAL);
        providerValidator.validateProviderNotFound(findProvider);
        listProvider.add(findProvider.get());

        findUser.get().setPassword(passwordEncoder.encode(request.getConfirmPassword()));
        userRepository.save(findUser.get());
        log.info("User id={} is updated password", findUser.get().getId());
    }

    @Override
    public void updateRole(RoleRequest request) {
        Optional<User> findUser = userRepository.findByUsername(request.getUsername());
        userValidator.validateUserNotFound(findUser);

        List<Role> listRole = new ArrayList<>();
        for (String v : request.getRoles()) {
            Optional<Role> findRole = roleRepository.findByName(ERole.valueOf(v.toUpperCase()));
            roleValidator.validateRoleNotFound(findRole);
            listRole.add(findRole.get());
        }

        findUser.get().setRoles(listRole);
        userRepository.save(findUser.get());
        log.info("User id={} is updated roles", findUser.get().getId());
    }
}
