package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.SystemAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


/**
 * Spring Data  repository for the SystemAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SystemAccountRepository extends JpaRepository<SystemAccount, UUID> {

    Optional<SystemAccount> findByName(String name);

}
