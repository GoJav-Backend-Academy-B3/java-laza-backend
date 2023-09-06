package com.phincon.laza.repository;

import com.phincon.laza.model.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Address a SET a.isPrimary = false WHERE a.user.id = :userId")
    void setAllAddressesNonPrimary(String userId);

    Address findFirstByUserIdOrderByCreatedAtDesc(String userId);

    List<Address> findAllByUserId(String userId);

    Integer countByUserId(String userId);
}
