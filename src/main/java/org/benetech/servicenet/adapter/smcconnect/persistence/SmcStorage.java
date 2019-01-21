package org.benetech.servicenet.adapter.smcconnect.persistence;

import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.adapter.shared.model.storage.EntryDictionary;
import org.benetech.servicenet.adapter.smcconnect.model.SmcBaseData;
import org.benetech.servicenet.adapter.smcconnect.model.SmcLocation;
import org.benetech.servicenet.adapter.smcconnect.model.SmcOrganization;
import org.benetech.servicenet.adapter.smcconnect.model.SmcService;

import java.util.Set;

class SmcStorage {

    private EntryDictionary<SmcBaseData> dictionary = new EntryDictionary<>();

    void addRawData(SmcBaseData data) {
        if (isLocationBased(data)) {
            addDataToDictionary(data.getLocationId(), data, SmcLocation.class);
        } else if (isServiceBased(data)) {
            addDataToDictionary(data.getServiceId(), data, SmcService.class);
        } else if (isOrganizationBased(data)) {
            addDataToDictionary(data.getOrganizationId(), data, SmcOrganization.class);
        } else {
            addDataToDictionary(data.getId(), data, SmcBaseData.class);
        }
    }

    <V extends SmcBaseData> Set<V> getRelatedEntities(Class<V> clazz, String key, Class relatedToClass) {
        return dictionary.getRelatedEntities(clazz, key, relatedToClass);
    }

    <V extends SmcBaseData> Set<V> getEntitiesOfClass(Class<V> clazz) {
        return dictionary.getEntitiesOfClass(clazz);
    }

    private void addDataToDictionary(String key, SmcBaseData data, Class relatedToClass) {
        dictionary.addData(key, data, relatedToClass);
    }

    private boolean isLocationBased(SmcBaseData data) {
        return StringUtils.isNotBlank(data.getLocationId());
    }

    private boolean isServiceBased(SmcBaseData data) {
        return StringUtils.isNotBlank(data.getServiceId());
    }

    private boolean isOrganizationBased(SmcBaseData data) {
        return StringUtils.isNotBlank(data.getOrganizationId());
    }

}
