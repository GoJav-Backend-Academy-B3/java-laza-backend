package com.phincon.laza.validator;

import com.phincon.laza.exception.custom.ConflictException;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.entity.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserValidator {

    public void validateUserNotFound(Optional<User> findUser) {
        if (findUser.isEmpty()) {
            throw new NotFoundException("User not found");
        }
    }
}
