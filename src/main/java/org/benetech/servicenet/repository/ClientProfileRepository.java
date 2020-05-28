package org.benetech.servicenet.repository;

import java.util.Optional;
import java.util.UUID;
import org.benetech.servicenet.domain.ClientProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ClientProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClientProfileRepository extends JpaRepository<ClientProfile, UUID> {

    Optional<ClientProfile> findByClientId(String id);

    void deleteByClientId(String clientId);
}
