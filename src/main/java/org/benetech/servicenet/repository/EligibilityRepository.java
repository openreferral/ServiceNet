package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.Eligibility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Eligibility entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EligibilityRepository extends JpaRepository<Eligibility, Long> {

}
