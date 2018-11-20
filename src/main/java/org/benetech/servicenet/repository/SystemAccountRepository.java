package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.SystemAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SystemAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SystemAccountRepository extends JpaRepository<SystemAccount, Long> {

}
