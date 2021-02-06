package org.benetech.servicenet.adapter.smcconnect;

import org.benetech.servicenet.adapter.MultipleDataAdapter;
import org.benetech.servicenet.adapter.shared.model.MultipleImportData;
import org.benetech.servicenet.adapter.smcconnect.persistence.SmcDataManager;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.manager.ImportManager;
import org.benetech.servicenet.service.TransactionSynchronizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("SMCConnectDataAdapter")
public class SMCConnectDataAdapter extends MultipleDataAdapter {

    private static final int NUMBER_OF_FILES = 10;

    @Autowired
    private ImportManager importManager;

    @Autowired
    private TransactionSynchronizationService transactionSynchronizationService;

    @Override
    public DataImportReport importData(MultipleImportData data) {
        verifyData(data);
        DataImportReport report = new SmcDataManager(importManager, data).importData(data);
        transactionSynchronizationService.registerSynchronizationOfMatchingOrganizations();
        return report;
    }

    @Override
    protected int getNumberOfFilesToProcess() {
        return NUMBER_OF_FILES;
    }
}
