package com.phincon.laza.validator;

import com.phincon.laza.exception.custom.BadRequestException;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.exception.custom.NotProcessException;
import com.phincon.laza.model.entity.VerificationCode;
import com.phincon.laza.repository.VerificationCodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class VerificationCodeValidatorTest {
    @Mock
    private VerificationCodeRepository verificationCodeRepository;

    @InjectMocks
    private VerificationCodeValidator verificationCodeValidator;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testValidateVerificationCode_thenNotFound() {
        Optional<VerificationCode> findCode = Optional.empty();
        lenient().when(verificationCodeRepository.findById(any())).thenReturn(findCode);

        assertThrows(NotFoundException.class, () -> {
            verificationCodeValidator.validateVerificationCodeNotFound(findCode);
        });
    }

    @Test
    public void testValidateVerificationCode_thenAlreadyConfirm() {
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setConfirmedAt(LocalDateTime.now());

        Optional<VerificationCode> findCode = Optional.of(verificationCode);
        lenient().when(verificationCodeRepository.findById(any())).thenReturn(findCode);

        assertThrows(NotProcessException.class, () -> {
            verificationCodeValidator.validateVerificationCodeAlreadyConfirm(findCode);
        });
    }

    @Test
    public void testValidateVerificationCode_thenNotAlreadyConfirm() {
        Optional<VerificationCode> findCode = Optional.of(new VerificationCode());
        lenient().when(verificationCodeRepository.findById(any())).thenReturn(findCode);

        assertThrows(BadRequestException.class, () -> {
            verificationCodeValidator.validateVerificationCodeNotAlreadyConfirm(findCode);
        });
    }

    @Test
    public void testValidateVerificationCode_thenAlreadyExpire() {
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setExpiresAt(LocalDateTime.now().minusMinutes(1));

        Optional<VerificationCode> findCode = Optional.of(verificationCode);
        lenient().when(verificationCodeRepository.findById(any())).thenReturn(findCode);

        assertThrows(NotProcessException.class, () -> {
            verificationCodeValidator.validateVerificationCodeAlreadyExpire(findCode);
        });
    }
}
