package org.benetech.servicenet.matching.model;

import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class EntityEquivalent {

    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected Set<EntityEquivalent> otherEquivalents;

    private UUID baseResourceId;

    private UUID partnerResourceId;

    private Class<?> clazz;
}
