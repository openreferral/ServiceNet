package org.benetech.servicenet.service.impl;

import static org.benetech.servicenet.config.Constants.SERVICE_PROVIDER;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.domain.AbstractEntity;
import org.benetech.servicenet.domain.DailyUpdate;
import org.benetech.servicenet.domain.Eligibility;
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
import org.benetech.servicenet.service.DailyUpdateService;
import org.benetech.servicenet.service.EligibilityService;
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

    private final DailyUpdateService dailyUpdateService;

    private final EligibilityService eligibilityService;

    public OrganizationServiceImpl(OrganizationRepository organizationRepository, OrganizationMapper organizationMapper,
        UserService userService, TransactionSynchronizationService transactionSynchronizationService,
        ServiceMapper serviceMapper, LocationMapper locationMapper, LocationService locationService,
        ServiceService serviceService, ServiceAtLocationService serviceAtLocationService,
        TaxonomyService taxonomyService, ServiceTaxonomyService serviceTaxonomyService,
        DailyUpdateService dailyUpdateService, EligibilityService eligibilityService) {
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
        this.dailyUpdateService = dailyUpdateService;
        this.eligibilityService = eligibilityService;
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
        UserProfile userProfile = userService.getCurrentUserProfile();
        organization.setAccount(userProfile.getSystemAccount());
        organization.setUserProfiles(Collections.singleton(userProfile));

        if (organization.getId() != null) {
            Organization existingOrganization = findOne(organization.getId()).get();
            organization.setLocations(existingOrganization.getLocations());
            organization.setServices(existingOrganization.getServices());
            organization.setDailyUpdates(existingOrganization.getDailyUpdates());
        }
        organization = organizationRepository.save(organization);

        List<Location> locations = saveLocations(
            organization,
            (organizationDTO.getLocations()) != null ? organizationDTO.getLocations() : Collections.emptyList()
        );
        saveServices(
            organization,
            (organizationDTO.getServices()) != null ? organizationDTO.getServices() : Collections.emptyList(),
            locations
        );
        saveDailyUpdates(organization, organizationDTO);
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
        log.debug("Request to get all organizations");
        return organizationRepository.findAll(pageable)
            .map(organizationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Organization> findAllOrganizations(UserProfile userProfile, Pageable pageable) {
        log.debug("Request to get all organizations without user profile");
        return organizationRepository.findAllWithoutUserProfile(userProfile, SERVICE_PROVIDER, pageable);
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
        return findOne(id).map(this::mapToSimpleDto);
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

    @Override
    @Transactional(readOnly = true)
    public Optional<Organization> findOneWithIdAndUserProfile(UUID id, UserProfile userProfile) {
        return organizationRepository.findOneWithIdAndUserProfile(id, userProfile);
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

    /**
     * Deactivate the organization by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void deactivate(UUID id) {
        log.debug("Request to deactivate Organization : {}", id);
        Organization organization = organizationRepository.findById(id).orElse(null);
        if (organization != null) {
            organization.setActive(false);
            organization.setDeactivatedAt(ZonedDateTime.now());
            organizationRepository.save(organization);
        }
    }

    /**
     * Deactivate the organization by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void reactivate(UUID id) {
        log.debug("Request to reactivate Organization : {}", id);
        Organization organization = organizationRepository.findById(id).orElse(null);
        if (organization != null) {
            organization.setActive(true);
            organization.setDeactivatedAt(null);
            organizationRepository.save(organization);
        }
    }

    @Override
    public List<Organization> findAllByAccountNameAndNotActive() {
        log.debug("Request to get deactivated organizations");
        List<Organization> organizations = organizationRepository
            .findAllByAccountNameAndNotActive(SERVICE_PROVIDER);
        return organizations;
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
        Set<UUID> locationsToKeep = dtos.stream().map(SimpleLocationDTO::getId).collect(Collectors.toSet());
        Set<Location> locationsToRemove = organization.getLocations().stream()
            .filter(l -> !locationsToKeep.contains(l.getId())).collect(Collectors.toSet());
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
        for (Location location : locationsToRemove) {
            locationService.delete(location.getId());
        }
        organization.setLocations(new HashSet<>(locations));
        return locations;
    }

    private Set<Service> saveServices(Organization organization, List<SimpleServiceDTO> dtos,
        List<Location> locations) {
        Set<Service> services = new HashSet<>();
        Set<UUID> servicesToKeep = dtos.stream().map(SimpleServiceDTO::getId).collect(Collectors.toSet());
        Set<Service> servicesToRemove = organization.getServices().stream()
            .filter(s -> !servicesToKeep.contains(s.getId())).collect(Collectors.toSet());
        for (SimpleServiceDTO serviceDTO : dtos) {
            Service service = serviceMapper.toEntity(serviceDTO);
            UUID id = service.getId();
            if (id != null) {
                Service existingService = organization.getServices().stream()
                    .filter(s -> s.getId().equals(id)).findFirst().orElse(service);
                service.setTaxonomies(existingService.getTaxonomies());
                service.setLocations(existingService.getLocations());
                service.setEligibility(existingService.getEligibility());
            }
            service.setProviderName(SERVICE_PROVIDER);
            service.setOrganization(organization);
            service = serviceService.save(service);
            service.setTaxonomies(
                saveTaxonomies(serviceDTO, service)
            );
            service.setLocations(
                saveLocations(serviceDTO, service, locations)
            );
            service.setEligibility(
                saveEligibility(service, serviceDTO)
            );
            services.add(service);
        }
        for (Service service : servicesToRemove) {
            serviceService.delete(service.getId());
        }
        organization.setServices(services);
        return services;
    }

    private SimpleOrganizationDTO mapToSimpleDto(Organization org) {
        SimpleOrganizationDTO organizationDto = organizationMapper.toSimpleDto(org);
        organizationDto.getServices().forEach(dto -> {
            Service service = org.getServices().stream()
                .filter(s -> s.getId().equals(dto.getId())).findAny().get();
            dto.setLocationIndexes(
                service.getLocations().stream()
                    .map(ServiceAtLocation::getLocation)
                    .map(loc -> organizationDto.getLocations().stream()
                        .map(SimpleLocationDTO::getId).collect(Collectors.toList())
                        .indexOf(loc.getId()))
                    .collect(Collectors.toList())
            );
            dto.setTaxonomyIds(
                service.getTaxonomies().stream()
                    .map(st -> st.getTaxonomy().getId().toString())
                    .collect(Collectors.toList())
            );
        });
        return organizationDto;
    }

    private Set<ServiceTaxonomy> saveTaxonomies(SimpleServiceDTO serviceDTO, Service service) {
        HashSet<ServiceTaxonomy> taxonomies = new HashSet<>();
        Set<UUID> existingTaxonomies = (service.getTaxonomies() != null)
            ? service.getTaxonomies().stream()
            .map(st -> st.getTaxonomy().getId()).collect(Collectors.toSet())
            : Collections.emptySet();
        Set<UUID> dtoTaxonomies = serviceDTO.getTaxonomyIds().stream().map(UUID::fromString).collect(
            Collectors.toSet());
        Set<UUID> taxonomiesToAdd = dtoTaxonomies.stream().filter(id -> !existingTaxonomies.contains(id)).collect(
            Collectors.toSet());
        Set<UUID> serviceTaxonomiesToRemove = (service.getTaxonomies() != null)
            ? service.getTaxonomies().stream()
            .filter(st -> !dtoTaxonomies.contains(st.getTaxonomy().getId()))
            .map(AbstractEntity::getId).collect(Collectors.toSet())
            : Collections.emptySet();
        for (UUID taxonomyId : taxonomiesToAdd) {
            Optional<Taxonomy> taxonomy = taxonomyService
                .findById(taxonomyId);
            if (taxonomy.isPresent()) {
                ServiceTaxonomy serviceTaxonomy = new ServiceTaxonomy();
                serviceTaxonomy.setSrvc(service);
                serviceTaxonomy.setTaxonomy(taxonomy.get());
                serviceTaxonomy.setProviderName(SERVICE_PROVIDER);
                taxonomies.add(serviceTaxonomyService.save(serviceTaxonomy));
            }
        }
        for (UUID taxonomyId : serviceTaxonomiesToRemove) {
            serviceTaxonomyService.delete(taxonomyId);
        }
        return taxonomies;
    }

    private Set<ServiceAtLocation> saveLocations(SimpleServiceDTO serviceDTO, Service service,
        List<Location> locations) {
        Set<Location> serviceLocations = serviceDTO.getLocationIndexes().stream().map(
            locations::get
        ).collect(Collectors.toSet());
        Set<ServiceAtLocation> servicesAtLocation = new HashSet<>();
        Set<UUID> existingLocations = (service.getLocations() != null) ? service.getLocations().stream()
            .map(sat -> sat.getLocation().getId()).collect(Collectors.toSet())
            : Collections.emptySet();
        Set<ServiceAtLocation> servicesAtLocationToRemove = (service.getLocations() != null) ? service.getLocations().stream()
            .filter(sat -> !serviceLocations.contains(sat.getLocation())).collect(Collectors.toSet())
            : Collections.emptySet();
        for (Location location : serviceLocations) {
            if (location.getId() == null || !existingLocations.contains(location.getId())) {
                ServiceAtLocation serviceAtLocation = new ServiceAtLocation();
                serviceAtLocation.setSrvc(service);
                serviceAtLocation.setLocation(location);
                serviceAtLocation.setProviderName(SERVICE_PROVIDER);
                servicesAtLocation.add(serviceAtLocationService.save(serviceAtLocation));
            }
        }
        for (ServiceAtLocation sat : servicesAtLocationToRemove) {
            serviceAtLocationService.delete(sat.getId());
        }
        return servicesAtLocation;
    }

    private Set<DailyUpdate> saveDailyUpdates(Organization organization, SimpleOrganizationDTO organizationDTO) {
        Set<DailyUpdate> dailyUpdates = organization.getDailyUpdates();
        if (organizationDTO.getUpdate() != null) {
            ZonedDateTime now = ZonedDateTime.now();
            List<DailyUpdate> sortedDailyUpdates = dailyUpdates.stream().sorted(
                Comparator.comparing(DailyUpdate::getCreatedAt)
            ).collect(Collectors.toList());
            DailyUpdate latestDailyUpdate = (sortedDailyUpdates.size() > 0)
                ? sortedDailyUpdates.get(sortedDailyUpdates.size() - 1) : null;
            if (latestDailyUpdate == null || !organizationDTO.getUpdate().equals(latestDailyUpdate.getUpdate())) {
                DailyUpdate dailyUpdate = new DailyUpdate();
                dailyUpdate.setUpdate(organizationDTO.getUpdate());
                dailyUpdate.setOrganization(organization);
                dailyUpdate.setCreatedAt(now);
                dailyUpdates.add(dailyUpdateService.save(dailyUpdate));
                if (latestDailyUpdate != null) {
                    latestDailyUpdate.setExpiry(now);
                    dailyUpdateService.save(latestDailyUpdate);
                }
            }
        }
        return dailyUpdates;
    }

    private Eligibility saveEligibility(Service service, SimpleServiceDTO serviceDTO) {
        Eligibility existingEligibility = service.getEligibility();
        Eligibility eligibility = (existingEligibility != null) ? existingEligibility: new Eligibility();
        if (StringUtils.isNotBlank(serviceDTO.getEligibilityCriteria())) {
            eligibility.setEligibility(serviceDTO.getEligibilityCriteria());
            eligibility.setSrvc(service);
            return eligibilityService.save(eligibility);
        } else if (existingEligibility != null) {
            eligibilityService.delete(existingEligibility.getId());
        }
        return null;
    }
}
