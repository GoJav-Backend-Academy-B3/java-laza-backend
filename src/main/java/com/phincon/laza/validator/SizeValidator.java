package com.phincon.laza.validator;


import com.phincon.laza.exception.custom.BadRequestException;
import com.phincon.laza.exception.custom.ConflictException;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.entity.Category;
import com.phincon.laza.model.entity.Size;
import com.phincon.laza.repository.CategoryRepository;
import com.phincon.laza.repository.SizeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SizeValidator {
    private final SizeRepository sizeRepository;
    public void validateSizeNotFound(Optional<Size> size) throws Exception {
        if (size.isEmpty()) {
            throw new NotFoundException("Size not found");
        }
    }

    public void validateSizeAlreadyExists(String sizeName) throws Exception {
        Optional<Size> existingSize = sizeRepository.findBySize(sizeName);
        if (existingSize.isPresent()) {
            throw new ConflictException("Size already exists");
        }
    }

}
