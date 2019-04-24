package org.benetech.servicenet.service.util;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public final class EntityManagerUtils {

    public static void safeRemove(EntityManager em, Object entity) {
        em.remove(em.contains(entity) ? entity : em.merge(entity));
    }

    public static <T> void updateCollection(EntityManager em, Collection<T> oldList,
        Collection<T> newList, BiPredicate<T, T> pred) {
        Set<T> toRemove = oldList.stream()
            .filter(item -> newList.stream()
                .noneMatch(x -> pred.test(x, item)))
            .collect(Collectors.toSet());

        toRemove.forEach(lang -> {
            oldList.remove(lang);
            safeRemove(em, lang);
        });

        newList.stream()
            .filter(item -> oldList.stream()
                .noneMatch(x -> pred.test(x, item)))
            .forEach(lang -> {
                oldList.add(lang);
                em.persist(lang);
            });
    }

    private EntityManagerUtils() {
    }
}
