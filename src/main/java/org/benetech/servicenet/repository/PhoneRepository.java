package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;


/**
 * Spring Data  repository for the Phone entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PhoneRepository extends JpaRepository<Phone, UUID> {

    @Query("select phone from Phone phone where phone.organization.id =:orgId")
    Set<Phone> findAllByOrganization(@Param("orgId") UUID orgId);

}
