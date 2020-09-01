package org.benetech.servicenet.service.impl;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.benetech.servicenet.adapter.shared.model.ImportData;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.service.LocationBasedImportService;
import org.benetech.servicenet.service.LocationImportService;
import org.benetech.servicenet.service.LocationService;
import org.benetech.servicenet.service.annotation.ConfidentialFilter;
import org.benetech.servicenet.validator.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LocationImportServiceImpl implements LocationImportService {

    @Autowired
    private LocationService locationService;

    @Autowired
    private LocationBasedImportService locationBasedImportService;

    @Override
    @ConfidentialFilter
    public Location createOrUpdateLocation(Location filledLocation, String externalDbId, ImportData importData) {
        EntityValidator.validateAndFix(
            filledLocation, filledLocation.getOrganization(), importData.getReport(), externalDbId);

        Location location = new Location(filledLocation);
        Optional<Location> locationFromDb = locationService.findWithEagerAssociations(externalDbId,
            importData.getProviderName());

        if (locationFromDb.isPresent()) {
            if (locationFromDb.get().deepEquals(filledLocation)) {
                return locationFromDb.get();
            }
            fillDataFromDb(location, locationFromDb.get());
        }
        locationService.save(location);

        persistRelatedEntities(filledLocation, importData, location);

        return location;
    }

    private void persistRelatedEntities(Location filledLocation, ImportData importData, Location location) {
        locationBasedImportService.createOrUpdatePhysicalAddress(
            filledLocation.getPhysicalAddress(), location, importData.getReport());
        // update geocoding results
        locationService.save(location);
        locationBasedImportService.createOrUpdatePostalAddress(
            filledLocation.getPostalAddress(), location, importData.getReport());
        locationBasedImportService.createOrUpdateLangsForLocation(
            filledLocation.getLangs(), location, importData.getReport());
        locationBasedImportService.createOrUpdatePhonesForLocation(
            filledLocation.getPhones(), location, importData.getReport());
        locationBasedImportService.createOrUpdateOpeningHoursForLocation(
            filledLocation.getRegularSchedule(), location, importData.getReport());
        locationBasedImportService.createOrUpdateHolidaySchedulesForLocation(
            filledLocation.getHolidaySchedules(), location, importData.getReport());
        locationBasedImportService.createOrUpdateAccessibilities(filledLocation.getAccessibilities(),
            location, importData.getReport());
    }

    private void fillDataFromDb(Location newLocation, Location locationFromDb) {
        newLocation.setPhysicalAddress(locationFromDb.getPhysicalAddress());
        newLocation.setPostalAddress(locationFromDb.getPostalAddress());
        newLocation.setAccessibilities(locationFromDb.getAccessibilities());
        newLocation.setId(locationFromDb.getId());
        newLocation.setPhones(locationFromDb.getPhones());
        newLocation.setLangs(locationFromDb.getLangs());
        newLocation.setRegularSchedule(locationFromDb.getRegularSchedule());
        newLocation.setHolidaySchedules(locationFromDb.getHolidaySchedules());
    }
}
