package com.phincon.laza.validator;

import com.phincon.laza.exception.custom.BadRequestException;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.exception.custom.NotProcessException;
import com.phincon.laza.model.entity.EProvider;
import com.phincon.laza.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthValidator {
    private final JwtService jwtService;

    public void validateAuthHeaderNotFound(String authHeader) {
        if (Objects.isNull(authHeader) || !authHeader.startsWith("Bearer ")) {
            throw new NotFoundException("Auth Header not found");
        }
    }

    public void validateAuthTokenInvalid(String refreshToken, UserDetails user) {
        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new BadRequestException("JWT Token invalid");
        }
    }

    public void validateAuthUsernameNull(String username) {
        if (Objects.isNull(username)) {
            throw new NotFoundException("username is not found");
        }
    }

    public void validateAuthEmailNull(String email) {
        if (Objects.isNull(email)) {
            throw new NotProcessException("email not found from OAuth2 provider");
        }
    }
    public void validateAuthProvider(EProvider dbProvider, String reqProvider) {
        if (!dbProvider.equals(EProvider.valueOf(reqProvider.toUpperCase()))) {
            throw new NotProcessException(String.format("Looks like you're signed up with %s account. Please use your %s account to login.", dbProvider, reqProvider));
        }
    }
}
