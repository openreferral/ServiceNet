package org.benetech.servicenet.service.impl;

import org.apache.commons.lang3.BooleanUtils;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.OpeningHours;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.RegularSchedule;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.repository.RegularScheduleRepository;
import org.benetech.servicenet.service.SharedImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.benetech.servicenet.service.util.EntityManagerUtils.safeRemove;

@Component
public class SharedImportServiceImpl implements SharedImportService {

    @Autowired
    private EntityManager em;

    @Autowired
    private RegularScheduleRepository regularScheduleRepository;

    @Override
    public void createOrUpdateOpeningHours(Set<OpeningHours> openingHours, Service service, RegularSchedule schedule) {
        createOrUpdateOpeningHours(openingHours, service, null, schedule);
    }

    @Override
    public void createOrUpdateOpeningHours(Set<OpeningHours> openingHours, Location location, RegularSchedule schedule) {
        createOrUpdateOpeningHours(openingHours, null, location, schedule);
    }

    @Override
    public Set<Phone> persistPhones(Set<Phone> phonesToSave, Set<Phone> phonesInDatabase) {
        Set<Phone> common = new HashSet<>(phonesToSave);
        common.retainAll(phonesInDatabase);

        phonesInDatabase.stream().filter(phone -> !common.contains(phone)).forEach(x -> safeRemove(em, x));
        phonesToSave.stream().filter(phone -> !common.contains(phone)).forEach(em::persist);

        return phonesToSave;
    }

    private void createOrUpdateOpeningHours(Set<OpeningHours> openingHours, Service service, Location location,
                                            RegularSchedule schedule) {
        Set<OpeningHours> filtered = openingHours.stream().filter(x -> BooleanUtils.isNotTrue(x.getIsConfidential()))
            .collect(Collectors.toSet());
        createOrUpdateFilteredOpeningHours(filtered, service, location, schedule);
    }

    private void createOrUpdateFilteredOpeningHours(Set<OpeningHours> openingHours, Service service, Location location,
                                                    RegularSchedule scheduleToSave) {
        Optional<RegularSchedule> scheduleFormDb = Optional.empty();
        if (service != null) {
            scheduleFormDb = regularScheduleRepository.findOneByServiceId(service.getId());
        } else if (location != null) {
            scheduleFormDb = regularScheduleRepository.findOneByLocationId(location.getId());
        }
        if (scheduleFormDb.isPresent()) {
            scheduleToSave.setId(scheduleFormDb.get().getId());
            Set<OpeningHours> common = new HashSet<>(openingHours);
            common.retainAll(scheduleFormDb.get().getOpeningHours());

            scheduleFormDb.get().getOpeningHours().stream().filter(o -> !common.contains(o)).forEach(o -> safeRemove(em, o));
            openingHours.stream().filter(o -> !common.contains(o)).forEach(o -> em.persist(o));

            em.merge(scheduleToSave.openingHours(new HashSet<>(openingHours)).location(location).srvc(service));
            setSchedule(scheduleToSave, location, service);
        } else {
            openingHours.forEach(o -> em.persist(o));
            RegularSchedule regularSchedule = new RegularSchedule()
                .openingHours(new HashSet<>(openingHours)).location(location).srvc(service);
            em.persist(regularSchedule);
            setSchedule(regularSchedule, location, service);
        }
    }

    private void setSchedule(RegularSchedule schedule, Location location, Service service) {
        if (service != null) {
            service.setRegularSchedule(schedule);
        }
        if (location != null) {
            location.setRegularSchedule(schedule);
        }
    }
}
