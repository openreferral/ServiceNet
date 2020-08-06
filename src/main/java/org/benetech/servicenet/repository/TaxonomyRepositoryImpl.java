package org.benetech.servicenet.repository;

import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.ServiceTaxonomy;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Taxonomy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class TaxonomyRepositoryImpl implements TaxonomyRepositoryCustom {
    private static final String ID = "id";

    private static final String SERVICES = "services";
    private static final String TAXONOMIES = "taxonomies";
    private static final String TAXONOMY = "taxonomy";
    private static final String ACTIVE = "active";
    private static final String ORGANIZATION = "organizationRecord";

    private final EntityManager em;
    private final CriteriaBuilder cb;

    public TaxonomyRepositoryImpl(EntityManager em) {
        this.em = em;
        this.cb = em.getCriteriaBuilder();
    }

    public List<Taxonomy> findAssociatedTaxonomies() {
        CriteriaQuery<Taxonomy> queryCriteria = cb.createQuery(Taxonomy.class);
        Root<Taxonomy> selectRoot = queryCriteria.from(Taxonomy.class);

        queryCriteria.select(selectRoot);

        addFilters(queryCriteria, selectRoot);

        return em.createQuery(queryCriteria)
            .getResultList();
    }

    public Page<Taxonomy> findAssociatedTaxonomies(Pageable pageable) {

        CriteriaQuery<Taxonomy> queryCriteria = cb.createQuery(Taxonomy.class);
        Root<Taxonomy> selectRoot = queryCriteria.from(Taxonomy.class);

        queryCriteria.select(selectRoot);

        addFilters(queryCriteria, selectRoot);

        CriteriaQuery<Long> countCriteria = cb.createQuery(Long.class);
        Root<Taxonomy> selectRootCount = countCriteria.from(Taxonomy.class);

        countCriteria.select(cb.countDistinct(selectRootCount));

        addFilters(countCriteria, selectRootCount);

        List<Taxonomy> results = em.createQuery(queryCriteria)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .getResultList();

        Long total = em.createQuery(countCriteria).getSingleResult();

        return new PageImpl<>(results, pageable, total.intValue());
    }

    private <T> void addFilters(CriteriaQuery<T> query, Root<Taxonomy> root) {
        Subquery<UUID> subquery = query.subquery(UUID.class);
        Root<Organization> subRoot = subquery.from(Organization.class);
        Join<Organization, Service> serviceJoin = subRoot.join(SERVICES, JoinType.LEFT);
        Join<Service, ServiceTaxonomy> taxonomiesJoin = serviceJoin.join(TAXONOMIES, JoinType.LEFT);
        subquery.select(taxonomiesJoin.get(TAXONOMY).get(ID)).where(subRoot.get(ACTIVE));

        Predicate predicate = cb.in(root.get(ID)).value(subquery);
        query.where(predicate);
    }

}
