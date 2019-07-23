package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.GeocodingResult;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


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
}
