package com.phincon.laza.service.impl;

import com.phincon.laza.exception.custom.BadRequestException;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.dto.request.AddressRequest;
import com.phincon.laza.model.entity.Address;
import com.phincon.laza.model.entity.User;
import com.phincon.laza.repository.AddressRepository;
import com.phincon.laza.repository.UserRepository;
import com.phincon.laza.service.AddressService;
import com.phincon.laza.service.RajaongkirService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RajaongkirService rajaongkirService;


    @Override
    public Address add(String userId, AddressRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User Not Found"));

        Address address = new Address();

        Integer count = addressRepository.countByUserId(userId);

        if (count == 0) {
            address.setPrimary(true);
        } else {
            address.setPrimary(request.isPrimary());
        }

        rajaongkirService.existsProvince(request.getProvinceName());
        rajaongkirService.existsCity(request.getCityName());

        address.setCityName(request.getCityName());
        address.setProvinceName(request.getProvinceName());
        address.setReceiverName(request.getReceiverName());
        address.setPhoneNumber(request.getPhone());
        address.setFullAddress(request.getFullAddress());
        address.setUser(user);

        if (request.isPrimary()) {
            addressRepository.setAllAddressesNonPrimary(userId);
        }

        return addressRepository.save(address);

    }

    @Override
    public List<Address> findAllByUserId(String userId) {
        Optional<User> user = userRepository.findById(userId);

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
    public Address update(String userId, Long id, AddressRequest request) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User Not Found"));
        Optional<Address> optionalAddress = addressRepository.findById(id);

        if (optionalAddress.isPresent()) {
            Address address = optionalAddress.get();

            // Jika optionalAddress.get().isPrimary() adalah true dan request.isPrimary() adalah false,
            // jangan ubah isPrimary
            if (optionalAddress.get().isPrimary() && !request.isPrimary()) {
                throw new BadRequestException("Cannot change address primary to non primary");
            }

            if (request.isPrimary()) {
                addressRepository.setAllAddressesNonPrimary(userId);
            }

            rajaongkirService.existsProvince(request.getProvinceName().toLowerCase());
            rajaongkirService.existsCity(request.getCityName().toLowerCase());

            address.setCityName(request.getCityName());
            address.setProvinceName(request.getProvinceName());
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
                throw new BadRequestException("Cannot delete address primary");

            } else {
                addressRepository.delete(optionalAddress.get());
                return;
            }
        }

        throw new NotFoundException("Address Not Found");
    }
}
