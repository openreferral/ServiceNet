package org.benetech.servicenet.adapter.icarol.eden;

import org.benetech.servicenet.adapter.icarol.AbstractICarolDataAdapter;
import org.benetech.servicenet.adapter.shared.model.SingleImportData;
import org.benetech.servicenet.domain.DataImportReport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("EdenDataAdapter")
public class EdenDataAdapter extends AbstractICarolDataAdapter {

    @Value("${scheduler.eden-data.api-key}")
    private String edenApiKey;

    @Value("${adapter.icarol.uri}")
    private String uri;

    @Override
    public DataImportReport importData(SingleImportData importData) {
        return super.importData(importData, uri);
    }

    @Override
    protected String getApiKey() {
        return edenApiKey;
    }
}
