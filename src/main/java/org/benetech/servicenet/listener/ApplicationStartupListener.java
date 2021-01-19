package org.benetech.servicenet.listener;

import org.benetech.servicenet.service.TransactionSynchronizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartupListener {

    @Autowired
    private TransactionSynchronizationService transactionSynchronizationService;

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        transactionSynchronizationService.updateOrganizationMatchesWithoutSynchronization();
    }
}
