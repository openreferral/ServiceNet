package org.benetech.servicenet.util;

import org.benetech.servicenet.domain.Metadata;
import org.benetech.servicenet.domain.PersistentAuditEvent;
import org.benetech.servicenet.domain.enumeration.ActionType;

public class HibernateListenerUtils {

    private static final String FIELD_NAME = "ALL FIELDS";

    public static boolean shouldTrackMetadata(Object object) {
        return !(object instanceof PersistentAuditEvent || object instanceof Metadata);
    }

    public static Metadata prepareMetadataForAllFields(String resourceId, ActionType actionType) {
        Metadata result = new Metadata();
        result.setResourceId(resourceId);
        result.setFieldName(FIELD_NAME);
        result.setLastActionType(actionType);
        return result;
    }
}
