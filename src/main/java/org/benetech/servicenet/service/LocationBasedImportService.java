package org.benetech.servicenet.service;

import org.benetech.servicenet.domain.AccessibilityForDisabilities;
import org.benetech.servicenet.domain.HolidaySchedule;
import org.benetech.servicenet.domain.Language;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.PostalAddress;
import org.benetech.servicenet.domain.RegularSchedule;

import java.util.Set;

public interface LocationBasedImportService {

    void createOrUpdatePhysicalAddress(PhysicalAddress physicalAddress, Location location);

    void createOrUpdatePostalAddress(PostalAddress postalAddress, Location location);

    AccessibilityForDisabilities createOrUpdateAccessibility(AccessibilityForDisabilities accessibility,
                                                                     Location location);

    void createOrUpdateOpeningHoursForLocation(RegularSchedule schedule, Location location);

    void createOrUpdateHolidayScheduleForLocation(HolidaySchedule schedule, Location location);

    void createOrUpdateLangsForLocation(Set<Language> langs, Location location);

    void createOrUpdatePhonesForLocation(Set<Phone> phones, Location location);
}
