package org.benetech.servicenet.adapter.icarol.eden;

import org.benetech.servicenet.adapter.icarol.AbstractICarolDataAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("EdenDataAdapter")
public class EdenDataAdapter extends AbstractICarolDataAdapter {

    @Value("${scheduler.eden-data.api-key}")
    private String edenApiKey;

    @Override
    protected String getApiKey() {
        return edenApiKey;
    }
}
