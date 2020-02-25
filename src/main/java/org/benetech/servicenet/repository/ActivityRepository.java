package org.benetech.servicenet.repository;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
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
import org.benetech.servicenet.domain.enumeration.SearchOn;
import org.benetech.servicenet.domain.view.ActivityInfo;
import org.benetech.servicenet.service.dto.ActivityFilterDTO;
import org.benetech.servicenet.web.rest.SearchField;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
public class ActivityRepository {

    private static final String SIMILARITY = "similarity";
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

    private static final String PHONES = "phones";
    private static final String NUMBER = "number";
    private static final String CONTACTS = "contacts";
    private static final String ELIGIBILITY = "eligibility";
    private static final String DOCS = "docs";
    private static final String DOCUMENT = "document";
    private static final String LANGS = "langs";
    private static final String LANGUAGE = "language";
    private static final String PHYSICAL_ADDRESS = "physicalAddress";
    private static final String POSTAL_ADDRESS = "postalAddress";
    private static final String ADDRESS_1 = "address1";
    private static final String ACCESSIBILITIES = "accessibilities";
    private static final String ACCESSIBILITY = "accessibility";

    private static final Integer WEEK = 7;
    private static final Integer MONTH = 30;

    private static final String HIDDEN = "hidden";

    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";

    private static final Double HIGHLY_MATCH_THRESHOLD = 0.5;

    private final EntityManager em;
    private final CriteriaBuilder cb;

    public ActivityRepository(EntityManager em) {
        this.em = em;
        this.cb = em.getCriteriaBuilder();
    }

    public Page<ActivityInfo> findAllWithFilters(UUID ownerId, String searchName,
        ActivityFilterDTO activityFilterDTO, Pageable pageable) {

        CriteriaQuery<ActivityInfo> queryCriteria = cb.createQuery(ActivityInfo.class);
        Root<ActivityInfo> selectRoot = queryCriteria.from(ActivityInfo.class);

        queryCriteria.select(selectRoot);

        addFilters(queryCriteria, selectRoot, ownerId, searchName,
            activityFilterDTO);
        Expression accountMatchExpression = cb.selectCase()
            .when(cb.equal(selectRoot.get(ACCOUNT_ID), ownerId), 1)
            .otherwise(0);
        queryCriteria.groupBy(selectRoot.get(ID), selectRoot.get(ACCOUNT_ID), selectRoot.get(ALTERNATE_NAME),
            selectRoot.get(NAME), selectRoot.get(RECENT), selectRoot.get(RECOMMENDED), selectRoot.get(SIMILARITY),
            selectRoot.get(LAST_UPDATED), accountMatchExpression);

        addSorting(queryCriteria, pageable.getSort(), selectRoot, accountMatchExpression);

        CriteriaQuery<Long> countCriteria = cb.createQuery(Long.class);
        Root<ActivityInfo> selectRootCount = countCriteria.from(ActivityInfo.class);

        countCriteria.select(cb.countDistinct(selectRootCount));

        addFilters(countCriteria, selectRootCount, ownerId, searchName, activityFilterDTO);

        List<ActivityInfo> results = null;
        Long total = null;

        Query query = em.createQuery(queryCriteria);
        if (pageable.isPaged()) {
            query
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize());
        }

        if (applyPositionFiltering(activityFilterDTO)) {
            results = query
                .setParameter("lat", activityFilterDTO.getLatitude())
                .setParameter("lon", activityFilterDTO.getLongitude())
                .getResultList();

            total = em.createQuery(countCriteria)
                .setParameter("lat", activityFilterDTO.getLatitude())
                .setParameter("lon", activityFilterDTO.getLongitude())
                .getSingleResult();
        } else {
            results = query.getResultList();

            total = em.createQuery(countCriteria).getSingleResult();
        }

        return new PageImpl<>(results, pageable, total.intValue());
    }

    private Path getFieldPath(From from, SearchField searchField) {
        if (searchField.equals(SearchField.PHONE)) {
            return from.join(PHONES, JoinType.LEFT).get(NUMBER);
        } else if (searchField.equals(SearchField.CONTACT_NAME)) {
            return from.join(CONTACTS).get(NAME);
        } else if (searchField.equals(SearchField.CONTACT_PHONE)) {
            return from.join(CONTACTS).join(PHONES).get(NUMBER);
        } else if (searchField.equals(SearchField.ELIGIBILITY)) {
            return from.join(ELIGIBILITY).get(ELIGIBILITY);
        } else if (searchField.equals(SearchField.REQUIRED_DOCUMENT)) {
            return from.join(DOCS).get(DOCUMENT);
        } else if (searchField.equals(SearchField.LANGUAGE)) {
            return from.join(LANGS).get(LANGUAGE);
        } else if (searchField.equals(SearchField.PHYSICAL_ADDRESS)) {
            return from.join(PHYSICAL_ADDRESS).get(ADDRESS_1);
        } else if (searchField.equals(SearchField.POSTAL_ADDRESS)) {
            return from.join(POSTAL_ADDRESS).get(ADDRESS_1);
        } else if (searchField.equals(SearchField.ACCESSIBILITY)) {
            return from.join(ACCESSIBILITIES).get(ACCESSIBILITY);
        }
        return from.get(searchField.getValue());
    }

    private Boolean applyPositionFiltering(ActivityFilterDTO activityFilterDTO) {
        return activityFilterDTO.isApplyLocationSearch() &&
            activityFilterDTO.getRadius() != null &&
            activityFilterDTO.getLatitude() != null &&
            activityFilterDTO.getLongitude() != null;
    }

    private Predicate addPartnerFilters(Predicate predicate, ActivityFilterDTO activityFilterDTO, UUID ownerId,
        Predicate isCurrentAccount, Join<ActivityInfo, OrganizationMatch> matchJoin, Join<ActivityInfo,
        Organization> orgJoin) {
        boolean hasPartnerFilters = (CollectionUtils.isNotEmpty(activityFilterDTO.getPartnerFilterList()));

        Predicate updatePredicate = predicate;
        if (activityFilterDTO.isShowPartner() || hasPartnerFilters) {
            Join<OrganizationMatch, Organization> matchOrgJoin = matchJoin.join(PARTNER_VERSION, JoinType.LEFT);
            Join<Organization, SystemAccount> matchAccountJoin = matchOrgJoin.join(ACCOUNT, JoinType.LEFT);
            if (activityFilterDTO.isShowPartner()) {
                updatePredicate = cb.or(predicate, cb.notEqual(matchAccountJoin.get(ID), ownerId));
                if (hasPartnerFilters) {
                    Join<Organization, SystemAccount> accountJoin = orgJoin.join(ACCOUNT);
                    updatePredicate = cb.and(predicate, cb.or(
                        cb.and(cb.not(isCurrentAccount), accountJoin.get(ID).in(activityFilterDTO.getPartnerFilterList())),
                        matchAccountJoin.get(ID).in(activityFilterDTO.getPartnerFilterList())
                    ));
                }
            } else {
                updatePredicate = cb.and(predicate, matchAccountJoin.get(ID).in(activityFilterDTO.getPartnerFilterList()));
            }
        }
        return updatePredicate;
    }

    private Predicate addSimilarityFilter(Predicate predicate, ActivityFilterDTO activityFilterDTO, Join<ActivityInfo,
        OrganizationMatch> matchJoin) {
        if (activityFilterDTO.isShowOnlyHighlyMatched()) {
            return cb.and(predicate, cb.greaterThan(matchJoin.get(SIMILARITY), BigDecimal.valueOf(HIGHLY_MATCH_THRESHOLD)));
        }
        return predicate;
    }

    private Predicate addLocationFilters(Predicate predicate, ActivityFilterDTO activityFilterDTO,
        Join<Organization, Location> locationJoin) {

        if (CollectionUtils.isNotEmpty(activityFilterDTO.getCitiesFilterList())
            || CollectionUtils.isNotEmpty(activityFilterDTO.getRegionFilterList())
            || CollectionUtils.isNotEmpty(activityFilterDTO.getPostalCodesFilterList())
            || applyPositionFiltering(activityFilterDTO)) {

            Join<Location, GeocodingResult> geocodingResultJoin = locationJoin.join(GEOCODING_RESULTS, JoinType.LEFT);

            Predicate updatedPredicate = predicate;
            if (CollectionUtils.isNotEmpty(activityFilterDTO.getCitiesFilterList())) {
                updatedPredicate = cb.and(updatedPredicate,
                    geocodingResultJoin.get(CITY).in(activityFilterDTO.getCitiesFilterList())
                );
            }

            if (CollectionUtils.isNotEmpty(activityFilterDTO.getRegionFilterList())) {
                updatedPredicate = cb.and(updatedPredicate,
                    geocodingResultJoin.get(REGION).in(activityFilterDTO.getRegionFilterList())
                );
            }

            if (CollectionUtils.isNotEmpty(activityFilterDTO.getPostalCodesFilterList())) {
                updatedPredicate = cb.and(updatedPredicate,
                    geocodingResultJoin.get(POSTAL_CODE).in(activityFilterDTO.getPostalCodesFilterList())
                );
            }

            if (applyPositionFiltering(activityFilterDTO)) {
                Expression<Double> distance = cb.function(
                    "calculate_distance",
                    Double.class,
                    cb.parameter(Double.class, "lat"),
                    cb.parameter(Double.class, "lon"),
                    geocodingResultJoin.get(LATITUDE),
                    geocodingResultJoin.get(LONGITUDE)
                );

                updatedPredicate = cb.and(updatedPredicate, cb.lessThanOrEqualTo(distance, activityFilterDTO.getRadius()));
                cb.asc(distance);
            }

            return updatedPredicate;
        }
        return predicate;
    }

    private Predicate addTaxonomiesFilter(Predicate predicate, ActivityFilterDTO activityFilterDTO, Join<Organization,
        Service> serviceJoin) {
        if (CollectionUtils.isNotEmpty(activityFilterDTO.getTaxonomiesFilterList())) {
            Join<Service, ServiceTaxonomy> taxonomiesJoin = serviceJoin.join(TAXONOMIES, JoinType.LEFT);
            Join<ServiceTaxonomy, Taxonomy> taxonomyJoin = taxonomiesJoin.join(TAXONOMY, JoinType.LEFT);

            return cb.and(predicate, taxonomyJoin.get(NAME).in(activityFilterDTO.getTaxonomiesFilterList()));
        }
        return predicate;
    }

    private Predicate addSearchFilter(Predicate predicate, ActivityFilterDTO activityFilterDTO, String searchName,
        Join<ActivityInfo, Organization> orgJoin, Join<Organization, Service> serviceJoin, Join<Organization,
        Location> locationJoin) {

        if (StringUtils.isBlank(searchName)) {
            return predicate;
        }

        From from;
        if (SearchOn.SERVICES.equals(activityFilterDTO.getSearchOn())) {
            from = serviceJoin;
        } else if (SearchOn.LOCATIONS.equals(activityFilterDTO.getSearchOn())) {
            from = locationJoin;
        } else {
            from = orgJoin;
        }

        List<Predicate> likePredicates = new ArrayList<>();
        for (String value : activityFilterDTO.getSearchFields()) {
            SearchField searchField = SearchField.fromValue(value);
            likePredicates.add(
                cb.like(cb.upper(getFieldPath(from, searchField)), '%' + searchName.trim().toUpperCase() + '%')
            );
        }
        return cb.and(predicate, cb.or(likePredicates.toArray(new Predicate[0])));
    }

    private Predicate addDateFilter(Predicate predicate, ActivityFilterDTO activityFilterDTO, Root<ActivityInfo> root) {
        if (activityFilterDTO.getDateFilter() == null) {
            return predicate;
        }

        Predicate updatedPredicate = predicate;
        ZonedDateTime now = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS);
        ZonedDateTime fromDate = null;
        ZonedDateTime toDate = null;

        switch (activityFilterDTO.getDateFilter()) {
            case LAST_7_DAYS:
                fromDate = now.minusDays(WEEK);
                break;
            case LAST_30_DAYS:
                fromDate = now.minusDays(MONTH);
                break;
            default:
                if (activityFilterDTO.getFromDate() != null) {
                    fromDate = activityFilterDTO.getFromDate().atStartOfDay(ZoneId.systemDefault());
                }
                if (activityFilterDTO.getToDate() != null) {
                    toDate = activityFilterDTO.getToDate().atTime(LocalTime.MAX).atZone(ZoneId.systemDefault());
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

    private <T> void addFilters(CriteriaQuery<T> query, Root<ActivityInfo> root, UUID ownerId,
        String searchName, ActivityFilterDTO activityFilterDTO) {

        Predicate predicate = cb.conjunction();
        Predicate isCurrentAccount = cb.equal(root.get(ACCOUNT_ID), ownerId);
        if (!activityFilterDTO.isShowPartner()) {
            predicate = isCurrentAccount;
        }

        Join<ActivityInfo, Organization> orgJoin = root.join(ORGANIZATIONS, JoinType.LEFT);
        Join<Organization, Location> locationJoin = orgJoin.join(LOCATIONS, JoinType.LEFT);
        Join<Organization, Service> serviceJoin = orgJoin.join(SERVICES, JoinType.LEFT);
        Join<ActivityInfo, OrganizationMatch> matchJoin = root.join(ORGANIZATION_MATCHES, JoinType.LEFT);

        predicate = cb.and(predicate, cb.equal(matchJoin.get(HIDDEN), activityFilterDTO.isHiddenFilter()));

        predicate = addPartnerFilters(predicate, activityFilterDTO, ownerId, isCurrentAccount, matchJoin, orgJoin);

        predicate = addLocationFilters(predicate, activityFilterDTO, locationJoin);

        predicate = addTaxonomiesFilter(predicate, activityFilterDTO, serviceJoin);

        predicate = addSearchFilter(predicate, activityFilterDTO, searchName, orgJoin, serviceJoin, locationJoin);

        predicate = addDateFilter(predicate, activityFilterDTO, root);

        predicate = addSimilarityFilter(predicate, activityFilterDTO, matchJoin);

        query.where(predicate);
    }

    private void addSorting(CriteriaQuery<ActivityInfo> queryCriteria, Sort sort,
        Root<ActivityInfo> root, Expression accountMatchExpression) {
        List<Order> orderList = new ArrayList<>();
        orderList.add(cb.desc(accountMatchExpression));
        addOrder(root, orderList, sort, SIMILARITY);
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
