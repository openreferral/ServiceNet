package org.benetech.servicenet.listener;

import org.benetech.servicenet.domain.enumeration.ActionType;
import org.benetech.servicenet.service.MetadataService;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.UUID;

@Component
public class HibernatePostCreateListener extends AbstractHibernateListener implements PostInsertEventListener {

    @Autowired
    private MetadataService metadataService;

    @Override
    public boolean requiresPostCommitHanding(EntityPersister persister) {
        return false;
    }

    @Override
    public void onPostInsert(PostInsertEvent event) {
        if (shouldTrackMetadata(event.getEntity())) {
            persistMetaData(event);
        }
    }

    private void persistMetaData(PostInsertEvent event) {
        metadataService.saveForCurrentOrSystemUser(
            Collections.singletonList(prepareMetadataForAllFields(
                (UUID) event.getId(), ActionType.CREATE, event.getEntity().getClass().getSimpleName())));
    }
}
