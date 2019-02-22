package org.benetech.servicenet.service.impl;

import org.apache.commons.lang3.BooleanUtils;
import org.benetech.servicenet.domain.AccessibilityForDisabilities;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.HolidaySchedule;
import org.benetech.servicenet.domain.Language;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.PostalAddress;
import org.benetech.servicenet.domain.RegularSchedule;
import org.benetech.servicenet.repository.RegularScheduleRepository;
import org.benetech.servicenet.service.LocationBasedImportService;
import org.benetech.servicenet.service.SharedImportService;
import org.benetech.servicenet.service.annotation.ConfidentialFilter;
import org.benetech.servicenet.validator.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.benetech.servicenet.service.util.EntityManagerUtils.safeRemove;
import static org.benetech.servicenet.validator.EntityValidator.isValid;

@Component
public class LocationBasedImportServiceImpl implements LocationBasedImportService {

    @Autowired
    private EntityManager em;

    @Autowired
    private RegularScheduleRepository regularScheduleRepository;

    @Autowired
    private SharedImportService sharedImportService;

    @Override
    @ConfidentialFilter
    public void createOrUpdatePhysicalAddress(PhysicalAddress physicalAddress, Location location, DataImportReport report) {
        if (EntityValidator.isNotValid(physicalAddress, report, location.getExternalDbId())) {
            return;
        }
        physicalAddress.setLocation(location);
        if (location.getPhysicalAddress() != null) {
            physicalAddress.setId(location.getPhysicalAddress().getId());
            em.merge(physicalAddress);
        } else {
            em.persist(physicalAddress);
        }

        location.setPhysicalAddress(physicalAddress);
    }

    @Override
    @ConfidentialFilter
    public void createOrUpdatePostalAddress(PostalAddress postalAddress, Location location, DataImportReport report) {
        if (EntityValidator.isNotValid(postalAddress, report, location.getExternalDbId())) {
            return;
        }
        postalAddress.setLocation(location);
        if (location.getPostalAddress() != null) {
            postalAddress.setId(location.getPostalAddress().getId());
            em.merge(postalAddress);
        } else {
            em.persist(postalAddress);
        }

        location.setPostalAddress(postalAddress);
    }

    @Override
    public void createOrUpdateOpeningHoursForLocation(RegularSchedule schedule, Location location, DataImportReport report) {
        if (schedule != null) {
            sharedImportService.createOrUpdateOpeningHours(schedule.getOpeningHours().stream()
                .filter(x -> isValid(x, report, location.getExternalDbId()))
                .collect(Collectors.toSet()), location, schedule);
        }
    }

    @Override
    @ConfidentialFilter
    public void createOrUpdateHolidayScheduleForLocation(HolidaySchedule schedule, Location location,
                                                         DataImportReport report) {
        if (EntityValidator.isNotValid(schedule, report, location.getExternalDbId())) {
            return;
        }
        schedule.setLocation(location);
        if (location.getHolidaySchedule() != null) {
            schedule.setId(location.getHolidaySchedule().getId());
            em.merge(schedule);
        } else {
            em.persist(schedule);
        }
        location.setHolidaySchedule(schedule);
    }

    @Override
    public void createOrUpdateLangsForLocation(Set<Language> langs, Location location, DataImportReport report) {
        Set<Language> filtered = langs.stream().filter(x -> BooleanUtils.isNotTrue(x.getIsConfidential())
            && isValid(x, report, location.getExternalDbId()))
            .collect(Collectors.toSet());
        createOrUpdateFilteredLangsForLocation(filtered, location);
    }

    @Override
    public void createOrUpdatePhonesForLocation(Set<Phone> phones, Location location, DataImportReport report) {
        Set<Phone> filtered = phones.stream().filter(x -> BooleanUtils.isNotTrue(x.getIsConfidential())
            && isValid(x, report, location.getExternalDbId()))
            .collect(Collectors.toSet());
        filtered.forEach(p -> p.setLocation(location));
        createOrUpdateFilteredPhonesForLocation(filtered, location);
    }

    @Override
    @ConfidentialFilter
    public AccessibilityForDisabilities createOrUpdateAccessibility(AccessibilityForDisabilities accessibility,
                                                                               Location location, DataImportReport report) {
        if (EntityValidator.isNotValid(accessibility, report, location.getExternalDbId())) {
            return null;
        }
        accessibility.setLocation(location);
        Optional<AccessibilityForDisabilities> existingAccessibility = getExistingAccessibility(accessibility, location);
        if (existingAccessibility.isPresent()) {
            accessibility.setId(existingAccessibility.get().getId());
            em.merge(accessibility);
        } else {
            em.persist(accessibility);
        }

        return accessibility;
    }

    private Optional<AccessibilityForDisabilities> getExistingAccessibility(AccessibilityForDisabilities accessibility,
                                                                            Location location) {
        if (accessibility == null) {
            return Optional.empty();
        }
        return location.getAccessibilities().stream()
            .filter(a -> a.getAccessibility().equals(accessibility.getAccessibility()))
            .findFirst();
    }

    private void createOrUpdateFilteredLangsForLocation(Set<Language> langs, Location location) {
        if (location != null) {
            Set<Language> common = new HashSet<>(langs);
            common.retainAll(location.getLangs());
            location.getLangs().stream().filter(lang -> !common.contains(lang)).forEach(x -> safeRemove(em, x));
            langs.stream().filter(lang -> !common.contains(lang)).forEach(lang -> {
                lang.setLocation(location);
                em.persist(lang);
            });
        }
    }

    private void createOrUpdateFilteredPhonesForLocation(Set<Phone> phones, @Nonnull Location location) {
        location.setPhones(sharedImportService.persistPhones(phones, location.getPhones()));
    }

}
