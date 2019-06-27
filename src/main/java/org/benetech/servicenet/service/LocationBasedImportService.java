package org.benetech.servicenet.service;

import org.benetech.servicenet.domain.AccessibilityForDisabilities;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.HolidaySchedule;
import org.benetech.servicenet.domain.Language;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.PostalAddress;
import org.benetech.servicenet.domain.RegularSchedule;

import java.util.Set;

public interface LocationBasedImportService {

    void createOrUpdatePhysicalAddress(PhysicalAddress physicalAddress, Location location, DataImportReport report);

    void createOrUpdatePostalAddress(PostalAddress postalAddress, Location location, DataImportReport report);

    void createOrUpdateAccessibilities(Set<AccessibilityForDisabilities> accessibilities,
        Location location, DataImportReport report);

    void createOrUpdateOpeningHoursForLocation(RegularSchedule schedule, Location location, DataImportReport report);

    void createOrUpdateHolidayScheduleForLocation(HolidaySchedule schedule, Location location, DataImportReport report);

    void createOrUpdateLangsForLocation(Set<Language> langs, Location location, DataImportReport report);

    void createOrUpdatePhonesForLocation(Set<Phone> phones, Location location, DataImportReport report);
}
