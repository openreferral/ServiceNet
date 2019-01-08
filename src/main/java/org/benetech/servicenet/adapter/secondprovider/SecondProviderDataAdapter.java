package org.benetech.servicenet.adapter.secondprovider;

import org.benetech.servicenet.adapter.MultipleDataAdapter;
import org.benetech.servicenet.adapter.shared.model.MultipleImportData;
import org.benetech.servicenet.domain.DataImportReport;
import org.springframework.stereotype.Component;

/**
 * DataAdapter for the second example data set
 */
@Component("SecondProviderDataAdapter")
public class SecondProviderDataAdapter extends MultipleDataAdapter {

    @Override
    public DataImportReport importData(MultipleImportData data) {
        //TODO
        return data.getReport();
    }
}
