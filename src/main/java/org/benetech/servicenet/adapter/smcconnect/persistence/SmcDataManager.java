package org.benetech.servicenet.adapter.smcconnect.persistence;

import org.benetech.servicenet.adapter.shared.model.MultipleImportData;
import org.benetech.servicenet.adapter.smcconnect.model.SmcBaseData;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.service.ImportService;

import java.util.List;

public class SmcDataManager {

    private final Storage storage = new Storage();
    private final DataImportReport report;
    private final RelationManager relationManager;

    public SmcDataManager(ImportService importService, MultipleImportData data) {
        this.report = data.getReport();
        this.relationManager = new RelationManager(importService, data, storage);
    }

    public DataImportReport importData(MultipleImportData data) {
        collectData(data);
        return persistData();
    }

    private void collectData(MultipleImportData data, DataResolver dataResolver, int i) {
        String objectJson = data.getMultipleObjectsData().get(i);
        String filename = data.getDocumentUploads().get(i).getFilename();

        List<SmcBaseData> baseDataList = dataResolver.getDataFromJson(objectJson, filename);

        for (SmcBaseData baseData : baseDataList) {
            storage.addData(baseData);
        }
    }

    private void collectData(MultipleImportData data) {
        DataResolver dataResolver = new DataResolver();
        for (int i = 0; i < data.getDocumentUploads().size(); i++) {
            collectData(data, dataResolver, i);
        }
    }

    private DataImportReport persistData() {
        relationManager.saveOrganizationsAndRelatedData();
        relationManager.saveLocationsAndLocationRelatedData();
        relationManager.saveServicesAndServiceRelatedData();
        return report;
    }
}
