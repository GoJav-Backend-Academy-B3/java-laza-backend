package com.phincon.laza.repository;


import com.phincon.laza.model.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, String> {

    Optional<Province> findByProvinceIgnoreCase(String name);
}
