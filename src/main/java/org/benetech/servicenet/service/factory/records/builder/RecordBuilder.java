package org.benetech.servicenet.service.factory.records.builder;

import org.benetech.servicenet.domain.FieldExclusion;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.service.ExclusionsConfigService;
import org.benetech.servicenet.service.FieldExclusionService;
import org.benetech.servicenet.service.dto.ConflictDTO;
import org.benetech.servicenet.service.dto.FieldExclusionDTO;
import org.benetech.servicenet.service.dto.RecordDTO;
import org.benetech.servicenet.service.mapper.FieldExclusionMapper;
import org.benetech.servicenet.service.mapper.LocationMapper;
import org.benetech.servicenet.service.mapper.OrganizationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RecordBuilder {

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private LocationMapper locationMapper;

    @Autowired
    private ExclusionsConfigService exclusionsConfigService;

    @Autowired
    private FieldExclusionService fieldExclusionService;

    @Autowired
    private FieldExclusionMapper exclusionMapper;

    public RecordDTO buildBasicRecord(Organization organization, List<ConflictDTO> conflictDTOS) {
        return new RecordDTO(
            organizationMapper.toDto(organization),
            new HashSet<>(),
            conflictDTOS);
    }

    public RecordDTO buildFilteredRecord(Organization organization, List<ConflictDTO> conflictDTOS,
                                         Set<FieldExclusion> baseExclusions, Set<FieldExclusion> allExclusions)
                                         throws IllegalAccessException {
        Organization filteredOrg = OrganizationBuilder.buildFilteredOrganization(organization, allExclusions);
        List<ConflictDTO> filteredConflicts = ConflictsBuilder.buildFilteredConflicts(conflictDTOS, allExclusions);
        Set<FieldExclusionDTO> baseOrgFieldExclusions = getMappedExclusions(baseExclusions);

        return new RecordDTO(
            organizationMapper.toDto(filteredOrg),
            baseOrgFieldExclusions,
            filteredConflicts);
    }

    private Set<FieldExclusionDTO> getMappedExclusions(Set<FieldExclusion> exclusions) {
        return exclusions.stream()
            .map(exclusionMapper::toDto).collect(Collectors.toSet());
    }
}
