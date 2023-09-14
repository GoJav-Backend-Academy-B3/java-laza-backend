package com.phincon.laza.validator;

import com.phincon.laza.exception.custom.BadRequestException;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.entity.Role;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RoleValidator {
    public void validateRoleNotFound(Optional<Role> findRole) {
        if (findRole.isEmpty()) {
            throw new NotFoundException("Role not found");
        }
    }

    public void validateRoleDuplicate(List<String> listRole) {
        if (listRole.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream().anyMatch(entry -> entry.getValue() > 1)) {
            throw new BadRequestException("Role has duplicate name");
        }
    }
}
