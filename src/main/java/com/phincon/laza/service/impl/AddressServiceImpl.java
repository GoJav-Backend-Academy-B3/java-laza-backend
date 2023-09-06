package com.phincon.laza.service.impl;

import com.phincon.laza.exception.custom.BadRequestException;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.dto.request.AddressRequest;
import com.phincon.laza.model.entity.Address;
import com.phincon.laza.model.entity.User;
import com.phincon.laza.repository.AddressRepository;
import com.phincon.laza.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public Address add(AddressRequest request) throws Exception {
        Address address = new Address();

        User user = new User();
        user.setId(request.getUserId());

        address.setCity(request.getCity());
        address.setCountry(request.getCountry());
        address.setPrimary(request.isPrimary());
        address.setReceiverName(request.getReceiverName());
        address.setPhoneNumber(request.getPhone());
        address.setUser(user);

        if (request.isPrimary()) {
            addressRepository.setAllAddressesNonPrimary(request.getUserId());
        }

        return addressRepository.save(address);
    }

    @Override
    public List<Address> findAllByUserId(String userId) {
        return addressRepository.findAllByUserId(userId);
    }


    @Override
    public Address findById(Long id) throws Exception {
        return addressRepository.findById(id).orElseThrow(() -> new NotFoundException("Address not found"));
    }

    @Override
    public Address update(Long id, AddressRequest request) throws Exception {
        Optional<Address> optionalAddress = addressRepository.findById(id);

        if (optionalAddress.isPresent()) {
            User user = new User();
            user.setId(request.getUserId());

            Address address = optionalAddress.get();

            address.setCity(request.getCity());
            address.setCountry(request.getCountry());
            address.setPrimary(request.isPrimary());
            address.setReceiverName(request.getReceiverName());
            address.setPhoneNumber(request.getPhone());
            address.setUser(user);

            if (request.isPrimary()) {
                addressRepository.setAllAddressesNonPrimary(request.getUserId());
            }

            return addressRepository.save(address);
        }

        throw new NotFoundException("Address not found");
    }


    @Override
    public void delete(Long id) throws Exception {
        Optional<Address> optionalAddress = addressRepository.findById(id);

        if (optionalAddress.isPresent()) {
            Integer count = addressRepository.countByUserId(optionalAddress.get().getUser().getId());

            if (count == 1) {
                throw new BadRequestException("Cannot delete address");
            }

            if (optionalAddress.get().isPrimary()) {
                addressRepository.delete(optionalAddress.get());

                Address latestAddress = addressRepository.findFirstByUserIdOrderByCreatedAtDesc(optionalAddress.get().getUser().getId());

                latestAddress.setPrimary(true);
                addressRepository.save(latestAddress);
                return;

            } else {
                addressRepository.delete(optionalAddress.get());
                return;
            }
        }

        throw new NotFoundException("Address Not Found");
    }
}
