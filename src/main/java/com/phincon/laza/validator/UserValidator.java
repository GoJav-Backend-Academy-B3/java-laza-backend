package com.phincon.laza.validator;

import com.phincon.laza.exception.custom.BadRequestException;
import com.phincon.laza.exception.custom.ConflictException;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.exception.custom.NotProcessException;
import com.phincon.laza.model.entity.EProvider;
import com.phincon.laza.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
            throw new BadRequestException("User not verification account!, please confirm account");
        }
    }

    public void validateUserBadCredentials(Optional<User> findUser, String password) {
        if (findUser.isPresent() && !passwordEncoder.matches(password, findUser.get().getPassword())) {
            throw new BadRequestException("Username or Password is incorrect");
        }
    }

    public void validateUserBadProviderLocal(Optional<User> findUser) {
        if (!findUser.get().getProviders().stream()
                .anyMatch(provider -> EProvider.LOCAL.equals(provider.getName()))) {
            List<String> listName = findUser.get().getProviders().stream()
                    .map(provider -> String.valueOf(provider.getName()))
                    .collect(Collectors.toList());

            String result = String.join(", ", listName).toLowerCase();
            throw new NotProcessException(String.format("Looks like you're signed up with %s account. Please use your %s account to login.", result, result));
        }
    }

    public void validateUserPasswordNotMatch(String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new BadRequestException("New password and Confirm password do not match");
        }
    }

    public void validateUserInvalidOldPassword(String oldPassword, String dbPassword) {
        if (Objects.nonNull(dbPassword) && !passwordEncoder.matches(oldPassword, dbPassword)) {
            throw new BadRequestException("Old password is incorrect");
        }
    }
}
