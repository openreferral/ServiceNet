package org.benetech.servicenet.adapter.icarol.uwba;

import org.benetech.servicenet.adapter.icarol.AbstractICarolDataAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("UWBADataAdapter")
public class UWBADataAdapter extends AbstractICarolDataAdapter {

    @Value("${scheduler.uwba-data.api-key}")
    private String uwbaApiKey;

    @Override
    protected String getApiKey() {
        return uwbaApiKey;
    }
}
