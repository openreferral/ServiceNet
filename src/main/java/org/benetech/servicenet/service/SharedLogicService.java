package org.benetech.servicenet.service;

import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.OpeningHours;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.RegularSchedule;
import org.benetech.servicenet.domain.Service;

import java.util.Set;

public interface SharedLogicService {

    void createOrUpdateOpeningHours(Set<OpeningHours> openingHours, Service service,
                                    RegularSchedule schedule);

    void createOrUpdateOpeningHours(Set<OpeningHours> openingHours, Location location,
                                    RegularSchedule schedule);

    Set<Phone> persistPhones(Set<Phone> phonesToSave, Set<Phone> phonesInDatabase);
}
