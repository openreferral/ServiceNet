package org.benetech.servicenet.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.domain.Beds_;
import org.benetech.servicenet.domain.GeocodingResult;
import org.benetech.servicenet.domain.GeocodingResult_;
import org.benetech.servicenet.domain.Option;
import org.benetech.servicenet.domain.Option_;
import org.benetech.servicenet.domain.Shelter;
import org.benetech.servicenet.domain.Shelter_;
import org.benetech.servicenet.domain.UserProfile_;
import org.benetech.servicenet.service.dto.ShelterFiltersDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class ShelterRepositoryImpl implements ShelterRepositoryCustom {

    private static final String DISTANCE = "distance";

    private final EntityManager em;
    private final CriteriaBuilder cb;

    public ShelterRepositoryImpl(EntityManager em) {
        this.em = em;
        this.cb = em.getCriteriaBuilder();
    }

    public Page<Shelter> search(ShelterFiltersDTO filters, Pageable pageable) {

        CriteriaQuery<Shelter> queryCriteria = cb.createQuery(Shelter.class);
        Root<Shelter> selectRoot = queryCriteria.from(Shelter.class);

        queryCriteria.select(selectRoot);

        addFilters(queryCriteria, selectRoot, filters);
        addSorting(queryCriteria, pageable.getSort(), selectRoot);

        CriteriaQuery<Long> countCriteria = cb.createQuery(Long.class);
        Root<Shelter> selectRootCount = countCriteria.from(Shelter.class);

        countCriteria.select(cb.countDistinct(selectRootCount));

        addFilters(countCriteria, selectRootCount, filters);

        List<Shelter> results = null;
        Long total = null;

        Query query = em.createQuery(queryCriteria);
        if (pageable.isPaged()) {
            query
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize());
        }

        if (applyPositionFiltering(filters)) {
            results = query
                .setParameter("lat", filters.getLatitude())
                .setParameter("lon", filters.getLongitude())
                .getResultList();

            if (results != null && results.size() > 0) {
                total = em.createQuery(countCriteria)
                    .setParameter("lat", filters.getLatitude())
                    .setParameter("lon", filters.getLongitude())
                    .getSingleResult();
            } else {
                total = 0L;
            }
        } else {
            results = query.getResultList();

            if (results != null && results.size() > 0) {
                total = em.createQuery(countCriteria).getSingleResult();
            } else {
                total = 0L;
            }
        }

        return new PageImpl<>(results, pageable, total.intValue());
    }

    private <T> void addFilters(CriteriaQuery<T> query, Root<Shelter> root, ShelterFiltersDTO filters) {

        List<Predicate> predicates = new ArrayList<>();
        Join<Shelter, Option> bedsJoin = root.join(Shelter_.BEDS, JoinType.LEFT);

        if (StringUtils.isNotBlank(filters.getSearchQuery())) {
            String sq = filters.getSearchQuery().trim().toUpperCase();
            predicates.add(cb.or(
                cb.like(cb.upper(root.join(Shelter_.AGENCY_NAME)), '%' + sq + '%'),
                cb.like(cb.upper(root.join(Shelter_.PROGRAM_NAME)), '%' + sq + '%'),
                cb.like(cb.upper(root.join(Shelter_.ALTERNATE_NAME)), '%' + sq + '%')
            ));
        }
        if (filters.isShowOnlyAvailableBeds()) {
            predicates.add(cb.greaterThan(bedsJoin.get(Beds_.AVAILABLE_BEDS), 0));
        }
        if (CollectionUtils.isNotEmpty(filters.getDefinedCoverageAreas())) {
            Join<Shelter, Option> join = root.join(Shelter_.DEFINED_COVERAGE_AREAS, JoinType.LEFT);
            predicates.add(join.get(Option_.VALUE).in(filters.getDefinedCoverageAreas()));
        }
        if (CollectionUtils.isNotEmpty(filters.getTags())) {
            for (Serializable conditionColumnValue : filters.getTags()) {
                Join<Shelter, Option> join = root.join(Shelter_.TAGS, JoinType.LEFT);
                Subquery<Option> optionSubquery = query.subquery(Option.class);
                Root<Option> optionRoot = optionSubquery.from(Option.class);
                predicates.add(cb.exists(optionSubquery.select(optionRoot).where(cb.and(
                    cb.equal(optionRoot.get(Option_.ID), join.get(Option_.ID)),
                    cb.equal(optionRoot.get(Option_.VALUE), conditionColumnValue)
                ))));
            }
        }
        if (filters.getUserId() != null) {
            Join<Shelter, Option> join = root.join(Shelter_.USER_PROFILES, JoinType.LEFT);
            predicates.add(cb.equal(join.get(UserProfile_.ID), UUID.fromString(filters.getUserId())));
        }
        if (applyPositionFiltering(filters)) {
            Expression<String> address = cb.function(
                "get_address",
                String.class,
                root.join(Shelter_.ADDRESS1),
                root.join(Shelter_.ADDRESS2),
                root.join(Shelter_.CITY),
                root.join(Shelter_.ZIPCODE)
            );
            Root<GeocodingResult> geocodingResultRoot = query.from(GeocodingResult.class);
            predicates.add(cb.equal(geocodingResultRoot.get(GeocodingResult_.ADDRESS), address));

            Expression<Double> distance = cb.function(
                "calculate_distance",
                Double.class,
                cb.parameter(Double.class, "lat"),
                cb.parameter(Double.class, "lon"),
                geocodingResultRoot.get(GeocodingResult_.LATITUDE),
                geocodingResultRoot.get(GeocodingResult_.LONGITUDE)
            );
            predicates.add(cb.lessThanOrEqualTo(distance, filters.getRadius()));
            cb.asc(distance);
        }

        query.groupBy(root.join(Shelter_.ID), root.join(Shelter_.AGENCY_NAME), root.join(Shelter_.PROGRAM_NAME),
            root.join(Shelter_.ALTERNATE_NAME), root.join(Shelter_.BEDS), bedsJoin.get(Beds_.AVAILABLE_BEDS));

        query.where(cb.and(predicates.toArray(new Predicate[0])));
    }

    private void addSorting(CriteriaQuery<Shelter> queryCriteria, Sort sort, Root<Shelter> root) {
        List<Order> orderList = new ArrayList<>();
        addOrder(orderList, root, sort, DISTANCE);
        addOrder(orderList, root, sort, Shelter_.BEDS);
        orderList.add(cb.desc(root.join(Shelter_.AGENCY_NAME)));
        queryCriteria.orderBy(orderList);
    }

    private void addOrder(List<Order> orderList, Root<Shelter> root,
        final Sort sort, final String field) {

        Sort.Order order = sort.getOrderFor(field);

        if (order != null && Shelter_.BEDS.equals(field)) {
            if (order.isAscending()) {
                orderList.add(cb.asc(cb.coalesce(root.join(Shelter_.BEDS).get(Beds_.AVAILABLE_BEDS), 0)));
            } else {
                orderList.add(cb.desc(cb.coalesce(root.join(Shelter_.BEDS).get(Beds_.AVAILABLE_BEDS), 0)));
            }
        }
    }

    private Boolean applyPositionFiltering(ShelterFiltersDTO filtersDTO) {
        return filtersDTO.isApplyLocationSearch() &&
            filtersDTO.getRadius() != null &&
            filtersDTO.getLatitude() != null &&
            filtersDTO.getLongitude() != null;
    }
}
