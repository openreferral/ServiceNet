package org.benetech.servicenet.adapter.healthleads;

import org.benetech.servicenet.adapter.MultipleDataAdapter;
import org.benetech.servicenet.adapter.healthleads.model.BaseData;
import org.benetech.servicenet.adapter.shared.model.MultipleImportData;
import org.benetech.servicenet.domain.DataImportReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.List;

@Component("healthleadsDataAdapter")
public class HealthleadsDataAdapter extends MultipleDataAdapter {

    @Autowired
    private EntityManager em;

    @Override
    public DataImportReport importData(MultipleImportData data) {
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

        collector.persistData(em, mapper);
        return data.getReport();
    }
}
