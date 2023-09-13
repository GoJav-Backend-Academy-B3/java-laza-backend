package com.phincon.laza.repository;

import com.phincon.laza.model.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    Optional<Brand> findByNameAndIsDeletedFalse(String name);

    Page<Brand> findAllByIsDeletedFalse(Pageable pageable);

    Optional<Brand> findByIdAndIsDeletedFalse(Long id);
}
