package org.benetech.servicenet.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.benetech.servicenet.service.comparator.ConflictsComparator;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordDTO implements Serializable {

    private OrganizationDTO organization;

    private List<ConflictDTO> conflicts;

    public List<ConflictDTO> getConflicts() {
        conflicts.sort(new ConflictsComparator());
        return conflicts;
    }
}
