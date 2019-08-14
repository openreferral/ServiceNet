package org.benetech.servicenet.repository;

import java.util.List;
import java.util.SortedSet;
import java.util.UUID;
import org.benetech.servicenet.domain.GeocodingResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the GeocodingResult entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GeocodingResultRepository extends JpaRepository<GeocodingResult, UUID> {

    String ADDRESS_CACHE = "addressCache";

    @Cacheable(ADDRESS_CACHE)
    List<GeocodingResult> findAllByAddress(String address);

    @Override
    @CacheEvict(value = "addressCache", allEntries = true)
    <S extends GeocodingResult> List<S> saveAll(Iterable<S> var1);

    @Override
    @CacheEvict(value = "addressCache", allEntries = true)
    <S extends GeocodingResult> S saveAndFlush(S var1);

    @Override
    @CacheEvict(value = "addressCache", allEntries = true)
    void deleteInBatch(Iterable<GeocodingResult> var1);

    @Override
    @CacheEvict(value = "addressCache", allEntries = true)
    void deleteAllInBatch();

    Page<GeocodingResult> findAll(Pageable pageable);

    @Query("SELECT DISTINCT gr.postalCode FROM Location l "
        + "JOIN l.geocodingResults gr "
        + "WHERE gr.postalCode != ''"
        + "ORDER BY gr.postalCode")
    SortedSet<String> getDistinctPostalCodesFromGeoResults();

    @Query("SELECT DISTINCT gr.administrativeAreaLevel2 FROM Location l "
        + "JOIN l.geocodingResults gr "
        + "WHERE gr.administrativeAreaLevel2 != ''"
        + "ORDER BY gr.administrativeAreaLevel2")
    SortedSet<String> getDistinctRegionsFromGeoResults();

    @Query("SELECT DISTINCT gr.locality FROM Location l "
        + "JOIN l.geocodingResults gr "
        + "WHERE gr.locality != ''"
        + "ORDER BY gr.locality")
    SortedSet<String> getDistinctCityFromGeoResults();
}
