package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * Spring Data  repository for the Organization entity.
 */
@Repository
public interface OrganizationRepository extends JpaRepository<Organization, UUID> {

    @Query("SELECT org FROM Organization org WHERE org.account.id = :ownerId")
    List<Organization> findAllWithOwnerId(@Param("ownerId") UUID ownerId);

    @Query("SELECT org FROM Organization org WHERE org.account.id = :ownerId")
    Page<Organization> findAllWithOwnerId(@Param("ownerId") UUID ownerId, Pageable pageable);

    @Query("SELECT org FROM Organization org " +
        "LEFT JOIN FETCH org.contacts " +
        "WHERE org.externalDbId = :externalDbId AND org.account.name = :providerName")
    Optional<Organization> findOneWithEagerAssociationsByExternalDbIdAndProviderName(@Param("externalDbId")
                                                                                         String externalDbId,
                                                                                     @Param("providerName")
                                                                                         String providerName);

    @Query("SELECT org FROM Organization org WHERE org.account.name != :providerName")
    List<Organization> findAllByProviderNameNot(@Param("providerName") String providerName);

    @Query(value = "SELECT O.* FROM\n" +
                   "(SELECT O.*, C.RECENT, C.RECOMMENDED\n" +
                   "FROM ORGANIZATION O,\n" +
                   "(SELECT RESOURCE_ID, COUNT(RESOURCE_ID) RECOMMENDED, MAX(OFFERED_VALUE_DATE) RECENT\n" +
                   "FROM CONFLICT\n" +
                   "WHERE STATE = 'PENDING'\n" +
                   "GROUP BY RESOURCE_ID\n" +
                   "ORDER BY RESOURCE_ID) C\n" +
                   "WHERE ID = RESOURCE_ID\n" +
                   "AND ACCOUNT_ID = :ownerId) O",
        countQuery = "SELECT COUNT(ID)\n" +
                     "FROM ORGANIZATION O,\n" +
                     "(SELECT RESOURCE_ID, COUNT(RESOURCE_ID) RECOMMENDED, MAX(OFFERED_VALUE_DATE) RECENT\n" +
                     "FROM CONFLICT\n" +
                     "WHERE STATE = 'PENDING'\n" +
                     "GROUP BY RESOURCE_ID\n" +
                     "ORDER BY RESOURCE_ID) C\n" +
                     "WHERE ID = RESOURCE_ID\n" +
                     "AND ACCOUNT_ID = :ownerId\n",
        nativeQuery = true)
    Page<Organization> findAllOrgIdsWithOwnerId(@Param("ownerId") UUID ownerId, Pageable pageable);
}
