package com.phincon.laza.service.impl;

import com.phincon.laza.exception.custom.BadRequestException;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.dto.request.AddressRequest;
import com.phincon.laza.model.entity.Address;
import com.phincon.laza.model.entity.User;
import com.phincon.laza.repository.AddressRepository;
import com.phincon.laza.repository.UserRepository;
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

    @Autowired
    private UserRepository userRepository;


    @Override
    public Address add(String username, AddressRequest request){
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            Address address = new Address();

           address.setCity_id(request.getCity_id());
           address.setProvince_id(request.getProvince_id());
            address.setPrimary(request.isPrimary());
            address.setReceiverName(request.getReceiverName());
            address.setPhoneNumber(request.getPhone());
            address.setFullAddress(request.getFullAddress());
            address.setUser(user.get());

            if (request.isPrimary()) {
                addressRepository.setAllAddressesNonPrimary(user.get().getId());
            }

            return addressRepository.save(address);
        }

        throw new NotFoundException("Username doesn't exists");
    }

    @Override
    public List<Address> findAllByUsername(String username){
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            return addressRepository.findAllByUserId(user.get().getId());
        }

        throw new NotFoundException("Username doesn't exists");
    }


    @Override
    public Address findById(Long id) throws Exception {
        return addressRepository.findById(id).orElseThrow(() -> new NotFoundException("Address not found"));
    }

    @Override
    public Address update(String username, Long id, AddressRequest request) throws Exception {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("Username doesn't exists"));

        Optional<Address> optionalAddress = addressRepository.findById(id);

        if (optionalAddress.isPresent()) {
            Address address = optionalAddress.get();

            if (request.isPrimary()) {
                addressRepository.setAllAddressesNonPrimary(user.getId());
            }

            address.setCity_id(request.getCity_id());
            address.setProvince_id(request.getProvince_id());
            address.setPrimary(request.isPrimary());
            address.setReceiverName(request.getReceiverName());
            address.setPhoneNumber(request.getPhone());
            address.setFullAddress(request.getFullAddress());
            address.setUser(user);


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
