package org.benetech.servicenet.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.benetech.servicenet.service.comparator.ConflictsComparator;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityRecordDTO implements Serializable {

    private OrganizationDTO organization;

    private ZonedDateTime lastUpdated;

    private Set<LocationRecordDTO> locations;

    private Set<ServiceRecordDTO> services;

    private Set<ContactDTO> contacts;

    private Set<FieldExclusionDTO> exclusions;

    private List<ConflictDTO> conflicts;

    private OwnerDTO owner;

    public List<ConflictDTO> getConflicts() {
        conflicts.sort(new ConflictsComparator());
        return conflicts;
    }
}
