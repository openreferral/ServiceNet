package org.benetech.servicenet.util;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.benetech.servicenet.domain.AbstractEntity;

public final class CollectionUtils {

    public static <V> Set<V> filterNulls(Collection<V> collection) {
        return collection.stream().filter(Objects::nonNull).collect(Collectors.toSet());
    }

    public static <V extends AbstractEntity> Set<UUID> getIds(Set<V> entities) {
        return entities.stream().map(AbstractEntity::getId).collect(Collectors.toSet());
    }

    private CollectionUtils() {
    }
}
