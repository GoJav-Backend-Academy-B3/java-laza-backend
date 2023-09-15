package com.phincon.laza.service.impl;

import com.phincon.laza.exception.custom.ConflictException;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.entity.AddressOrderDetail;
import com.phincon.laza.repository.AddressOrderDetailRepository;
import com.phincon.laza.service.AddressOrderDetailService;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressOrderDetailServiceImpl implements AddressOrderDetailService {

    private final AddressOrderDetailRepository addressOrderDetailRepository;

    @Autowired
    public AddressOrderDetailServiceImpl(AddressOrderDetailRepository addressOrderDetailRepository) {
        this.addressOrderDetailRepository = addressOrderDetailRepository;
    }

    @Override
    public List<AddressOrderDetail> getAllAddressOrderDetails() {
        return addressOrderDetailRepository.findAll();
    }

    @Override
    public AddressOrderDetail getAddressOrderDetailById(String id) {
        Optional<AddressOrderDetail> addressOrderDetail = addressOrderDetailRepository.findById(id);
        if (addressOrderDetail.isPresent()) {
            return addressOrderDetail.get();
        } else {
            throw new NotFoundException(String.format("AddressOrderDetail with id %s not found", id));
        }
    }

    @Override
    public AddressOrderDetail createAddressOrderDetail(AddressOrderDetail addressOrderDetail) {
        try {
            return addressOrderDetailRepository.save(addressOrderDetail);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            throw new ConflictException(e.getMessage());
        }

    }

    @Override
    public AddressOrderDetail updateAddressOrderDetail(String id, AddressOrderDetail addressOrderDetail) {
        if (addressOrderDetailRepository.existsById(id)) {
            addressOrderDetail.setId(id);
            return addressOrderDetailRepository.save(addressOrderDetail);
        } else {
            throw new NotFoundException(String.format("AddressOrderDetail with id %s not found", id));
        }
    }

    @Override
    public void deleteAddressOrderDetail(String id) {
        if (addressOrderDetailRepository.existsById(id)) {
            addressOrderDetailRepository.deleteById(id);
        } else {
            throw new NotFoundException(String.format("AddressOrderDetail with id %s not found", id));
        }
    }
}

