package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.Funding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the Funding entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FundingRepository extends JpaRepository<Funding, UUID> {

}
