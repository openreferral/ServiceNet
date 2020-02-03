package org.benetech.servicenet.repository;

import java.util.UUID;
import org.benetech.servicenet.domain.PostalAddressFieldsValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PostalAddressFieldsValue entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PostalAddressFieldsValueRepository extends JpaRepository<PostalAddressFieldsValue, UUID> {

}
