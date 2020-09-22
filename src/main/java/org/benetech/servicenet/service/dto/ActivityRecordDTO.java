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
import org.benetech.servicenet.service.dto.provider.SimpleLocationDTO;
import org.benetech.servicenet.service.dto.provider.SimpleServiceDTO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityRecordDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private OrganizationDTO organization;

    private ZonedDateTime lastUpdated;

    private Set<SimpleLocationDTO> locations;

    private Set<SimpleServiceDTO> services;

    private Set<ContactDTO> contacts;

    private Set<FieldExclusionDTO> exclusions;

    private List<ConflictDTO> conflicts;

    private OwnerDTO owner;

    public List<ConflictDTO> getConflicts() {
        conflicts.sort(new ConflictsComparator());
        return conflicts;
    }
}
