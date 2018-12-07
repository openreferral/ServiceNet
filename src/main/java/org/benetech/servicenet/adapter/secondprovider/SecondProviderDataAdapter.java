package org.benetech.servicenet.adapter.secondprovider;

import org.benetech.servicenet.adapter.MultipleDataAdapter;
import org.benetech.servicenet.adapter.shared.model.MultipleImportData;
import org.springframework.stereotype.Component;

/**
 * DataAdapter for the second example data set
 */
@Component("SecondProviderDataAdapter")
public class SecondProviderDataAdapter extends MultipleDataAdapter {

    @Override
    public void importData(MultipleImportData data) {
        //TODO
    }
}
