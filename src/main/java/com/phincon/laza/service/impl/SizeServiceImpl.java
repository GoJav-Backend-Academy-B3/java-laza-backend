package com.phincon.laza.service.impl;


import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.dto.request.SizeRequest;
import com.phincon.laza.model.entity.Brand;
import com.phincon.laza.model.entity.Category;
import com.phincon.laza.model.entity.Size;
import com.phincon.laza.repository.SizeRepository;
import com.phincon.laza.service.SizeService;
import com.phincon.laza.service.SizeService;
import com.phincon.laza.validator.SizeValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SizeServiceImpl implements SizeService {
    private final SizeRepository sizeRepository;
    private final SizeValidator sizeValidator;

    public List<Size> getAllSize() {
        return sizeRepository.findAll();
    }

    public Size getSizeById(Long id) throws Exception {
        Optional<Size> sizeOptional = sizeRepository.findById(id);
        sizeValidator.validateSizeNotFound(sizeOptional);
        return sizeOptional.get();
    }
    public Size getSizeByName(String size) throws Exception {
        Optional<Size> categori = sizeRepository.findBySize(size);
        sizeValidator.validateSizeNotFound(categori);
        return categori.get();
    }

    public Size save(SizeRequest sizeRequest) throws Exception {
        String sizeName = sizeRequest.getSize();
        sizeValidator.validateSizeAlreadyExists(sizeName);
        Size size = new Size();
        size.setSize(sizeName);

        return sizeRepository.save(size);
    }

    public Size update(Long id, SizeRequest updatedSize) throws Exception {
        Optional<Size> existingSizeOptional = sizeRepository.findById(id);
        sizeValidator.validateSizeNotFound(existingSizeOptional);
        Size existingSize = existingSizeOptional.get();
        String updatedSizeName = updatedSize.getSize();
        existingSize.setSize(updatedSizeName);
        return sizeRepository.save(existingSize);
    }

    public void delete(Long id) throws Exception {
        Optional<Size> existingSizeOptional = sizeRepository.findById(id);

        if (existingSizeOptional.isPresent()) {
            Size sizes = existingSizeOptional.get();

            sizes.setIsDeleted(true);
            sizeRepository.save(sizes);
            return;
        }
        sizeValidator.validateSizeNotFound(existingSizeOptional);
    }
}
