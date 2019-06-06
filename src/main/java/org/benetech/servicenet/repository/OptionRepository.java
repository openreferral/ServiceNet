package org.benetech.servicenet.repository;

import java.util.List;
import java.util.UUID;
import org.benetech.servicenet.domain.Option;
import org.benetech.servicenet.domain.enumeration.OptionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Option entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OptionRepository extends JpaRepository<Option, UUID> {
    List<Option> findByType(OptionType type);

}
