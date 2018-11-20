package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.PostalAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the PostalAddress entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PostalAddressRepository extends JpaRepository<PostalAddress, UUID> {

}
