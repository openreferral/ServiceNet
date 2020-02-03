package org.benetech.servicenet.repository;

import java.util.UUID;
import org.benetech.servicenet.domain.ContactDetailsFieldsValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ContactDetailsFieldsValue entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactDetailsFieldsValueRepository extends JpaRepository<ContactDetailsFieldsValue, UUID> {

}
