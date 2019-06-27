package org.benetech.servicenet.service.factory.records;

import lombok.extern.slf4j.Slf4j;
import org.benetech.servicenet.domain.ExclusionsConfig;
import org.benetech.servicenet.domain.FieldExclusion;
import org.benetech.servicenet.domain.LocationExclusion;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.view.ActivityInfo;
import org.benetech.servicenet.domain.view.ActivityRecord;
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

    public Optional<ActivityRecordDTO> getFilteredRecord(ActivityRecord record) {
        List<ConflictDTO> conflicts = getBaseConflicts(record.getId());

        Map<UUID, ExclusionsConfig> exclusionsMap = exclusionsConfigService.getAllBySystemAccountId();

        Set<ExclusionsConfig> baseExclusions = getBaseExclusions(record.getAccountId(), exclusionsMap);
        Set<FieldExclusion> fieldExclusions = baseExclusions.stream()
            .flatMap(e -> e.getExclusions().stream())
            .collect(Collectors.toSet());

        Set<LocationExclusion> locationExclusions = baseExclusions.stream()
            .flatMap(e -> e.getLocationExclusions().stream())
            .collect(Collectors.toSet());

        List<ConflictDTO> filteredConflicts = filterConflicts(conflicts, fieldExclusions, exclusionsMap);

        return filterRecord(record, filteredConflicts, fieldExclusions, locationExclusions);
    }

    public ActivityDTO getFilteredResult(ActivityInfo info, Map<UUID, ExclusionsConfig> exclusionsMap) {
        Set<FieldExclusion> baseExclusions = Optional.ofNullable(exclusionsMap.get(info.getAccountId()))
            .map(ExclusionsConfig::getExclusions).orElse(new HashSet<>());

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
        Map<UUID, ExclusionsConfig> exclusionsMap) {

        return conflicts.stream()
            .filter(conf -> isNotExcluded(conf, baseExclusions)
                && isNotExcluded(conf, Optional.ofNullable(exclusionsMap.get(conf.getPartnerId()))
                .map(ExclusionsConfig::getExclusions).orElse(new HashSet<>())))
            .collect(Collectors.toList());
    }

    private boolean isNotExcluded(ConflictDTO conflict, Set<FieldExclusion> exclusions) {
        return exclusions.stream().noneMatch(x ->
            x.getEntity().equals(conflict.getEntityPath())
                && x.getExcludedFields().contains(conflict.getFieldName()));
    }

    private Set<ExclusionsConfig> getBaseExclusions(UUID accountId, Map<UUID, ExclusionsConfig> exclusionsMap) {
        Set<ExclusionsConfig> exclusions = new HashSet<>();

        userService.getCurrentSystemAccount()
            .map(systemAccount -> Optional.ofNullable(exclusionsMap.get(systemAccount.getId())))
            .ifPresent(exclusionsConfig -> exclusionsConfig.ifPresent(exclusions::add));

        Optional.ofNullable(exclusionsMap.get(accountId))
            .ifPresent(exclusions::add);

        return exclusions;
    }

    private Optional<ActivityRecordDTO> filterRecord(ActivityRecord record, List<ConflictDTO> conflicts,
        Set<FieldExclusion> exclusions, Set<LocationExclusion> locationExclusions) {
        try {
            return Optional.of(buildRecord(record, conflicts, exclusions, locationExclusions));
        } catch (IllegalAccessException e) {
            log.error("Unable to filter record.");
            return Optional.empty();
        }
    }

    private ActivityRecordDTO buildRecord(ActivityRecord record, List<ConflictDTO> conflictDTOS,
        Set<FieldExclusion> exclusions, Set<LocationExclusion> locationExclusions) throws IllegalAccessException {

        Organization org = record.getOrganization();

        if (exclusions.isEmpty()) {
            return recordBuilder.buildBasicRecord(org, record.getLastUpdated(), conflictDTOS, locationExclusions);
        } else {
            return recordBuilder.buildFilteredRecord(org, record.getLastUpdated(),
                conflictDTOS, exclusions, locationExclusions);
        }
    }
}
