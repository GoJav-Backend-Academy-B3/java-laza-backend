package com.phincon.laza.validator;

import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.exception.custom.NotProcessException;
import com.phincon.laza.model.entity.VerificationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class VerificationTokenValidator {
    public void validateVerificationTokenNotFound(Optional<VerificationToken> findToken) {
        if (findToken.isEmpty()) {
            throw new NotFoundException("Token is not found");
        }
    }

    public void validateVerificationTokenAlreadyConfirm(Optional<VerificationToken> findToken) {
        if (Objects.nonNull(findToken.get().getConfirmedAt())) {
            throw new NotProcessException("Account is already verification");
        }
    }

    public void validateVerificationTokenAlreadyExpire(Optional<VerificationToken> findToken) {
        if (findToken.get().getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new NotProcessException("Token is already expired");
        }
    }
}
