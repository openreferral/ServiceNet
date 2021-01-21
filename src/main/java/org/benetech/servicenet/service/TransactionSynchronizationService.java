package org.benetech.servicenet.service;

import java.util.UUID;

public interface TransactionSynchronizationService {
    void registerSynchronizationOfMatchingOrganizations();

    void registerSynchronizationOfMatchingOrganizations(UUID organizationId);
}
