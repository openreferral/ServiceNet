package org.benetech.servicenet.adapter.shared.model.storage;

import lombok.Data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
public class EntryDictionary<T> {

    private Map<String, EntryCollection<T>> map = new HashMap<>();

    public void addData(String resourceId, T data, Class relatedToClass) {
        String key = getKey(resourceId, data.getClass());
        EntryCollection<T> entryCollection = map.get(key);
        if (entryCollection == null) {
            entryCollection = new EntryCollection<>();
            entryCollection.addData(relatedToClass, data);
            map.put(key, entryCollection);
        } else {
            entryCollection.addData(relatedToClass, data);
        }
    }

    public <V extends T> Set<V> getRelatedEntities(Class<V> baseClass, String resourceId, Class relatedToClass) {
        String key = getKey(resourceId, baseClass);
        EntryCollection<T> entryCollection = map.get(key);
        if (entryCollection == null) {
            return new HashSet<>();
        } else {
            return entryCollection.getRelatedEntities(baseClass, relatedToClass);
        }
    }

    public <V extends T> Set<V> getEntitiesOfClass(Class<V> clazz) {
        Set<V> result = new HashSet<>();
        for (EntryCollection<T> entryCollection : map.values()) {
            result.addAll(entryCollection.getEntitiesOfClass(clazz));
        }
        return result;
    }

    private String getKey(String resourceId, Class clazz) {
        return clazz.getCanonicalName() + "-" + resourceId;
    }
}
