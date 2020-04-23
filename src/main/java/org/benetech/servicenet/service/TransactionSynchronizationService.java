package org.benetech.servicenet.service;

import org.benetech.servicenet.domain.Organization;

public interface TransactionSynchronizationService {

    void registerSynchronizationOfMatchingOrganizations(Organization organization);
}
