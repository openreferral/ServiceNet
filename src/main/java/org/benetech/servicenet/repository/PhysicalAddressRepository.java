package org.benetech.servicenet.repository;

import java.util.UUID;

import org.benetech.servicenet.domain.PhysicalAddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for the PhysicalAddress entity.
 */
@Repository
public interface PhysicalAddressRepository extends JpaRepository<PhysicalAddress, UUID> {
    Page<PhysicalAddress> findAll(Pageable pageable);
}
