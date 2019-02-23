package org.benetech.servicenet.service;

import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.matching.model.MatchingContext;

public interface TransactionSynchronizationService {

    void registerSynchronizationOfMatchingOrganizations(Organization organization, MatchingContext context);
}
