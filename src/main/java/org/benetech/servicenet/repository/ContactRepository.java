package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


/**
 * Spring Data  repository for the Contact entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactRepository extends JpaRepository<Contact, UUID> {

    Optional<Contact> findOneByExternalDbIdAndProviderName(String externalDbId, String providerName);
}
