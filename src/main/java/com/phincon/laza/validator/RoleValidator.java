package com.phincon.laza.validator;

import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.entity.Role;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleValidator {
    public void validateRoleNotFound(Optional<Role> findRole) throws Exception {
        if (findRole.isEmpty()) {
            throw new NotFoundException("Role not found");
        }
    }
}
