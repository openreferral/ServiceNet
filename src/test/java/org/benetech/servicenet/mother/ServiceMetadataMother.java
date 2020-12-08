package org.benetech.servicenet.mother;

import org.benetech.servicenet.domain.ServiceMetadata;

public final class ServiceMetadataMother {

    public static final String UPDATED_BY = "User";
    public static final String LAST_ACTION = "Update";

    public static ServiceMetadata createDefault() {
        ServiceMetadata serviceMetadata = new ServiceMetadata();
        serviceMetadata.setUpdatedBy(UPDATED_BY);
        serviceMetadata.setLastActionType(LAST_ACTION);
        return serviceMetadata;
    }

    private ServiceMetadataMother() {
    }
}
