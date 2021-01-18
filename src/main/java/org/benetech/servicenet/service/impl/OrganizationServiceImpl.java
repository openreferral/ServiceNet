package org.benetech.servicenet.service.impl;

import static org.benetech.servicenet.config.Constants.SERVICE_PROVIDER;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.domain.AbstractEntity;
import org.benetech.servicenet.domain.DailyUpdate;
import org.benetech.servicenet.domain.Eligibility;
import org.benetech.servicenet.domain.GeocodingResult;
import org.benetech.servicenet.domain.HolidaySchedule;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.OpeningHours;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.PostalAddress;
import org.benetech.servicenet.domain.RegularSchedule;
import org.benetech.servicenet.domain.RequiredDocument;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.ServiceAtLocation;
import org.benetech.servicenet.domain.ServiceTaxonomy;
import org.benetech.servicenet.domain.Silo;
import org.benetech.servicenet.domain.Taxonomy;
import org.benetech.servicenet.domain.UserGroup;
import org.benetech.servicenet.domain.UserProfile;
import org.benetech.servicenet.domain.enumeration.RecordType;
import org.benetech.servicenet.errors.BadRequestAlertException;
import org.benetech.servicenet.repository.OrganizationErrorRepository;
import org.benetech.servicenet.repository.OrganizationMatchRepository;
import org.benetech.servicenet.repository.OrganizationRepository;
import org.benetech.servicenet.repository.UserProfileRepository;
import org.benetech.servicenet.service.ConflictService;
import org.benetech.servicenet.service.DailyUpdateService;
import org.benetech.servicenet.service.EligibilityService;
import org.benetech.servicenet.service.LocationService;
import org.benetech.servicenet.service.OrganizationService;
import org.benetech.servicenet.service.PhoneService;
import org.benetech.servicenet.service.RequiredDocumentService;
import org.benetech.servicenet.service.ServiceAtLocationService;
import org.benetech.servicenet.service.ServiceService;
import org.benetech.servicenet.service.ServiceTaxonomyService;
import org.benetech.servicenet.service.TaxonomyService;
import org.benetech.servicenet.service.TransactionSynchronizationService;
import org.benetech.servicenet.service.UserService;
import org.benetech.servicenet.service.dto.OpeningHoursRow;
import org.benetech.servicenet.service.dto.OrganizationDTO;
import org.benetech.servicenet.service.dto.OrganizationOptionDTO;
import org.benetech.servicenet.service.dto.PhoneDTO;
import org.benetech.servicenet.service.dto.provider.ProviderLocationDTO;
import org.benetech.servicenet.service.dto.provider.ProviderOrganizationDTO;
import org.benetech.servicenet.service.dto.provider.ProviderRequiredDocumentDTO;
import org.benetech.servicenet.service.dto.provider.ProviderServiceDTO;
import org.benetech.servicenet.service.mapper.LocationMapper;
import org.benetech.servicenet.service.mapper.OrganizationMapper;
import org.benetech.servicenet.service.mapper.ServiceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

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

    private final UserProfileRepository userProfileRepository;

    private final RequiredDocumentService requiredDocumentService;

    private final OrganizationMatchRepository organizationMatchRepository;

    private final ConflictService conflictService;

    private final OrganizationErrorRepository organizationErrorRepository;

    private final PhoneService phoneService;

    private final EntityManager em;

    @SuppressWarnings("PMD.ExcessiveParameterList")
    public OrganizationServiceImpl(OrganizationRepository organizationRepository, OrganizationMapper organizationMapper,
        UserService userService, TransactionSynchronizationService transactionSynchronizationService,
        ServiceMapper serviceMapper, LocationMapper locationMapper, LocationService locationService,
        ServiceService serviceService, ServiceAtLocationService serviceAtLocationService,
        TaxonomyService taxonomyService, ServiceTaxonomyService serviceTaxonomyService,
        DailyUpdateService dailyUpdateService, EligibilityService eligibilityService,
        UserProfileRepository userProfileRepository, RequiredDocumentService requiredDocumentService,
        OrganizationMatchRepository organizationMatchRepository, ConflictService conflictService,
        PhoneService phoneService,
        OrganizationErrorRepository organizationErrorRepository, EntityManager em) {
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
        this.userProfileRepository = userProfileRepository;
        this.requiredDocumentService = requiredDocumentService;
        this.organizationMatchRepository = organizationMatchRepository;
        this.conflictService = conflictService;
        this.organizationErrorRepository = organizationErrorRepository;
        this.phoneService = phoneService;
        this.em = em;
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
        if (!organizationDTO.getUserProfiles().isEmpty()) {
            UserProfile userProfile = organizationDTO.getUserProfiles().iterator().next();
            userProfile = userProfileRepository.findOneByLogin(userProfile.getLogin()).orElse(userProfile);
            addUserProfile(organization, userProfile);
        }
        organization.setUpdatedAt(ZonedDateTime.now());
        organization.setNeedsMatching(true);
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
    public OrganizationDTO saveWithUser(ProviderOrganizationDTO organizationDTO) {
        log.debug("Request to save Organization : {}", organizationDTO);

        Organization organization = organizationMapper.toEntity(organizationDTO);
        UserProfile userProfile = userService.getCurrentUserProfile();
        organization.setAccount(userProfile.getSystemAccount());
        addUserProfile(organization, userProfile);

        if (organization.getId() != null) {
            Organization existingOrganization = findOne(organization.getId()).get();
            organization.setLocations(existingOrganization.getLocations());
            organization.setServices(existingOrganization.getServices());
            organization.setDailyUpdates(existingOrganization.getDailyUpdates());
            organization.setPhones(existingOrganization.getPhones());
        }
        organization.setUpdatedAt(ZonedDateTime.now());
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
        savePhones(organizationDTO, organization);
        saveDailyUpdates(organization, organizationDTO);
        saveOpeningHours(organizationDTO.getOpeningHoursByLocation(), locations);
        saveDatesClosed(organizationDTO.getDatesClosedByLocation(), locations);
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
    public Optional<Organization> findFirstThatNeedsMatching() {
        log.debug("Request to get all Organizations");
        return organizationRepository.findFirstByNeedsMatchingIsTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public Long countOrganizationsByNeedsMatching() {
        return organizationRepository.countOrganizationsByNeedsMatchingIsTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganizationOptionDTO> findAllOptions() {
        log.debug("Request to get all Organizations");
        return organizationRepository.findAll().stream()
            .map(organizationMapper::toOptionDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganizationOptionDTO> findAllOptions(String providerName) {
        log.debug("Request to get Organization options for provider: " + providerName);
        return organizationRepository.findAllByAccountNameAndActive(providerName, true).stream()
            .map(organizationMapper::toOptionDto)
            .collect(Collectors.toCollection(LinkedList::new));
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
    public Page<Organization> findAllByUserProfile(Pageable pageable, UserProfile userProfile) {
        log.debug("Request to get all Organizations which are associated with userProfile: {}", userProfile);
        return organizationRepository.findAllWithUserProfile(pageable, userProfile);
    }

    @Override
    public Page<Organization> findAllByUserGroups(Pageable pageable,
        List<UserGroup> userGroups) {
        List<UserProfile> userProfiles = userProfileRepository.findAllWithUserGroups(userGroups);
        return organizationRepository.findAllWithUserProfiles(pageable, userProfiles);
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

    @Override
    @Transactional(readOnly = true)
    public List<UUID> findAllOtherIds(String providerName) {
        return organizationRepository.findAllIdsByProviderNameNot(providerName);
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
    public Optional<ProviderOrganizationDTO> findOneDTOForProvider(UUID id) {
        log.debug("Request to get Organization : {}", id);
        return findOne(id).map(this::mapToSimpleDto);
    }

    /**
     * Get one organization by id and silo for provider view.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ProviderOrganizationDTO> findOneDTOForProviderAndSilo(UUID id, Silo silo) {
        log.debug("Request to get Organization : {}", id);
        Optional<Organization> optionalOrganization = findOneByIdAndSilo(id, silo);
        if (optionalOrganization.isEmpty()) {
            throw new BadRequestAlertException("There is no organization with this id and silo", "providerRecord", "idnull");
        }
        return optionalOrganization.map(this::mapToSimpleDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Organization> findOne(UUID id) {
        return organizationRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Organization> findOneByIdAndSilo(UUID id, Silo silo) {
        return organizationRepository.findByIdAndSilo(id, silo);
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

    @Override
    @Transactional(readOnly = true)
    public Optional<Organization> findOneWithIdAndUserProfileInUserGroups(UUID id, UserProfile userProfile) {
        List<UserGroup> userGroups = new ArrayList<>(userProfile.getUserGroups());
        List<UserProfile> userProfiles = userProfileRepository.findAllWithUserGroups(userGroups);
        return organizationRepository.findAllWithIdAndUserProfiles(id, userProfiles);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Organization> findOneWithIdAndUserProfileInUserGroupsAndNotActive(UUID id, UserProfile userProfile) {
        List<UserGroup> userGroups = new ArrayList<>(userProfile.getUserGroups());
        List<UserProfile> userProfiles = userProfileRepository.findAllWithUserGroups(userGroups);
        return organizationRepository.findAllWithIdAndUserProfilesAndNotActive(id, userProfiles);
    }

    @Override
    public Optional<Organization> findOneWithIdAndUserProfileAndNotActive(UUID id, UserProfile userProfile) {
        List<UserProfile> userProfiles = Collections.singletonList(userProfile);
        return organizationRepository.findAllWithIdAndUserProfilesAndNotActive(id, userProfiles);
    }

    /**
     * Delete the organization by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Organization : {}", id);
        organizationMatchRepository.deleteByOrganizationRecordIdOrPartnerVersionId(id, id);
        conflictService.deleteByResourceOrPartnerResourceId(id);
        organizationErrorRepository.findAllByOrganizationId(id).forEach(organizationError -> {
            if (organizationError.getDataImportReport() != null) {
                organizationError.getDataImportReport().getOrganizationErrors().remove(organizationError);
            }
            organizationErrorRepository.delete(organizationError);
        });
        organizationRepository.findById(id).ifPresent(org -> {
            org.getUserProfiles().forEach(userProfile -> userProfile.getOrganizations().remove(org));
            organizationRepository.delete(org);
        });
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
    public List<Organization> findAllByAccountNameAndNotActiveAndCurrentUser() {
        log.debug("Request to get deactivated organizations");
        UserProfile userProfile = userService.getCurrentUserProfile();
        List<UserGroup> userGroups = new ArrayList<>(userProfile.getUserGroups());
        List<Organization> organizations;
        if (userGroups.isEmpty()) {
            organizations = organizationRepository
                .findAllByAccountNameAndNotActiveAndCurrentUser(SERVICE_PROVIDER, userProfile);
        } else {
            List<UserProfile> userProfiles = userProfileRepository.findAllWithUserGroups(userGroups);
            organizations = organizationRepository
                .findAllByAccountNameAndNotActiveAndCurrentUserInUserGroups(SERVICE_PROVIDER, userProfiles);
        }

        return organizations;
    }

    @Override
    public Page<OrganizationDTO> findAllByNameLikeAndAccountNameWithUserProfile(
        String name, String accountName, Pageable pageable) {
        return organizationRepository.findAllByNameLikeAndAccountNameWithUserProfile(
            StringUtils.isEmpty(name) ? null : '%' + name.toLowerCase() + '%',
            StringUtils.isEmpty(accountName) ? null : accountName,
            pageable
        )
        .map(organizationMapper::toDto);
    }

    @Override
    public Organization cloneOrganizationForServiceProvider(UUID orgId, UserProfile user) {
        Organization organization = organizationRepository.findOneWithAllEagerAssociationsByIdOrExternalDbId(orgId, null);
        Set<Service> services = organization.getServices();
        Set<Location> locations = organization.getLocations();
        Set<DailyUpdate> dailyUpdates = organization.getDailyUpdates();
        Set<Phone> phones = organization.getPhones();

        Organization orgClone = new Organization(organization);
        orgClone.setId(null);
        orgClone.setUserProfiles(Collections.singleton(user));
        orgClone.setAccount(user.getSystemAccount());
        user.getOrganizations().add(orgClone);
        orgClone.setServices(Collections.emptySet());
        orgClone.setLocations(Collections.emptySet());
        orgClone.setDailyUpdates(Collections.emptySet());
        orgClone.setPhones(Collections.emptySet());
        if (organization.getExternalDbId() != null) {
            orgClone.setExternalDbId(
                String.join(" - ", organization.getExternalDbId(), organization.getAccount().getName())
            );
        }
        orgClone.setReplacedBy(null);
        organizationRepository.save(orgClone);

        Set<Service> clonedServices = cloneServices(services, orgClone);

        Set<Location> clonedLocations = cloneLocations(locations, orgClone);

        HashSet<DailyUpdate> clonedDailyUpdate = new HashSet<>();
        dailyUpdates.forEach((DailyUpdate dailyUpdate) -> {
            DailyUpdate duClone = new DailyUpdate()
                .update(dailyUpdate.getUpdate())
                .createdAt(dailyUpdate.getCreatedAt())
                .expiry(dailyUpdate.getExpiry())
                .organization(orgClone);
            clonedDailyUpdate.add(dailyUpdateService.save(duClone));
        });

        Set<Phone> clonedPhones = new HashSet<>();
        phones.forEach((Phone phone) -> {
            Phone phoneClone = new Phone(phone);
            phone.setOrganization(orgClone);
            clonedPhones.add(phoneService.save(phoneClone));
        });

        orgClone.setServices(clonedServices);
        orgClone.setLocations(clonedLocations);
        orgClone.setDailyUpdates(clonedDailyUpdate);
        orgClone.setPhones(clonedPhones);

        organization.setReplacedBy(orgClone);
        orgClone.setReplacedBy(organization);
        return orgClone;
    }

    private void savePhones(ProviderOrganizationDTO organizationDTO, Organization organization) {
        Set<Phone> phones = new HashSet<>();
        if (organization.getPhones() != null) {
            for (Phone phone : organization.getPhones()) {
                if (phone.getId() != null ) {
                    phoneService.delete(phone.getId());
                }
            }
        }
        if (organizationDTO.getPhones() != null) {
            for (PhoneDTO p : organizationDTO.getPhones()) {
                if (StringUtils.isNotEmpty(p.getNumber())) {
                    p.setOrganizationId(organization.getId());
                    phones.add(phoneService.saveEntity(p));
                }
            }
        }
        organization.setPhones(phones);
    }

    @Override
    public void claimRecords(List<UUID> recordsToClaim) {
        UserProfile currentUserProfile = userService.getCurrentUserProfile();

        recordsToClaim.forEach((UUID recordId) -> cloneOrganizationForServiceProvider(recordId, currentUserProfile));

        currentUserProfile.setHasClaimedRecords(true);
        userService.saveProfile(currentUserProfile);
    }

    @Override
    public void unclaimRecord(UUID recordToUnclaim) {
        Organization orgToUnclaim = organizationRepository.getOne(recordToUnclaim);
        Organization originalOrg = organizationRepository.getOne(orgToUnclaim.getReplacedBy().getId());
        originalOrg.setReplacedBy(null);

        organizationRepository.save(originalOrg);
        delete(recordToUnclaim);
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
        transactionSynchronizationService.registerSynchronizationOfMatchingOrganizations(organization.getId());
    }

    private List<Location> saveLocations(Organization organization, List<ProviderLocationDTO> dtos) {
        List<Location> locations = new ArrayList<>();
        Set<UUID> locationsToKeep = dtos.stream().map(ProviderLocationDTO::getId).collect(Collectors.toSet());
        Set<Location> locationsToRemove = organization.getLocations().stream()
            .filter(l -> !locationsToKeep.contains(l.getId())).collect(Collectors.toSet());
        for (ProviderLocationDTO locationDTO : dtos) {
            Location location = locationMapper.toEntity(locationDTO);
            Location existingLocation = (location.getId() != null) ?
                locationService.findById(location.getId()).get() : null;
            PhysicalAddress physicalAddress = locationMapper.extractPhysicalAddress(locationDTO, existingLocation);
            location.setPhysicalAddress(physicalAddress);
            location.setPostalAddress(locationMapper.extractPostalAddress(locationDTO, existingLocation));
            location.providerName(SERVICE_PROVIDER);
            location.setOrganization(organization);
            location.setName(String.join(", ", physicalAddress.getAddress1(),
                physicalAddress.getCity(), physicalAddress.getStateProvince()));
            location.setOpen247(locationDTO.getOpen247());
            if (existingLocation != null) {
                location.setRegularSchedule(existingLocation.getRegularSchedule());
                location.setHolidaySchedules(existingLocation.getHolidaySchedules());
            }
            locations.add(locationService.saveWithRelations(location));
        }
        for (Location location : locationsToRemove) {
            locationService.delete(location.getId());
        }
        organization.setLocations(new HashSet<>(locations));
        return locations;
    }

    private Set<Service> saveServices(Organization organization, List<ProviderServiceDTO> dtos,
        List<Location> locations) {
        Set<Service> services = new HashSet<>();
        Set<UUID> servicesToKeep = dtos.stream().map(ProviderServiceDTO::getId).collect(Collectors.toSet());
        Set<Service> servicesToRemove = organization.getServices().stream()
            .filter(s -> !servicesToKeep.contains(s.getId())).collect(Collectors.toSet());
        for (ProviderServiceDTO serviceDTO : dtos) {
            Service service = serviceMapper.toEntity(serviceDTO);
            UUID id = service.getId();
            if (id != null) {
                Service existingService = organization.getServices().stream()
                    .filter(s -> s.getId().equals(id)).findFirst().orElse(service);
                service.setTaxonomies(existingService.getTaxonomies());
                service.setLocations(existingService.getLocations());
                service.setEligibility(existingService.getEligibility());
                service.setDocs(existingService.getDocs());
                service.setPhones(existingService.getPhones());
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
            service.setDocs(
                saveDocs(serviceDTO, service)
            );
            service.setPhones(
                savePhones(serviceDTO, service)
            );
            services.add(service);
        }
        for (Service service : servicesToRemove) {
            serviceService.delete(service.getId());
        }
        organization.setServices(services);
        return services;
    }

    private ProviderOrganizationDTO mapToSimpleDto(Organization org) {
        ProviderOrganizationDTO organizationDto = organizationMapper.toSimpleDto(org);
        organizationDto.getServices().forEach(dto -> {
            Service service = org.getServices().stream()
                .filter(s -> s.getId().equals(dto.getId())).findAny().get();
            dto.setLocationIndexes(
                service.getLocations().stream()
                    .map(ServiceAtLocation::getLocation)
                    .map(loc -> organizationDto.getLocations().stream()
                        .map(ProviderLocationDTO::getId).collect(Collectors.toList())
                        .indexOf(loc.getId()))
                    .collect(Collectors.toList())
            );
            dto.setTaxonomyIds(
                service.getTaxonomies().stream()
                    .filter(st -> st.getTaxonomy() != null)
                    .map(st -> st.getTaxonomy().getId().toString())
                    .collect(Collectors.toList())
            );
        });
        return organizationDto;
    }

    private Set<ServiceTaxonomy> saveTaxonomies(ProviderServiceDTO serviceDTO, Service service) {
        HashSet<ServiceTaxonomy> taxonomies = new HashSet<>();
        Set<UUID> existingTaxonomies = (service.getTaxonomies() != null)
            ? service.getTaxonomies().stream()
            .filter(st -> st.getTaxonomy() != null)
            .map(st -> st.getTaxonomy().getId()).collect(Collectors.toSet())
            : Collections.emptySet();
        Set<UUID> dtoTaxonomies = serviceDTO.getTaxonomyIds() != null ? serviceDTO.getTaxonomyIds().stream().map(UUID::fromString).collect(
            Collectors.toSet()) : Collections.emptySet();
        Set<UUID> taxonomiesToAdd = dtoTaxonomies.stream().filter(id -> !existingTaxonomies.contains(id)).collect(
            Collectors.toSet());
        Set<UUID> serviceTaxonomiesToRemove = (service.getTaxonomies() != null)
            ? service.getTaxonomies().stream()
            .filter(st -> st.getTaxonomy() != null && !dtoTaxonomies.contains(st.getTaxonomy().getId()))
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

    private Set<ServiceAtLocation> saveLocations(ProviderServiceDTO serviceDTO, Service service,
        List<Location> locations) {
        Set<Location> serviceLocations = serviceDTO.getLocationIndexes() != null
            ? serviceDTO.getLocationIndexes().stream().map(
                locations::get
            ).collect(Collectors.toSet()) : Collections.emptySet();
        Set<ServiceAtLocation> servicesAtLocation = new HashSet<>();
        Set<UUID> existingLocations = (service.getLocations() != null) ? service.getLocations().stream()
            .map(sat -> sat.getLocation().getId()).collect(Collectors.toSet())
            : Collections.emptySet();
        Set<ServiceAtLocation> servicesAtLocationToRemove = (service.getLocations() != null) ? service.getLocations()
            .stream().filter(sat -> !serviceLocations.contains(sat.getLocation())).collect(Collectors.toSet())
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

    private Set<DailyUpdate> saveDailyUpdates(Organization organization, ProviderOrganizationDTO organizationDTO) {
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

    private Set<RequiredDocument> saveDocs(ProviderServiceDTO serviceDTO, Service service) {
        Set<RequiredDocument> docs = new HashSet<>();
        for (ProviderRequiredDocumentDTO docDto : serviceDTO.getDocs()) {
            Optional<RequiredDocument> existingDocOptional = service.getDocs().stream()
                .filter(doc -> doc.getId().equals(docDto.getId())).findFirst();
            if (existingDocOptional.isPresent()) {
                RequiredDocument requiredDocument = existingDocOptional.get();
                requiredDocument.setDocument(docDto.getDocument());
                docs.add(requiredDocumentService.save(requiredDocument));
            } else {
                RequiredDocument requiredDocument = new RequiredDocument();
                requiredDocument.setDocument(docDto.getDocument());
                requiredDocument.setSrvc(service);
                requiredDocument.setProviderName(SERVICE_PROVIDER);
                docs.add(requiredDocumentService.save(requiredDocument));
            }
        }
        Set<UUID> docIds = docs.stream().map(AbstractEntity::getId).collect(Collectors.toSet());
        Set<RequiredDocument> docsToRemove = service.getDocs().stream()
            .filter(doc -> !docIds.contains(doc.getId())).collect(Collectors.toSet());
        for (RequiredDocument doc : docsToRemove) {
            requiredDocumentService.delete(doc.getId());
        }
        return docs;
    }

    private Set<Phone> savePhones(ProviderServiceDTO providerServiceDTO, Service service) {
        Set<Phone> phones = new HashSet<>();
        if (service.getPhones() != null) {
            for (Phone phone : service.getPhones()) {
                if (phone.getId() != null ) {
                    phoneService.delete(phone.getId());
                }
            }
        }
        if (providerServiceDTO.getPhones() != null) {
            for (PhoneDTO p : providerServiceDTO.getPhones()) {
                if (StringUtils.isNotEmpty(p.getNumber())) {
                    p.setSrvcId(service.getId());
                    phones.add(phoneService.saveEntity(p));
                }
            }
        }
        return phones;
    }

    private Eligibility saveEligibility(Service service, ProviderServiceDTO serviceDTO) {
        Eligibility existingEligibility = service.getEligibility();
        Eligibility eligibility = (existingEligibility != null) ? existingEligibility : new Eligibility();
        if (StringUtils.isNotBlank(serviceDTO.getEligibilityCriteria())) {
            eligibility.setEligibility(serviceDTO.getEligibilityCriteria());
            eligibility.setSrvc(service);
            return eligibilityService.save(eligibility);
        } else if (existingEligibility != null) {
            eligibilityService.delete(existingEligibility.getId());
        }
        return null;
    }

    private void saveOpeningHours(Map<Integer, List<OpeningHoursRow>> openingHoursByLocation, List<Location> locations) {
        if (openingHoursByLocation != null) {
            openingHoursByLocation.forEach((idx, rows) -> {
                Location location = locations.get(idx);
                RegularSchedule regularSchedule = location.getRegularSchedule() != null ? location.getRegularSchedule() : new RegularSchedule();
                regularSchedule.setLocation(location);
                Set<OpeningHours> existingOpeningHours = regularSchedule.getOpeningHours() != null ? regularSchedule.getOpeningHours() : new HashSet<>();
                Set<OpeningHours> openingHoursSet = new HashSet<>();
                rows.forEach(row -> {
                    if (row.getActiveDays() != null && row.getFrom() != null && row.getTo() != null) {
                        for (Integer day : row.getActiveDays()) {
                            OpeningHours openingHours = existingOpeningHours.stream()
                                .filter(oh -> oh.getWeekday().equals(day))
                                .findFirst().orElse(new OpeningHours());
                            openingHours.setOpensAt(row.getFrom());
                            openingHours.setClosesAt(row.getTo());
                            openingHours.setWeekday(day);
                            openingHours.setRegularSchedule(regularSchedule);
                            em.persist(openingHours);
                            openingHoursSet.add(openingHours);
                        }
                    }
                });
                regularSchedule.setOpeningHours(openingHoursSet);
                Set<OpeningHours> openingHoursToRemove = existingOpeningHours.stream().filter(
                    eoh -> !openingHoursSet.contains(eoh)
                ).collect(Collectors.toSet());
                for (OpeningHours openingHourToRemove : openingHoursToRemove) {
                    em.remove(openingHourToRemove);
                }
                em.persist(regularSchedule);

                location.setRegularSchedule(regularSchedule);
            });
        }
    }

    private void saveDatesClosed(Map<Integer, List<String>> datesClosedByLocation, List<Location> locations) {
        if (datesClosedByLocation != null) {
            datesClosedByLocation.forEach((idx, dateStrings) -> {
                Location location = locations.get(idx);
                Set<HolidaySchedule> existingHolidaySchedules = location.getHolidaySchedules() != null ? location.getHolidaySchedules() : new HashSet<>();
                Set<HolidaySchedule> holidaySchedules = new HashSet<>();
                dateStrings.forEach(dateString -> {
                    if (StringUtils.isNotBlank(dateString)) {
                        LocalDate date = ZonedDateTime.parse(dateString).toLocalDate();
                        Optional<HolidaySchedule> existingSchedule = existingHolidaySchedules
                            .stream().filter(
                                hs -> hs.isClosed() && date.equals(hs.getStartDate()) && date
                                    .equals(hs.getEndDate()))
                            .findFirst();
                        HolidaySchedule holidaySchedule;
                        if (existingSchedule.isPresent()) {
                            holidaySchedule = existingSchedule.get();
                        } else {
                            holidaySchedule = new HolidaySchedule();
                            holidaySchedule.setClosed(true);
                            holidaySchedule.setStartDate(date);
                            holidaySchedule.setEndDate(date);
                            holidaySchedule.setLocation(location);
                            holidaySchedule.setProviderName(location.getProviderName());
                            em.persist(holidaySchedule);
                        }
                        holidaySchedules.add(holidaySchedule);
                    }
                });
                Set<HolidaySchedule> schedulesToRemove = existingHolidaySchedules.stream().filter(
                    ehs -> !holidaySchedules.contains(ehs)
                ).collect(Collectors.toSet());
                for (HolidaySchedule scheduleToRemove : schedulesToRemove) {
                    em.remove(scheduleToRemove);
                }
                location.setHolidaySchedules(holidaySchedules);
            });
        }
    }

    private Set<Service> cloneServices(Set<Service> services, Organization orgClone) {
        Set<Service> clonedServices = new HashSet<>();
        services.forEach((Service service) -> {
            Service srvClone = new Service(service);
            srvClone.setId(null);
            srvClone.setProviderName(SERVICE_PROVIDER);
            srvClone.setOrganization(orgClone);
            if (service.getExternalDbId() != null) {
                srvClone.setExternalDbId(
                    String.join(" - ", service.getExternalDbId(), service.getProviderName())
                );
            }
            srvClone.setTaxonomies(Collections.emptySet());
            srvClone.setDocs(Collections.emptySet());
            srvClone.setPhones(Collections.emptySet());
            serviceService.save(srvClone);

            Set<ServiceTaxonomy> serviceTaxonomies = service.getTaxonomies();
            HashSet<ServiceTaxonomy> clonedTaxonomies = new HashSet<>();
            serviceTaxonomies.forEach((ServiceTaxonomy serviceTaxonomy) -> {
                ServiceTaxonomy serviceTaxonomyClone = new ServiceTaxonomy()
                    .taxonomyDetails(serviceTaxonomy.getTaxonomyDetails())
                    .providerName(SERVICE_PROVIDER)
                    .srvc(srvClone);
                if (serviceTaxonomy.getExternalDbId() != null) {
                    serviceTaxonomyClone.externalDbId(
                        String.join(" - ", serviceTaxonomy.getExternalDbId(), serviceTaxonomy.getProviderName())
                    );
                }

                Taxonomy taxonomy = serviceTaxonomy.getTaxonomy();
                if (taxonomy != null) {
                    Taxonomy spExistingTaxonomy = taxonomyService
                        .findByNameAndProviderName(taxonomy.getName(), SERVICE_PROVIDER);
                    if (spExistingTaxonomy == null) {
                        Taxonomy taxonomyClone = new Taxonomy()
                            .name(taxonomy.getName())
                            .taxonomyId(taxonomy.getTaxonomyId())
                            .vocabulary(taxonomy.getVocabulary())
                            .providerName(SERVICE_PROVIDER);
                        if (taxonomy.getExternalDbId() != null) {
                            taxonomyClone.externalDbId(
                                String.join(" - ", taxonomy.getExternalDbId(), taxonomy.getProviderName())
                            );
                        }
                        taxonomyService.save(taxonomyClone);
                        serviceTaxonomyClone.taxonomy(taxonomyClone);
                    } else {
                        serviceTaxonomyClone.taxonomy(spExistingTaxonomy);
                    }
                }

                serviceTaxonomyService.save(serviceTaxonomyClone);
                clonedTaxonomies.add(serviceTaxonomyClone);
            });
            srvClone.setTaxonomies(clonedTaxonomies);

            Set<RequiredDocument> docs = service.getDocs();
            HashSet<RequiredDocument> clonedDocs = new HashSet<>();
            docs.forEach((RequiredDocument doc) -> {
                RequiredDocument docClone = new RequiredDocument()
                    .document(doc.getDocument())
                    .providerName(SERVICE_PROVIDER)
                    .srvc(srvClone);
                if (doc.getExternalDbId() != null) {
                    docClone.externalDbId(
                        String.join(" - ", doc.getExternalDbId(), doc.getProviderName())
                    );
                }
                requiredDocumentService.save(docClone);
                clonedDocs.add(docClone);
            });
            srvClone.setDocs(clonedDocs);

            Set<Phone> phones = service.getPhones();
            Set<Phone> clonedPhones = new HashSet<>();
            if (phones != null) {
                phones.forEach((Phone phone) -> {
                    Phone phoneClone = new Phone(phone);
                    phone.setSrvc(srvClone);
                    clonedPhones.add(phoneService.save(phoneClone));
                });
                srvClone.setPhones(clonedPhones);
            }

            Eligibility eligibility = service.getEligibility();
            if (eligibility != null) {
                Eligibility clonedEligibility = new Eligibility()
                    .eligibility(eligibility.getEligibility())
                    .srvc(srvClone);
                eligibilityService.save(clonedEligibility);
                srvClone.setEligibility(clonedEligibility);
            }
            clonedServices.add(serviceService.save(srvClone));
        });
        return clonedServices;
    }

    private Set<Location> cloneLocations(Set<Location> locations, Organization orgClone) {
        Set<Location> clonedLocations = new HashSet<>();

        locations.forEach((Location location) -> {
            Location locClone = new Location(location);
            locClone.setId(null);
            locClone.setProviderName(SERVICE_PROVIDER);
            locClone.setOrganization(orgClone);
            if (location.getExternalDbId() != null) {
                locClone.setExternalDbId(
                    String.join(" - ", location.getExternalDbId(), location.getProviderName())
                );
            }
            locClone.setHolidaySchedules(Collections.emptySet());
            locationService.save(locClone);

            PhysicalAddress physicalAddress = location.getPhysicalAddress();
            PostalAddress postalAddress = location.getPostalAddress();

            if (physicalAddress != null) {
                PhysicalAddress physicalClone = new PhysicalAddress()
                    .attention(physicalAddress.getAttention())
                    .address1(physicalAddress.getAddress1())
                    .address2(physicalAddress.getAddress2())
                    .city(physicalAddress.getCity())
                    .region(physicalAddress.getRegion())
                    .stateProvince(physicalAddress.getStateProvince())
                    .postalCode(physicalAddress.getPostalCode())
                    .country(physicalAddress.getCountry())
                    .location(locClone);
                em.persist(physicalClone);
                locClone.setPhysicalAddress(physicalClone);
            } else if (postalAddress != null) {
                PhysicalAddress physicalClone = new PhysicalAddress()
                    .attention(postalAddress.getAttention())
                    .address1(postalAddress.getAddress1())
                    .address2(postalAddress.getAddress2())
                    .city(postalAddress.getCity())
                    .region(postalAddress.getRegion())
                    .stateProvince(postalAddress.getStateProvince())
                    .postalCode(postalAddress.getPostalCode())
                    .country(postalAddress.getCountry())
                    .location(locClone);
                em.persist(physicalClone);
                locClone.setPhysicalAddress(physicalClone);
            }

            if (postalAddress != null) {
                PostalAddress postalClone = new PostalAddress()
                    .attention(postalAddress.getAttention())
                    .address1(postalAddress.getAddress1())
                    .address2(postalAddress.getAddress2())
                    .city(postalAddress.getCity())
                    .region(postalAddress.getRegion())
                    .stateProvince(postalAddress.getStateProvince())
                    .postalCode(postalAddress.getPostalCode())
                    .country(postalAddress.getCountry())
                    .location(locClone);
                em.persist(postalClone);
                locClone.setPostalAddress(postalClone);
            } else if (physicalAddress != null) {
                PostalAddress postalClone = new PostalAddress()
                    .attention(physicalAddress.getAttention())
                    .address1(physicalAddress.getAddress1())
                    .address2(physicalAddress.getAddress2())
                    .city(physicalAddress.getCity())
                    .region(physicalAddress.getRegion())
                    .stateProvince(physicalAddress.getStateProvince())
                    .postalCode(physicalAddress.getPostalCode())
                    .country(physicalAddress.getCountry())
                    .location(locClone);
                em.persist(postalClone);
                locClone.setPostalAddress(postalClone);
            }

            RegularSchedule regularSchedule = location.getRegularSchedule();
            if (regularSchedule != null) {
                RegularSchedule rsClone = new RegularSchedule()
                    .notes(regularSchedule.getNotes())
                    .location(locClone);
                em.persist(rsClone);
                Set<OpeningHours> clonedOpeningHours = new HashSet<>();
                regularSchedule.getOpeningHours().forEach((OpeningHours oh) -> {
                    OpeningHours ohClone = new OpeningHours()
                        .weekday(oh.getWeekday())
                        .opensAt(oh.getOpensAt())
                        .closesAt(oh.getClosesAt())
                        .regularSchedule(rsClone);
                    em.persist(ohClone);
                    clonedOpeningHours.add(ohClone);
                });
                rsClone.setOpeningHours(clonedOpeningHours);
                locClone.setRegularSchedule(rsClone);
            }

            Set<HolidaySchedule> holidaySchedules = location.getHolidaySchedules();
            HashSet<HolidaySchedule> clonedHs = new HashSet<>();
            holidaySchedules.forEach(((HolidaySchedule hs) -> {
                HolidaySchedule hsClone = new HolidaySchedule()
                    .closed(hs.isClosed())
                    .opensAt(hs.getOpensAt())
                    .closesAt(hs.getClosesAt())
                    .startDate(hs.getStartDate())
                    .endDate(hs.getEndDate())

                    .providerName(SERVICE_PROVIDER)
                    .location(locClone);
                if (hs.getExternalDbId() != null) {
                    hsClone.externalDbId(String.join(" - ", hs.getExternalDbId(), hs.getProviderName()));
                }
                em.persist(hsClone);
                clonedHs.add(hsClone);
            }));
            locClone.setHolidaySchedules(clonedHs);

            location.getGeocodingResults().forEach((GeocodingResult gr) -> {
                gr.getLocations().add(locClone);
                em.persist(gr);
            });

            clonedLocations.add(locClone);
        });
        return clonedLocations;
    }

    private void addUserProfile(Organization organization, UserProfile userProfile) {
        if (organization.getId() != null) {
            Organization existingOrganization = organizationRepository
                .findOneWithEagerProfileAndLocations(organization.getId());
            organization.setUserProfiles(existingOrganization.getUserProfiles());
        }
        Set<UserProfile> userProfiles = organization.getUserProfiles() != null ? organization.getUserProfiles() : new HashSet<>();
        userProfiles.add(userProfile);
        organization.setUserProfiles(userProfiles);
        userProfile.getOrganizations().add(organization);
    }
}
