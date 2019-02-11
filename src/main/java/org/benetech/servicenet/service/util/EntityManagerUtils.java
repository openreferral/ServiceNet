package org.benetech.servicenet.service.util;

import javax.persistence.EntityManager;

public final class EntityManagerUtils {

    public static void safeRemove(EntityManager em, Object entity) {
        em.remove(em.contains(entity) ? entity : em.merge(entity));
    }

    private EntityManagerUtils() {
    }
}
