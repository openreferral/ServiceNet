package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Silo;
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

    @Query(value = "SELECT org FROM Organization org "
        + "LEFT JOIN FETCH org.userProfiles profiles "
        + "LEFT JOIN FETCH org.account "
        + "LEFT JOIN FETCH org.locations locs "
        + "LEFT JOIN FETCH org.services srvs "
        + "LEFT JOIN FETCH org.contacts "
        + "LEFT JOIN FETCH org.phones "
        + "LEFT JOIN FETCH org.programs "
        + "LEFT JOIN FETCH locs.regularSchedule lRS "
        + "LEFT JOIN FETCH lRS.openingHours "
        + "LEFT JOIN FETCH locs.holidaySchedules "
        + "LEFT JOIN FETCH locs.langs "
        + "LEFT JOIN FETCH locs.accessibilities "
        + "LEFT JOIN FETCH srvs.regularSchedule sRS "
        + "LEFT JOIN FETCH sRS.openingHours "
        + "LEFT JOIN FETCH srvs.holidaySchedules "
        + "LEFT JOIN FETCH srvs.funding "
        + "LEFT JOIN FETCH srvs.eligibility "
        + "LEFT JOIN FETCH srvs.docs "
        + "LEFT JOIN FETCH srvs.paymentsAccepteds "
        + "LEFT JOIN FETCH srvs.langs "
        + "LEFT JOIN FETCH srvs.taxonomies "
        + "LEFT JOIN FETCH srvs.phones "
        + "LEFT JOIN FETCH srvs.contacts "
        + "WHERE :userProfile MEMBER OF org.userProfiles "
        + "AND org.active = true",
    countQuery = "SELECT COUNT(org) FROM Organization org "
        + "WHERE :userProfile MEMBER OF org.userProfiles "
        + "AND org.active = true")
    Page<Organization> findAllWithUserProfile(Pageable pageable, @Param("userProfile") UserProfile userProfile);

    @Query(value = "SELECT org FROM Organization org "
        + "LEFT JOIN FETCH org.userProfiles profiles "
        + "LEFT JOIN FETCH org.account "
        + "LEFT JOIN FETCH org.locations locs "
        + "LEFT JOIN FETCH org.services srvs "
        + "LEFT JOIN FETCH org.contacts "
        + "LEFT JOIN FETCH org.phones "
        + "LEFT JOIN FETCH org.programs "
        + "LEFT JOIN FETCH locs.regularSchedule lRS "
        + "LEFT JOIN FETCH lRS.openingHours "
        + "LEFT JOIN FETCH locs.holidaySchedules "
        + "LEFT JOIN FETCH locs.langs "
        + "LEFT JOIN FETCH locs.accessibilities "
        + "LEFT JOIN FETCH srvs.regularSchedule sRS "
        + "LEFT JOIN FETCH sRS.openingHours "
        + "LEFT JOIN FETCH srvs.holidaySchedules "
        + "LEFT JOIN FETCH srvs.funding "
        + "LEFT JOIN FETCH srvs.eligibility "
        + "LEFT JOIN FETCH srvs.docs "
        + "LEFT JOIN FETCH srvs.paymentsAccepteds "
        + "LEFT JOIN FETCH srvs.langs "
        + "LEFT JOIN FETCH srvs.taxonomies "
        + "LEFT JOIN FETCH srvs.phones "
        + "LEFT JOIN FETCH srvs.contacts "
        + "WHERE org.id = :id ")
    Organization findOneWithEagerAssociations(@Param("id") UUID id);

    @Query(value = "SELECT org FROM Organization org "
        + "LEFT JOIN org.userProfiles profile "
        + "WHERE profile IN (:userProfiles) AND org.active = true")
    Page<Organization> findAllWithUserProfiles(Pageable pageable, @Param("userProfiles") List<UserProfile> userProfiles);

    @Query(value = "SELECT org FROM Organization org "
        + "LEFT JOIN FETCH org.userProfiles profile "
        + "WHERE profile IN (:userProfiles) "
        + "AND org.id = :id "
        + "AND org.active = true")
    Optional<Organization> findAllWithIdAndUserProfiles(@Param("id") UUID id,
        @Param("userProfiles") List<UserProfile> userProfiles);

    @Query(value = "SELECT org FROM Organization org "
        + "LEFT JOIN FETCH org.userProfiles profile "
        + "WHERE profile IN (:userProfiles) "
        + "AND org.id = :id "
        + "AND org.active = false")
    Optional<Organization> findAllWithIdAndUserProfilesAndNotActive(@Param("id") UUID id,
        @Param("userProfiles") List<UserProfile> userProfiles);

    @Query("SELECT org FROM Organization org WHERE org.id = :id AND :userProfile MEMBER OF org.userProfiles")
    Optional<Organization> findOneWithIdAndUserProfile(@Param("id") UUID id, @Param("userProfile") UserProfile userProfile);

    @Query("SELECT org FROM Organization org WHERE :userProfile NOT MEMBER OF org.userProfiles AND "
        + "org.account.name = :accountName AND org.active = true")
    Page<Organization> findAllWithoutUserProfile(
        @Param("userProfile") UserProfile userProfile,
        @Param("accountName") String accountName, Pageable pageable
    );

    @Query("SELECT org FROM Organization org WHERE org.account.name = :accountName AND org.active = false "
        + "AND :userProfile MEMBER OF org.userProfiles")
    List<Organization> findAllByAccountNameAndNotActiveAndCurrentUser(@Param("accountName") String accountName,
        @Param("userProfile") UserProfile userProfile);

    @Query("SELECT org FROM Organization org "
        + "LEFT JOIN FETCH org.userProfiles profile "
        + "WHERE org.account.name = :accountName "
        + "AND org.active = false "
        + "AND profile IN :userProfiles")
    List<Organization> findAllByAccountNameAndNotActiveAndCurrentUserInUserGroups(@Param("accountName") String accountName,
        @Param("userProfiles") List<UserProfile> userProfiles);

    @Query("SELECT org FROM Organization org WHERE org.account.id = :ownerId")
    Page<Organization> findAllWithOwnerId(@Param("ownerId") UUID ownerId, Pageable pageable);

    Optional<Organization> findFirstByNeedsMatchingIsTrue();

    Optional<Organization> findFirstByNeedsMatchingIsTrueAndIdNot(UUID id);

    @Query("SELECT COUNT(org) FROM Organization org WHERE org.needsMatching = TRUE")
    Long countOrganizationsByNeedsMatchingIsTrue();

    @Query("SELECT org FROM Organization org "
        + "LEFT JOIN FETCH org.account "
        + "LEFT JOIN FETCH org.locations")
    List<Organization> findAllWithEagerAssociations();

    @Query("SELECT org FROM Organization org "
        + "LEFT JOIN FETCH org.account "
        + "LEFT JOIN FETCH org.locations "
        + "WHERE org.id = :id")
    Organization findOneWithEagerProfileAndLocations(@Param("id") UUID id);

    @Query("SELECT org FROM Organization org " +
        "LEFT JOIN FETCH org.contacts " +
        "WHERE org.externalDbId = :externalDbId AND org.account.name = :providerName")
    Optional<Organization> findOneWithEagerAssociationsByExternalDbIdAndProviderName(@Param("externalDbId")
                                                                                         String externalDbId,
                                                                                     @Param("providerName")
                                                                                         String providerName);

    List<Organization> findAllByIdOrExternalDbId(UUID id, String externalDbId);

    @Query("SELECT org FROM Organization org "
        + "WHERE org.account.name != :providerName AND org.active = True")
    List<Organization> findAllByProviderNameNot(@Param("providerName") String providerName);

    @Query("SELECT org.id FROM Organization org "
        + "WHERE org.account.name != :providerName AND org.active = True")
    List<UUID> findAllIdsByProviderNameNot(@Param("providerName") String providerName);

    @Query("SELECT org FROM Organization org WHERE org.id NOT IN :ids "
        + "AND org.account.name != :providerName AND org.active = True")
    List<Organization> findAllByProviderNameNotAnAndIdNotIn(@Param("providerName") String providerName,
        @Param("ids") List<UUID> ids);

    Page<Organization> findAll(Pageable pageable);

    @Query("SELECT org FROM Organization org "
        + "LEFT JOIN FETCH org.account "
        + "LEFT JOIN FETCH org.locations locs "
        + "LEFT JOIN FETCH org.services srvs "
        + "LEFT JOIN FETCH org.contacts "
        + "LEFT JOIN FETCH org.phones "
        + "LEFT JOIN FETCH org.programs "
        + "LEFT JOIN FETCH locs.regularSchedule lRS "
        + "LEFT JOIN FETCH lRS.openingHours "
        + "LEFT JOIN FETCH locs.holidaySchedules "
        + "LEFT JOIN FETCH locs.langs "
        + "LEFT JOIN FETCH locs.accessibilities "
        + "LEFT JOIN FETCH srvs.regularSchedule sRS "
        + "LEFT JOIN FETCH sRS.openingHours "
        + "LEFT JOIN FETCH srvs.holidaySchedules "
        + "LEFT JOIN FETCH srvs.funding "
        + "LEFT JOIN FETCH srvs.eligibility "
        + "LEFT JOIN FETCH srvs.docs "
        + "LEFT JOIN FETCH srvs.paymentsAccepteds "
        + "LEFT JOIN FETCH srvs.langs "
        + "LEFT JOIN FETCH srvs.taxonomies "
        + "LEFT JOIN FETCH srvs.phones "
        + "LEFT JOIN FETCH srvs.contacts "
        + "WHERE org.id = :id OR "
        + "org.externalDbId = :externalDbId OR "
        + "locs.id = :id OR "
        + "locs.externalDbId = :externalDbId OR "
        + "srvs.id = :id OR "
        + "srvs.externalDbId = :externalDbId")
    Organization findOneWithAllEagerAssociationsByIdOrExternalDbId(
        @Param("id") UUID id,
        @Param("externalDbId") String externalDbId
    );

    @Query("SELECT org from Organization org "
        + "LEFT JOIN FETCH org.userProfiles profile "
        + "LEFT JOIN FETCH org.additionalSilos additionalSilos "
        + "WHERE org.id = :id AND "
        + "org.active = true AND "
        + "(profile.silo = :silo OR additionalSilos = :silo)")
    Optional<Organization> findByIdAndSilo(@Param("id") UUID id,  @Param("silo") Silo silo);

    @Query("SELECT org FROM Organization org "
        + "LEFT JOIN org.userProfiles profile "
        + "WHERE (:name IS NULL OR LOWER(org.name) LIKE :name) "
        + "AND (:accountName IS NULL OR org.account.name = :accountName) "
        + "AND org.active = true "
        + "AND org.userProfiles.size > 0")
    Page<Organization> findAllByNameLikeAndAccountNameWithUserProfile(
        @Param("name") String name,
        @Param("accountName") String accountName,
        Pageable pageable
    );

    List<Organization> findAllByAccountNameAndActive(String accountName, boolean active);
}
