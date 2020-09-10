package org.benetech.servicenet.generator;

import static org.benetech.servicenet.config.Constants.SERVICE_PROVIDER;

import com.github.javafaker.Faker;
import java.util.List;
import javax.persistence.EntityManager;
import org.benetech.servicenet.domain.GeocodingResult;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.PostalAddress;

public class LocationMother implements BaseMother<Location> {

    public static final LocationMother INSTANCE = new LocationMother();

    private static final Faker FAKER = FakerInstance.INSTANCE;

    private static final GeocodingResultMother GEOCODING_RESULT_MOTHER = GeocodingResultMother.INSTANCE;

    private static final PostalAddressMother POSTAL_ADDRESS_MOTHER = PostalAddressMother.INSTANCE;

    private static final PhysicalAddressMother PHYSICAL_ADDRESS_MOTHER = PhysicalAddressMother.INSTANCE;

    private LocationMother () {}

    @Override
    public Location generate(EntityManager em) {
        Location loc = Location.builder()
            .name(FAKER.address().firstName())
            .providerName(SERVICE_PROVIDER)
            .latitude(Double.parseDouble(FAKER.address().latitude()))
            .longitude(Double.parseDouble(FAKER.address().longitude()))
            .build();

        PostalAddress postalAddress = POSTAL_ADDRESS_MOTHER.generate(em);
        postalAddress.setLocation(loc);
        em.persist(postalAddress);

        GeocodingResult geocodingResult = GEOCODING_RESULT_MOTHER.generate(em);
        geocodingResult.setLocations(List.of(loc));
        em.persist(geocodingResult);

        PhysicalAddress physicalAddress = PHYSICAL_ADDRESS_MOTHER.generate(em);
        physicalAddress.setLocation(loc);
        em.persist(physicalAddress);

        return loc;
    }
}
