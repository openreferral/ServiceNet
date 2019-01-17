package org.benetech.servicenet.adapter.laac;

import org.benetech.servicenet.adapter.SingleDataAdapter;
import org.benetech.servicenet.adapter.shared.model.SingleImportData;
import org.benetech.servicenet.domain.DataImportReport;
import org.springframework.stereotype.Component;

@Component("LAACDataAdapter")
public class LAACDataAdapter extends SingleDataAdapter {

    @Override
    public DataImportReport importData(SingleImportData data) {
        return null;
    }
}
