package org.benetech.servicenet.util;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class CollectionUtils {

    public static <V> Set<V> filterNulls(Collection<V> collection) {
        return collection.stream().filter(Objects::nonNull).collect(Collectors.toSet());
    }

    private CollectionUtils() {
    }
}
