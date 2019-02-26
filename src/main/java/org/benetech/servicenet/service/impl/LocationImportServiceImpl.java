package org.benetech.servicenet.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.benetech.servicenet.adapter.shared.model.ImportData;
import org.benetech.servicenet.domain.AccessibilityForDisabilities;
import org.benetech.servicenet.domain.DataImportReport;
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
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.benetech.servicenet.util.CollectionUtils.filterNulls;

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
        if (EntityValidator.isNotValid(filledLocation, importData.getReport(), externalDbId)) {
            return null;
        }
        long startTime = System.currentTimeMillis();
        Location location = new Location(filledLocation);
        Optional<Location> locationFromDb = locationService.findWithEagerAssociations(externalDbId,
            importData.getProviderName());

        if (locationFromDb.isPresent()) {
            fillDataFromDb(location, locationFromDb.get());
            em.merge(location);
        } else {
            em.persist(location);
        }

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
        locationBasedImportService.createOrUpdateHolidayScheduleForLocation(
            filledLocation.getHolidaySchedule(), location, importData.getReport());
        importAccessibilities(filledLocation.getAccessibilities(), location, importData.getReport());
    }

    private void fetchGeocodeIfNeeded(ImportData importData, Location location) {
        if (geocodeShouldBeFetched(location)) {
            geocodingResultService.createOrUpdateGeocodingResult(location, importData.getContext());
        }
    }

    private boolean geocodeShouldBeFetched(Location location) {
        return location.getLongitude() == null || location.getLatitude() == null
            || location.getLongitude().equals(0.0) || location.getLatitude().equals(0.0);
    }

    private void importAccessibilities(Set<AccessibilityForDisabilities> accessibilities,
                                       Location location, DataImportReport report) {
        Set<AccessibilityForDisabilities> savedAccessibilities = new HashSet<>();
        for (AccessibilityForDisabilities accessibility : accessibilities) {
            savedAccessibilities.add(
                locationBasedImportService.createOrUpdateAccessibility(accessibility, location, report));
        }
        location.setAccessibilities(filterNulls(savedAccessibilities));
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
