package com.phincon.laza.repository;

import com.phincon.laza.model.entity.EProvider;
import com.phincon.laza.model.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProviderRepository extends JpaRepository<Provider, Long> {
    Optional<Provider> findByName(EProvider name);
}
