package org.benetech.servicenet.service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
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
     */
    Set<String> getTaxonomies();

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
    Set<ActivityFilterDTO> getAllForCurrentUser();

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
