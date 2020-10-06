package org.benetech.servicenet.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.benetech.servicenet.service.comparator.ConflictsComparator;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

/**
 * A DTO for the Activity entity.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID organizationId;

    private String accountName;

    private String organizationName;

    private ZonedDateTime lastUpdated;

    private List<ConflictDTO> conflicts;

    private List<UUID> organizationMatches;

    private OwnerDTO owner;

    public List<ConflictDTO> getConflicts() {
        conflicts.sort(new ConflictsComparator());
        return conflicts;
    }

    public List<UUID> getOrganizationMatches() {
        return organizationMatches;
    }
}
