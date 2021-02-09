package org.benetech.servicenet.conflict.detector;

import static org.benetech.servicenet.config.Constants.LINK_FOR_CARE_PROVIDER;
import static org.benetech.servicenet.config.Constants.SERVICE_PROVIDER;
import static org.benetech.servicenet.util.CollectionUtils.getExistingItems;
import static org.benetech.servicenet.util.CollectionUtils.getItemsToCreate;
import static org.benetech.servicenet.util.CollectionUtils.getItemsToRemove;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.domain.Conflict;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.ServiceTaxonomy;
import org.benetech.servicenet.domain.Taxonomy;
import org.benetech.servicenet.util.UrlNormalizationUtils;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("checkstyle:booleanExpressionComplexity")
@Service("OrganizationConflictDetector")
public class OrganizationConflictDetector extends AbstractDetector<Organization> implements ConflictDetector<Organization> {

    public static final String PHONE = "phone";
    public static final String LOCATION = "location";
    public static final String SERVICE = "service";

    @Override
    public List<Conflict> detectConflicts(Organization current, Organization offered) {
        List<Conflict> conflicts = new LinkedList<>();
        conflicts.addAll(detectConflicts(current, offered, current.getName(), offered.getName(), "name"));
        conflicts.addAll(detectConflicts(current, offered, current.getAlternateName(), offered.getAlternateName(),
            "alternateName"));
        conflicts.addAll(detectConflicts(current, offered, current.getDescription(), offered.getDescription(),
            "description"));
        conflicts.addAll(detectConflicts(current, offered, current.getEmail(), offered.getEmail(), "email"));
        conflicts.addAll(detectConflicts(current, offered, current.getTaxStatus(), offered.getTaxStatus(), "taxStatus"));
        conflicts.addAll(detectConflicts(current, offered, current.getTaxId(), offered.getTaxId(), "taxId"));
        conflicts.addAll(detectConflicts(current, offered, current.getYearIncorporated(), offered.getYearIncorporated(),
            "yearIncorporated"));
        conflicts.addAll(detectConflicts(current, offered, current.getLegalStatus(), offered.getLegalStatus(),
            "legalStatus"));
        if (detect(UrlNormalizationUtils.normalize(current.getUrl()), UrlNormalizationUtils.normalize(offered.getUrl()))) {
            conflicts.add(createConflict(current, offered, current.getUrl(), offered.getUrl(), "url"));
        }
        if (current.getReplacedBy() != null && current.getReplacedBy().getId().equals(offered.getId())
            && SERVICE_PROVIDER.equals(current.getAccount().getName())) {
            conflicts.addAll(detectConflicts(current, offered, current.getCovidProtocols(), offered.getCovidProtocols(), "covidProtocols"));
            conflicts.addAll(detectPhoneConflicts(current, offered));
            conflicts.addAll(detectLocationConflicts(current, offered));
            conflicts.addAll(detectServiceConflicts(current, offered));
        }
        return conflicts;
    }

    private List<Conflict> detectPhoneConflicts(Organization clonedRecord, Organization originalRecord) {
        Optional<Phone> phoneOptional = clonedRecord.getPhones().stream().findFirst();
        if (phoneOptional.isPresent()) {
            Phone existingPhone = phoneOptional.get();
            if (originalRecord.getPhones().stream().noneMatch(p -> p.getNumber().equalsIgnoreCase(existingPhone.getNumber()))) {
                Optional<Phone> phone = originalRecord.getPhones().stream().findFirst();
                String offeredNumber = phone.map(Phone::getNumber).orElse(null);
                return detectConflicts(clonedRecord, originalRecord, existingPhone.getNumber(), offeredNumber, PHONE);
            }
        }
        return Collections.emptyList();
    }

    private List<Conflict> detectLocationConflicts(Organization clonedRecord, Organization originalRecord) {
        List<Conflict> conflicts = new ArrayList<>();
        getExistingItems(clonedRecord.getLocations(), originalRecord.getLocations(),
            (c, o) -> StringUtils.startsWith(c.getExternalDbId(), o.getExternalDbId()))
            .forEach((currentLocation, offeredLocation) -> {
                conflicts.addAll(detectConflicts(clonedRecord, originalRecord, currentLocation.getName(), offeredLocation.getName(), LOCATION + ".name"));
                conflicts.addAll(detectConflicts(clonedRecord, originalRecord, currentLocation.getPhysicalAddress().getAddress2(), currentLocation.getPhysicalAddress().getAddress2(), LOCATION + ".address2"));
                conflicts.addAll(detectConflicts(clonedRecord, originalRecord, currentLocation.getRegularSchedule().preview(), offeredLocation.getRegularSchedule().preview(), LOCATION + ".regularSchedule"));
                conflicts.addAll(detectConflicts(clonedRecord, originalRecord, currentLocation.holidaySchedulePreview(), offeredLocation.holidaySchedulePreview(), LOCATION + ".holidaySchedule"));
                if (LINK_FOR_CARE_PROVIDER.equals(originalRecord.getAccount().getName())) {
                    conflicts.addAll(detectConflicts(clonedRecord, originalRecord, currentLocation.isOpen247(), offeredLocation.isOpen247(), LOCATION + ".open247"));
                    conflicts.addAll(detectConflicts(clonedRecord, originalRecord, currentLocation.isRemote(), offeredLocation.isRemote(), LOCATION + ".remote"));
                }
            }
        );
        getItemsToRemove(clonedRecord.getLocations(), originalRecord.getLocations(),
            (c, o) -> StringUtils.startsWith(c.getExternalDbId(), o.getExternalDbId()))
            .forEach(currentLocation -> {
                    conflicts.addAll(detectConflicts(clonedRecord, originalRecord, currentLocation.getName(), null, LOCATION));
                }
            );
        getItemsToCreate(clonedRecord.getLocations(), originalRecord.getLocations(),
            (c, o) -> StringUtils.startsWith(c.getExternalDbId(), o.getExternalDbId()))
            .forEach(offeredLocation -> {
                    conflicts.addAll(detectConflicts(clonedRecord, originalRecord, null, offeredLocation.getName(), LOCATION));
                }
            );
        return conflicts;
    }

    private List<Conflict> detectServiceConflicts(Organization clonedRecord, Organization originalRecord) {
        List<Conflict> conflicts = new ArrayList<>();
        getExistingItems(clonedRecord.getServices(), originalRecord.getServices(),
            (c, o) -> StringUtils.startsWith(c.getExternalDbId(), o.getExternalDbId()))
            .forEach((currentService, offeredService) -> {
                conflicts.addAll(detectConflicts(clonedRecord, originalRecord, currentService.getName(), offeredService.getName(), SERVICE + ".name"));
                conflicts.addAll(detectConflicts(clonedRecord, originalRecord, currentService.getDescription(), offeredService.getDescription(), SERVICE + ".description"));
                conflicts.addAll(detectConflicts(clonedRecord, originalRecord, currentService.getApplicationProcess(), offeredService.getApplicationProcess(), SERVICE + ".applicationProcess"));
                conflicts.addAll(detectConflicts(clonedRecord, originalRecord, currentService.getEligibility() != null ? currentService.getEligibility().getEligibility() : null,
                    offeredService.getEligibility() != null ? offeredService.getEligibility().getEligibility() : null, SERVICE + ".eligibilityCriteria"));
                conflicts.addAll(detectConflicts(clonedRecord, originalRecord, currentService.getFees(), offeredService.getFees(), SERVICE + ".fees"));
                if (!currentService.getDocs().isEmpty() && !offeredService.getDocs().isEmpty()) {
                    conflicts.addAll(detectConflicts(clonedRecord, originalRecord, currentService.getDocs().stream().findFirst().get().getDocument(),
                        offeredService.getDocs().stream().findFirst().get().getDocument(), SERVICE + ".requiredDocuments"));
                }
                String currentTaxonomies = currentService.getTaxonomies().stream().map(
                    ServiceTaxonomy::getTaxonomy).map(Taxonomy::getName).sorted().collect(Collectors.joining(", "));
                String offeredTaxonomies = offeredService.getTaxonomies().stream().map(
                    ServiceTaxonomy::getTaxonomy).map(Taxonomy::getName).sorted().collect(Collectors.joining(", "));
                    conflicts.addAll(detectConflicts(clonedRecord, originalRecord, currentTaxonomies, offeredTaxonomies, SERVICE + ".taxonomies"));
                }
            );
        getItemsToRemove(clonedRecord.getServices(), originalRecord.getServices(),
            (c, o) -> StringUtils.startsWith(c.getExternalDbId(), o.getExternalDbId()))
            .forEach(currentService -> {
                    conflicts.addAll(detectConflicts(clonedRecord, originalRecord, currentService.getName(), null, SERVICE));
                }
            );
        getItemsToCreate(clonedRecord.getServices(), originalRecord.getServices(),
            (c, o) -> StringUtils.startsWith(c.getExternalDbId(), o.getExternalDbId()))
            .forEach(offeredService -> {
                    conflicts.addAll(detectConflicts(clonedRecord, originalRecord, null, offeredService.getName(), SERVICE));
                }
            );
        return conflicts;
    }
}
