package org.benetech.servicenet.adapter.healthleads.persistence;

import org.benetech.servicenet.adapter.healthleads.model.HealthleadsBaseData;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class Storage {

    private Map<Class<? extends HealthleadsBaseData>, Map<String, HealthleadsBaseData>> entitiesMap = new HashMap<>();
    private Map<Class<? extends HealthleadsBaseData>, Map<String, Set<HealthleadsBaseData>>> entitiesSetsMap =
        new HashMap<>();

    void addBaseData(Class<? extends HealthleadsBaseData> clazz, String id, HealthleadsBaseData data) {
        if (!entitiesMap.containsKey(clazz)) {
            entitiesMap.put(clazz, new HashMap<>());
        }
        entitiesMap.get(clazz).put(id, data);
    }

    void addBaseDataToSet(Class<? extends HealthleadsBaseData> clazz, String id, HealthleadsBaseData data) {
        if (!entitiesSetsMap.containsKey(clazz)) {
            entitiesSetsMap.put(clazz, new HashMap<>());
        }
        Map<String, Set<HealthleadsBaseData>> map = entitiesSetsMap.get(clazz);
        if (!map.containsKey(id)) {
            map.put(id, new HashSet<>());
        }
        map.get(id).add(data);
        entitiesSetsMap.put(clazz, map);
    }

    <T extends HealthleadsBaseData> Collection<T> getValuesOfClass(Class<T> clazz) {
        return (Collection<T>) entitiesMap.get(clazz).values();
    }

    <T extends HealthleadsBaseData> T getBaseData(Class<T> clazz, String id) {
        return (T) entitiesMap.get(clazz).get(id);
    }

    <T extends HealthleadsBaseData> Set<T> getBaseDataSet(Class<T> clazz, String id) {
        return (Set<T>) entitiesSetsMap.get(clazz).get(id);
    }
}
