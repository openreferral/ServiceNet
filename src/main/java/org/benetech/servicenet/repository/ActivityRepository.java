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
import org.benetech.servicenet.domain.AccessibilityForDisabilities_;
import org.benetech.servicenet.domain.Contact_;
import org.benetech.servicenet.domain.Eligibility_;
import org.benetech.servicenet.domain.GeocodingResult;
import org.benetech.servicenet.domain.GeocodingResult_;
import org.benetech.servicenet.domain.Language_;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Location_;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.OrganizationMatch;
import org.benetech.servicenet.domain.OrganizationMatch_;
import org.benetech.servicenet.domain.Organization_;
import org.benetech.servicenet.domain.Phone_;
import org.benetech.servicenet.domain.PhysicalAddress_;
import org.benetech.servicenet.domain.PostalAddress_;
import org.benetech.servicenet.domain.RequiredDocument_;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.ServiceTaxonomy;
import org.benetech.servicenet.domain.ServiceTaxonomy_;
import org.benetech.servicenet.domain.Service_;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.domain.SystemAccount_;
import org.benetech.servicenet.domain.Taxonomy;
import org.benetech.servicenet.domain.Taxonomy_;
import org.benetech.servicenet.domain.enumeration.SearchField;
import org.benetech.servicenet.domain.enumeration.SearchOn;
import org.benetech.servicenet.domain.view.ActivityInfo;
import org.benetech.servicenet.domain.view.ActivityInfo_;
import org.benetech.servicenet.service.dto.ActivityFilterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
public class ActivityRepository {

    private static final Integer WEEK = 7;
    private static final Integer MONTH = 30;

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
            .when(cb.equal(selectRoot.get(ActivityInfo_.ACCOUNT_ID), ownerId), 1)
            .otherwise(0);
        queryCriteria.groupBy(selectRoot.get(ActivityInfo_.ID), selectRoot.get(ActivityInfo_.ACCOUNT_ID), selectRoot.get(ActivityInfo_.ALTERNATE_NAME),
            selectRoot.get(ActivityInfo_.NAME), selectRoot.get(ActivityInfo_.RECENT), selectRoot.get(ActivityInfo_.RECOMMENDED), selectRoot.get(ActivityInfo_.SIMILARITY),
            selectRoot.get(ActivityInfo_.LAST_UPDATED), accountMatchExpression);

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

    @SuppressWarnings("PMD.CyclomaticComplexity")
    private Path getFieldPath(From from, SearchField searchField) {
        if (searchField.equals(SearchField.PHONE)) {
            return from.join(Organization_.PHONES, JoinType.LEFT).get(Phone_.NUMBER);
        } else if (searchField.equals(SearchField.CONTACT_NAME)) {
            return from.join(Organization_.CONTACTS).get(Contact_.NAME);
        } else if (searchField.equals(SearchField.CONTACT_PHONE)) {
            return from.join(Organization_.CONTACTS).join(Contact_.PHONES).get(Phone_.NUMBER);
        } else if (searchField.equals(SearchField.ELIGIBILITY)) {
            return from.join(Service_.ELIGIBILITY).get(Eligibility_.ELIGIBILITY);
        } else if (searchField.equals(SearchField.REQUIRED_DOCUMENT)) {
            return from.join(Service_.DOCS).get(RequiredDocument_.DOCUMENT);
        } else if (searchField.equals(SearchField.LANGUAGE)) {
            return from.join(Service_.LANGS).get(Language_.LANGUAGE);
        } else if (searchField.equals(SearchField.PHYSICAL_ADDRESS)) {
            return from.join(Location_.PHYSICAL_ADDRESS).get(PhysicalAddress_.ADDRESS1);
        } else if (searchField.equals(SearchField.POSTAL_ADDRESS)) {
            return from.join(Location_.POSTAL_ADDRESS).get(PostalAddress_.ADDRESS1);
        } else if (searchField.equals(SearchField.ACCESSIBILITY)) {
            return from.join(Location_.ACCESSIBILITIES).get(AccessibilityForDisabilities_.ACCESSIBILITY);
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
            Join<OrganizationMatch, Organization> matchOrgJoin = matchJoin.join(OrganizationMatch_.PARTNER_VERSION, JoinType.LEFT);
            Join<Organization, SystemAccount> matchAccountJoin = matchOrgJoin.join(Organization_.ACCOUNT, JoinType.LEFT);
            if (activityFilterDTO.isShowPartner()) {
                updatePredicate = cb.or(predicate, cb.notEqual(matchAccountJoin.get(SystemAccount_.ID), ownerId));
                if (hasPartnerFilters) {
                    Join<Organization, SystemAccount> accountJoin = orgJoin.join(Organization_.ACCOUNT);
                    updatePredicate = cb.and(predicate, cb.or(
                        cb.and(cb.not(isCurrentAccount), accountJoin.get(SystemAccount_.ID).in(activityFilterDTO.getPartnerFilterList())),
                        matchAccountJoin.get(SystemAccount_.ID).in(activityFilterDTO.getPartnerFilterList())
                    ));
                }
            } else {
                updatePredicate = cb.and(predicate, matchAccountJoin.get(SystemAccount_.ID).in(activityFilterDTO.getPartnerFilterList()));
            }
        }
        return updatePredicate;
    }

    private Predicate addSimilarityFilter(Predicate predicate, ActivityFilterDTO activityFilterDTO, Join<ActivityInfo,
        OrganizationMatch> matchJoin) {
        if (activityFilterDTO.isShowOnlyHighlyMatched()) {
            return cb.and(predicate, cb.greaterThan(matchJoin.get(OrganizationMatch_.SIMILARITY), BigDecimal.valueOf(HIGHLY_MATCH_THRESHOLD)));
        }
        return predicate;
    }

    private Predicate addLocationFilters(Predicate predicate, ActivityFilterDTO activityFilterDTO,
        Join<Organization, Location> locationJoin) {

        if (CollectionUtils.isNotEmpty(activityFilterDTO.getCitiesFilterList())
            || CollectionUtils.isNotEmpty(activityFilterDTO.getRegionFilterList())
            || CollectionUtils.isNotEmpty(activityFilterDTO.getPostalCodesFilterList())
            || applyPositionFiltering(activityFilterDTO)) {

            Join<Location, GeocodingResult> geocodingResultJoin = locationJoin.join(Location_.GEOCODING_RESULTS, JoinType.LEFT);

            Predicate updatedPredicate = predicate;
            if (CollectionUtils.isNotEmpty(activityFilterDTO.getCitiesFilterList())) {
                updatedPredicate = cb.and(updatedPredicate,
                    geocodingResultJoin.get(GeocodingResult_.LOCALITY).in(activityFilterDTO.getCitiesFilterList())
                );
            }

            if (CollectionUtils.isNotEmpty(activityFilterDTO.getRegionFilterList())) {
                updatedPredicate = cb.and(updatedPredicate,
                    geocodingResultJoin.get(GeocodingResult_.ADMINISTRATIVE_AREA_LEVEL2).in(activityFilterDTO.getRegionFilterList())
                );
            }

            if (CollectionUtils.isNotEmpty(activityFilterDTO.getPostalCodesFilterList())) {
                updatedPredicate = cb.and(updatedPredicate,
                    geocodingResultJoin.get(GeocodingResult_.POSTAL_CODE).in(activityFilterDTO.getPostalCodesFilterList())
                );
            }

            if (applyPositionFiltering(activityFilterDTO)) {
                Expression<Double> distance = cb.function(
                    "calculate_distance",
                    Double.class,
                    cb.parameter(Double.class, "lat"),
                    cb.parameter(Double.class, "lon"),
                    geocodingResultJoin.get(GeocodingResult_.LATITUDE),
                    geocodingResultJoin.get(GeocodingResult_.LONGITUDE)
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
            Join<Service, ServiceTaxonomy> taxonomiesJoin = serviceJoin.join(Service_.TAXONOMIES, JoinType.LEFT);
            Join<ServiceTaxonomy, Taxonomy> taxonomyJoin = taxonomiesJoin.join(ServiceTaxonomy_.TAXONOMY, JoinType.LEFT);

            return cb.and(predicate, taxonomyJoin.get(Taxonomy_.NAME).in(activityFilterDTO.getTaxonomiesFilterList()));
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
            updatedPredicate = cb.and(updatedPredicate, cb.greaterThanOrEqualTo(root.get(ActivityInfo_.RECENT), fromDate));
        }

        if (toDate != null) {
            updatedPredicate = cb.and(updatedPredicate, cb.lessThanOrEqualTo(root.get(ActivityInfo_.RECENT), toDate));
        }

        return updatedPredicate;
    }

    private <T> void addFilters(CriteriaQuery<T> query, Root<ActivityInfo> root, UUID ownerId,
        String searchName, ActivityFilterDTO activityFilterDTO) {

        Predicate predicate = cb.conjunction();
        Predicate isCurrentAccount = cb.equal(root.get(ActivityInfo_.ACCOUNT_ID), ownerId);
        if (!activityFilterDTO.isShowPartner()) {
            predicate = isCurrentAccount;
        }

        Join<ActivityInfo, Organization> orgJoin = root.join(ActivityInfo_.ORGANIZATIONS, JoinType.LEFT);
        Join<Organization, Location> locationJoin = orgJoin.join(Organization_.LOCATIONS, JoinType.LEFT);
        Join<Organization, Service> serviceJoin = orgJoin.join(Organization_.SERVICES, JoinType.LEFT);
        Join<ActivityInfo, OrganizationMatch> matchJoin = root.join(ActivityInfo_.ORGANIZATION_MATCHES, JoinType.LEFT);

        predicate = cb.and(predicate, cb.or(matchJoin.isNull(),
            cb.equal(matchJoin.get(OrganizationMatch_.HIDDEN), activityFilterDTO.isHiddenFilter()))
        );

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
        addOrder(root, orderList, sort, ActivityInfo_.SIMILARITY);
        addOrder(root, orderList, sort, ActivityInfo_.RECENT);
        addOrder(root, orderList, sort, ActivityInfo_.RECOMMENDED);
        queryCriteria.orderBy(orderList);
    }

    private void addOrder(Root<ActivityInfo> root, List<Order> orderList, final Sort sort, final String field) {

        Sort.Order order = sort.getOrderFor(field);

        if (order != null) {
            if (ActivityInfo_.SIMILARITY.equals(field)) {
                orderList.add(cb.desc(cb.selectCase()
                    .when(cb.equal(cb.size(root.get(ActivityInfo_.ORGANIZATION_MATCHES)), 0), 0).otherwise(1)));
            }
            if (order.isAscending()) {
                orderList.add(cb.asc(root.get(field)));
            } else {
                orderList.add(cb.desc(root.get(field)));
            }
        }
    }
}
