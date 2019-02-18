package org.benetech.servicenet.listener;

import org.benetech.servicenet.config.Constants;
import org.benetech.servicenet.domain.Metadata;
import org.benetech.servicenet.domain.PersistentAuditEvent;
import org.benetech.servicenet.domain.enumeration.ActionType;

abstract class AbstractHibernateListener {

    boolean shouldTrackMetadata(Object object) {
        return !(object instanceof PersistentAuditEvent || object instanceof Metadata);
    }

    Metadata prepareMetadataForAllFields(String resourceId, ActionType actionType, String resourceClass) {
        return new Metadata()
            .resourceId(resourceId)
            .fieldName(Constants.ALL_FIELDS)
            .lastActionType(actionType)
            .resourceClass(resourceClass);
    }
}
