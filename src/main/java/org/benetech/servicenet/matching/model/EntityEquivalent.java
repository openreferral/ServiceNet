package org.benetech.servicenet.matching.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class EntityEquivalent {

    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected Set<EntityEquivalent> otherEquivalents = new HashSet<>();

    @NonNull
    private UUID baseResourceId;

    @NonNull
    private UUID partnerResourceId;

    @NonNull
    private Class<?> clazz;
}
