package org.benetech.servicenet.service;

import org.benetech.servicenet.domain.Contact;
import org.benetech.servicenet.domain.HolidaySchedule;
import org.benetech.servicenet.domain.Language;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.OpeningHours;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.RegularSchedule;
import org.benetech.servicenet.domain.Service;

import java.util.Set;

public interface SharedImportService {

    void createOrUpdateOpeningHours(Set<OpeningHours> openingHours, Service service,
                                    RegularSchedule schedule);

    void createOrUpdateOpeningHours(Set<OpeningHours> openingHours, Location location,
                                    RegularSchedule schedule);

    void persistPhones(Set<Phone> phonesInDatabase, Set<Phone> phonesToSave);

    void persistLangs(Set<Language> langsInDatabase, Set<Language> langsToSave);

    Set<Contact> createOrUpdateContacts(Set<Contact> contacts);

    Set<HolidaySchedule> createOrUpdateHolidaySchedules(Set<HolidaySchedule> schedules);
}
