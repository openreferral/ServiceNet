package org.benetech.servicenet.service.factory.records;

import lombok.extern.slf4j.Slf4j;
import org.benetech.servicenet.domain.FieldExclusion;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.view.ActivityInfo;
import org.benetech.servicenet.service.ConflictService;
import org.benetech.servicenet.service.ExclusionsConfigService;
import org.benetech.servicenet.service.UserService;
import org.benetech.servicenet.service.dto.ActivityDTO;
import org.benetech.servicenet.service.dto.ActivityRecordDTO;
import org.benetech.servicenet.service.dto.ConflictDTO;
import org.benetech.servicenet.service.factory.records.builder.RecordBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Slf4j
public class RecordFactory {

    @Autowired
    private ExclusionsConfigService exclusionsConfigService;

    @Autowired
    private UserService userService;

    @Autowired
    private ConflictService conflictService;

    @Autowired
    private RecordBuilder recordBuilder;

    public Optional<ActivityRecordDTO> getFilteredRecord(ActivityInfo info) {
        List<ConflictDTO> conflicts = getBaseConflicts(info.getId());

        Map<UUID, Set<FieldExclusion>> exclusionsMap = exclusionsConfigService.getAllBySystemAccountId();

        Set<FieldExclusion> baseExclusions = getBaseExclusions(info.getAccountId(), exclusionsMap);
        List<ConflictDTO> filteredConflicts = filterConflicts(conflicts, baseExclusions, exclusionsMap);

        return filterRecord(info, filteredConflicts, baseExclusions);
    }

    public ActivityDTO getFilteredResult(ActivityInfo info, Map<UUID, Set<FieldExclusion>> exclusionsMap) {
        Set<FieldExclusion> baseExclusions = exclusionsMap.get(info.getAccountId());

        List<ConflictDTO> conflicts = getBaseConflicts(info.getId());
        List<ConflictDTO> filteredConflicts = filterConflicts(conflicts, baseExclusions, exclusionsMap);

        return ActivityDTO.builder()
            .organizationId(info.getId())
            .organizationName(info.getName())
            .lastUpdated(info.getRecent())
            .conflicts(filteredConflicts)
            .build();
    }

    private List<ConflictDTO> getBaseConflicts(UUID resourceId) {
        return conflictService.findAllPendingWithResourceId(resourceId);
    }

    private List<ConflictDTO> filterConflicts(List<ConflictDTO> conflicts, Set<FieldExclusion> baseExclusions,
        Map<UUID, Set<FieldExclusion>> exclusionsMap) {

        return conflicts.stream()
            .filter(conf -> isNotExcluded(conf, baseExclusions)
                && isNotExcluded(conf, exclusionsMap.get(conf.getPartnerId())))
            .collect(Collectors.toList());
    }

    private boolean isNotExcluded(ConflictDTO conflict, Set<FieldExclusion> exclusions) {
        return exclusions == null || exclusions.stream().noneMatch(x ->
            x.getEntity().equals(conflict.getEntityPath())
                && x.getExcludedFields().contains(conflict.getFieldName()));
    }

    private Set<FieldExclusion> getBaseExclusions(UUID accountId, Map<UUID, Set<FieldExclusion>> exclusionsMap) {
        Set<FieldExclusion> exclusions = userService.getCurrentSystemAccount()
            .map(systemAccount -> exclusionsMap.get(systemAccount.getId()))
            .orElseGet(HashSet::new);

        Set<FieldExclusion> baseExclusions = exclusionsMap.get(accountId);

        if (baseExclusions != null) {
            exclusions.addAll(baseExclusions);
        }

        return exclusions;
    }

    private Optional<ActivityRecordDTO> filterRecord(ActivityInfo info, List<ConflictDTO> conflicts,
        Set<FieldExclusion> exclusions) {
        try {
            return Optional.of(buildRecord(info, conflicts, exclusions));
        } catch (IllegalAccessException e) {
            log.error("Unable to filter record.");
            return Optional.empty();
        }
    }

    private ActivityRecordDTO buildRecord(ActivityInfo info, List<ConflictDTO> conflictDTOS,
        Set<FieldExclusion> exclusions) throws IllegalAccessException {

        Organization org = info.getOrganization();

        if (exclusions.isEmpty()) {
            return recordBuilder.buildBasicRecord(org, info.getLastUpdated(), conflictDTOS);
        } else {
            return recordBuilder.buildFilteredRecord(org, info.getLastUpdated(), conflictDTOS, exclusions);
        }
    }
}
