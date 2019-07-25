package org.benetech.servicenet.repository;

import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.OrganizationMatch;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.PostalAddress;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.ServiceTaxonomy;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.domain.Taxonomy;
import org.benetech.servicenet.domain.view.ActivityInfo;
import org.benetech.servicenet.domain.view.ActivityRecord;
import org.benetech.servicenet.service.dto.FiltersActivityDTO;
import org.benetech.servicenet.web.rest.SearchOn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
public class ActivityRepository {

    private static final String RECENT = "recent";
    private static final String RECOMMENDED = "recommended";

    private static final String ACCOUNT_ID = "accountId";
    private static final String ID = "id";

    private static final String ORGANIZATIONS = "organizations";
    private static final String NAME = "name";
    private static final String ALTERNATE_NAME = "alternateName";

    private static final String LOCATIONS = "locations";
    private static final String POSTAL_ADDRESS = "postalAddress";
    private static final String PHYSICAL_ADDRESS = "physicalAddress";
    private static final String CITY = "city";
    private static final String REGION = "region";
    private static final String POSTAL_CODE = "postalCode";

    private static final String ORGANIZATION_MATCHES = "organizationMatches";
    private static final String PARTNER_VERSION = "partnerVersion";
    private static final String ACCOUNT = "account";

    private static final String SERVICES = "services";
    private static final String TAXONOMIES = "taxonomies";
    private static final String TAXONOMY = "taxonomy";

    private final EntityManager em;
    private final CriteriaBuilder cb;

    public ActivityRepository(EntityManager em) {
        this.em = em;
        this.cb = em.getCriteriaBuilder();
    }

    public ActivityRecord findOneByOrganizationId(UUID orgId) {
        CriteriaQuery<ActivityRecord> queryCriteria = cb.createQuery(ActivityRecord.class);
        Root<ActivityRecord> root = queryCriteria.from(ActivityRecord.class);

        queryCriteria.select(root);

        queryCriteria.where(cb.equal(root.get(ID), orgId));

        return em.createQuery(queryCriteria).getSingleResult();
    }

    public Page<ActivityInfo> findAllWithFilters(UUID ownerId, String searchName, SearchOn searchOn,
        FiltersActivityDTO filtersActivityDTO, Pageable pageable) {

        CriteriaQuery<ActivityInfo> queryCriteria = cb.createQuery(ActivityInfo.class);
        Root<ActivityInfo> selectRoot = queryCriteria.from(ActivityInfo.class);

        queryCriteria.select(selectRoot).distinct(true);

        addFilters(queryCriteria, selectRoot, ownerId, searchName, searchOn, filtersActivityDTO);
        addSorting(queryCriteria, pageable.getSort(), selectRoot);

        CriteriaQuery<Long> countCriteria = cb.createQuery(Long.class);
        Root<ActivityInfo> selectRootCount = countCriteria.from(ActivityInfo.class);

        countCriteria.select(cb.countDistinct(selectRootCount));

        addFilters(countCriteria, selectRootCount, ownerId, searchName, searchOn, filtersActivityDTO);

        List<ActivityInfo> results = em.createQuery(queryCriteria)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .getResultList();

        Long total = em.createQuery(countCriteria).getSingleResult();

        return new PageImpl<>(results, pageable, total.intValue());
    }

    private Predicate searchNames(Predicate predicate, From from, String searchName) {
        return cb.and(predicate, cb.or(
            cb.like(cb.upper(from.get(NAME)), '%' + searchName.trim().toUpperCase() + '%'),
            cb.like(cb.upper(from.get(ALTERNATE_NAME)), '%' + searchName.trim().toUpperCase() + '%')
        ));
    }

    private Predicate addLocationFilters(FiltersActivityDTO filtersActivityDTO, Predicate predicate,
        Join<Organization, Location> locationJoin) {
        Join<Location, PostalAddress> postalAddressJoin = locationJoin.join(POSTAL_ADDRESS, JoinType.LEFT);
        Join<Location, PhysicalAddress> physicalAddressJoin = locationJoin.join(PHYSICAL_ADDRESS, JoinType.LEFT);

        Predicate updatedPredicate = predicate;
        if (CollectionUtils.isNotEmpty(filtersActivityDTO.getCitiesFilterList())) {
            updatedPredicate = cb.and(updatedPredicate, cb.or(
                postalAddressJoin.get(CITY).in(filtersActivityDTO.getCitiesFilterList()),
                physicalAddressJoin.get(CITY).in(filtersActivityDTO.getCitiesFilterList())
            ));
        }

        if (CollectionUtils.isNotEmpty(filtersActivityDTO.getRegionFilterList())) {
            updatedPredicate = cb.and(updatedPredicate, cb.or(
                postalAddressJoin.get(REGION).in(filtersActivityDTO.getRegionFilterList()),
                physicalAddressJoin.get(REGION).in(filtersActivityDTO.getRegionFilterList())
            ));
        }

        if (CollectionUtils.isNotEmpty(filtersActivityDTO.getPostalCodesFilterList())) {
            updatedPredicate = cb.and(updatedPredicate, cb.or(
                postalAddressJoin.get(POSTAL_CODE).in(filtersActivityDTO.getPostalCodesFilterList()),
                physicalAddressJoin.get(POSTAL_CODE).in(filtersActivityDTO.getPostalCodesFilterList())
            ));
        }
        return updatedPredicate;
    }

    @SuppressWarnings("checkstyle:cyclomaticComplexity")
    private <T> void addFilters(CriteriaQuery<T> query, Root<ActivityInfo> root, UUID ownerId,
        String searchName, SearchOn searchOn, FiltersActivityDTO filtersActivityDTO) {

        Predicate predicate = cb.equal(root.get(ACCOUNT_ID), ownerId);

        Join<ActivityInfo, Organization> orgJoin = null;
        Join<Organization, Location> locationJoin = null;
        Join<Organization, Service> serviceJoin = null;

        if (StringUtils.isNotBlank(searchName)) {
            if (searchOn.equals(SearchOn.ORGANIZATION)) {
                predicate = searchNames(predicate, root, searchName);
            } else if (searchOn.equals(SearchOn.LOCATIONS)) {
                orgJoin = root.join(ORGANIZATIONS, JoinType.LEFT);
                locationJoin = orgJoin.join(LOCATIONS, JoinType.LEFT);
                predicate = searchNames(predicate, locationJoin, searchName);
            } else {
                orgJoin = root.join(ORGANIZATIONS, JoinType.LEFT);
                serviceJoin = orgJoin.join(SERVICES, JoinType.LEFT);
                predicate = searchNames(predicate, serviceJoin, searchName);
            }
        }

        if (CollectionUtils.isNotEmpty(filtersActivityDTO.getPartnerFilterList())) {
            Join<ActivityInfo, OrganizationMatch> matchJoin = root.join(ORGANIZATION_MATCHES, JoinType.LEFT);
            Join<OrganizationMatch, Organization> matchOrgJoin = matchJoin.join(PARTNER_VERSION, JoinType.LEFT);
            Join<Organization, SystemAccount> accountJoin = matchOrgJoin.join(ACCOUNT, JoinType.LEFT);

            predicate = cb.and(predicate, accountJoin.get(ID).in(filtersActivityDTO.getPartnerFilterList()));
        }

        if (CollectionUtils.isNotEmpty(filtersActivityDTO.getCitiesFilterList())
            || CollectionUtils.isNotEmpty(filtersActivityDTO.getRegionFilterList())
            || CollectionUtils.isNotEmpty(filtersActivityDTO.getPostalCodesFilterList())) {

            orgJoin = (orgJoin != null) ? orgJoin : root.join(ORGANIZATIONS, JoinType.LEFT);
            locationJoin = (locationJoin != null) ? locationJoin : orgJoin.join(LOCATIONS, JoinType.LEFT);

            predicate = addLocationFilters(filtersActivityDTO, predicate, locationJoin);
        }

        if (CollectionUtils.isNotEmpty(filtersActivityDTO.getTaxonomiesFilterList())) {
            orgJoin = (orgJoin != null) ? orgJoin : root.join(ORGANIZATIONS, JoinType.LEFT);
            serviceJoin = (serviceJoin != null) ? serviceJoin : orgJoin.join(SERVICES, JoinType.LEFT);
            Join<Service, ServiceTaxonomy> taxonomiesJoin = serviceJoin.join(TAXONOMIES, JoinType.LEFT);
            Join<ServiceTaxonomy, Taxonomy> taxonomyJoin = taxonomiesJoin.join(TAXONOMY, JoinType.LEFT);

            predicate = cb.and(predicate, taxonomyJoin.get(NAME).in(filtersActivityDTO.getTaxonomiesFilterList()));
        }

        query.where(predicate);
    }

    private void addSorting(CriteriaQuery<ActivityInfo> queryCriteria, Sort sort, Root<ActivityInfo> root) {
        setOrder(queryCriteria, root, sort, RECENT);
        setOrder(queryCriteria, root, sort, RECOMMENDED);
    }

    private void setOrder(CriteriaQuery<ActivityInfo> queryCriteria, Root<ActivityInfo> root,
        final Sort sort, final String field) {

        Sort.Order order = sort.getOrderFor(field);

        if (order != null) {
            if (order.isAscending()) {
                queryCriteria.orderBy(cb.asc(root.get(field)));
            } else {
                queryCriteria.orderBy(cb.desc(root.get(field)));
            }
        }
    }
}
