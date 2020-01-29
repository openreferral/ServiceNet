package org.benetech.servicenet.repository;

import java.util.Set;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Metadata;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.enumeration.ActionType;
import org.benetech.servicenet.util.CollectionUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data  repository for the Metadata entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MetadataRepository extends JpaRepository<Metadata, UUID> {

    @Query("select metadata from Metadata metadata where metadata.user.login = ?#{principal.username}")
    List<Metadata> findByUserIsCurrentUser();

    @Query("select metadata from Metadata metadata where metadata.resourceId in (:resourceIds)")
    List<Metadata> findByResourceIds(@Param("resourceIds") Set<UUID> resourceIds);

    @Query("select metadata from Metadata metadata where metadata.resourceId = :#{#location.id} "
        + "or metadata.resourceId = :#{#location.physicalAddress?.id} "
        + "or metadata.resourceId = :#{#location.postalAddress?.id} "
        + "or metadata.resourceId = :#{#location.regularSchedule?.id}")
    List<Metadata> findByLocationWithRelations(@Param("location") Location location);

    @Query("select metadata from Metadata metadata where metadata.resourceId = :#{#service.id} "
        + "or metadata.resourceId = :#{#service.eligibility?.id} "
        + "or metadata.resourceId = :#{#service.funding?.id} "
        + "or metadata.resourceId = :#{#service.regularSchedule?.id}")
    List<Metadata> findByServiceWithRelations(@Param("service") Service service);

    default List<Metadata> findByService(@Param("service") Service service) {
        List<Metadata> metadata = findByServiceWithRelations(service);
        if (!service.getHolidaySchedules().isEmpty()) {
            metadata.addAll(findByResourceIds(CollectionUtils.getIds(service.getHolidaySchedules())));
        }
        if (!service.getContacts().isEmpty()) {
            metadata.addAll(findByResourceIds(CollectionUtils.getIds(service.getContacts())));
        }
        if (!service.getDocs().isEmpty()) {
            metadata.addAll(findByResourceIds(CollectionUtils.getIds(service.getDocs())));
        }
        if (!service.getTaxonomies().isEmpty()) {
            metadata.addAll(findByResourceIds(CollectionUtils.getIds(service.getTaxonomies())));
        }
        return metadata;
    }

    default List<Metadata> findByLocation(@Param("location") Location location) {
        List<Metadata> metadata = findByLocationWithRelations(location);
        if (!location.getHolidaySchedules().isEmpty()) {
            metadata.addAll(findByResourceIds(CollectionUtils.getIds(location.getHolidaySchedules())));
        }
        return metadata;
    }

    Optional<Metadata> findFirstByResourceIdAndFieldNameAndReplacementValueOrderByLastActionDateAsc(
                        UUID resourceId,
                        String fieldName,
                        String replacementValue);

    Optional<Metadata> findFirstByResourceIdAndFieldNameAndLastActionTypeOrderByLastActionDateAsc(
                        UUID resourceId,
                        String fieldName,
                        ActionType lastActionType);

    @Query("select metadata from Metadata metadata where metadata.user.id = :id")
    List<Metadata> findAllByUserId(@Param("id") UUID id);
}
