package com.phincon.laza.repository;

import com.phincon.laza.model.entity.Category;
import com.phincon.laza.model.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface SizeRepository extends JpaRepository<Size, Long> {
    Optional<Size> findBySize(String size);

}