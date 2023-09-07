package com.phincon.laza.validator;

import com.phincon.laza.exception.custom.BadRequestException;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.exception.custom.NotProcessException;
import com.phincon.laza.model.entity.VerificationCode;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class VerificationCodeValidator {
    public void validateVerificationCodeNotFound(Optional<VerificationCode> findCode) {
        if (findCode.isEmpty()) {
            throw new NotFoundException("Code is not found");
        }
    }

    public void validateVerificationCodeAlreadyConfirm(Optional<VerificationCode> findCode) {
        if (Objects.nonNull(findCode.get().getConfirmedAt())) {
            throw new NotProcessException("Account is already verification");
        }
    }

    public void validateVerificationCodeNotAlreadyConfirm(Optional<VerificationCode> findCode) {
        if (Objects.isNull(findCode.get().getConfirmedAt())) {
            throw new BadRequestException("Account is not already verification!, please verification");
        }
    }

    public void validateVerificationCodeAlreadyExpire(Optional<VerificationCode> findCode) {
        if (findCode.get().getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new NotProcessException("Code is already expired");
        }
    }
}
