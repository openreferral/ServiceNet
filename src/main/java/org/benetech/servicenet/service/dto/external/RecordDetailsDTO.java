package org.benetech.servicenet.service.dto.external;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.benetech.servicenet.service.comparator.ConflictsComparator;
import org.benetech.servicenet.service.dto.ConflictDTO;
import org.benetech.servicenet.service.dto.OrganizationMatchDTO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordDetailsDTO implements Serializable {

    private RecordDetailsOrganizationDTO organization;

    private Set<RecordDetailsOrganizationDTO> partnerOrganizations;

    private List<OrganizationMatchDTO> organizationMatches;

    private List<ConflictDTO> conflicts;

    public List<ConflictDTO> getConflicts() {
        conflicts.sort(new ConflictsComparator());
        return conflicts;
    }
}
