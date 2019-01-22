package org.benetech.servicenet.adapter.shared.model.storage;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
class EntryCollection<T> {

    private Set<EntryDefinition<T>> set = new HashSet<>();

    void addData(Class relatedToClass, T data) {
        set.add(new EntryDefinition<>(data.getClass(), relatedToClass, data));
    }

    <V extends T> Set<V> getRelatedEntities(Class<V> baseClass, Class relatedToClass) {
        return getSubsetOfClass(baseClass).stream()
            .filter(e -> e.getRelatedToClass().equals(relatedToClass))
            .map(EntryDefinition::getEntry)
            .collect(Collectors.toSet());
    }

    <V extends T> Set<V> getEntitiesOfClass(Class<V> clazz) {
        return getSubsetOfClass(clazz).stream()
            .map(EntryDefinition::getEntry)
            .collect(Collectors.toSet());
    }

    private <V extends T> Set<EntryDefinition<V>> getSubsetOfClass(Class<V> clazz) {
        return set.stream()
            .filter(e -> clazz.equals(e.getBaseClass()))
            .map(e -> (EntryDefinition<V>) e)
            .collect(Collectors.toSet());
    }
}
