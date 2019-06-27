package org.benetech.servicenet.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.domain.Option;
import org.benetech.servicenet.domain.Shelter;
import org.benetech.servicenet.service.dto.ShelterFiltersDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class ShelterRepositoryImpl implements ShelterRepositoryCustom {
    private static final String BEDS = "beds";
    private static final String DISTANCE = "distance";
    private static final String AVAILABLE_BEDS = "availableBeds";
    private static final String AGENCY_NAME = "agencyName";
    private static final String PROGRAM_NAME = "programName";
    private static final String ALTERNATE_NAME = "alternateName";
    private static final String DEFINED_COVERAGE_AREAS = "definedCoverageAreas";
    private static final String TAGS = "tags";
    private static final String VALUE = "value";
    private static final String ID = "id";
    private static final String SHELTER_ID = "shelter_id";

    private final EntityManager em;
    private final CriteriaBuilder cb;

    public ShelterRepositoryImpl(EntityManager em) {
        this.em = em;
        this.cb = em.getCriteriaBuilder();
    }

    public Page<Shelter> search(ShelterFiltersDTO filters, Pageable pageable) {

        CriteriaQuery<Shelter> queryCriteria = cb.createQuery(Shelter.class);
        Root<Shelter> selectRoot = queryCriteria.from(Shelter.class);

        queryCriteria.select(selectRoot).distinct(true);

        addFilters(queryCriteria, selectRoot, filters);
        addSorting(queryCriteria, pageable.getSort(), selectRoot);

        CriteriaQuery<Long> countCriteria = cb.createQuery(Long.class);
        Root<Shelter> selectRootCount = countCriteria.from(Shelter.class);

        countCriteria.select(cb.countDistinct(selectRootCount));

        addFilters(countCriteria, selectRootCount, filters);

        List<Shelter> results = em.createQuery(queryCriteria)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .getResultList();

        Long total = em.createQuery(countCriteria).getSingleResult();

        return new PageImpl<>(results, pageable, total.intValue());
    }

    private <T> void addFilters(CriteriaQuery<T> query, Root<Shelter> root, ShelterFiltersDTO filters) {

        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.isNotBlank(filters.getSearchQuery())) {
            String sq = filters.getSearchQuery().trim().toUpperCase();
            predicates.add(cb.or(
                cb.like(cb.upper(root.get(AGENCY_NAME)), '%' + sq + '%'),
                cb.like(cb.upper(root.get(PROGRAM_NAME)), '%' + sq + '%'),
                cb.like(cb.upper(root.get(ALTERNATE_NAME)), '%' + sq + '%')
            ));
        }
        if (filters.isShowOnlyAvailableBeds()) {
            Join<Shelter, Option> join = root.join(BEDS, JoinType.LEFT);
            predicates.add(cb.greaterThan(join.get(AVAILABLE_BEDS), 0));
        }
        if (CollectionUtils.isNotEmpty(filters.getDefinedCoverageAreas())) {
            Join<Shelter, Option> join = root.join(DEFINED_COVERAGE_AREAS, JoinType.LEFT);
            predicates.add(join.get(VALUE).in(filters.getDefinedCoverageAreas()));
        }
        if (CollectionUtils.isNotEmpty(filters.getTags())) {
            for (Serializable conditionColumnValue : filters.getTags()) {
                Join<Shelter, Option> join = root.join(TAGS, JoinType.LEFT);
                Subquery<Option> optionSubquery = query.subquery(Option.class);
                Root<Option> optionRoot = optionSubquery.from(Option.class);
                predicates.add(cb.exists(optionSubquery.select(optionRoot).where(cb.and(
                    cb.equal(optionRoot.get(ID), join.get(ID)),
                    cb.equal(optionRoot.get(VALUE), conditionColumnValue)
                ))));
            }
        }
        query.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
    }

    private void addSorting(CriteriaQuery<Shelter> queryCriteria, Sort sort, Root<Shelter> root) {
        setOrder(queryCriteria, root, sort, BEDS);
        setOrder(queryCriteria, root, sort, DISTANCE);
        queryCriteria.orderBy(cb.desc(root.get(AGENCY_NAME)));
    }

    private void setOrder(CriteriaQuery<Shelter> queryCriteria, Root<Shelter> root,
        final Sort sort, final String field) {

        Sort.Order order = sort.getOrderFor(field);

        if (order != null) {
            if (BEDS.equals(field)) {
                if (order.isAscending()) {
                    queryCriteria.orderBy(cb.asc(root.get(BEDS).get(AVAILABLE_BEDS)));
                } else {
                    queryCriteria.orderBy(cb.desc(root.get(BEDS).get(AVAILABLE_BEDS)));
                }
            }
        }
    }

}
