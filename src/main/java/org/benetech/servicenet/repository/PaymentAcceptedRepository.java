package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.PaymentAccepted;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the PaymentAccepted entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentAcceptedRepository extends JpaRepository<PaymentAccepted, UUID> {

}
