package org.benetech.servicenet.repository;

import java.util.UUID;

import org.benetech.servicenet.domain.PostalAddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for the PostalAddress entity.
 */
@Repository
public interface PostalAddressRepository extends JpaRepository<PostalAddress, UUID> {
    Page<PostalAddress> findAll(Pageable pageable);
}
