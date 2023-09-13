package com.phincon.laza.validator;

import com.phincon.laza.exception.custom.BadRequestException;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.exception.custom.NotProcessException;
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
}
