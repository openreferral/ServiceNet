package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.Conflict;
import org.benetech.servicenet.domain.enumeration.ConflictStateEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data  repository for the Conflict entity.
 */
@Repository
public interface ConflictRepository extends JpaRepository<Conflict, UUID> {

    @Query("select conflict from Conflict conflict where conflict.resourceId =:resourceId and conflict.owner.id =:ownerId")
    List<Conflict> findAllWithResourceIdAndOwnerId(
        @Param("resourceId") UUID resourceId, @Param("ownerId") UUID ownerId);

    @Query("select conflict from Conflict conflict where conflict.resourceId =:resourceId " +
            "and conflict.state = 'PENDING'")
    List<Conflict> findAllPendingWithResourceId(@Param("resourceId") UUID resourceId);

    @Query("select max(conflict.stateDate) from Conflict conflict " +
        "where conflict.resourceId =:resourceId and conflict.state = 'PENDING'")
    Optional<ZonedDateTime> findMostRecentStateDate(@Param("resourceId") UUID resourceId);

    Optional<Conflict> findByResourceIdAndAcceptedThisChangeNameAndFieldNameAndState(
        UUID resourceId, String acceptedThisChange, String fieldName, ConflictStateEnum state);
}
