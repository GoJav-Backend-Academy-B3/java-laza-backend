package com.phincon.laza.service.impl;

import com.phincon.laza.model.dto.request.UserRequest;
import com.phincon.laza.model.entity.User;
import com.phincon.laza.repository.UserRepository;
import com.phincon.laza.service.UserService;
import com.phincon.laza.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;

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
    public User update(String id, UserRequest request) {
        Optional<User> findUser = userRepository.findById(id);
        userValidator.validateUserNotFound(findUser);

        User user = findUser.get();
        user.setFullName(request.getFullName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        return user;
    }

}
