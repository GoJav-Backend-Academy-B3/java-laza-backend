package com.phincon.laza.validator;

import com.phincon.laza.exception.custom.BadRequestException;
import com.phincon.laza.exception.custom.ConflictException;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserValidator {
    private final PasswordEncoder passwordEncoder;

    public void validateUserNotFound(Optional<User> findUser) {
        if (findUser.isEmpty()) {
            throw new NotFoundException("User not found");
        }
    }

    public void validateUsernameIsExists(Optional<User> findByUsername) {
        if (findByUsername.isPresent()) {
            throw new ConflictException("Username has been exists");
        }
    }

    public void validateEmailIsExists(Optional<User> findByEmail) {
        if (findByEmail.isPresent()) {
            throw new ConflictException("Email has been exists");
        }
    }

    public void validateUserNotIsVerfied(Optional<User> findUser) {
        if (Objects.isNull(findUser.get().isVerified()) || !findUser.get().isVerified()) {
            throw new BadRequestException("User not verification account!, please confirm account!");
        }
    }

    public void validateUserPasswordNotMatch(String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new BadRequestException("New password and Confirm password do not match!");
        }
    }

    public void validateInvalidOldPassword(String oldPassword, String dbPassword) {
        if (!passwordEncoder.matches(oldPassword, dbPassword)) {
            throw new BadRequestException("Old password is incorrect!");
        }
    }
}
