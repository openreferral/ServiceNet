package org.benetech.servicenet.service.impl;

import java.util.UUID;
import org.benetech.servicenet.service.OrganizationMatchService;
import org.benetech.servicenet.service.TransactionSynchronizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
public class TransactionSynchronizationServiceImpl implements TransactionSynchronizationService {

    @Autowired
    private OrganizationMatchService organizationMatchService;

    @Override
    public void updateOrganizationMatchesWithoutSynchronization() {
        organizationMatchService.createOrUpdateOrganizationMatches();
    }

    @Override
    public void registerSynchronizationOfMatchingOrganizations(UUID organizationId) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                organizationMatchService.createOrUpdateOrganizationMatches(organizationId);
            }
        });
    }
}
