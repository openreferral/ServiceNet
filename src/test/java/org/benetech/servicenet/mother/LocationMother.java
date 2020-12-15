package org.benetech.servicenet.mother;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.EntityManager;
import org.benetech.servicenet.domain.AccessibilityForDisabilities;
import org.benetech.servicenet.domain.HolidaySchedule;
import org.benetech.servicenet.domain.Language;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.PostalAddress;
import org.benetech.servicenet.domain.RegularSchedule;

public final class LocationMother {

    public static final String NAME = "Location name";
    public static final String ALTERNATE_NAME = "Location alternate name";
    public static final String DESCRIPTION = "Location description";
    public static final String TRANSPORTATION = "Transportation";
    public static final String SERVICE_PROVIDER = "ServiceProvider";
    public static final double LATITUDE = 20.3443;
    public static final double LONGITUDE = -78.2323;

    public static Location createDefault() {
        return new Location()
            .name(NAME)
            .alternateName(ALTERNATE_NAME)
            .description(DESCRIPTION)
            .transportation(TRANSPORTATION)
            .latitude(LATITUDE)
            .longitude(LONGITUDE);
    }

    public static Location createForServiceProvider() {
        return new Location().providerName(SERVICE_PROVIDER);
    }

    public static Location createDefaultWithAllRelationsAndPersist(EntityManager em) {
        Location loc = createDefault();
        PhysicalAddress physicalAddress = PhysicalAddressMother.createDefault();
        PostalAddress postalAddress = PostalAddressMother.createDefault();
        RegularSchedule regularSchedule = RegularScheduleMother.createWithTwoOpeningHours();
        regularSchedule.getOpeningHours().forEach(openingHours -> {
            openingHours.setRegularSchedule(regularSchedule);
            em.persist(openingHours);
        });
        HolidaySchedule holidaySchedule = HolidayScheduleMother.createDefault();
        Language language = LanguageMother.createDefault();
        Phone phone = PhoneMother.createDefault();
        AccessibilityForDisabilities accessibilityForDisabilities = AccessibilityForDisabilitiesMother.createDefault();

        loc.setPhysicalAddress(physicalAddress);
        loc.setPostalAddress(postalAddress);
        loc.setRegularSchedule(regularSchedule);
        Set<HolidaySchedule> holidaySchedules = new HashSet<>();
        holidaySchedules.add(holidaySchedule);
        loc.setHolidaySchedules(holidaySchedules);
        Set<Language> languages = new HashSet<>();
        languages.add(language);
        loc.setLangs(languages);
        Set<Phone> phones = new HashSet<>();
        phones.add(phone);
        loc.setPhones(phones);
        Set<AccessibilityForDisabilities> accessibilities = new HashSet<>();
        accessibilities.add(accessibilityForDisabilities);
        loc.setAccessibilities(accessibilities);

        em.persist(physicalAddress);
        em.persist(postalAddress);
        em.persist(regularSchedule);
        em.persist(holidaySchedule);
        em.persist(language);
        em.persist(phone);
        em.persist(accessibilityForDisabilities);
        em.persist(loc);
        em.flush();
        return loc;
    }

    public static Location createForServiceProviderAndPersist(EntityManager em) {
        Location loc = createForServiceProvider();
        PhysicalAddress physicalAddress = PhysicalAddressMother.createForServiceProvider();
        PostalAddress postalAddress = PostalAddressMother.createForServiceProvider();

        loc.setName(String.join(physicalAddress.getCity(), physicalAddress.getStateProvince(), physicalAddress.getAddress1()));
        loc.setPhysicalAddress(physicalAddress);
        loc.setPostalAddress(postalAddress);

        em.persist(physicalAddress);
        em.persist(postalAddress);
        em.persist(loc);
        em.flush();
        return loc;
    }

    public static Location createForServiceProviderWithRelations() {
        Location loc = createForServiceProvider();
        PhysicalAddress physicalAddress = PhysicalAddressMother.createForServiceProvider();
        PostalAddress postalAddress = PostalAddressMother.createForServiceProvider();

        loc.setName(String.join(physicalAddress.getCity(), physicalAddress.getStateProvince(), physicalAddress.getAddress1()));
        loc.setPhysicalAddress(physicalAddress);
        loc.setPostalAddress(postalAddress);

        return loc;
    }

    private LocationMother() {
    }
}
