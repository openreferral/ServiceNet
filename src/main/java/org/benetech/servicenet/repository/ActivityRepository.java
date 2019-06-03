package org.benetech.servicenet.repository;

import org.apache.commons.collections4.CollectionUtils;
import org.benetech.servicenet.domain.view.ActivityInfo;
import org.benetech.servicenet.service.dto.FiltersActivityDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Repository
public class ActivityRepository {
    
    private static final String RECENT = "recent";

    private static final String RECOMMENDED = "recommended";

    private final EntityManager em;

    public ActivityRepository(EntityManager em) {
        this.em = em;
    }

    public Page<ActivityInfo> findAllWithOwnerIdAndSearchPhraseAndFilter(UUID ownerId, String searchName, Pageable pageable,
                                                                         FiltersActivityDTO filtersActivityDTO) {
        List<String> cityParameter = CollectionUtils.isNotEmpty(
            filtersActivityDTO.getCitiesFilterList()) ?
            filtersActivityDTO.getCitiesFilterList() : new ArrayList<>(Arrays.asList(""));

        List<String> regionParameter = CollectionUtils.isNotEmpty(
            filtersActivityDTO.getRegionFilterList()) ?
            filtersActivityDTO.getRegionFilterList() : new ArrayList<>(Arrays.asList(""));

        List<String> postalCodeParameter = CollectionUtils.isNotEmpty(
            filtersActivityDTO.getPostalCodesFilterList()) ?
            filtersActivityDTO.getPostalCodesFilterList() : new ArrayList<>(Arrays.asList(""));

        List<UUID> partnersParameter = CollectionUtils.isNotEmpty(
            filtersActivityDTO.getPartnerFilterList()) ?
            filtersActivityDTO.getPartnerFilterList() : new ArrayList<>(Arrays.asList(new UUID(0L, 0L)));

        String recommendedSortParameter = pageable.getSort().getOrderFor(RECOMMENDED) != null ?
            Objects.requireNonNull(pageable.getSort().getOrderFor(RECOMMENDED)).getDirection().toString() : "";

        if (recommendedSortParameter.equalsIgnoreCase(Sort.Direction.DESC.toString())) {
            recommendedSortParameter = recommendedSortParameter + " NULLS LAST";
        }

        String recentSortParameter = pageable.getSort().getOrderFor(RECENT) != null ?
            Objects.requireNonNull(pageable.getSort().getOrderFor(RECENT)).getDirection().toString() : "";

        if (recentSortParameter.equalsIgnoreCase(Sort.Direction.DESC.toString())) {
            recentSortParameter = recentSortParameter + " NULLS LAST";
        }

        List<ActivityInfo> results = em.createNativeQuery(
        "SELECT org.id, org.NAME, org.alternate_name, org.account_id, conf.recent, conf.recommended " +
                "FROM ORGANIZATION org " +
                "LEFT JOIN " +
                    "(SELECT resource_id, count(resource_id) recommended, " +
            "max(offered_value_date) recent " +
                    "FROM CONFLICT " +
                    "WHERE state = 'PENDING' " +
                    "GROUP BY resource_id) conf " +
                "ON org.id = conf.resource_id " +
                "LEFT JOIN location l ON l.organization_id = org.id " +
                "LEFT JOIN postal_address pos_a ON l.id = pos_a.location_id " +
                "LEFT JOIN physical_address ph_a ON l.id = ph_a.location_id " +
                "LEFT JOIN organization_match om ON om.organization_record_id = org.id " +
                "LEFT JOIN organization matchedOrganization ON matchedOrganization.id = om.partner_version_id " +
                "WHERE " +
                    "('' IN (:citiesFilterList) OR " +
                        "pos_a.city IN (:citiesFilterList) OR " +
                        "ph_a.city IN (:citiesFilterList)) AND " +
                    "('' IN (:regionFilterList) OR " +
                        "pos_a.region IN (:regionFilterList) OR " +
                        "ph_a.region IN (:regionFilterList)) AND " +
                    "('' IN (:postalCodesFilterList) OR " +
                        "pos_a.postal_code IN (:postalCodesFilterList) OR " +
                        "ph_a.postal_code IN (:postalCodesFilterList)) AND " +
                    "('00000000-0000-0000-0000-000000000000' IN (:partnersList) OR " +
                        "matchedOrganization.account_id IN (:partnersList)) AND " +
                    "(org.name ILIKE :searchName OR " +
                    "org.alternate_name ILIKE :searchName) AND " +
                    "org.account_id = :ownerId AND " +
                    "org.active = true " +
                "GROUP BY " +
                    "org.id, " +
                    "org.name, " +
                    "org.account_id, " +
                    "conf.recent, " +
                    "conf.recommended " +
                "ORDER BY " +
                    (!recentSortParameter.isEmpty() ?
                    "conf.recent " + recentSortParameter :
                    "conf.recommended " + recommendedSortParameter) + " " +
                "OFFSET :resultOffset LIMIT :resultLimit",
             ActivityInfo.class)
            .setParameter("citiesFilterList", cityParameter)
            .setParameter("regionFilterList", regionParameter)
            .setParameter("postalCodesFilterList", postalCodeParameter)
            .setParameter("partnersList", partnersParameter)
            .setParameter("searchName", String.format("%%%s%%", searchName))
            .setParameter("ownerId", ownerId)
            .setParameter("resultOffset", pageable.getOffset())
            .setParameter("resultLimit", pageable.getPageSize())
            .getResultList();

        Object totalCount = em.createNativeQuery(
    "SELECT COUNT (*) FROM " +
                 "(SELECT org.id, org.name, org.alternate_name, org.account_id " +
            "FROM ORGANIZATION org " +
            "LEFT JOIN " +
                 "(SELECT resource_id " +
                 "FROM CONFLICT " +
                 "WHERE state = 'PENDING' " +
                 "GROUP BY resource_id) conf " +
            "ON org.id = conf.resource_id " +
            "LEFT JOIN location l ON l.organization_id = org.id " +
            "LEFT JOIN postal_address pos_a ON l.id = pos_a.location_id " +
            "LEFT JOIN physical_address ph_a ON l.id = ph_a.location_id " +
            "LEFT JOIN organization_match om ON om.organization_record_id = org.id " +
            "LEFT JOIN organization matchedOrganization ON matchedOrganization.id = om.partner_version_id " +
            "WHERE " +
                "('' IN (:citiesFilterList) OR " +
                    "pos_a.city IN (:citiesFilterList) OR " +
                    "ph_a.city IN (:citiesFilterList)) AND " +
                "('' IN (:regionFilterList) OR " +
                    "pos_a.region IN (:regionFilterList) OR " +
                    "ph_a.region IN (:regionFilterList)) AND " +
                "('' IN (:postalCodesFilterList) OR " +
                    "pos_a.postal_code IN (:postalCodesFilterList) OR " +
                    "ph_a.postal_code IN (:postalCodesFilterList)) AND " +
                "('00000000-0000-0000-0000-000000000000' IN (:partnersList) OR " +
                    "matchedOrganization.account_id IN (:partnersList)) AND " +
                "(org.name ILIKE :searchName OR " +
                "org.alternate_name ILIKE :searchName) AND " +
                "org.account_id = :ownerId AND " +
                "org.active = true " +
            "GROUP BY " +
                 "org.id, " +
                 "org.name, " +
                 "org.account_id) o"
        )
            .setParameter("citiesFilterList", cityParameter)
            .setParameter("regionFilterList", regionParameter)
            .setParameter("postalCodesFilterList", postalCodeParameter)
            .setParameter("partnersList", partnersParameter)
            .setParameter("searchName", String.format("%%%s%%", searchName))
            .setParameter("ownerId", ownerId)
            .getSingleResult();

        return new PageImpl<>(results, pageable, Long.valueOf(totalCount.toString()));
    }
}
