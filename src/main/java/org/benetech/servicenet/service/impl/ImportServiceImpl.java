package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.domain.AccessibilityForDisabilities;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.PostalAddress;
import org.benetech.servicenet.service.ImportService;
import org.benetech.servicenet.service.LocationService;
import org.benetech.servicenet.service.PhysicalAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.Optional;

@Component
public class ImportServiceImpl implements ImportService {

    @Autowired
    private EntityManager em;

    @Autowired
    private LocationService locationService;

    @Autowired
    private PhysicalAddressService physicalAddressService;

    @Override
    public Location createOrUpdate(Location location, String externalDbId, String providerName) {
        Optional<Location> locationFromDb = locationService.findForExternalDb(externalDbId, providerName);
        if (locationFromDb.isPresent()) {
            location.setPhysicalAddress(locationFromDb.get().getPhysicalAddress());
            location.setPostalAddress(locationFromDb.get().getPostalAddress());
            location.setAccessibilities(locationFromDb.get().getAccessibilities());
            location.setId(locationFromDb.get().getId());
            em.merge(location);
        } else {
            em.persist(location);
        }
        return location;
    }

    @Override
    public PhysicalAddress createOrUpdate(PhysicalAddress physicalAddress, Location location) {
        if (location.getPhysicalAddress() != null) {
            physicalAddress.setId(location.getPhysicalAddress().getId());
            em.merge(physicalAddress);
        } else {
            physicalAddress.setLocation(location);
            em.persist(physicalAddress);
        }

        return physicalAddress;
    }

    @Override
    public PostalAddress createOrUpdate(PostalAddress postalAddress, Location location) {
        if (location.getPostalAddress() != null) {
            postalAddress.setId(location.getPostalAddress().getId());
            em.merge(postalAddress);
        } else {
            postalAddress.setLocation(location);
            em.persist(postalAddress);
        }

        return postalAddress;
    }

    @Override
    public AccessibilityForDisabilities createOrUpdate(AccessibilityForDisabilities accessibility, Location location) {
        Optional<AccessibilityForDisabilities> existingAccessibility = getExistingAccessibility(accessibility, location);
        if (existingAccessibility.isPresent()) {
            accessibility.setId(existingAccessibility.get().getId());
            em.merge(accessibility);
        } else {
            accessibility.setLocation(location);
            em.persist(accessibility);
        }

        return accessibility;
    }

    private Optional<AccessibilityForDisabilities> getExistingAccessibility(AccessibilityForDisabilities accessibility,
                                                                            Location location) {
        if (location.getAccessibilities() == null) {
            return Optional.empty();
        }
        return location.getAccessibilities().stream()
            .filter(a -> a.getAccessibility().equals(accessibility.getAccessibility()))
            .findFirst();
    }
}
