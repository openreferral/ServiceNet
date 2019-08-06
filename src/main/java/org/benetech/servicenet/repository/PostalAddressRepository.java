package org.benetech.servicenet.repository;

import java.util.SortedSet;
import java.util.UUID;

import org.benetech.servicenet.domain.PostalAddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for the PostalAddress entity.
 */
@Repository
public interface PostalAddressRepository extends JpaRepository<PostalAddress, UUID> {

  @Query("SELECT DISTINCT pa.postalCode FROM PostalAddress pa " + "LEFT JOIN Location l ON l = pa.location "
      + "LEFT JOIN Organization o ON l.organization = o WHERE pa.postalCode = trim(pa.postalCode) "
      + "AND pa.postalCode != '' AND o.account.id = :systemAccountId " + "ORDER BY pa.postalCode")
  SortedSet<String> getDistinctPostalCodesForSystemAccount(@Param("systemAccountId") UUID systemAccountId);

  @Query("SELECT DISTINCT pa.region FROM PostalAddress pa " + "LEFT JOIN Location l ON l = pa.location "
      + "LEFT JOIN Organization o ON l.organization = o " + "WHERE pa.region != '' AND o.account.id = :systemAccountId "
      + "ORDER BY pa.region")
  SortedSet<String> getRegionsForSystemAccount(@Param("systemAccountId") UUID systemAccountId);

  @Query("SELECT DISTINCT pa.city FROM PostalAddress pa " + "LEFT JOIN Location l ON l = pa.location "
      + "LEFT JOIN Organization o ON l.organization = o " + "WHERE pa.city != '' AND o.account.id = :systemAccountId "
      + "ORDER BY pa.city")
  SortedSet<String> getCitiesForSystemAccount(@Param("systemAccountId") UUID systemAccountId);

    Page<PostalAddress> findAll(Pageable pageable);
}
