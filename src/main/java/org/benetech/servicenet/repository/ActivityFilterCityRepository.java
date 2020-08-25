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
import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.domain.GeocodingResult;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Silo;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.domain.UserProfile;
import org.springframework.stereotype.Repository;

@Repository
public class ActivityFilterCityRepository {

    private static final String ID = "id";

    private static final String NAME = "name";
    private static final String ACTIVE = "active";

    private static final String ACCOUNT = "account";
    private static final String USER_PROFILES = "userProfiles";
    private static final String SILO = "silo";

    private static final String LOCATIONS = "locations";
    private static final String ORGANIZATION = "organization";

    private static final String LOCALITY = "locality";

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

        Join<GeocodingResult, Location> locationJoin = root.join(LOCATIONS, JoinType.LEFT);
        Join<Location, Organization> organizationJoin = locationJoin.join(ORGANIZATION, JoinType.LEFT);
        Join<Organization, SystemAccount> systemAccountJoin = organizationJoin.join(ACCOUNT, JoinType.LEFT);
        Join<Organization, UserProfile> userProfileJoin = organizationJoin.join(USER_PROFILES, JoinType.LEFT);

        predicate = cb.equal(organizationJoin.get(ACTIVE), true);

        predicate = cb.and(predicate, cb.equal(systemAccountJoin.get(NAME), SERVICE_PROVIDER));

        predicate = cb.and(predicate,
            cb.equal(userProfileJoin.get(SILO), (silo != null) ? silo : userProfile.getSilo()));

        if (userProfile != null) {
            predicate = cb
                .and(predicate, cb.notEqual(userProfileJoin.get(ID), userProfile.getId()));
        }

        predicate = cb.and(predicate, cb.isNotNull(root.get(LOCALITY)));

        predicate = cb.and(predicate, cb.notEqual(root.get(LOCALITY), ""));

        query.where(predicate);
        query.select(root.get(LOCALITY)).distinct(true);
        query.groupBy(root.get(ID), organizationJoin.get(ID));
    }
}
