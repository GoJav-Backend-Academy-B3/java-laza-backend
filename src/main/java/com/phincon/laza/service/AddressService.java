package com.phincon.laza.service;

import com.phincon.laza.model.dto.request.AddressRequest;
import com.phincon.laza.model.entity.Address;

import java.util.List;

public interface AddressService {
    Address add(String userId, AddressRequest request);

    List<Address> findAllByUserId(String userId);


    Address findByIdAndByUserId(String userId, Long id) throws Exception;

    Address update(String userId, Long id, AddressRequest request) throws Exception;

    void delete(String userId, Long id) throws Exception;
}
