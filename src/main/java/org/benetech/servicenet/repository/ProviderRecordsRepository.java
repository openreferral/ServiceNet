package org.benetech.servicenet.repository;

import static org.benetech.servicenet.config.Constants.SERVICE_PROVIDER;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.domain.GeocodingResult;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.ServiceTaxonomy;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.domain.Taxonomy;
import org.benetech.servicenet.domain.UserProfile;
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

    private static final String ACCOUNT = "account";
    private static final String USER_PROFILES = "userProfiles";

    private static final String CITY = "locality";
    private static final String REGION = "administrativeAreaLevel2";
    private static final String POSTAL_CODE = "postalCode";
    private static final String GEOCODING_RESULTS = "geocodingResults";
    private static final String LOCATIONS = "locations";

    private static final String SERVICES = "services";
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
        Long total = this.getTotal(providerFilterDTO, userProfile, search);

        return new PageImpl<>(results, pageable, total.intValue());
    }

    private <T> void addFilters(CriteriaQuery<T> query, Root<Organization> root, UserProfile userProfile,
        ProviderFilterDTO providerFilterDTO, String search) {
        Predicate predicate = cb.conjunction();

        Join<Organization, SystemAccount> systemAccountJoin = root.join(ACCOUNT, JoinType.LEFT);
        Join<Organization, UserProfile> userProfileJoin = root.join(USER_PROFILES, JoinType.LEFT);
        Join<Organization, Service> serviceJoin = root.join(SERVICES, JoinType.LEFT);
        Join<Organization, Location> locationJoin = root.join(LOCATIONS, JoinType.LEFT);

        predicate = cb.equal(systemAccountJoin.get(NAME), SERVICE_PROVIDER);

        predicate = this.addSearch(predicate, search, root, serviceJoin);

        predicate = cb.and(predicate, cb.notEqual(userProfileJoin.get(ID), userProfile.getId()));

        predicate = this.addTaxonomiesFilter(predicate, providerFilterDTO, serviceJoin);

        predicate = this.addLocationFilters(predicate, providerFilterDTO, locationJoin);

        query.where(predicate);
    }

    private Predicate addSearch(Predicate predicate, String search,
        Root<Organization> root, Join<Organization, Service> serviceJoin) {
        Predicate predicateResult = predicate;
        if (StringUtils.isNotBlank(search)) {
            String searchQuery = '%' + search.toUpperCase() + '%';
            Predicate searchPredicate = cb.or(
                cb.like(cb.upper(root.get(NAME)), searchQuery),
                cb.like(cb.upper(root.get(DESCRIPTION)), searchQuery),
                cb.like(cb.upper(serviceJoin.get(NAME)), searchQuery)
            );
            predicateResult = cb.and(predicate, searchPredicate);
        }
        return predicateResult;
    }

    private Predicate addLocationFilters(Predicate predicate, ProviderFilterDTO providerFilterDTO,
        Join<Organization, Location> locationJoin) {

        if (StringUtils.isNotEmpty(providerFilterDTO.getCity())
            || StringUtils.isNotEmpty(providerFilterDTO.getRegion())
            || StringUtils.isNotEmpty(providerFilterDTO.getZip())) {

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
