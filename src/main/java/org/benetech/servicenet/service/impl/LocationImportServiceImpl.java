package org.benetech.servicenet.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.benetech.servicenet.adapter.shared.model.ImportData;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.repository.OrganizationRepository;
import org.benetech.servicenet.service.GeocodingResultService;
import org.benetech.servicenet.service.LocationBasedImportService;
import org.benetech.servicenet.service.LocationImportService;
import org.benetech.servicenet.service.LocationService;
import org.benetech.servicenet.service.annotation.ConfidentialFilter;
import org.benetech.servicenet.validator.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.Optional;

@Slf4j
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

    @Autowired
    private GeocodingResultService geocodingResultService;

    @Override
    @ConfidentialFilter
    public Location createOrUpdateLocation(Location filledLocation, String externalDbId, ImportData importData) {
        long startTime = System.currentTimeMillis();

        EntityValidator.validateAndFix(
            filledLocation, filledLocation.getOrganization(), importData.getReport(), externalDbId);

        Location location = new Location(filledLocation);
        Optional<Location> locationFromDb = locationService.findWithEagerAssociations(externalDbId,
            importData.getProviderName());

        locationFromDb.ifPresent(value -> fillDataFromDb(location, value));
        locationService.save(location);

        persistRelatedEntities(filledLocation, importData, location);
        fetchGeocodeIfNeeded(importData, location);

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        //TODO: Remove time counting logic (#264)
        log.debug(importData.getProviderName() + "'s location '" + location.getName() + "' was imported in "
            + elapsedTime + "ms.");

        return location;
    }

    private void persistRelatedEntities(Location filledLocation, ImportData importData, Location location) {
        locationBasedImportService.createOrUpdatePhysicalAddress(
            filledLocation.getPhysicalAddress(), location, importData.getReport());
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

    private void fetchGeocodeIfNeeded(ImportData importData, Location location) {
        geocodingResultService.findAllForAddressOrFetchIfEmpty(location.getPhysicalAddress(),
            importData.getContext());
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
