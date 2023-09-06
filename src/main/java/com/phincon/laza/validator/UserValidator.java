package com.phincon.laza.validator;

import com.phincon.laza.exception.custom.ConflictException;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.entity.User;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserValidator {

    public void validateUserNotFound(Optional<User> findUser) {
        if (findUser.isEmpty()) {
            throw new NotFoundException("User not found");
        }
    }

    public void validateUsernameIsExists(Optional<User> findByUsername) throws Exception {
        if (findByUsername.isPresent()) {
            throw new ConflictException("Username has been exists");
        }
    }

    public void validateEmailIsExists(Optional<User> findByEmail) throws Exception {
        if (findByEmail.isPresent()) {
            throw new ConflictException("Email has been exists");
        }
    }

    public void validateUserNotIsVerfied(Optional<User> findUser) throws Exception {
        if (Objects.isNull(findUser.get().isVerified()) || !findUser.get().isVerified()) {
            throw new IllegalAccessException("User not verification account!, please confirm account!");
        }
    }
}
