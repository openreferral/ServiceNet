package org.benetech.servicenet.adapter.healthleads.persistence;

import org.benetech.servicenet.adapter.healthleads.model.BaseData;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Storage {

    private Map<Class<? extends BaseData>, Map<String, BaseData>> entitiesMap = new HashMap<>();
    private Map<Class<? extends BaseData>, Map<String, Set<BaseData>>> enitiiesSetsMap = new HashMap<>();

    public void addBaseData(Class<? extends BaseData> clazz, String id, BaseData data) {
        if (!entitiesMap.containsKey(clazz)) {
            entitiesMap.put(clazz, new HashMap<>());
        }
        entitiesMap.get(clazz).put(id, data);
    }

    public void addBaseDataToSet(Class<? extends BaseData> clazz, String id, BaseData data) {
        if (!enitiiesSetsMap.containsKey(clazz)) {
            enitiiesSetsMap.put(clazz, new HashMap<>());
        }
        Map<String, Set<BaseData>> map = enitiiesSetsMap.get(clazz);
        if (!map.containsKey(id)) {
            map.put(id, new HashSet<>());
        }
        map.get(id).add(data);
        enitiiesSetsMap.put(clazz, map);
    }

    public <T extends BaseData> Collection<T> getValuesOfClass(Class<T> clazz) {
        return (Collection<T>) entitiesMap.get(clazz).values();
    }

    public <T extends BaseData> T getBaseData(Class<T> clazz, String id) {
        return (T) entitiesMap.get(clazz).get(id);
    }

    public <T extends BaseData> Set<T> getBaseDataSet(Class<T> clazz, String id) {
        return (Set<T>) enitiiesSetsMap.get(clazz).get(id);
    }
}
