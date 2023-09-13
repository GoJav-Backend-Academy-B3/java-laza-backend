package com.phincon.laza.validator;

import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.entity.Provider;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProviderValidator {
    public void validateProviderNotFound(Optional<Provider> findProvider) {
        if (findProvider.isEmpty()) {
            throw new NotFoundException("Provider not found");
        }
    }
}
