package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.UserProfile;
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

    @Query("SELECT org FROM Organization org WHERE :userProfile MEMBER OF org.userProfiles")
    List<Organization> findAllWithUserProfile(@Param("userProfile") UserProfile userProfile);

    @Query("SELECT org FROM Organization org WHERE org.account.id = :ownerId")
    Page<Organization> findAllWithOwnerId(@Param("ownerId") UUID ownerId, Pageable pageable);

    @Query("SELECT org FROM Organization org "
        + "LEFT JOIN FETCH org.account "
        + "LEFT JOIN FETCH org.locations")
    List<Organization> findAllWithEagerAssociations();

    @Query("SELECT org FROM Organization org "
        + "LEFT JOIN FETCH org.account "
        + "LEFT JOIN FETCH org.locations "
        + "WHERE org.id = :id")
    Organization findOneWithEagerAssociations(@Param("id") UUID id);

    @Query("SELECT org FROM Organization org " +
        "LEFT JOIN FETCH org.contacts " +
        "WHERE org.externalDbId = :externalDbId AND org.account.name = :providerName")
    Optional<Organization> findOneWithEagerAssociationsByExternalDbIdAndProviderName(@Param("externalDbId")
                                                                                         String externalDbId,
                                                                                     @Param("providerName")
                                                                                         String providerName);

    List<Organization> findAllByIdOrExternalDbId(UUID id, String externalDbId);

    @Query("SELECT org FROM Organization org WHERE org.account.name != :providerName AND org.active = True")
    List<Organization> findAllByProviderNameNot(@Param("providerName") String providerName);

    @Query("SELECT org FROM Organization org WHERE org.id NOT IN :ids "
        + "AND org.account.name != :providerName AND org.active = True")
    List<Organization> findAllByProviderNameNotAnAndIdNotIn(@Param("providerName") String providerName,
        @Param("ids") List<UUID> ids);

    Page<Organization> findAll(Pageable pageable);
}
