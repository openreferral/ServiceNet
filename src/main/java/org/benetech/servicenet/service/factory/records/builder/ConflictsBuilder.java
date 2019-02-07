package org.benetech.servicenet.service.factory.records.builder;

import org.benetech.servicenet.domain.FieldExclusion;
import org.benetech.servicenet.service.dto.ConflictDTO;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

final class ConflictsBuilder {

    static List<ConflictDTO> buildFilteredConflicts(List<ConflictDTO> conflictDTOS, Set<FieldExclusion> exclusions) {
        return conflictDTOS.stream()
            .filter(x -> isFieldNotExcluded(exclusions, x))
            .collect(Collectors.toList());
    }

    private static boolean isFieldNotExcluded(Set<FieldExclusion> exclusions, ConflictDTO conflict) {
        return exclusions.stream().noneMatch(x ->
            x.getEntity().equals(conflict.getEntityPath())
                && x.getExcludedFields().contains(conflict.getFieldName()));
    }

    private ConflictsBuilder() {
    }
}

