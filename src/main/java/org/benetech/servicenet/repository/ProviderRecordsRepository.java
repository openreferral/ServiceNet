package org.benetech.servicenet.repository;

import static org.benetech.servicenet.config.Constants.SERVICE_PROVIDER;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.Query;
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
import org.benetech.servicenet.domain.Eligibility;
import org.benetech.servicenet.domain.ExclusionsConfig;
import org.benetech.servicenet.domain.GeocodingResult;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.LocationExclusion;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.ServiceTaxonomy;
import org.benetech.servicenet.domain.Silo;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.domain.Taxonomy;
import org.benetech.servicenet.domain.UserProfile;
import org.benetech.servicenet.service.dto.ProviderRecordForMapDTO;
import org.benetech.servicenet.service.dto.provider.ProviderFilterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

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
    private static final String SORT_UPDATED_AT = "updatedAt";

    private final EntityManager em;
    private final CriteriaBuilder cb;

    public ProviderRecordsRepository(EntityManager em) {
        this.em = em;
        this.cb = em.getCriteriaBuilder();
    }

    public Page<Organization> findAllWithFilters(UserProfile userProfile,
        ProviderFilterDTO providerFilterDTO, String search, Pageable pageable) {

        CriteriaQuery<Organization> queryCriteria = cb.createQuery(Organization.class);
        Root<Organization> selectRoot = queryCriteria.from(Organization.class);
        queryCriteria.select(selectRoot);

        addFilters(queryCriteria, selectRoot, userProfile, providerFilterDTO, search);
        queryCriteria.groupBy(selectRoot.get(ID));
        addSorting(queryCriteria, pageable.getSort(), selectRoot);

        Query query = em.createQuery(queryCriteria);
        if (pageable.isPaged()) {
            query
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize());
        }

        List<Organization> results = query.getResultList();

        Long total = 0L;
        if (results != null && results.size() > 0) {
            total = this.getTotal(providerFilterDTO, userProfile, search);
        }

        return new PageImpl<>(results, pageable, total.intValue());
    }

    public Page<Organization> findAllWithFiltersPublic(ProviderFilterDTO providerFilterDTO, Silo silo,
        String search, Pageable pageable) {

        CriteriaQuery<Organization> queryCriteria = cb.createQuery(Organization.class);
        Root<Organization> selectRoot = queryCriteria.from(Organization.class);
        queryCriteria.select(selectRoot);

        addFilters(queryCriteria, selectRoot, silo, providerFilterDTO, search);
        queryCriteria.groupBy(selectRoot.get(ID));
        addSorting(queryCriteria, pageable.getSort(), selectRoot);

        Query query = em.createQuery(queryCriteria);
        if (pageable.isPaged()) {
            query
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize());
        }

        List<Organization> results = query.getResultList();

        Long total = 0L;
        if (results != null && results.size() > 0) {
            total = this.getTotal(providerFilterDTO, silo, search);
        }

        return new PageImpl<>(results, pageable, total.intValue());
    }

    public Page<ProviderRecordForMapDTO> findAllWithFiltersForMap(Silo silo, ProviderFilterDTO providerFilterDTO,
        String search, List<ExclusionsConfig> exclusions) {

        CriteriaQuery<ProviderRecordForMapDTO> queryCriteria = cb.createQuery(ProviderRecordForMapDTO.class);
        Root<GeocodingResult> selectRoot = queryCriteria.from(GeocodingResult.class);

        addFiltersAndSelect(queryCriteria, selectRoot, providerFilterDTO, search, exclusions, null, silo);

        Query query = em.createQuery(queryCriteria);

        List<ProviderRecordForMapDTO> results = query.getResultList();

        return new PageImpl<>(results);
    }

    public Page<ProviderRecordForMapDTO> findProviderRecordsForMap(UserProfile userProfile,
        ProviderFilterDTO providerFilterDTO, String search, List<ExclusionsConfig> exclusions) {

        CriteriaQuery<ProviderRecordForMapDTO> queryCriteria = cb.createQuery(ProviderRecordForMapDTO.class);
        Root<GeocodingResult> selectRoot = queryCriteria.from(GeocodingResult.class);

        addFiltersAndSelect(queryCriteria, selectRoot, providerFilterDTO, search, exclusions, userProfile, null);

        Query query = em.createQuery(queryCriteria);

        List<ProviderRecordForMapDTO> results = query.getResultList();

        return new PageImpl<>(results);
    }

    private <T> void addFiltersAndSelect(CriteriaQuery<ProviderRecordForMapDTO> query, Root<GeocodingResult> root,
        ProviderFilterDTO providerFilterDTO, String search, List<ExclusionsConfig> exclusions, UserProfile userProfile, Silo silo) {
        Predicate predicate = cb.conjunction();

        Join<GeocodingResult, Location> locationJoin = root.join(LOCATIONS, JoinType.LEFT);
        Join<Location, Organization> organizationJoin = locationJoin.join(ORGANIZATION, JoinType.LEFT);
        Join<Organization, SystemAccount> systemAccountJoin = organizationJoin.join(ACCOUNT, JoinType.LEFT);
        Join<Organization, UserProfile> userProfileJoin = organizationJoin.join(USER_PROFILES, JoinType.LEFT);
        Join<Organization, Service> serviceJoin = organizationJoin.join(SERVICES, JoinType.LEFT);
        Join<Service, Eligibility> eligibilityJoin = serviceJoin.join(ELIGIBILITY, JoinType.LEFT);

        predicate = getCommonPredicate(
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

        predicate = this.addTaxonomiesFilter(predicate, providerFilterDTO, serviceJoin);

        predicate = this.addLocationFilters(predicate, providerFilterDTO, locationJoin, exclusions);

        query.where(predicate);
        query.select(cb.construct(ProviderRecordForMapDTO.class, organizationJoin.get("id"), root));
        query.groupBy(root.get(ID), organizationJoin.get(ID));
    }

    private <T> void addFilters(CriteriaQuery<T> query, Root<Organization> root, UserProfile userProfile,
        ProviderFilterDTO providerFilterDTO, String search) {
        Predicate predicate = cb.conjunction();

        Join<Organization, SystemAccount> systemAccountJoin = root.join(ACCOUNT, JoinType.LEFT);
        Join<Organization, UserProfile> userProfileJoin = root.join(USER_PROFILES, JoinType.LEFT);
        Join<Organization, Service> serviceJoin = root.join(SERVICES, JoinType.LEFT);
        Join<Service, Eligibility> eligibilityJoin = serviceJoin.join(ELIGIBILITY, JoinType.LEFT);
        Join<Organization, Location> locationJoin = root.join(LOCATIONS, JoinType.LEFT);

        Silo silo = userProfile.getSilo();

        predicate = getCommonPredicate(root, silo, search, systemAccountJoin,
            userProfileJoin,
            serviceJoin, eligibilityJoin);

        predicate = cb.and(predicate, cb.notEqual(userProfileJoin.get(ID), userProfile.getId()));

        predicate = this.addTaxonomiesFilter(predicate, providerFilterDTO, serviceJoin);

        predicate = this.addLocationFilters(predicate, providerFilterDTO, locationJoin, null);

        query.where(predicate);
    }

    private <T> void addFilters(CriteriaQuery<T> query, Root<Organization> root, UserProfile userProfile) {
        Predicate predicate = cb.conjunction();

        Join<Organization, SystemAccount> systemAccountJoin = root.join(ACCOUNT, JoinType.LEFT);
        Join<Organization, UserProfile> userProfileJoin = root.join(USER_PROFILES, JoinType.LEFT);
        Join<Organization, Service> serviceJoin = root.join(SERVICES, JoinType.LEFT);

        Silo silo = userProfile.getSilo();

        predicate = cb.equal(root.get(ACTIVE), true);

        predicate = cb.and(predicate, cb.equal(systemAccountJoin.get(NAME), SERVICE_PROVIDER));

        predicate = cb.and(predicate, cb.equal(userProfileJoin.get(SILO), silo));

        predicate = cb.and(predicate, cb.notEqual(userProfileJoin.get(ID), userProfile.getId()));

        query.where(predicate);
    }

    private <T> void addFilters(CriteriaQuery<T> query, Root<Organization> root, Silo silo) {
        Predicate predicate = cb.conjunction();

        Join<Organization, SystemAccount> systemAccountJoin = root.join(ACCOUNT, JoinType.LEFT);
        Join<Organization, UserProfile> userProfileJoin = root.join(USER_PROFILES, JoinType.LEFT);

        predicate = cb.equal(root.get(ACTIVE), true);

        predicate = cb.and(predicate, cb.equal(systemAccountJoin.get(NAME), SERVICE_PROVIDER));

        predicate = cb.and(predicate, cb.equal(userProfileJoin.get(SILO), silo));

        query.where(predicate);
    }

    private <T> void addFilters(CriteriaQuery<T> query, Root<Organization> root, Silo silo,
        ProviderFilterDTO providerFilterDTO, String search) {
        Predicate predicate = cb.conjunction();

        Join<Organization, SystemAccount> systemAccountJoin = root.join(ACCOUNT, JoinType.LEFT);
        Join<Organization, UserProfile> userProfileJoin = root.join(USER_PROFILES, JoinType.LEFT);
        Join<Organization, Service> serviceJoin = root.join(SERVICES, JoinType.LEFT);
        Join<Service, Eligibility> eligibilityJoin = serviceJoin.join(ELIGIBILITY, JoinType.LEFT);
        Join<Organization, Location> locationJoin = root.join(LOCATIONS, JoinType.LEFT);

        predicate = getCommonPredicate(root, silo, search, systemAccountJoin, userProfileJoin,
            serviceJoin,
            eligibilityJoin);

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
                cb.like(cb.upper(eligibilityJoin.get(ELIGIBILITY)), searchQuery)
            );
            predicateResult = cb.and(predicate, searchPredicate);
        }
        return predicateResult;
    }

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

    private Long getTotal(ProviderFilterDTO providerFilterDTO, UserProfile userProfile, String search) {
        CriteriaQuery<Long> countCriteria = cb.createQuery(Long.class);
        Root<Organization> selectRoot = countCriteria.from(Organization.class);
        this.addFilters(countCriteria, selectRoot, userProfile, providerFilterDTO, search);
        countCriteria.select(cb.countDistinct(selectRoot));
        return em.createQuery(countCriteria).getSingleResult();
    }

    private Long getTotal(ProviderFilterDTO providerFilterDTO, Silo silo, String search) {
        CriteriaQuery<Long> countCriteria = cb.createQuery(Long.class);
        Root<Organization> selectRoot = countCriteria.from(Organization.class);
        this.addFilters(countCriteria, selectRoot, silo, providerFilterDTO, search);
        countCriteria.select(cb.countDistinct(selectRoot));
        return em.createQuery(countCriteria).getSingleResult();
    }

    private void addSorting(CriteriaQuery<Organization> queryCriteria, Sort sort,
        Root<Organization> root) {
        List<Order> orderList = new ArrayList<>();
        addOrder(root, orderList, sort, SORT_NAME);
        addOrder(root, orderList, sort, SORT_UPDATED_AT);
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
