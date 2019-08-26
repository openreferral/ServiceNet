package org.benetech.servicenet.service.impl;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.benetech.servicenet.domain.ActivityFilter;
import org.benetech.servicenet.domain.Taxonomy;
import org.benetech.servicenet.domain.User;
import org.benetech.servicenet.repository.ActivityFilterRepository;
import org.benetech.servicenet.repository.GeocodingResultRepository;
import org.benetech.servicenet.repository.TaxonomyRepository;
import org.benetech.servicenet.service.ActivityFilterService;
import org.benetech.servicenet.service.UserService;
import org.benetech.servicenet.service.dto.ActivityFilterDTO;
import org.benetech.servicenet.service.mapper.ActivityFilterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ActivityFilter}.
 */
@Service
@Transactional
public class ActivityFilterServiceImpl implements ActivityFilterService {

    private final Logger log = LoggerFactory.getLogger(ActivityFilterServiceImpl.class);

    private final GeocodingResultRepository geocodingResultRepository;

    private final TaxonomyRepository taxonomyRepository;

    private final ActivityFilterRepository activityFilterRepository;

    private final ActivityFilterMapper activityFilterMapper;

    private final UserService userService;

    public ActivityFilterServiceImpl(GeocodingResultRepository geocodingResultRepository, UserService userService,
        TaxonomyRepository taxonomyRepository, ActivityFilterRepository activityFilterRepository,
        ActivityFilterMapper activityFilterMapper) {
        this.geocodingResultRepository = geocodingResultRepository;
        this.taxonomyRepository = taxonomyRepository;
        this.activityFilterRepository = activityFilterRepository;
        this.activityFilterMapper = activityFilterMapper;
        this.userService = userService;
    }

    @Override
    public Set<String> getPostalCodes() {
        return geocodingResultRepository.getDistinctPostalCodesFromGeoResults();
    }

    @Override
    public Set<String> getRegions() {
        return geocodingResultRepository.getDistinctRegionsFromGeoResults();
    }

    @Override
    public Set<String> getCities() {
        return geocodingResultRepository.getDistinctCityFromGeoResults();
    }

    @Override
    public Set<String> getTaxonomies() {
        return taxonomyRepository.findAll().stream().map(Taxonomy::getName).collect(Collectors.toSet());
    }

    /**
     * Save a activityFilter.
     *
     * @param activityFilterDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ActivityFilterDTO save(ActivityFilterDTO activityFilterDTO) {
        log.debug("Request to save ActivityFilter : {}", activityFilterDTO);
        ActivityFilter activityFilter = activityFilterMapper.toEntity(activityFilterDTO);
        activityFilter = activityFilterRepository.save(activityFilter);
        return activityFilterMapper.toDto(activityFilter);
    }

    @Override
    public ActivityFilterDTO saveForCurrentUser(ActivityFilterDTO activityFilterDTO) {
        User currentUser = userService.getCurrentUser();
        activityFilterDTO.setUserId(currentUser.getId());

        return save(activityFilterDTO);
    }

    /**
     * Get all the activityFilters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ActivityFilterDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ActivityFilters");
        return activityFilterRepository.findAll(pageable)
            .map(activityFilterMapper::toDto);
    }


    /**
     * Get one activityFilter by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ActivityFilterDTO> findOne(UUID id) {
        log.debug("Request to get ActivityFilter : {}", id);
        return activityFilterRepository.findById(id)
            .map(activityFilterMapper::toDto);
    }

    /**
     * Delete the activityFilter by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete ActivityFilter : {}", id);
        activityFilterRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<ActivityFilterDTO> getAllForCurrentUser() {
        return activityFilterRepository.findByUserIsCurrentUser().stream()
            .map(activityFilterMapper::toDto)
            .collect(Collectors.toSet());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ActivityFilterDTO> findByNameAndCurrentUser(String name) {
        return activityFilterRepository.findByNameAndCurrentUser(name).map(activityFilterMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ActivityFilterDTO getCurrentUserActivityFilter() {
        return activityFilterMapper.toDto(userService.getCurrentUser().getFilter());
    }

    @Override
    public void saveCurrentUserActivityFilter(ActivityFilterDTO activityFilterDTO) {
        User currentUser = userService.getCurrentUser();
        ActivityFilter activityFilter = activityFilterMapper.toEntity(activityFilterDTO);
        activityFilter.setId(null);
        activityFilter.setUser(null);

        if (currentUser.getFilter() != null) {
            activityFilter.setId(currentUser.getFilter().getId());
        }

        activityFilterRepository.save(activityFilter);

        currentUser.setFilter(activityFilter);
        userService.save(currentUser);
    }
}
