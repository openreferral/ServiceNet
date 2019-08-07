package org.benetech.servicenet.repository;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
import org.benetech.servicenet.domain.GeocodingResult;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.OrganizationMatch;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.ServiceTaxonomy;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.domain.Taxonomy;
import org.benetech.servicenet.domain.view.ActivityInfo;
import org.benetech.servicenet.domain.view.ActivityRecord;
import org.benetech.servicenet.service.dto.FiltersActivityDTO;
import org.benetech.servicenet.web.rest.SearchField;
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
    private static final String GEOCODING_RESULTS = "geocodingResults";

    private static final String CITY = "locality";
    private static final String REGION = "administrativeAreaLevel2";
    private static final String POSTAL_CODE = "postalCode";

    private static final String ORGANIZATION_MATCHES = "organizationMatches";
    private static final String PARTNER_VERSION = "partnerVersion";
    private static final String ACCOUNT = "account";

    private static final String SERVICES = "services";
    private static final String TAXONOMIES = "taxonomies";
    private static final String TAXONOMY = "taxonomy";

    private static final Integer WEEK = 7;
    private static final Integer MONTH = 30;

    private static final String HIDDEN = "hidden";

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

    private Predicate searchFields(Predicate predicate, From from, String searchName, List<String> searchFieldValues) {
        List<Predicate> likePredicates = new ArrayList<>();
        for (String value : searchFieldValues) {
            SearchField searchField = SearchField.fromValue(value);
            likePredicates.add(
                cb.like(cb.upper(from.get(searchField.getValue())), '%' + searchName.trim().toUpperCase() + '%')
            );
        }
        return cb.and(predicate, cb.or(likePredicates.toArray(new Predicate[0])));
    }

    private Predicate addLocationFilters(FiltersActivityDTO filtersActivityDTO, Predicate predicate,
        Join<Organization, Location> locationJoin) {
        Join<Location, GeocodingResult> geocodingResultJoin = locationJoin.join(GEOCODING_RESULTS, JoinType.LEFT);

        Predicate updatedPredicate = predicate;
        if (CollectionUtils.isNotEmpty(filtersActivityDTO.getCitiesFilterList())) {
            updatedPredicate = cb.and(updatedPredicate,
                geocodingResultJoin.get(CITY).in(filtersActivityDTO.getCitiesFilterList())
            );
        }

        if (CollectionUtils.isNotEmpty(filtersActivityDTO.getRegionFilterList())) {
            updatedPredicate = cb.and(updatedPredicate,
                geocodingResultJoin.get(REGION).in(filtersActivityDTO.getRegionFilterList())
            );
        }

        if (CollectionUtils.isNotEmpty(filtersActivityDTO.getPostalCodesFilterList())) {
            updatedPredicate = cb.and(updatedPredicate,
                geocodingResultJoin.get(POSTAL_CODE).in(filtersActivityDTO.getPostalCodesFilterList())
            );
        }
        return updatedPredicate;
    }

    private Predicate addDateFilter(FiltersActivityDTO filtersActivityDTO, Predicate predicate, Root<ActivityInfo> root) {
        if (filtersActivityDTO.getDateFilter() == null) {
            return predicate;
        }

        Predicate updatedPredicate = predicate;
        ZonedDateTime now = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS);
        ZonedDateTime fromDate = null;
        ZonedDateTime toDate = null;

        switch (filtersActivityDTO.getDateFilter()) {
            case LAST_7_DAYS:
                fromDate = now.minusDays(WEEK);
                break;
            case LAST_30_DAYS:
                fromDate = now.minusDays(MONTH);
                break;
            default:
                if (filtersActivityDTO.getFromDate() != null) {
                    fromDate = filtersActivityDTO.getFromDate().atStartOfDay(ZoneId.systemDefault());
                }
                if (filtersActivityDTO.getToDate() != null) {
                    toDate = filtersActivityDTO.getToDate().atTime(LocalTime.MAX).atZone(ZoneId.systemDefault());
                }
        }

        if (fromDate != null) {
            updatedPredicate = cb.and(updatedPredicate, cb.greaterThanOrEqualTo(root.get(RECENT), fromDate));
        }

        if (toDate != null) {
            updatedPredicate = cb.and(updatedPredicate, cb.lessThanOrEqualTo(root.get(RECENT), toDate));
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
            orgJoin = root.join(ORGANIZATIONS, JoinType.LEFT);
            if (searchOn.equals(SearchOn.ORGANIZATION)) {
                predicate = searchFields(predicate, orgJoin, searchName, filtersActivityDTO.getSearchFields());
            } else if (searchOn.equals(SearchOn.LOCATIONS)) {
                locationJoin = orgJoin.join(LOCATIONS, JoinType.LEFT);
                predicate = searchFields(predicate, locationJoin, searchName, filtersActivityDTO.getSearchFields());
            } else {
                serviceJoin = orgJoin.join(SERVICES, JoinType.LEFT);
                predicate = searchFields(predicate, serviceJoin, searchName, filtersActivityDTO.getSearchFields());
            }
        }

        predicate = addDateFilter(filtersActivityDTO, predicate, root);
        Join<ActivityInfo, OrganizationMatch> matchJoin = root.join(ORGANIZATION_MATCHES, JoinType.LEFT);
        predicate = cb.and(predicate, cb.equal(matchJoin.get(HIDDEN), filtersActivityDTO.getHiddenFilter()));

        if (CollectionUtils.isNotEmpty(filtersActivityDTO.getPartnerFilterList())) {
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
