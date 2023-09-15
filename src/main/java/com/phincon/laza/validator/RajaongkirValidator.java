package com.phincon.laza.validator;


import com.phincon.laza.exception.custom.BadRequestException;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.entity.Role;
import org.springframework.stereotype.Service;


@Service
public class RajaongkirValidator {

    public void validateCourierWeight(Integer weight) {
        if (weight>30000){
            throw new BadRequestException("The maximum weight is 30000");
        }
    }
}
