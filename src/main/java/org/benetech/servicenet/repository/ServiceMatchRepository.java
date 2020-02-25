package org.benetech.servicenet.repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.benetech.servicenet.domain.ServiceMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface ServiceMatchRepository extends JpaRepository<ServiceMatch, UUID> {
    Set<ServiceMatch> findByServiceId(UUID serviceId);

    Optional<ServiceMatch> findByServiceIdAndMatchingServiceId(@Param("serviceId") UUID serviceId,
        @Param("matchingServiceId") UUID matchingServiceId);

    Optional<ServiceMatch> findByMatchingServiceId(UUID id);

    void deleteByServiceIdAndMatchingServiceId(UUID serviceId, UUID matchingServiceId);
}
