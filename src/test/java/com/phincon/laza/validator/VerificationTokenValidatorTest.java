package com.phincon.laza.validator;

import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.exception.custom.NotProcessException;
import com.phincon.laza.model.entity.VerificationToken;
import com.phincon.laza.repository.VerificationTokenRepository;
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
public class VerificationTokenValidatorTest {
    @Mock
    private VerificationTokenRepository verificationTokenRepository;

    @InjectMocks
    private VerificationTokenValidator verificationTokenValidator;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testValidateVerificationToken_thenNotFound() {
        Optional<VerificationToken> findToken = Optional.empty();
        lenient().when(verificationTokenRepository.findById(any())).thenReturn(findToken);

        assertThrows(NotFoundException.class, () -> {
            verificationTokenValidator.validateVerificationTokenNotFound(findToken);
        });
    }

    @Test
    public void testValidateVerificationToken_thenAlreadyConfirm() {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setConfirmedAt(LocalDateTime.now());

        Optional<VerificationToken> findToken = Optional.of(verificationToken);
        lenient().when(verificationTokenRepository.findById(any())).thenReturn(findToken);

        assertThrows(NotProcessException.class, () -> {
            verificationTokenValidator.validateVerificationTokenAlreadyConfirm(findToken);
        });
    }

    @Test
    public void testValidateVerificationToken_thenAlreadyExpire() {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setExpiresAt(LocalDateTime.now().minusMinutes(1));

        Optional<VerificationToken> findToken = Optional.of(verificationToken);
        lenient().when(verificationTokenRepository.findById(any())).thenReturn(findToken);

        assertThrows(NotProcessException.class, () -> {
            verificationTokenValidator.validateVerificationTokenAlreadyExpire(findToken);
        });
    }
}
