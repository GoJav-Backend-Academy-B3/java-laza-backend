package com.phincon.laza.repository;


import com.phincon.laza.model.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, String > {
    Optional<City> findByCityNameIgnoreCaseAndProvincesProvinceId(String name, String provinceId);
    List<City> findByProvincesProvinceId(String provinceId);

}
