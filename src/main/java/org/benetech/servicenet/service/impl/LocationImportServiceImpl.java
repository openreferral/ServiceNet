package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.domain.AccessibilityForDisabilities;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.repository.OrganizationRepository;
import org.benetech.servicenet.service.LocationBasedImportService;
import org.benetech.servicenet.service.LocationImportService;
import org.benetech.servicenet.service.LocationService;
import org.benetech.servicenet.service.annotation.ConfidentialFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class LocationImportServiceImpl implements LocationImportService {

    @Autowired
    private EntityManager em;

    @Autowired
    private LocationService locationService;

    @Autowired
    private LocationBasedImportService locationBasedImportService;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Override
    @ConfidentialFilter
    public Location createOrUpdateLocation(Location filledLocation, String externalDbId,
                                           String providerName, DataImportReport report) {
        Location location = new Location(filledLocation);
        Optional<Location> locationFromDb = locationService.findWithEagerAssociations(externalDbId, providerName);
        if (locationFromDb.isPresent()) {
            fillDataFromDb(location, locationFromDb.get());
            em.merge(location);
        } else {
            em.persist(location);
        }

        locationBasedImportService.createOrUpdatePhysicalAddress(filledLocation.getPhysicalAddress(), location, report);
        locationBasedImportService.createOrUpdatePostalAddress(filledLocation.getPostalAddress(), location, report);
        locationBasedImportService.createOrUpdateLangsForLocation(filledLocation.getLangs(), location, report);
        locationBasedImportService.createOrUpdatePhonesForLocation(filledLocation.getPhones(), location, report);
        locationBasedImportService.createOrUpdateOpeningHoursForLocation(
            filledLocation.getRegularSchedule(), location, report);
        locationBasedImportService.createOrUpdateHolidayScheduleForLocation(
            filledLocation.getHolidaySchedule(), location, report);
        importAccessibilities(filledLocation.getAccessibilities(), location, report);

        return location;
    }

    private void importAccessibilities(Set<AccessibilityForDisabilities> accessibilities,
                                       Location location, DataImportReport report) {
        Set<AccessibilityForDisabilities> savedAccessibilities = new HashSet<>();
        for (AccessibilityForDisabilities accessibility : accessibilities) {
            savedAccessibilities.add(
                locationBasedImportService.createOrUpdateAccessibility(accessibility, location, report));
        }
        location.setAccessibilities(savedAccessibilities);
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
