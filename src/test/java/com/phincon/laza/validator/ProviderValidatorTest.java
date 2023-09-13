package com.phincon.laza.validator;

import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.entity.Provider;
import com.phincon.laza.repository.ProviderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class ProviderValidatorTest {
    @Mock
    private ProviderRepository providerRepository;

    @InjectMocks
    private ProviderValidator providerValidator;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testValidateProvider_thenNotFound() {
        Optional<Provider> findProvider = Optional.empty();
        lenient().when(providerRepository.findById(any())).thenReturn(findProvider);

        assertThrows(NotFoundException.class, () -> {
            providerValidator.validateProviderNotFound(findProvider);
        });
    }
}
