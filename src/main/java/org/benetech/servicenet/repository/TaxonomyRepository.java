package org.benetech.servicenet.repository;

import java.util.SortedSet;
import org.benetech.servicenet.domain.Taxonomy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


/**
 * Spring Data  repository for the Taxonomy entity.
 */
@Repository
public interface TaxonomyRepository extends JpaRepository<Taxonomy, UUID> {

    Optional<Taxonomy> findOneByExternalDbIdAndProviderName(String externalDbId, String providerName);

    Optional<Taxonomy> findOneByTaxonomyIdAndProviderName(String taxonomyId, String providerName);

    @Query("SELECT DISTINCT tax.name FROM Taxonomy tax WHERE tax.providerName = :providerName ORDER BY tax.name")
    SortedSet<String> getTaxonomyNamesForProviderName(@Param("providerName") String providerName);

    @Query("SELECT DISTINCT tax.name FROM Taxonomy tax "
        + "WHERE length(tax.taxonomyId) < 3 AND tax.providerName = :providerName ORDER BY tax.name")
    SortedSet<String> getICarolTaxonomyNamesForProviderName(@Param("providerName") String providerName);
    
    Page<Taxonomy> findAll(Pageable pageable);
}
