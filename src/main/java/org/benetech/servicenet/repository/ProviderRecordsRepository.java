package org.benetech.servicenet.repository;

import static org.benetech.servicenet.config.Constants.SERVICE_PROVIDER;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.domain.AbstractEntity;
import org.benetech.servicenet.domain.DailyUpdate;
import org.benetech.servicenet.domain.Eligibility;
import org.benetech.servicenet.domain.ExclusionsConfig;
import org.benetech.servicenet.domain.GeocodingResult;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.LocationExclusion;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.ServiceTaxonomy;
import org.benetech.servicenet.domain.Silo;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.domain.Taxonomy;
import org.benetech.servicenet.domain.UserProfile;
import org.benetech.servicenet.service.dto.DailyUpdateDTO;
import org.benetech.servicenet.service.dto.LocationRecordDTO;
import org.benetech.servicenet.service.dto.ProviderRecordDTO;
import org.benetech.servicenet.service.dto.ProviderRecordForMapDTO;
import org.benetech.servicenet.service.dto.ServiceRecordDTO;
import org.benetech.servicenet.service.dto.provider.ProviderFilterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@SuppressWarnings("CPD-START")
@Repository
public class ProviderRecordsRepository {

    private static final String ID = "id";

    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String ACTIVE = "active";

    private static final String ACCOUNT = "account";
    private static final String USER_PROFILES = "userProfiles";
    private static final String SILO = "silo";

    private static final String CITY = "locality";
    private static final String REGION = "administrativeAreaLevel2";
    private static final String POSTAL_CODE = "postalCode";
    private static final String GEOCODING_RESULTS = "geocodingResults";
    private static final String LOCATIONS = "locations";
    private static final String ORGANIZATION = "organization";

    private static final String SERVICES = "services";
    private static final String ELIGIBILITY = "eligibility";
    private static final String TAXONOMIES = "taxonomies";
    private static final String TAXONOMY = "taxonomy";

    private static final String SORT_NAME = "name";
    private static final String UPDATED_AT = "updatedAt";

    private static final String LOGIN = "login";
    private static final String UPDATE = "update";
    private static final String EXPIRY = "expiry";
    private static final String CREATED_AT = "createdAt";

    private static final String PHYSICAL_ADDRESS = "physicalAddress";
    private static final String ADDRESS_CITY = "city";
    private static final String STATE_PROVINCE = "stateProvince";
    private static final String ADDRESS_REGION = "region";

    private final EntityManager em;
    private final CriteriaBuilder cb;

    public ProviderRecordsRepository(EntityManager em) {
        this.em = em;
        this.cb = em.getCriteriaBuilder();
    }

    public Page<ProviderRecordDTO> findAllWithFilters(List<UserProfile> userProfiles, UserProfile excludedUserProfile,
        ProviderFilterDTO providerFilterDTO, String search, Pageable pageable) {

        CriteriaQuery<ProviderRecordDTO> queryCriteria = cb.createQuery(ProviderRecordDTO.class);
        Root<Organization> selectRoot = queryCriteria.from(Organization.class);
        Join<Organization, SystemAccount> systemAccountJoin = selectRoot.join(ACCOUNT, JoinType.LEFT);
        Join<Organization, UserProfile> userProfileJoin = selectRoot.join(USER_PROFILES, JoinType.LEFT);

        queryCriteria.select(cb.construct(ProviderRecordDTO.class, selectRoot.get(ID), selectRoot.get(NAME),
            systemAccountJoin.get(ID), userProfileJoin.get(LOGIN), selectRoot.get(UPDATED_AT)));

        addFilters(queryCriteria, selectRoot, systemAccountJoin, userProfileJoin, userProfiles, excludedUserProfile, providerFilterDTO, search);
        queryCriteria.groupBy(selectRoot.get(ID), selectRoot.get(NAME),
            systemAccountJoin.get(ID), userProfileJoin.get(LOGIN), selectRoot.get(UPDATED_AT));
        addSorting(queryCriteria, pageable.getSort(), selectRoot);

        Query query = createQueryWithPageable(queryCriteria, pageable);

        List<ProviderRecordDTO> results = query.getResultList();

        Long total = 0L;
        if (results != null && results.size() > 0) {
            total = this.getTotal(providerFilterDTO, userProfiles, excludedUserProfile, search);

            List<UUID> orgIds = results.stream().map(it -> it.getOrganization().getId()).collect(Collectors.toList());

            Map<UUID, Set<ServiceRecordDTO>> services = getServicesMap(orgIds);
            Map<UUID, Set<LocationRecordDTO>> locations = getLocationsMap(orgIds);
            Map<UUID, Set<DailyUpdateDTO>> dailyUpdates = getDailyUpdatesMap(orgIds);

            results.forEach(result -> {
                result.setServices(services.getOrDefault(result.getOrganization().getId(), new HashSet<>()));
                result.setLocations(locations.getOrDefault(result.getOrganization().getId(), new HashSet<>()));
                result.setDailyUpdates(dailyUpdates.getOrDefault(result.getOrganization().getId(), new HashSet<>()));
            });
        }

        return new PageImpl<>(results, pageable, total.intValue());
    }

    private Map<UUID, Set<ServiceRecordDTO>> getServicesMap(List<UUID> orgIds) {
        CriteriaQuery<ServiceRecordDTO> queryCriteria = cb.createQuery(ServiceRecordDTO.class);
        Root<Service> root = queryCriteria.from(Service.class);
        Join<Service, Organization> orgJoin = root.join(ORGANIZATION, JoinType.LEFT);

        queryCriteria.select(cb.construct(ServiceRecordDTO.class, root.get(ID), root.get(NAME), orgJoin.get(ID)));

        queryCriteria.where(orgJoin.get(ID).in(orgIds));

        TypedQuery<ServiceRecordDTO> query = em.createQuery(queryCriteria);
        List<ServiceRecordDTO> results = query.getResultList();

        return results.stream().collect(Collectors.groupingBy(service -> service.getService().getOrganizationId(), Collectors.toSet()));
    }

    private Map<UUID, Set<LocationRecordDTO>> getLocationsMap(List<UUID> orgIds) {
        CriteriaQuery<LocationRecordDTO> queryCriteria = cb.createQuery(LocationRecordDTO.class);
        Root<Location> root = queryCriteria.from(Location.class);
        Join<Location, Organization> orgJoin = root.join(ORGANIZATION, JoinType.LEFT);
        Join<Location, PhysicalAddress> addressJoin = root.join(PHYSICAL_ADDRESS, JoinType.LEFT);

        queryCriteria.select(cb.construct(LocationRecordDTO.class, addressJoin.get(ID), addressJoin.get(ADDRESS_CITY),
            addressJoin.get(STATE_PROVINCE), addressJoin.get(ADDRESS_REGION), orgJoin.get(ID)));

        queryCriteria.where(orgJoin.get(ID).in(orgIds));

        TypedQuery<LocationRecordDTO> query = em.createQuery(queryCriteria);
        List<LocationRecordDTO> results = query.getResultList();

        return results.stream().collect(Collectors.groupingBy(location -> location.getLocation().getOrganizationId(), Collectors.toSet()));
    }

    private Map<UUID, Set<DailyUpdateDTO>> getDailyUpdatesMap(List<UUID> orgIds) {
        CriteriaQuery<DailyUpdateDTO> queryCriteria = cb.createQuery(DailyUpdateDTO.class);
        Root<DailyUpdate> root = queryCriteria.from(DailyUpdate.class);
        Join<DailyUpdate, Organization> orgJoin = root.join(ORGANIZATION, JoinType.LEFT);

        queryCriteria.select(cb.construct(DailyUpdateDTO.class, root.get(ID), root.get(UPDATE),
            root.get(EXPIRY), root.get(CREATED_AT), orgJoin.get(ID)));

        queryCriteria.where(orgJoin.get(ID).in(orgIds));

        TypedQuery<DailyUpdateDTO> query = em.createQuery(queryCriteria);
        List<DailyUpdateDTO> results = query.getResultList();

        return results.stream().collect(Collectors.groupingBy(DailyUpdateDTO::getOrganizationId, Collectors.toSet()));
    }

    public Page<ProviderRecordDTO> findAllWithFiltersPublic(ProviderFilterDTO providerFilterDTO, Silo silo,
        String search, Pageable pageable) {

        CriteriaQuery<ProviderRecordDTO> queryCriteria = cb.createQuery(ProviderRecordDTO.class);
        Root<Organization> selectRoot = queryCriteria.from(Organization.class);
        Join<Organization, SystemAccount> systemAccountJoin = selectRoot.join(ACCOUNT, JoinType.LEFT);
        Join<Organization, UserProfile> userProfileJoin = selectRoot.join(USER_PROFILES, JoinType.LEFT);

        queryCriteria.select(cb.construct(ProviderRecordDTO.class, selectRoot.get(ID), selectRoot.get(NAME),
            systemAccountJoin.get(ID), userProfileJoin.get(LOGIN), selectRoot.get(UPDATED_AT)));

        addFilters(queryCriteria, selectRoot, systemAccountJoin, userProfileJoin, silo, providerFilterDTO, search);
        queryCriteria.groupBy(selectRoot.get(ID), selectRoot.get(NAME),
            systemAccountJoin.get(ID), userProfileJoin.get(LOGIN), selectRoot.get(UPDATED_AT));
        addSorting(queryCriteria, pageable.getSort(), selectRoot);

        Query query = createQueryWithPageable(queryCriteria, pageable);

        List<ProviderRecordDTO> results = query.getResultList();

        Long total = 0L;
        if (results != null && results.size() > 0) {
            total = this.getTotal(providerFilterDTO, silo, search);
        }

        return new PageImpl<>(results, pageable, total.intValue());
    }

    public Page<ProviderRecordForMapDTO> findAllWithFiltersForMap(
        Pageable pageable, Silo silo,
        ProviderFilterDTO providerFilterDTO,
        String search, List<ExclusionsConfig> exclusions,
        List<Double> boundaries) {

        CriteriaQuery<ProviderRecordForMapDTO> queryCriteria = cb.createQuery(ProviderRecordForMapDTO.class);
        Root<GeocodingResult> selectRoot = queryCriteria.from(GeocodingResult.class);

        Query query = addFiltersAndSelect(pageable, queryCriteria, selectRoot, providerFilterDTO, search, exclusions,
            null, boundaries, silo);

        List<ProviderRecordForMapDTO> results = query.getResultList();

        return new PageImpl<>(results);
    }

    public Page<ProviderRecordForMapDTO> findProviderRecordsForMap(Pageable pageable, UserProfile userProfile,
        ProviderFilterDTO providerFilterDTO, String search, List<ExclusionsConfig> exclusions,
        List<Double> boundaries) {

        CriteriaQuery<ProviderRecordForMapDTO> queryCriteria = cb.createQuery(ProviderRecordForMapDTO.class);
        Root<GeocodingResult> selectRoot = queryCriteria.from(GeocodingResult.class);

        Query query = addFiltersAndSelect(pageable, queryCriteria, selectRoot, providerFilterDTO, search,
            exclusions,
            userProfile, boundaries, null);

        List<ProviderRecordForMapDTO> results = query.getResultList();

        return new PageImpl<>(results);
    }

    private Query createQueryWithPageable(CriteriaQuery<?> queryCriteria, Pageable pageable) {
        Query query = em.createQuery(queryCriteria);
        if (pageable.isPaged()) {
            query
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize());
        }

        return query;
    }

    private <T> Query addFiltersAndSelect(Pageable pageable,
        CriteriaQuery<ProviderRecordForMapDTO> query, Root<GeocodingResult> root,
        ProviderFilterDTO providerFilterDTO, String search, List<ExclusionsConfig> exclusions,
        UserProfile userProfile,
        List<Double> boundaries, Silo silo) {
        Join<GeocodingResult, Location> locationJoin = root.join(LOCATIONS, JoinType.LEFT);
        Join<Location, Organization> organizationJoin = locationJoin.join(ORGANIZATION, JoinType.LEFT);
        Join<Organization, SystemAccount> systemAccountJoin = organizationJoin.join(ACCOUNT, JoinType.LEFT);
        Join<Organization, UserProfile> userProfileJoin = organizationJoin.join(USER_PROFILES, JoinType.LEFT);
        Join<Organization, Service> serviceJoin = organizationJoin.join(SERVICES, JoinType.LEFT);
        Join<Service, Eligibility> eligibilityJoin = serviceJoin.join(ELIGIBILITY, JoinType.LEFT);

        Predicate predicate = getCommonPredicate(
            organizationJoin,
            (silo != null) ? silo : userProfile.getSilo(),
            search,
            systemAccountJoin,
            userProfileJoin,
            serviceJoin, eligibilityJoin
        );

        if (userProfile != null) {
            predicate = cb
                .and(predicate, cb.notEqual(userProfileJoin.get(ID), userProfile.getId()));
        }

        if (boundaries != null && boundaries.size() == 4) {
            // boundaries come in lat_lo,lng_lo,lat_hi,lng_hi format
            if (boundaries.get(1) > boundaries.get(3)) {
                predicate = cb.and(predicate, cb.or(
                    cb.ge(root.get("longitude"), boundaries.get(1)),
                    cb.le(root.get("longitude"), boundaries.get(3))
                ));
            } else {
                predicate = cb.and(predicate, cb.and(
                    cb.ge(root.get("longitude"), boundaries.get(1)),
                    cb.le(root.get("longitude"), boundaries.get(3))
                ));
            }
            predicate = cb.and(predicate, cb.and(
                cb.ge(root.get("latitude"), boundaries.get(0)),
                cb.le(root.get("latitude"), boundaries.get(2))));
        }

        predicate = this.addTaxonomiesFilter(predicate, providerFilterDTO, serviceJoin);

        predicate = this.addLocationFilters(predicate, providerFilterDTO, locationJoin, exclusions);

        query.where(predicate);
        query.select(cb.construct(ProviderRecordForMapDTO.class, organizationJoin.get("id"),
            root.get("id"), root.get("address"), root.get("latitude"), root.get("longitude")));
        query.groupBy(root.get(ID), organizationJoin.get(ID));
        return createQueryWithPageable(query, pageable);
    }

    private <T> void addFilters(CriteriaQuery<T> query, Root<Organization> root,
        Join<Organization, SystemAccount> systemAccountJoin, Join<Organization, UserProfile> userProfileJoin,
        List<UserProfile> userProfiles, UserProfile excludedUserProfile, ProviderFilterDTO providerFilterDTO, String search) {
        boolean userProfilesNotEmpty = userProfiles != null && userProfiles.size() > 0;

        Join<Organization, Service> serviceJoin = root.join(SERVICES, JoinType.LEFT);
        Join<Service, Eligibility> eligibilityJoin = serviceJoin.join(ELIGIBILITY, JoinType.LEFT);
        Join<Organization, Location> locationJoin = root.join(LOCATIONS, JoinType.LEFT);

        Silo silo = (userProfilesNotEmpty) ? userProfiles.get(0).getSilo() : excludedUserProfile.getSilo();

        Predicate predicate = getCommonPredicate(root, silo, search, systemAccountJoin,
            userProfileJoin,
            serviceJoin, eligibilityJoin);

        if (userProfilesNotEmpty) {
            predicate = cb.and(predicate, userProfileJoin.get(ID).in(
                userProfiles.stream().map(UserProfile::getId).collect(Collectors.toList()))
            );
        }
        else {
            predicate = cb.and(cb.notEqual(userProfileJoin.get(ID), excludedUserProfile.getId()));

            predicate = this.addTaxonomiesFilter(predicate, providerFilterDTO, serviceJoin);

            predicate = this
                .addLocationFilters(predicate, providerFilterDTO, locationJoin, null);
        }

        query.where(predicate);
    }

    private <T> void addFilters(CriteriaQuery<T> query, Root<Organization> root,
        Join<Organization, SystemAccount> systemAccountJoin, Join<Organization, UserProfile> userProfileJoin,
        Silo silo, ProviderFilterDTO providerFilterDTO, String search) {

        Join<Organization, Service> serviceJoin = root.join(SERVICES, JoinType.LEFT);
        Join<Service, Eligibility> eligibilityJoin = serviceJoin.join(ELIGIBILITY, JoinType.LEFT);
        Join<Organization, Location> locationJoin = root.join(LOCATIONS, JoinType.LEFT);

        Predicate predicate = getCommonPredicate(root, silo, search, systemAccountJoin,
            userProfileJoin, serviceJoin, eligibilityJoin);

        predicate = this.addTaxonomiesFilter(predicate, providerFilterDTO, serviceJoin);

        predicate = this.addLocationFilters(predicate, providerFilterDTO, locationJoin, null);

        query.where(predicate);
    }

    private Predicate getCommonPredicate(From<? extends AbstractEntity, Organization> from, Silo silo, String search,
        Join<Organization, SystemAccount> systemAccountJoin,
        Join<Organization, UserProfile> userProfileJoin, Join<Organization, Service> serviceJoin,
        Join<Service, Eligibility> eligibilityJoin) {
        Predicate predicate;
        predicate = cb.equal(from.get(ACTIVE), true);

        predicate = cb.and(predicate, cb.equal(systemAccountJoin.get(NAME), SERVICE_PROVIDER));

        predicate = cb.and(predicate, cb.equal(userProfileJoin.get(SILO), silo));

        predicate = this.addSearch(predicate, search, from, serviceJoin, eligibilityJoin);
        return predicate;
    }

    private Predicate addSearch(Predicate predicate, String search,
        From<? extends AbstractEntity, Organization> from, Join<Organization, Service> serviceJoin,
        Join<Service, Eligibility> eligibilityJoin
    ) {
        Predicate predicateResult = predicate;
        if (StringUtils.isNotBlank(search)) {
            String searchQuery = '%' + search.toUpperCase() + '%';
            Predicate searchPredicate = cb.or(
                cb.like(cb.upper(from.get(NAME)), searchQuery),
                cb.like(cb.upper(from.get(DESCRIPTION)), searchQuery),
                cb.like(cb.upper(serviceJoin.get(NAME)), searchQuery),
                cb.like(cb.upper(serviceJoin.get(DESCRIPTION)), searchQuery),
                cb.like(cb.upper(eligibilityJoin.get(ELIGIBILITY)), searchQuery)
            );
            predicateResult = cb.and(predicate, searchPredicate);
        }
        return predicateResult;
    }

    @SuppressWarnings("PMD.CyclomaticComplexity")
    private Predicate addLocationFilters(Predicate predicate, ProviderFilterDTO providerFilterDTO,
        Join<? extends AbstractEntity, Location> locationJoin, List<ExclusionsConfig> exclusions) {
        Set<LocationExclusion> locationExclusions = (exclusions != null) ? exclusions.stream()
            .flatMap(e -> e.getLocationExclusions().stream()).collect(Collectors.toSet()) : Collections.emptySet();

        Set<String> excludedRegions = locationExclusions.stream()
            .filter(le -> StringUtils.isNotBlank(le.getRegion()))
            .map(LocationExclusion::getRegion)
            .collect(Collectors.toSet());
        Set<String> excludedCities = locationExclusions.stream()
            .filter(le -> StringUtils.isNotBlank(le.getCity()))
            .map(LocationExclusion::getCity)
            .collect(Collectors.toSet());

        if (StringUtils.isNotEmpty(providerFilterDTO.getCity())
            || StringUtils.isNotEmpty(providerFilterDTO.getRegion())
            || StringUtils.isNotEmpty(providerFilterDTO.getZip())
            || !excludedRegions.isEmpty() || !excludedCities.isEmpty()) {

            Join<Location, GeocodingResult> geocodingResultJoin = locationJoin.join(GEOCODING_RESULTS, JoinType.LEFT);

            Predicate updatedPredicate = predicate;
            if (StringUtils.isNotEmpty(providerFilterDTO.getCity())) {
                updatedPredicate = cb.and(updatedPredicate,
                    geocodingResultJoin.get(CITY).in(providerFilterDTO.getCity())
                );
            }

            if (StringUtils.isNotEmpty(providerFilterDTO.getRegion())) {
                updatedPredicate = cb.and(updatedPredicate,
                    geocodingResultJoin.get(REGION).in(providerFilterDTO.getRegion())
                );
            }

            if (StringUtils.isNotEmpty(providerFilterDTO.getZip())) {
                updatedPredicate = cb.and(updatedPredicate,
                    geocodingResultJoin.get(POSTAL_CODE).in(providerFilterDTO.getZip())
                );
            }

            if (!excludedRegions.isEmpty()) {
                for (String excludedRegion : excludedRegions) {
                    updatedPredicate = cb.and(updatedPredicate,
                        cb.notLike(cb.lower(geocodingResultJoin.get(REGION)), '%' + excludedRegion.toLowerCase() + '%'));
                }
            }


            if (!excludedCities.isEmpty()) {
                for (String excludedCity : excludedCities) {
                    updatedPredicate = cb.and(updatedPredicate,
                        cb.notLike(cb.lower(geocodingResultJoin.get(CITY)), '%' + excludedCity.toLowerCase() + '%'));
                }
            }

            return updatedPredicate;
        }
        return predicate;
    }

    private Predicate addTaxonomiesFilter(Predicate predicate, ProviderFilterDTO providerFilterDTO, Join<Organization,
        Service> serviceJoin) {
        if (CollectionUtils.isNotEmpty(providerFilterDTO.getServiceTypes())) {
            Join<Service, ServiceTaxonomy> taxonomiesJoin = serviceJoin.join(TAXONOMIES, JoinType.LEFT);
            Join<ServiceTaxonomy, Taxonomy> taxonomyJoin = taxonomiesJoin.join(TAXONOMY, JoinType.LEFT);
            return cb.and(predicate, taxonomyJoin.get(NAME).in(providerFilterDTO.getServiceTypes()));
        }
        return predicate;
    }

    private Long getTotal(ProviderFilterDTO providerFilterDTO, List<UserProfile> userProfiles,
        UserProfile excludedUserProfile, String search) {
        CriteriaQuery<Long> countCriteria = cb.createQuery(Long.class);
        Root<Organization> selectRoot = countCriteria.from(Organization.class);
        Join<Organization, SystemAccount> systemAccountJoin = selectRoot.join(ACCOUNT, JoinType.LEFT);
        Join<Organization, UserProfile> userProfileJoin = selectRoot.join(USER_PROFILES, JoinType.LEFT);

        this.addFilters(countCriteria, selectRoot, systemAccountJoin, userProfileJoin, userProfiles, excludedUserProfile, providerFilterDTO, search);
        countCriteria.select(cb.countDistinct(selectRoot));
        return em.createQuery(countCriteria).getSingleResult();
    }

    private Long getTotal(ProviderFilterDTO providerFilterDTO, Silo silo, String search) {
        CriteriaQuery<Long> countCriteria = cb.createQuery(Long.class);
        Root<Organization> selectRoot = countCriteria.from(Organization.class);
        Join<Organization, SystemAccount> systemAccountJoin = selectRoot.join(ACCOUNT, JoinType.LEFT);
        Join<Organization, UserProfile> userProfileJoin = selectRoot.join(USER_PROFILES, JoinType.LEFT);

        this.addFilters(countCriteria, selectRoot, systemAccountJoin, userProfileJoin, silo, providerFilterDTO, search);
        countCriteria.select(cb.countDistinct(selectRoot));
        return em.createQuery(countCriteria).getSingleResult();
    }

    private void addSorting(CriteriaQuery<?> queryCriteria, Sort sort,
        Root<Organization> root) {
        List<Order> orderList = new ArrayList<>();
        addOrder(root, orderList, sort, SORT_NAME);
        addOrder(root, orderList, sort, UPDATED_AT);
        queryCriteria.orderBy(orderList);
    }

    private void addOrder(Root<Organization> root, List<Order> orderList, final Sort sort, final String field) {
        Sort.Order order = sort.getOrderFor(field);
        if (order != null) {
            if (order.isAscending()) {
                orderList.add(cb.asc(root.get(field)));
            } else {
                orderList.add(cb.desc(root.get(field)));
            }
        }
    }
}
