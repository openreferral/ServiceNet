package org.benetech.servicenet.repository;

import static org.benetech.servicenet.config.Constants.SERVICE_PROVIDER;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.benetech.servicenet.domain.GeocodingResult;
import org.benetech.servicenet.domain.GeocodingResult_;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Location_;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Organization_;
import org.benetech.servicenet.domain.Silo;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.domain.SystemAccount_;
import org.benetech.servicenet.domain.UserProfile;
import org.benetech.servicenet.domain.UserProfile_;
import org.springframework.stereotype.Repository;

@Repository
public class ActivityFilterCityRepository {

    private final EntityManager em;
    private final CriteriaBuilder cb;

    public ActivityFilterCityRepository(EntityManager em) {
        this.em = em;
        this.cb = em.getCriteriaBuilder();
    }

    public Set<String> getCities(UserProfile userProfile, Silo silo) {
        CriteriaQuery<String> queryCriteria = cb.createQuery(String.class);
        Root<GeocodingResult> selectRoot = queryCriteria.from(GeocodingResult.class);

        addFiltersAndSelectForCities(queryCriteria, selectRoot, userProfile, silo);

        Query query = em.createQuery(queryCriteria);

        List<String> results = query.getResultList();
        if (results == null) {
            return new HashSet<>();
        } else {
            return new HashSet<>(results);
        }
    }

    private <T> void addFiltersAndSelectForCities(
        CriteriaQuery<String> query, Root<GeocodingResult> root, UserProfile userProfile, Silo silo) {
        Predicate predicate = cb.conjunction();

        Join<GeocodingResult, Location> locationJoin = root.join(GeocodingResult_.LOCATIONS, JoinType.LEFT);
        Join<Location, Organization> organizationJoin = locationJoin.join(Location_.ORGANIZATION, JoinType.LEFT);
        Join<Organization, SystemAccount> systemAccountJoin = organizationJoin.join(Organization_.ACCOUNT, JoinType.LEFT);
        Join<Organization, UserProfile> userProfileJoin = organizationJoin.join(Organization_.USER_PROFILES, JoinType.LEFT);

        predicate = cb.equal(organizationJoin.get(Organization_.ACTIVE), true);

        predicate = cb.and(predicate, cb.equal(systemAccountJoin.get(SystemAccount_.NAME), SERVICE_PROVIDER));

        predicate = cb.and(predicate,
            cb.equal(userProfileJoin.get(UserProfile_.SILO), (silo != null) ? silo : userProfile.getSilo()));

        if (userProfile != null) {
            predicate = cb
                .and(predicate, cb.notEqual(userProfileJoin.get(UserProfile_.ID), userProfile.getId()));
        }

        predicate = cb.and(predicate, cb.isNotNull(root.get(GeocodingResult_.LOCALITY)));

        predicate = cb.and(predicate, cb.notEqual(root.get(GeocodingResult_.LOCALITY), ""));

        query.where(predicate);
        query.select(root.get(GeocodingResult_.LOCALITY)).distinct(true);
        query.groupBy(root.get(GeocodingResult_.ID), organizationJoin.get(Organization_.ID));
    }
}
