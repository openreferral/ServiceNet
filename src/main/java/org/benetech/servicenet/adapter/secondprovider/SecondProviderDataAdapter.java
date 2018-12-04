package org.benetech.servicenet.adapter.secondprovider;

import org.apache.commons.lang3.NotImplementedException;
import org.benetech.servicenet.adapter.AbstractDataAdapter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * DataAdapter for the second example data set
 */
@Component("SecondProviderDataAdapter")
public class SecondProviderDataAdapter extends AbstractDataAdapter {

    @Override
    public void persistData(String data) {
        throw new NotImplementedException(SINGLE_MAPPING_NOT_SUPPORTED);
    }

    @Override
    public void persistData(List<String> data) {
        //TODO
    }

    @Override
    public boolean isUsedForSingleObjects() {
        return false;
    }
}
