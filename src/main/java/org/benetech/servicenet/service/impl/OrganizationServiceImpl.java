package org.benetech.servicenet.service.impl;

import static org.benetech.servicenet.config.Constants.SERVICE_PROVIDER;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.ServiceAtLocation;
import org.benetech.servicenet.domain.ServiceTaxonomy;
import org.benetech.servicenet.domain.Taxonomy;
import org.benetech.servicenet.domain.UserProfile;
import org.benetech.servicenet.domain.enumeration.RecordType;
import org.benetech.servicenet.errors.BadRequestAlertException;
import org.benetech.servicenet.repository.OrganizationRepository;
import org.benetech.servicenet.service.LocationService;
import org.benetech.servicenet.service.OrganizationService;
import org.benetech.servicenet.service.ServiceAtLocationService;
import org.benetech.servicenet.service.ServiceService;
import org.benetech.servicenet.service.ServiceTaxonomyService;
import org.benetech.servicenet.service.TaxonomyService;
import org.benetech.servicenet.service.TransactionSynchronizationService;
import org.benetech.servicenet.service.UserService;
import org.benetech.servicenet.service.dto.OrganizationDTO;
import org.benetech.servicenet.service.dto.provider.SimpleLocationDTO;
import org.benetech.servicenet.service.dto.provider.SimpleServiceDTO;
import org.benetech.servicenet.service.dto.provider.SimpleOrganizationDTO;
import org.benetech.servicenet.service.mapper.LocationMapper;
import org.benetech.servicenet.service.mapper.OrganizationMapper;
import org.benetech.servicenet.service.mapper.ServiceMapper;
import org.benetech.servicenet.service.util.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Organization.
 */
@org.springframework.stereotype.Service
@Transactional
public class OrganizationServiceImpl implements OrganizationService {

    private final Logger log = LoggerFactory.getLogger(OrganizationServiceImpl.class);

    private final OrganizationRepository organizationRepository;

    private final OrganizationMapper organizationMapper;

    private final ServiceMapper serviceMapper;

    private final LocationMapper locationMapper;

    private final LocationService locationService;

    private final ServiceAtLocationService serviceAtLocationService;

    private final ServiceService serviceService;

    private final UserService userService;

    private final TaxonomyService taxonomyService;

    private final TransactionSynchronizationService transactionSynchronizationService;

    private final ServiceTaxonomyService serviceTaxonomyService;

    public OrganizationServiceImpl(OrganizationRepository organizationRepository, OrganizationMapper organizationMapper,
        UserService userService, TransactionSynchronizationService transactionSynchronizationService,
        ServiceMapper serviceMapper, LocationMapper locationMapper, LocationService locationService,
        ServiceService serviceService, ServiceAtLocationService serviceAtLocationService,
        TaxonomyService taxonomyService, ServiceTaxonomyService serviceTaxonomyService) {
        this.organizationRepository = organizationRepository;
        this.organizationMapper = organizationMapper;
        this.userService = userService;
        this.transactionSynchronizationService = transactionSynchronizationService;
        this.serviceMapper = serviceMapper;
        this.locationMapper = locationMapper;
        this.locationService = locationService;
        this.serviceService = serviceService;
        this.serviceAtLocationService = serviceAtLocationService;
        this.taxonomyService = taxonomyService;
        this.serviceTaxonomyService = serviceTaxonomyService;
    }

    /**
     * Save a organization.
     *
     * @param organizationDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public OrganizationDTO save(
        OrganizationDTO organizationDTO) {
        log.debug("Request to save Organization : {}", organizationDTO);

        Organization organization = organizationMapper.toEntity(organizationDTO);
        organization = organizationRepository.save(organization);
        return organizationMapper.toDto(organization);
    }

    /**
     * Save a organization.
     *
     * @param organization the entity to save
     * @return the persisted entity
     */
    @Override
    public Organization save(Organization organization) {
        log.debug("Request to save Organization : {}", organization);

        return organizationRepository.save(organization);
    }

    /**
     * Save a organization with user profile.
     *
     * @param organizationDTO the entity to save
     * @return the persisted entity
     */
    @Transactional
    @Override
    public OrganizationDTO saveWithUser(SimpleOrganizationDTO organizationDTO) {
        log.debug("Request to save Organization : {}", organizationDTO);

        Organization organization = organizationMapper.toEntity(organizationDTO);
        if (organization.getId() == null) {
            UserProfile userProfile = userService.getCurrentUserProfile();
            organization.setAccount(userProfile.getSystemAccount());
            organization.setUserProfiles(Collections.singleton(userProfile));
            // TODO: Remove unique together (external_db_id, account_id) on db for organization and check it only before
            //  creating organization during uploading data from spreadsheets or from external APIs.
            //  Then next line can be removed. Issue: #956
            organization.setExternalDbId(RandomUtil.generateSeriesData());
        }
        organization = organizationRepository.save(organization);

        List<Location> locations = saveLocations(organization, organizationDTO.getLocations());
        saveServices(organization, organizationDTO.getServices(), locations);
        // TODO: Currently the matches are discovered with different external service providers (UWBA, Eden, LAAC, etc).
        //  For the independent user with Service Provider (not the external one) system account type, matching should look
        //  for all that kind of users. Issue: #957
        registerSynchronizationOfMatchingOrganizations(organization);
        return organizationMapper.toDto(organization);
    }

    /**
     * Get all the organizations.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrganizationDTO> findAllDTOs() {
        log.debug("Request to get all Organizations");
        return findAll().stream()
            .map(organizationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Organization> findAll() {
        log.debug("Request to get all Organizations");
        return organizationRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Organization> findAllWithEagerAssociations() {
        log.debug("Request to get all Organizations");
        return organizationRepository.findAllWithEagerAssociations();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Organization> findAllOthers(String providerName) {
        log.debug("Request to get all Organizations which are not associated with provider: {}", providerName);
        return organizationRepository.findAllByProviderNameNot(providerName);
    }

    @Override
    public List<Organization> findAllByUserProfile(UserProfile userProfile) {
        log.debug("Request to get all Organizations which are associated with userProfile: {}", userProfile);
        return organizationRepository.findAllWithUserProfile(userProfile);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Organization> findAllOthersExcept(String providerName, List<UUID> exceptIds) {
        log.debug("Request to get all Organizations which are not associated with provider: {} and not in: {}",
            providerName, exceptIds);
        if (exceptIds.size() > 0) {
            return organizationRepository
                .findAllByProviderNameNotAnAndIdNotIn(providerName, exceptIds);
        } else {
            return organizationRepository.findAllByProviderNameNot(providerName);
        }
    }

    /**
     * Get all the organizations on page.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrganizationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Conflicts");
        return organizationRepository.findAll(pageable)
            .map(organizationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Organization> findAllOrganizations(UserProfile userProfile, Pageable pageable) {
        log.debug("Request to get all Conflicts");
        return organizationRepository.findAllWithoutUserProfile(userProfile, pageable);
    }

    /**
     * get all the organizations where Funding is null.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<OrganizationDTO> findAllWhereFundingIsNull() {
        log.debug("Request to get all organizations where Funding is null");
        return organizationRepository.findAll().stream()
            .filter(organization -> organization.getFunding() == null)
            .map(organizationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the organizations on page.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrganizationDTO> findAllWhereFundingIsNull(Pageable pageable) {
        log.debug("Request to get all organizations where Funding is null");
        return organizationRepository.findAll(pageable)
            //.filter(organization -> organization.getFunding() == null)
            .map(organizationMapper::toDto);
    }

    @Override
    public Optional<Organization> findWithEagerAssociations(String externalDbId, String providerName) {
        return organizationRepository.findOneWithEagerAssociationsByExternalDbIdAndProviderName(externalDbId, providerName);
    }

    /**
     * Get one organization by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<OrganizationDTO> findOneDTO(UUID id) {
        log.debug("Request to get Organization : {}", id);
        return findOne(id)
            .map(organizationMapper::toDto);
    }

    /**
     * Get one organization by id for provider view.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SimpleOrganizationDTO> findOneDTOForProvider(UUID id) {
        log.debug("Request to get Organization : {}", id);
        return findOne(id)
            .map(organizationMapper::toSimpleDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Organization> findOne(UUID id) {
        return organizationRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Organization findOneWithEagerAssociations(UUID id) {
        return organizationRepository.findOneWithEagerAssociations(id);
    }

    @Override
    public Optional<Organization> findByIdOrExternalDbId(String id, UUID providerId) {
        List<Organization> orgList;
        try {
            UUID uuid = UUID.fromString(id);
            orgList = organizationRepository.findAllByIdOrExternalDbId(uuid, id);
        } catch (IllegalArgumentException e) {
            orgList = organizationRepository.findAllByIdOrExternalDbId(null, id);
        }
        return Optional.ofNullable(this.getProvidersOrganization(orgList, providerId));
    }

    @Override
    public Optional<Organization> findWithEagerByIdOrExternalDbId(String id) {
        Organization orgList;
        try {
            UUID uuid = UUID.fromString(id);
            orgList = organizationRepository.findOneWithAllEagerAssociationsByIdOrExternalDbId(uuid, id);
        } catch (IllegalArgumentException e) {
            orgList = organizationRepository.findOneWithAllEagerAssociationsByIdOrExternalDbId(null, id);
        }
        return Optional.ofNullable(orgList);
    }

    /**
     * Delete the organization by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Organization : {}", id);
        organizationRepository.deleteById(id);
    }

    private Organization getProvidersOrganization(List<Organization> organizations, UUID id) {
        for (Organization organization : organizations) {
            if (organization.getAccount().getId().equals(id)) {
                return organization;
            }
        }
        if (!organizations.isEmpty()) {
            throw new BadRequestAlertException("Organization does not belong to the provider.",
                RecordType.ORGANIZATION.toString(), "id");
        }
        return null;
    }

    private void registerSynchronizationOfMatchingOrganizations(Organization organization) {
        transactionSynchronizationService.registerSynchronizationOfMatchingOrganizations(organization);
    }

    private List<Location> saveLocations(Organization organization, List<SimpleLocationDTO> dtos) {
        List<Location> locations = new ArrayList<>();
        for (SimpleLocationDTO locationDTO : dtos) {
            Location location = locationMapper.toEntity(locationDTO);
            Location existingLocation = (location.getId() != null) ?
                locationService.findById(location.getId()).get() : null;
            location.setPhysicalAddress(locationMapper.extractPhysicalAddress(locationDTO, existingLocation));
            location.setPostalAddress(locationMapper.extractPostalAddress(locationDTO, existingLocation));
            location.providerName(SERVICE_PROVIDER);
            location.setOrganization(organization);
            locations.add(locationService.saveWithRelations(location));
        }
        organization.setLocations(new HashSet<>(locations));
        return locations;
    }

    private Set<Service> saveServices(Organization organization, List<SimpleServiceDTO> dtos,
        List<Location> locations) {
        Set<Service> services = new HashSet<>();
        for (SimpleServiceDTO serviceDTO : dtos) {
            Service service = serviceMapper.toEntity(serviceDTO);
            service.setProviderName(SERVICE_PROVIDER);
            service.setOrganization(organization);
            service = serviceService.save(service);
            HashSet<ServiceTaxonomy> taxonomies = new HashSet<>();
            Set<UUID> existingTaxonomies = service.getTaxonomies().stream().map(
                st -> st.getTaxonomy().getId()
            ).collect(Collectors.toSet());
            for (String taxonomyId : serviceDTO.getType()) {
                UUID id = UUID.fromString(taxonomyId);
                if (!existingTaxonomies.contains(id)) {
                    Optional<Taxonomy> taxonomy = taxonomyService
                        .findById(id);
                    if (taxonomy.isPresent()) {
                        ServiceTaxonomy serviceTaxonomy = new ServiceTaxonomy();
                        serviceTaxonomy.setSrvc(service);
                        serviceTaxonomy.setTaxonomy(taxonomy.get());
                        serviceTaxonomy.setProviderName(SERVICE_PROVIDER);
                        taxonomies.add(serviceTaxonomyService.save(serviceTaxonomy));
                    }
                }
            }
            service.setTaxonomies(taxonomies);
            Set<Location> serviceLocations = serviceDTO.getLocationIndexes().stream().map(
                idx -> locations.get(Integer.parseInt(idx))
            ).collect(Collectors.toSet());
            Set<ServiceAtLocation> servicesAtLocation = new HashSet<>();
            Set<UUID> existingLocations = service.getLocations().stream().map(
                sat -> sat.getLocation().getId()
            ).collect(Collectors.toSet());
            for (Location location : serviceLocations) {
                if (location.getId() == null || !existingLocations.contains(location.getId())) {
                    ServiceAtLocation serviceAtLocation = new ServiceAtLocation();
                    serviceAtLocation.setSrvc(service);
                    serviceAtLocation.setLocation(location);
                    serviceAtLocation.setProviderName(SERVICE_PROVIDER);
                    servicesAtLocation.add(serviceAtLocationService.save(serviceAtLocation));
                }
            }
            service.setLocations(servicesAtLocation);
            services.add(service);
        }
        organization.setServices(services);
        return services;
    }
}
