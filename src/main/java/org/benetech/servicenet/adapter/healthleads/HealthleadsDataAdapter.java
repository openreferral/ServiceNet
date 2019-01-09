package org.benetech.servicenet.adapter.healthleads;

import org.benetech.servicenet.adapter.MultipleDataAdapter;
import org.benetech.servicenet.adapter.healthleads.model.BaseData;
import org.benetech.servicenet.adapter.shared.model.MultipleImportData;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.service.ImportService;
import org.benetech.servicenet.web.rest.errors.IncorrectFilesNumberEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("healthleadsDataAdapter")
public class HealthleadsDataAdapter extends MultipleDataAdapter {

    private static final int NUMBER_OF_FILES_TO_PROCESS = 11;

    @Autowired
    private ImportService importService;

    @Override
    public DataImportReport importData(MultipleImportData data) {
        if (data.getDocumentUploads().size() != NUMBER_OF_FILES_TO_PROCESS) {
            throw new IncorrectFilesNumberEException(NUMBER_OF_FILES_TO_PROCESS);
        }
        JsonDataResolver dataResolver = new JsonDataResolver();
        HealthLeadsDataMapper mapper = HealthLeadsDataMapper.INSTANCE;
        HealthleadsDataPersistence collector = new HealthleadsDataPersistence();
        for (int i = 0; i < data.getDocumentUploads().size(); i++) {
            String objectJson = data.getMultipleObjectsData().get(i);
            String filename = data.getDocumentUploads().get(i).getFilename();

            List<BaseData> baseDataList = dataResolver.getDataFromJson(objectJson, filename);

            for (BaseData baseData : baseDataList) {
                collector.addData(baseData);
            }
        }

        collector.persistData(importService, mapper, data.getProviderName(), data.getReport());
        return data.getReport();
    }
}
