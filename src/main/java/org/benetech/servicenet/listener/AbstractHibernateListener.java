package org.benetech.servicenet.listener;

import org.benetech.servicenet.domain.Metadata;
import org.benetech.servicenet.domain.PersistentAuditEvent;
import org.benetech.servicenet.domain.enumeration.ActionType;

abstract class AbstractHibernateListener {

    private static final String FIELD_NAME = "ALL FIELDS";

    boolean shouldTrackMetadata(Object object) {
        return !(object instanceof PersistentAuditEvent || object instanceof Metadata);
    }

    Metadata prepareMetadataForAllFields(String resourceId, ActionType actionType, String resourceClass) {
        return new Metadata()
            .resourceId(resourceId)
            .fieldName(FIELD_NAME)
            .lastActionType(actionType)
            .resourceClass(resourceClass);
    }
}
