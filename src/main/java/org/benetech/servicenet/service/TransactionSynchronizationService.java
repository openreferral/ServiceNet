package org.benetech.servicenet.service;

import java.util.UUID;

public interface TransactionSynchronizationService {
    void updateOrganizationMatchesWithoutSynchronization();

    void registerSynchronizationOfMatchingOrganizations(UUID organizationId);
}
