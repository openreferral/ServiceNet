package org.benetech.servicenet.adapter.smcconnect.persistence;

import org.benetech.servicenet.adapter.shared.model.MultipleImportData;
import org.benetech.servicenet.adapter.smcconnect.model.SmcBaseData;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.DocumentUpload;
import org.benetech.servicenet.manager.ImportManager;

import java.util.List;

public class SmcDataManager {

    private final SmcStorage storage = new SmcStorage();
    private final DataImportReport report;
    private final PersistenceManager persistenceManager;

    public SmcDataManager(ImportManager importManager, MultipleImportData data) {
        this.report = data.getReport();
        this.persistenceManager = new PersistenceManager(importManager, data, storage);
    }

    public DataImportReport importData(MultipleImportData data) {
        DocumentUpload documentUpload = collectData(data);
        return persistData(documentUpload);
    }

    private void collectData(MultipleImportData data, DataResolver dataResolver, int i) {
        String objectJson = data.getMultipleObjectsData().get(i);
        String filename = data.getDocumentUploads().get(i).getFilename();

        List<SmcBaseData> baseDataList = dataResolver.getDataFromJson(objectJson, filename);

        for (SmcBaseData baseData : baseDataList) {
            storage.addRawData(baseData);
        }
    }

    private DocumentUpload collectData(MultipleImportData data) {
        DataResolver dataResolver = new DataResolver();
        DocumentUpload documentUpload = null;
        for (int i = 0; i < data.getDocumentUploads().size(); i++) {
            if (isOrganizationsFile(data, i)) {
                documentUpload = data.getDocumentUploads().get(i);
            }
            collectData(data, dataResolver, i);
        }
        return documentUpload;
    }

    private boolean isOrganizationsFile(MultipleImportData data, int i) {
        return DataType.valueOf(data.getDocumentUploads().get(i).getFilename().toUpperCase().split("\\.")[0])
            .equals(DataType.ORGANIZATIONS);
    }

    private DataImportReport persistData(DocumentUpload sourceDocument) {
        persistenceManager.saveOrganizationsAndRelatedData(sourceDocument);
        return report;
    }
}
