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
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
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

    private static final String LAST_UPDATED = "lastUpdated";

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
        FiltersActivityDTO filtersActivityDTO, Pageable pageable, Boolean showPartner) {

        CriteriaQuery<ActivityInfo> queryCriteria = cb.createQuery(ActivityInfo.class);
        Root<ActivityInfo> selectRoot = queryCriteria.from(ActivityInfo.class);

        queryCriteria.select(selectRoot);

        addFilters(queryCriteria, selectRoot, ownerId, searchName, searchOn,
            filtersActivityDTO, showPartner);
        Expression accountMatchExpression = cb.selectCase()
            .when(cb.equal(selectRoot.get(ACCOUNT_ID), ownerId), 1)
            .otherwise(0);
        queryCriteria.groupBy(selectRoot.get(ID), selectRoot.get(ACCOUNT_ID), selectRoot.get(ALTERNATE_NAME),
            selectRoot.get(NAME), selectRoot.get(RECENT), selectRoot.get(RECOMMENDED),
            selectRoot.get(LAST_UPDATED), accountMatchExpression);

        addSorting(queryCriteria, pageable.getSort(), selectRoot, accountMatchExpression);

        CriteriaQuery<Long> countCriteria = cb.createQuery(Long.class);
        Root<ActivityInfo> selectRootCount = countCriteria.from(ActivityInfo.class);

        countCriteria.select(cb.countDistinct(selectRootCount));

        addFilters(countCriteria, selectRootCount, ownerId, searchName, searchOn, filtersActivityDTO, showPartner);

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
        String searchName, SearchOn searchOn, FiltersActivityDTO filtersActivityDTO, Boolean partnerRecords) {

        Predicate predicate = cb.conjunction();
        Predicate isCurrentAccount = cb.equal(root.get(ACCOUNT_ID), ownerId);
        if (!partnerRecords) {
            predicate = isCurrentAccount;
        }

        Join<ActivityInfo, Organization> orgJoin = root.join(ORGANIZATIONS, JoinType.LEFT);
        Join<Organization, Location> locationJoin = null;
        Join<Organization, Service> serviceJoin = null;

        if (StringUtils.isNotBlank(searchName)) {
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

        boolean hasPartnerFilters = (CollectionUtils.isNotEmpty(filtersActivityDTO.getPartnerFilterList()));
        if (partnerRecords || hasPartnerFilters) {
            Join<OrganizationMatch, Organization> matchOrgJoin = matchJoin.join(PARTNER_VERSION, JoinType.LEFT);
            Join<Organization, SystemAccount> matchAccountJoin = matchOrgJoin.join(ACCOUNT, JoinType.LEFT);
            if (partnerRecords) {
                predicate = cb.and(predicate, cb.notEqual(matchAccountJoin.get(ID), ownerId));
                if (hasPartnerFilters) {
                    Join<Organization, SystemAccount> accountJoin = orgJoin.join(ACCOUNT);
                    predicate = cb.and(predicate, cb.or(
                        cb.and(cb.not(isCurrentAccount), accountJoin.get(ID).in(filtersActivityDTO.getPartnerFilterList())),
                        matchAccountJoin.get(ID).in(filtersActivityDTO.getPartnerFilterList())
                    ));
                }
            } else {
                predicate = cb.and(predicate, matchAccountJoin.get(ID).in(filtersActivityDTO.getPartnerFilterList()));
            }
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

    private void addSorting(CriteriaQuery<ActivityInfo> queryCriteria, Sort sort,
        Root<ActivityInfo> root, Expression accountMatchExpression) {
        List<Order> orderList = new ArrayList<>();
        orderList.add(cb.desc(accountMatchExpression));
        addOrder(root, orderList, sort, RECENT);
        addOrder(root, orderList, sort, RECOMMENDED);
        queryCriteria.orderBy(orderList);
    }

    private void addOrder(Root<ActivityInfo> root, List<Order> orderList, final Sort sort, final String field) {

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
