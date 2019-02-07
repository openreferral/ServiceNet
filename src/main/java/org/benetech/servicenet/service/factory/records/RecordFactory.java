package org.benetech.servicenet.service.factory.records;

import lombok.extern.slf4j.Slf4j;
import org.benetech.servicenet.domain.FieldExclusion;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.service.ConflictService;
import org.benetech.servicenet.service.ExclusionsConfigService;
import org.benetech.servicenet.service.FieldExclusionService;
import org.benetech.servicenet.service.UserService;
import org.benetech.servicenet.service.dto.ConflictDTO;
import org.benetech.servicenet.service.dto.ExclusionsConfigDTO;
import org.benetech.servicenet.service.dto.RecordDTO;
import org.benetech.servicenet.service.factory.records.builder.RecordBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
    private FieldExclusionService fieldExclusionService;

    @Autowired
    private UserService userService;

    @Autowired
    private ConflictService conflictService;

    @Autowired
    private RecordBuilder recordBuilder;

    public Optional<RecordDTO> getFilteredResult(UUID resourceId, Organization organization) {
        List<ConflictDTO> conflictDTOS = getBaseConflicts(resourceId);
        List<ConflictDTO> filteredByPartners = filterWithPartnersConfigs(conflictDTOS);
        Set<FieldExclusion> exclusions = getExclusions(organization);
        return filterExclusions(organization, filteredByPartners, exclusions);
    }

    private List<ConflictDTO> getBaseConflicts(UUID resourceId) {
        return conflictService.findAllWithResourceId(resourceId);
    }

    private List<ConflictDTO> filterWithPartnersConfigs(List<ConflictDTO> conflictDTOS) {
        Set<String> conflictingProviders = conflictDTOS.stream()
            .flatMap(c -> c.getAcceptedThisChangeNames().stream())
            .collect(Collectors.toSet());

        List<ExclusionsConfigDTO> configs = exclusionsConfigService.findAllBySystemAccountNameIn(conflictingProviders);

        for (ExclusionsConfigDTO config : configs) {
            filterConflictsByConfig(conflictDTOS, config);
        }

        return conflictDTOS;
    }

    private Set<FieldExclusion> getExclusions(Organization organization) {
        Set<FieldExclusion> exclusions = new HashSet<>();

        exclusions.addAll(getBaseExclusions(organization));
        exclusions.addAll(getPartnerExclusions());

        return exclusions;
    }

    private Set<FieldExclusion> getBaseExclusions(Organization organization) {
        return getOrganizationConfig(organization.getAccount())
            .map(conf -> getExclusions(conf.getId()))
            .orElseGet(HashSet::new);
    }

    private Set<FieldExclusion> getPartnerExclusions() {
        return getCurrentUserConfig()
            .map(conf -> getExclusions(conf.getId()))
            .orElseGet(HashSet::new);
    }

    private Optional<ExclusionsConfigDTO> getCurrentUserConfig() {
        return userService.getCurrentSystemAccount()
            .flatMap(this::getOrganizationConfig);
    }

    private Optional<ExclusionsConfigDTO> getOrganizationConfig(SystemAccount account) {
        return exclusionsConfigService.findOneBySystemAccountName(
            account.getName());
    }

    private void filterConflictsByConfig(List<ConflictDTO> conflictDTOS, ExclusionsConfigDTO config) {
        Set<FieldExclusion> excludedFields = fieldExclusionService.findAllByConfigId(config.getId());
        Iterator<ConflictDTO> conflictsIterator = conflictDTOS.iterator();
        while (conflictsIterator.hasNext()) {
            filterConflictByConfig(config, excludedFields, conflictsIterator);
        }
    }

    private void filterConflictByConfig(ExclusionsConfigDTO config, Set<FieldExclusion> excludedFields,
                                        Iterator<ConflictDTO> conflictsIterator) {
        ConflictDTO conflictDTO = conflictsIterator.next();
        for (FieldExclusion exclusion : excludedFields) {
            if (shouldConflictBeFiltered(config, conflictDTO, exclusion)) {
                removeAccountFromConflict(config, conflictsIterator, conflictDTO);
            }
        }
    }

    private void removeAccountFromConflict(ExclusionsConfigDTO config, Iterator<ConflictDTO> conflictsIterator,
                                           ConflictDTO conflictDTO) {
        if (isOnlyOneAccepting(conflictDTO)) {
            conflictsIterator.remove();
        } else {
            conflictDTO.removeAcceptedThisChange(config.getAccountName());
        }
    }

    private boolean isOnlyOneAccepting(ConflictDTO conflictDTO) {
        return conflictDTO.getAcceptedThisChange().size() == 1;
    }

    private boolean shouldConflictBeFiltered(ExclusionsConfigDTO config, ConflictDTO conflictDTO, FieldExclusion exclusion) {
        return conflictDTO.getAcceptedThisChangeNames().contains(config.getAccountName())
            && exclusion.getEntity().equals(conflictDTO.getEntityPath())
            && exclusion.getExcludedFields().contains(conflictDTO.getFieldName());
    }

    private Optional<RecordDTO> filterExclusions(Organization organization, List<ConflictDTO> conflictDTOS,
                                                 Set<FieldExclusion> exclusions) {
        try {
            return Optional.of(buildRecord(organization, conflictDTOS, exclusions));
        } catch (IllegalAccessException e) {
            log.error("Unable to filter record.");
            return Optional.empty();
        }
    }

    private Set<FieldExclusion> getExclusions(UUID configId) {
        return new HashSet<>(fieldExclusionService.findAllByConfigId(configId));
    }

    private RecordDTO buildRecord(Organization organization, List<ConflictDTO> conflictDTOS,
                                  Set<FieldExclusion> allExclusions) throws IllegalAccessException {
        if (allExclusions.isEmpty()) {
            return recordBuilder.buildBasicRecord(organization, conflictDTOS);
        } else {
            return recordBuilder.buildFilteredRecord(
                organization, conflictDTOS, getBaseExclusions(organization), allExclusions);
        }
    }
}
