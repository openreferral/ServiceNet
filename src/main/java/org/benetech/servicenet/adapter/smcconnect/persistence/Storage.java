package org.benetech.servicenet.adapter.smcconnect.persistence;

import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.adapter.smcconnect.model.SmcBaseData;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class Storage {

    private Map<Class<? extends SmcBaseData>, Map<String, Set<SmcBaseData>>> entitiesSetsMap =
        new HashMap<>();

    void addBaseDataToSet(Class<? extends SmcBaseData> clazz, String baseId, SmcBaseData data, String additionalIdentifier) {
        if (!entitiesSetsMap.containsKey(clazz)) {
            entitiesSetsMap.put(clazz, new HashMap<>());
        }
        Map<String, Set<SmcBaseData>> map = entitiesSetsMap.get(clazz);
        String id = additionalIdentifier + baseId;
        if (!map.containsKey(id)) {
            map.put(id, new HashSet<>());
        }
        map.get(id).add(data);
        entitiesSetsMap.put(clazz, map);
    }

    <T extends SmcBaseData> Set<T> getBaseDataSet(Class<T> clazz, String baseId, String additionalIdentifier) {
        if (baseId == null) {
            return getBaseDataSet(clazz);
        }
        String id = additionalIdentifier + baseId;
        if (entitiesSetsMap.get(clazz).containsKey(id)) {
            return (Set<T>) entitiesSetsMap.get(clazz).get(id);
        } else {
            return new HashSet<>();
        }
    }

    <T extends SmcBaseData> Set<T> getBaseDataSet(Class<T> clazz) {
        Set<T> result = new HashSet<>();
        for (Collection c : entitiesSetsMap.get(clazz).values()) {
            result.addAll(c);
        }
        return result;
    }

    void addData(SmcBaseData data) {
        Class<? extends SmcBaseData> clazz = data.getClass();

        if (isLocationBased(data)) {
            addBaseDataToSet(clazz, data.getLocationId(), data, "L");
        } else if (isServiceBased(data)) {
            addBaseDataToSet(clazz, data.getServiceId(), data, "S");
        } else if (isOrganizationBased(data)) {
            addBaseDataToSet(clazz, data.getOrganizationId(), data, "O");
        } else {
            addBaseDataToSet(clazz, data.getId(), data, "A");
        }
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
