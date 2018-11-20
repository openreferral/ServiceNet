package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.AccessibilityForDisabilities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the AccessibilityForDisabilities entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccessibilityForDisabilitiesRepository extends JpaRepository<AccessibilityForDisabilities, Long> {

}
