package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.view.ActivityInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Repository
public class ActivityRepository {
    
    private static final String NAME = "name";
    private final ParameterExpression<String> nameSearch;

    private static final String ALTERNATE_NAME = "alternateName";
    private final ParameterExpression<String> alternateNameSearch;

    private static final String RECENT = "recent";

    private static final String RECOMMENDED = "recommended";

    private static final String ACCOUNT_ID = "accountId";
    private final ParameterExpression<UUID> accountIdSearch;

    private static final int NONE = 0;
    private static final Date START_DATE = new Date(0L);

    private final EntityManager em;

    private final CriteriaBuilder cb;

    public ActivityRepository(EntityManager em) {
        this.em = em;
        this.cb = em.getCriteriaBuilder();
        accountIdSearch = cb.parameter(UUID.class);
        nameSearch = cb.parameter(String.class);
        alternateNameSearch = cb.parameter(String.class);
    }

    public Page<ActivityInfo> findAllWithOwnerId(UUID ownerId, Pageable pageable) {
        CriteriaQuery<ActivityInfo> queryCriteria = cb.createQuery(ActivityInfo.class);
        Root<ActivityInfo> selectRoot = queryCriteria.from(ActivityInfo.class);
        queryCriteria.select(selectRoot);
        addNullsLastSorting(queryCriteria, pageable.getSort(), selectRoot);
        setWhereOwnerId(queryCriteria, selectRoot);
        TypedQuery<ActivityInfo> query = em.createQuery(queryCriteria);

        CriteriaQuery<Long> countCriteria = cb.createQuery(Long.class);
        Root<ActivityInfo> selectRootCount = countCriteria.from(ActivityInfo.class);
        countCriteria.select(cb.count(selectRootCount));
        setWhereOwnerId(countCriteria, selectRootCount);
        TypedQuery<Long> queryCount = em.createQuery(countCriteria);

        List<ActivityInfo> results = query.setParameter(accountIdSearch, ownerId)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .getResultList();

        Long total = queryCount.setParameter(accountIdSearch, ownerId)
            .getSingleResult();

        return new PageImpl<>(results, pageable, total.intValue());
    }

    public Page<ActivityInfo> findAllWithOwnerIdAndSearchPhrase(UUID ownerId, String search, Pageable pageable) {
        CriteriaQuery<ActivityInfo> queryCriteria = cb.createQuery(ActivityInfo.class);
        Root<ActivityInfo> selectRoot = queryCriteria.from(ActivityInfo.class);
        queryCriteria.select(selectRoot);
        setWhereOwnerIdAndSearchPhrase(queryCriteria, selectRoot);
        addNullsLastSorting(queryCriteria, pageable.getSort(), selectRoot);
        TypedQuery<ActivityInfo> query = em.createQuery(queryCriteria);

        CriteriaQuery<Long> countCriteria = cb.createQuery(Long.class);
        Root<ActivityInfo> selectRootCount = countCriteria.from(ActivityInfo.class);
        countCriteria.select(cb.count(selectRootCount));
        setWhereOwnerIdAndSearchPhrase(countCriteria, selectRootCount);
        TypedQuery<Long> queryCount = em.createQuery(countCriteria);

        String searchLike = "%" + search.toUpperCase() + "%";

        List<ActivityInfo> results = query.setParameter(accountIdSearch, ownerId)
            .setParameter(nameSearch, searchLike)
            .setParameter(alternateNameSearch, searchLike)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .getResultList();

        Long total = queryCount.setParameter(accountIdSearch, ownerId)
            .setParameter(nameSearch, searchLike)
            .setParameter(alternateNameSearch, searchLike)
            .getSingleResult();

        return new PageImpl<>(results, pageable, total.intValue());
    }

    private void setWhereOwnerId(CriteriaQuery<?> criteriaQuery, Root<ActivityInfo> root) {
        criteriaQuery.where(cb.equal(root.get(ACCOUNT_ID), accountIdSearch));
    }

    private void setWhereOwnerIdAndSearchPhrase(CriteriaQuery<?> criteriaQuery, Root<ActivityInfo> root) {
        criteriaQuery.where(
            cb.and(
                cb.equal(root.get(ACCOUNT_ID), accountIdSearch),
                cb.or(
                    cb.like(cb.upper(root.get(NAME)), nameSearch),
                    cb.like(cb.upper(root.get(ALTERNATE_NAME)), alternateNameSearch)
                )
            ));
    }

    private void addNullsLastSorting(CriteriaQuery<ActivityInfo> queryCriteria,
                                                Sort sort,
                                                Root<ActivityInfo> root) {
        setOrder(queryCriteria, root, sort, RECENT, START_DATE);
        setOrder(queryCriteria, root, sort, RECOMMENDED, NONE);
    }

    private void setOrder(CriteriaQuery<ActivityInfo> queryCriteria, Root<ActivityInfo> root,
                          final Sort sort, final String field, final Object defaultVal) {
        Sort.Order recent = sort.getOrderFor(field);
        if (recent != null) {
            if (recent.isAscending()) {
                queryCriteria.orderBy(
                    cb.asc(cb.coalesce(root.get(field), defaultVal)));
            } else {
                queryCriteria.orderBy(
                    cb.desc(cb.coalesce(root.get(field), defaultVal)));
            }
        }
    }
}
