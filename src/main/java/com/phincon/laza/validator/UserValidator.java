package com.phincon.laza.validator;

import com.phincon.laza.exception.custom.*;
import com.phincon.laza.model.entity.EProvider;
import com.phincon.laza.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
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

    public void validateUserUsernameIsExists(Optional<User> findByUsername) {
        if (findByUsername.isPresent()) {
            throw new ConflictException("Username has been exists");
        }
    }

    public void validateUserEmailIsExists(Optional<User> findByEmail) {
        if (findByEmail.isPresent()) {
            throw new ConflictException("Email has been exists");
        }
    }

    public void validateUserIsVerified(Optional<User> findUser) {
        if (findUser.get().isVerified()) {
            throw new NotProcessException("Account is already verification");
        }
    }

    public void validateUserNotIsVerified(Optional<User> findUser) {
        if (Objects.isNull(findUser.get().isVerified()) || !findUser.get().isVerified()) {
            throw new NotProcessException("Account is not already verification!, please verification");
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

    public void validateUserNotEqualProvider(Optional<User> findUser, String registrationId) {
        if (findUser.get().getProviders().stream()
                .noneMatch(provider -> provider.getName().name().equalsIgnoreCase(registrationId))) {
            List<String> listName = findUser.get().getProviders().stream()
                    .map(provider -> String.valueOf(provider.getName()))
                    .collect(Collectors.toList());

            String result = String.join(", ", listName).toLowerCase();
            throw new OAuth2ProcessingException(String.format("Looks like you're signed up with %s account. Please use your %s account to login.", result, result));
        }
    }

    public void validateUserPasswordNotMatch(String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new BadRequestException("New password and Confirm password do not match");
        }
    }

    public void validateUserCheckPasswordStrength(String password) {
        boolean hasLower = false, hasUpper = false,
                hasDigit = false, specialChar = false;

        Set<Character> set = new HashSet<Character>(
                Arrays.asList('!', '@', '#', '$', '%', '^', '&',
                        '*', '(', ')', '-', '+'));

        for (char i : password.toCharArray())
        {
            if (Character.isLowerCase(i))
                hasLower = true;
            if (Character.isUpperCase(i))
                hasUpper = true;
            if (Character.isDigit(i))
                hasDigit = true;
            if (set.contains(i))
                specialChar = true;
        }

        if (!hasLower && !hasUpper && !specialChar && !hasDigit) {
            throw new BadRequestException("Password so week, please input upper, lower, digit and symbol in character");
        }
    }

    public void validateUserInvalidOldPassword(String oldPassword, String dbPassword) {
        if (Objects.nonNull(dbPassword) && !passwordEncoder.matches(oldPassword, dbPassword)) {
            throw new BadRequestException("Old password is incorrect");
        }
    }
}
