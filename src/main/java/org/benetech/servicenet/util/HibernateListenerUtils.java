package org.benetech.servicenet.util;

import org.benetech.servicenet.domain.Metadata;
import org.benetech.servicenet.domain.PersistentAuditEvent;
import org.benetech.servicenet.domain.enumeration.ActionType;

public final class HibernateListenerUtils {

    private static final String FIELD_NAME = "ALL FIELDS";

    public static boolean shouldTrackMetadata(Object object) {
        return !(object instanceof PersistentAuditEvent || object instanceof Metadata);
    }

    public static Metadata prepareMetadataForAllFields(String resourceId, ActionType actionType, String resourceClass) {
        return new Metadata()
            .resourceId(resourceId)
            .fieldName(FIELD_NAME)
            .lastActionType(actionType)
            .resourceClass(resourceClass);
    }

    private HibernateListenerUtils() {
    }
}
