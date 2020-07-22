package org.benetech.servicenet.service;

import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Silo;
import org.benetech.servicenet.domain.UserGroup;
import org.benetech.servicenet.domain.UserProfile;
import org.benetech.servicenet.service.dto.OrganizationDTO;
import org.benetech.servicenet.service.dto.provider.SimpleOrganizationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing Organization.
 */
public interface OrganizationService {

    /**
     * Save a organization.
     *
     * @param organizationDTO the entity to save
     * @return the persisted entity
     */
    OrganizationDTO save(OrganizationDTO organizationDTO);

    /**
     * Save a organization.
     *
     * @param organization the entity to save
     * @return the persisted entity
     */
    Organization save(Organization organization);

    /**
     * Save a organization owned by current user.
     *
     * @param organizationDTO the entity to save
     * @return the persisted entity
     */
    OrganizationDTO saveWithUser(SimpleOrganizationDTO organizationDTO);

    /**
     * Get all the organizations.
     *
     * @return the list of entities
     */
    List<org.benetech.servicenet.service.dto.OrganizationDTO> findAllDTOs();

    List<Organization> findAll();

    Page<Organization> findAllOrganizations(UserProfile userProfile, Pageable pageable);

    List<Organization> findAllWithEagerAssociations();

    List<Organization> findAllOthers(String providerName);

    List<Organization> findAllByUserProfile(UserProfile userProfile);

    List<Organization> findAllOthersExcept(String providerName, List<UUID> exceptIds);

    /**
     * Get all the organizations on page.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<org.benetech.servicenet.service.dto.OrganizationDTO> findAll(Pageable pageable);

    /**
     * Get all the OrganizationDTO where Funding is null.
     *
     * @return the list of entities
     */
    List<org.benetech.servicenet.service.dto.OrganizationDTO> findAllWhereFundingIsNull();

    Optional<Organization> findWithEagerAssociations(String externalDbId, String providerName);

    /**
     * Get all the OrganizationDTO where Funding is null on page.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<org.benetech.servicenet.service.dto.OrganizationDTO> findAllWhereFundingIsNull(Pageable pageable);

    /**
     * Get the "id" organization.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<org.benetech.servicenet.service.dto.OrganizationDTO> findOneDTO(UUID id);

    Optional<Organization> findOne(UUID id);

    Optional<Organization> findOneByIdAndSilo(UUID id, Silo silo);

    Organization findOneWithEagerAssociations(UUID id);

    Optional<Organization> findByIdOrExternalDbId(String id, UUID providerId);

    Optional<Organization> findWithEagerByIdOrExternalDbId(String id);

    /**
     * Delete the "id" organization.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    /**
     * Deactivate the "id" organization.
     *
     * @param id the id of the entity
     */
    void deactivate(UUID id);


    /**
     * Reactivate the "id" organization.
     *
     * @param id the id of the entity
     */
    void reactivate(UUID id);

    Optional<SimpleOrganizationDTO> findOneDTOForProvider(UUID id);

    Optional<SimpleOrganizationDTO> findOneDTOForProviderAndSilo(UUID id, Silo silo);

    Optional<Organization> findOneWithIdAndUserProfile(UUID id, UserProfile userProfile);

    List<Organization> findAllByAccountNameAndNotActiveAndCurrentUser();

    List<Organization> findAllByUserGroups(List<UserGroup> userGroups);

    Optional<Organization> findOneWithIdAndUserProfileInUserGroups(UUID id, UserProfile userProfile);

    Optional<Organization> findOneWithIdAndUserProfileInUserGroupsAndNotActive(UUID id, UserProfile userProfile);
}
