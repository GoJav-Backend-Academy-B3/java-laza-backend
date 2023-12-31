package com.phincon.laza.service.impl;

import com.phincon.laza.exception.custom.BadRequestException;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.dto.request.AddressRequest;
import com.phincon.laza.model.entity.Address;
import com.phincon.laza.model.entity.City;
import com.phincon.laza.model.entity.Province;
import com.phincon.laza.model.entity.User;
import com.phincon.laza.repository.AddressRepository;
import com.phincon.laza.repository.CityRepository;
import com.phincon.laza.repository.ProvinceRepository;
import com.phincon.laza.repository.UserRepository;
import com.phincon.laza.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private CityRepository cityRepository;


    @Override
    public Address add(String userId, AddressRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User Not Found"));

        Address address = new Address();

        Integer addressCount = addressRepository.countByUserId(userId);
        boolean isPrimary = (addressCount == 0) || request.isPrimary();

        Province province = provinceRepository.findByProvinceIgnoreCase(request.getProvinceName()).orElseThrow(() -> new NotFoundException("Province not found"));

        City city = cityRepository.findByCityNameIgnoreCaseAndProvincesProvinceId(request.getCityName(), province.getProvinceId()).orElseThrow(() -> new NotFoundException("City not found"));

        address.setPrimary(isPrimary);
        address.setCity(city);
        address.setReceiverName(request.getReceiverName());
        address.setPhoneNumber(request.getPhone());
        address.setFullAddress(request.getFullAddress());
        address.setUser(user);

        if (isPrimary) {
            addressRepository.setAllAddressesNonPrimary(userId);
        }

        return addressRepository.save(address);

    }

    @Override
    public List<Address> findAllByUserId(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Id doesn't exists"));


        return addressRepository.findAllByUserId(user.getId());

    }

    @Override
    public Address findByIdAndByUserId(String userId, Long id) throws Exception {
        return addressRepository.findByIdAndUserId(id, userId).orElseThrow(() -> new NotFoundException("Address not founc"));
    }

    @Override
    public Address update(String userId, Long id, AddressRequest request) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User Not Found"));

        Address address = addressRepository.findByIdAndUserId(id, userId).orElseThrow(() -> new NotFoundException("Address not founc"));

        Province province = provinceRepository.findByProvinceIgnoreCase(request.getProvinceName()).orElseThrow(() -> new NotFoundException("Province not found"));

        City city = cityRepository.findByCityNameIgnoreCaseAndProvincesProvinceId(request.getCityName(), province.getProvinceId()).orElseThrow(() -> new NotFoundException("City not found"));

            /* Jika optionalAddress.get().isPrimary() adalah true dan request.isPrimary() adalah false,
            jangan ubah isPrimary */
        if (address.isPrimary() && !request.isPrimary()) {
            throw new BadRequestException("Cannot change address primary to non primary");
        }

        if (request.isPrimary()) {
            addressRepository.setAllAddressesNonPrimary(userId);
        }

        address.setCity(city);
        address.setPrimary(request.isPrimary());
        address.setReceiverName(request.getReceiverName());
        address.setPhoneNumber(request.getPhone());
        address.setFullAddress(request.getFullAddress());
        address.setUser(user);

        return addressRepository.save(address);

    }

    @Override
    public void delete(String userId, Long id) throws Exception {
        boolean exists = userRepository.existsById(userId);

        if (exists) {
            Address address = addressRepository.findByIdAndUserId(id, userId).orElseThrow(() -> new NotFoundException("Address Not Found"));

            if (address.isPrimary()) {
                throw new BadRequestException("Cannot delete address primary");

            } else {
                addressRepository.delete(address);
                return;
            }

        }

        throw new NotFoundException("Id doesn't exists");
    }
}
