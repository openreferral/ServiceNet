package org.benetech.servicenet.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.benetech.servicenet.domain.Silo;
import org.benetech.servicenet.domain.UserProfile;
import org.benetech.servicenet.service.dto.ActivityFilterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link org.benetech.servicenet.domain.ActivityFilter}.
 */
public interface ActivityFilterService {

    /**
     * Get all the Postal Codes
     *
     * @return the list of Postal Codes
     */
    Set<String> getPostalCodes();

    /**
     * Get all the Regions
     *
     * @return the list of Regions
     */
    Set<String> getRegions();

    /**
     * Get all the Cities
     *
     * @return the list of Cities
     */
    Set<String> getCities();

    /**
     * Get all the Taxonomies
     *
     * @return the list of Taxonomies
     * @param siloId
     * @param providerName
     */
    Map<String, Set<String>> getTaxonomies(UUID siloId, String providerName);

    /**
     * Get all the Postal Codes
     *
     * @return the list of Postal Codes for Service Providers View
     * @param currentUserProfile
     */
    Set<String> getPostalCodesForServiceProviders(UserProfile currentUserProfile);

    /**
     * Get all the Postal Codes
     *
     * @return the list of Postal Codes for Service Providers View
     * @param silo
     */
    Set<String> getPostalCodesForServiceProviders(Silo silo);

    /**
     * Get all the Regions
     *
     * @return the list of Regions for Service Providers View
     * @param currentUserProfile
     */
    Set<String> getRegionsForServiceProviders(UserProfile currentUserProfile);

    /**
     * Get all the Regions
     *
     * @return the list of Regions for Service Providers View
     * @param silo
     */
    Set<String> getRegionsForServiceProviders(Silo silo);

    /**
     * Get all the Cities for Service Providers View
     *
     * @return the list of Cities
     * @param currentUserProfile
     */
    Set<String> getCitiesForServiceProviders(UserProfile currentUserProfile);

    /**
     * Get all the Cities for Service Providers View
     *
     * @return the list of Cities
     * @param silo
     */
    Set<String> getCitiesForServiceProviders(Silo silo);

    /**
     * Save a activityFilter.
     *
     * @param activityFilterDTO the entity to save.
     * @return the persisted entity.
     */
    ActivityFilterDTO save(ActivityFilterDTO activityFilterDTO);

    /**
     * Save a activityFilter for current user.
     *
     * @param activityFilterDTO the entity to save.
     * @return the persisted entity.
     */
    ActivityFilterDTO saveForCurrentUser(ActivityFilterDTO activityFilterDTO);

    /**
     * Get all the activityFilters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ActivityFilterDTO> findAll(Pageable pageable);

    /**
     * Get one activityFilter by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ActivityFilterDTO> findOne(UUID id);

    /**
     * Delete the activityFilter by id.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    /**
     * Get all the activityFilters for current user.
     *
     * @return the list of entities.
     */
    List<ActivityFilterDTO> getAllForCurrentUser();

    /**
     * Get the activityFilter by name and current user.
     *
     * @param name the name of the filter.
     * @return filter with given name.
     */
    Optional<ActivityFilterDTO> findByNameAndCurrentUser(String name);

    /**
     * Get current user activityFilter.
     */
    ActivityFilterDTO getCurrentUserActivityFilter();

    /**
     * Save a activityFilter for current user.
     *
     * @param activityFilterDTO the entity to save.
     */
    void saveCurrentUserActivityFilter(ActivityFilterDTO activityFilterDTO);
}
