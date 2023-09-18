package com.phincon.laza.repository;

import com.phincon.laza.model.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AddressRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private RoleRepository roleRepository;


    private List<User> users = new ArrayList<>();
    private List<Address> addresses = new ArrayList<>();

    private List<City> cities = new ArrayList<>();

    private List<Province> provinces = new ArrayList<>();

    private Set<Role> roles = new HashSet<>();

    @BeforeEach
    public void init() {
        Role roleI = new Role();
        roleI.setName(ERole.valueOf("ADMIN"));
        roleRepository.save(roleI);
        Role findRole = roleRepository.findByName(ERole.valueOf("ADMIN")).get();
        roles.add(findRole);

        User user = new User();
        user.setUsername("user");
        user.setRoles(roles);
        user.setEmail("user@gmail.com");
        user.setPassword("password");
        user.setName("user");
        user.setImageUrl("Image");
        User userNew = userRepository.save(user);
        users.add(userNew);

        Province province = new Province();
        province.setProvinceId("1");
        province.setProvince("Jawa Barat");
        province.setCities(cities);
        provinces.add(provinceRepository.save(province));

        City city = new City();
        city.setCityId("1");
        city.setCityName("Bekasi");
        city.setType("Kota");
        city.setProvinces(province);
        city.setPostalCode("17173");
        city.setAddresses(addresses);
        cities.add(cityRepository.save(city));

        Address address = new Address();
        address.setId(1L);
        address.setPhoneNumber("102499814");
        address.setFullAddress("Jln jln");
        address.setReceiverName("ari");
        address.setPrimary(true);
        address.setUser(user);
        address.setCity(city);
        addresses.add(addressRepository.save(address));

        Address address1 = new Address();
        address1.setId(2L);
        address1.setPhoneNumber("102499814");
        address1.setFullAddress("Jln jln");
        address1.setReceiverName("ari");
        address1.setPrimary(true);
        address1.setUser(user);
        address1.setCity(city);
        addresses.add(addressRepository.save(address1));
    }

    @Test
    public void whenFindAllByUserId_thenCorrectResponse() {
        List<Address> addressList = addressRepository.findAllByUserId(users.get(0).getId());
        assertEquals(2, addressList.size());
    }

    @Test
    public void whenCountByUserId_thenCorrectResponse() {
        Integer count = addressRepository.countByUserId(users.get(0).getId());
        assertEquals(2, count);
    }

    @Test
    public void whenFindByIdAndUserId_thenCorrectResponse() {
        Optional<Address> addressOptional = addressRepository.findByIdAndUserId(1L, users.get(0).getId());
        Address newAddress = addressOptional.get();

        assertEquals("ari", newAddress.getReceiverName());
    }

   /* @Test
    public void whensetAllAddressesNonPrimary_thenCorrectResponse() {
       addressRepository.setAllAddressesNonPrimary(users.get(0).getId());
       List<Address> addressList = addressRepository.findAllByUserId(users.get(0).getId());
       log.info(String.valueOf(addressList.get(0).isPrimary()));


        assertFalse(addressList.get(0).isPrimary());
    }*/


}
