package com.phincon.laza.validator;

import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.entity.Role;
import com.phincon.laza.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@ExtendWith(MockitoExtension.class)
public class RoleValidatorTest {
    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleValidator roleValidator;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testValidateRole_thenNotFound() {
        Optional<Role> findRole = Optional.empty();
        lenient().when(roleRepository.findById(any())).thenReturn(findRole);

        assertThrows(NotFoundException.class, () -> {
            roleValidator.validateRoleNotFound(findRole);
        });

        log.info("[COMPLETE] testing validate role then not found");
    }
}
