package org.benetech.servicenet.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import org.benetech.servicenet.domain.AbstractEntity;

public final class CollectionUtils {

    public static <V> Set<V> filterNulls(Collection<V> collection) {
        return collection.stream().filter(Objects::nonNull).collect(Collectors.toSet());
    }

    public static <V extends AbstractEntity> Set<UUID> getIds(Set<V> entities) {
        return entities.stream().map(AbstractEntity::getId).collect(Collectors.toSet());
    }

    public static <V> Set<V> singletonSet(V entity) {
        Set<V> set = new HashSet<>();
        set.add(entity);
        return set;
    }

    public static <T> Map<T, T> getExistingItems(Collection<T> oldList, Collection<T> newList, BiPredicate<T, T> pred) {
        return oldList.stream()
            .map(item -> {
                Optional<T> existing = newList.stream().filter(x -> pred.test(item, x)).findFirst();
                return existing.map(t -> Map.entry(item, t)).orElse(null);
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }

    public static <T> List<T> getItemsToRemove(Collection<T> oldList, Collection<T> newList, BiPredicate<T, T> pred) {
        return oldList.stream()
            .filter(item -> newList.stream()
                .noneMatch(x -> pred.test(item, x)))
            .collect(Collectors.toList());
    }

    public static <T> List<T> getItemsToCreate(Collection<T> oldList, Collection<T> newList, BiPredicate<T, T> pred) {
        return newList.stream()
            .filter(item -> oldList.stream()
                .noneMatch(x -> pred.test(x, item)))
            .collect(Collectors.toList());
    }

    private CollectionUtils() {
    }
}
