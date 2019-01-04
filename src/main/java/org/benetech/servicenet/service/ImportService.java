package org.benetech.servicenet.service;

import org.benetech.servicenet.domain.AccessibilityForDisabilities;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.PostalAddress;

public interface ImportService {

    Location createOrUpdate(Location location, String externalDbId, String providerName);

    PhysicalAddress createOrUpdate(PhysicalAddress physicalAddress, Location location);

    PostalAddress createOrUpdate(PostalAddress postalAddress, Location location);

    AccessibilityForDisabilities createOrUpdate(AccessibilityForDisabilities accessibility, Location location);
}
