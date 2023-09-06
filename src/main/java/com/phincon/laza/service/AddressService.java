package com.phincon.laza.service;

import com.phincon.laza.model.dto.request.AddressRequest;
import com.phincon.laza.model.entity.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AddressService {
    Address add(String username, AddressRequest request);

    List<Address> findAllByUsername(String username);

    Address findById(Long id) throws Exception;

    Address update(String username, Long id, AddressRequest request) throws Exception;

    void delete(Long id) throws Exception;
}
