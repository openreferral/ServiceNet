package org.benetech.servicenet.service.impl;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.domain.Contact;
import org.benetech.servicenet.domain.Language;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.OpeningHours;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.RegularSchedule;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.repository.RegularScheduleRepository;
import org.benetech.servicenet.service.ContactService;
import org.benetech.servicenet.service.SharedImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.benetech.servicenet.service.util.EntityManagerUtils.updateCollection;

@Component
public class SharedImportServiceImpl implements SharedImportService {

    @Autowired
    private EntityManager em;

    @Autowired
    private RegularScheduleRepository regularScheduleRepository;

    @Autowired
    private ContactService contactService;

    @Override
    public void createOrUpdateOpeningHours(Set<OpeningHours> openingHours, Service service, RegularSchedule schedule) {
        createOrUpdateOpeningHours(openingHours, service, null, schedule);
    }

    @Override
    public void createOrUpdateOpeningHours(Set<OpeningHours> openingHours, Location location, RegularSchedule schedule) {
        createOrUpdateOpeningHours(openingHours, null, location, schedule);
    }

    @SuppressWarnings("checkstyle:booleanExpressionComplexity")
    @Override
    public void persistPhones(Set<Phone> phonesInDatabase, Set<Phone> phonesToSave) {
        updateCollection(em, phonesInDatabase, phonesToSave, (p1, p2) ->
            p1.getNumber().equals(p2.getNumber()) && Objects.equals(p1.getExtension(), p2.getExtension())
                && StringUtils.equals(p1.getType(), p2.getType()) && StringUtils.equals(p1.getLanguage(), p2.getLanguage())
                && StringUtils.equals(p1.getDescription(), p2.getDescription()));
    }

    @Override
    public void persistLangs(Set<Language> langsInDatabase, Set<Language> langsToSave) {
        updateCollection(em, langsInDatabase, langsToSave, (lang1, lang2) ->
            lang1.getLanguage().equals(lang2.getLanguage()));
    }

    @Override
    public Set<Contact> createOrUpdateContacts(Set<Contact> contacts) {
        contacts.forEach(c -> {
            Optional<Contact> contactFromDb = contactService.findForExternalDb(c.getExternalDbId(), c.getProviderName());

            contactFromDb.ifPresentOrElse(
                contact -> {
                    c.setId(contact.getId());
                    em.merge(c);
                },
                () -> em.persist(c)
            );
        });

        return contacts;
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
        scheduleFormDb.ifPresentOrElse(
            schedule -> {
                scheduleToSave.setId(schedule.getId());

                openingHours.forEach(o -> o.setRegularSchedule(schedule));
                updateCollection(em, schedule.getOpeningHours(), openingHours, (o1, o2) ->
                    o1.getWeekday().equals(o2.getWeekday()) && StringUtils.equals(o1.getClosesAt(), o2.getClosesAt())
                        && StringUtils.equals(o1.getOpensAt(), o2.getOpensAt()));

                em.merge(scheduleToSave.openingHours(schedule.getOpeningHours()).location(location).srvc(service));
                setSchedule(scheduleToSave, location, service);
            },
            () -> {
                openingHours.forEach(o -> em.persist(o));
                RegularSchedule regularSchedule = new RegularSchedule()
                    .openingHours(new HashSet<>(openingHours)).location(location).srvc(service);
                em.persist(regularSchedule);
                setSchedule(regularSchedule, location, service);
                openingHours.forEach(o -> o.setRegularSchedule(regularSchedule));
            });
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
