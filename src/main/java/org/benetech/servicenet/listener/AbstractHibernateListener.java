package org.benetech.servicenet.listener;

import org.benetech.servicenet.config.Constants;
import org.benetech.servicenet.domain.Authority;
import org.benetech.servicenet.domain.Metadata;
import org.benetech.servicenet.domain.PersistentAuditEvent;
import org.benetech.servicenet.domain.PersistentToken;
import org.benetech.servicenet.domain.enumeration.ActionType;

import java.util.UUID;

abstract class AbstractHibernateListener {

    boolean shouldTrackMetadata(Object object) {
        return !(object instanceof PersistentAuditEvent || object instanceof Metadata
            || object instanceof PersistentToken || object instanceof Authority);
    }

    Metadata prepareMetadataForAllFields(UUID resourceId, ActionType actionType, String resourceClass) {
        return new Metadata()
            .resourceId(resourceId)
            .fieldName(Constants.ALL_FIELDS)
            .lastActionType(actionType)
            .resourceClass(resourceClass);
    }
}
