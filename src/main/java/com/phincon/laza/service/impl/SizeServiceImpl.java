package com.phincon.laza.service.impl;


import com.phincon.laza.exception.custom.ConflictException;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.dto.request.CategoryRequest;
import com.phincon.laza.model.dto.request.SizeRequest;
import com.phincon.laza.model.entity.*;
import com.phincon.laza.repository.SizeRepository;
import com.phincon.laza.service.SizeService;
import com.phincon.laza.validator.SizeValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SizeServiceImpl implements SizeService {

    @Autowired
    private  SizeRepository sizeRepository;
    @Autowired
    private  SizeValidator sizeValidator;
    @Override
    public List<Size> getAllSize() {
        return sizeRepository.findAll();
    }

    @Override
    public Size getSizeById(Long id) {
        Optional<Size> sizeOptional = sizeRepository.findById(id);
        if (!sizeOptional.isPresent() || sizeOptional.get().isDeleted()) {
            throw new NotFoundException("Size not found");
        }
        return sizeOptional.get();
    }
    @Override
    public Size save(SizeRequest sizeRequest) {
        String sizeName = sizeRequest.getSize();
        Optional<Size> existingSize = sizeRepository.findBySize(sizeName);
        if (existingSize.isPresent()) {
            throw new ConflictException("Size already exists");
        }
        Size size = new Size();
        size.setSize(sizeName);
        return sizeRepository.save(size);
    }

    @Override
    public Size update(Long id, SizeRequest request){

        Optional<Size> existingSizeOptional = sizeRepository.findById(id);
        if (!existingSizeOptional.isPresent() || existingSizeOptional.get().isDeleted()) {
            throw new NotFoundException("Size not found");
        }
        Size existingSize = existingSizeOptional.get();
        String updatedSizeName = request.getSize();
        existingSize.setSize(updatedSizeName);
        return sizeRepository.save(existingSize);
    }


    @Override
    public void delete(Long id) {
        Optional<Size> existingSizeOptional = sizeRepository.findById(id);

        if (existingSizeOptional.isPresent()) {
            Size sizes = existingSizeOptional.get();

            sizes.setIsDeleted(true);
            sizeRepository.save(sizes);
            return;
        }
        throw new NotFoundException("Sizes Not Found");
    }


}
