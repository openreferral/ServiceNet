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
import org.benetech.servicenet.service.LocationImportService;
import org.benetech.servicenet.service.LocationService;
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
public class LocationImportServiceImpl implements LocationImportService {

    @Autowired
    private EntityManager em;

    @Autowired
    private SharedImportService sharedImportService;

    @Autowired
    private LocationService locationService;

    @Override
    public Location createOrUpdateLocation(Location filledLocation, String externalDbId,
                                           String providerName, DataImportReport report) {
        if (EntityValidator.isNotValid(filledLocation, report, externalDbId)) {
            return null;
        }
        Location location = new Location(filledLocation);
        Optional<Location> locationFromDb = locationService.findWithEagerAssociations(externalDbId, providerName);
        if (locationFromDb.isPresent()) {
            fillDataFromDb(location, locationFromDb.get());
            em.merge(location);
        } else {
            em.persist(location);
        }

        createOrUpdatePhysicalAddress(filledLocation.getPhysicalAddress(), location, report);
        createOrUpdatePostalAddress(filledLocation.getPostalAddress(), location, report);
        createOrUpdateLangsForLocation(filledLocation.getLangs(), location, report);
        createOrUpdatePhonesForLocation(filledLocation.getPhones(), location, report);
        importAccessibilities(filledLocation.getAccessibilities(), location, report);
        createOrUpdateOpeningHoursForLocation(filledLocation.getRegularSchedule(), location, report);
        createOrUpdateHolidayScheduleForLocation(filledLocation.getHolidaySchedule(), location, report);

        return location;
    }

    @ConfidentialFilter
    private void createOrUpdatePostalAddress(PostalAddress postalAddress, Location location, DataImportReport report) {
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

    @ConfidentialFilter
    private void createOrUpdatePhysicalAddress(PhysicalAddress physicalAddress,
                                               Location location, DataImportReport report) {
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

    private void importAccessibilities(Set<AccessibilityForDisabilities> accessibilities,
                                       Location location, DataImportReport report) {
        Set<AccessibilityForDisabilities> savedAccessibilities = new HashSet<>();
        for (AccessibilityForDisabilities accessibility : accessibilities) {
            createOrUpdateAccessibility(accessibility, location, report)
                .ifPresent(savedAccessibilities::add);
        }
        location.setAccessibilities(savedAccessibilities);
    }

    @ConfidentialFilter
    private Optional<AccessibilityForDisabilities> createOrUpdateAccessibility(AccessibilityForDisabilities accessibility,
                                                                               Location location, DataImportReport report) {
        if (EntityValidator.isNotValid(accessibility, report, location.getExternalDbId())) {
            return Optional.empty();
        }
        accessibility.setLocation(location);
        Optional<AccessibilityForDisabilities> existingAccessibility = getExistingAccessibility(accessibility, location);
        if (existingAccessibility.isPresent()) {
            accessibility.setId(existingAccessibility.get().getId());
            em.merge(accessibility);
        } else {
            em.persist(accessibility);
        }

        return Optional.of(accessibility);
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

    private void createOrUpdateOpeningHoursForLocation(RegularSchedule schedule, Location location,
                                                       DataImportReport report) {
        if (schedule != null) {
            sharedImportService.createOrUpdateOpeningHours(schedule.getOpeningHours().stream()
                .filter(x -> isValid(x, report, location.getExternalDbId()))
                .collect(Collectors.toSet()), location, schedule);
        }
    }

    @ConfidentialFilter
    private void createOrUpdateHolidayScheduleForLocation(HolidaySchedule schedule, Location location,
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

    private void createOrUpdateLangsForLocation(Set<Language> langs, Location location, DataImportReport report) {
        Set<Language> filtered = langs.stream().filter(x -> BooleanUtils.isNotTrue(x.getIsConfidential())
            && isValid(x, report, location.getExternalDbId()))
            .collect(Collectors.toSet());
        createOrUpdateFilteredLangsForLocation(filtered, location);
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

    private void createOrUpdatePhonesForLocation(Set<Phone> phones, Location location, DataImportReport report) {
        Set<Phone> filtered = phones.stream().filter(x -> BooleanUtils.isNotTrue(x.getIsConfidential())
            && isValid(x, report, location.getExternalDbId()))
            .collect(Collectors.toSet());
        filtered.forEach(p -> p.setLocation(location));
        createOrUpdateFilteredPhonesForLocation(filtered, location);
    }

    private void createOrUpdateFilteredPhonesForLocation(Set<Phone> phones, @Nonnull Location location) {
        location.setPhones(sharedImportService.persistPhones(phones, location.getPhones()));
    }

    private void fillDataFromDb(Location newLocation, Location locationFromDb) {
        newLocation.setPhysicalAddress(locationFromDb.getPhysicalAddress());
        newLocation.setPostalAddress(locationFromDb.getPostalAddress());
        newLocation.setAccessibilities(locationFromDb.getAccessibilities());
        newLocation.setId(locationFromDb.getId());
        newLocation.setPhones(locationFromDb.getPhones());
        newLocation.setLangs(locationFromDb.getLangs());
        newLocation.setRegularSchedule(locationFromDb.getRegularSchedule());
        newLocation.setHolidaySchedule(locationFromDb.getHolidaySchedule());
    }
}
