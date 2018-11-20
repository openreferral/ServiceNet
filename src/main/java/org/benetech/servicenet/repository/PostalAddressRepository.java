package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.PostalAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PostalAddress entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PostalAddressRepository extends JpaRepository<PostalAddress, Long> {

}
