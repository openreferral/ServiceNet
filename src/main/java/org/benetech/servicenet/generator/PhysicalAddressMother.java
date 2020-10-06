package org.benetech.servicenet.generator;

import com.github.javafaker.Faker;
import javax.persistence.EntityManager;
import org.benetech.servicenet.domain.PhysicalAddress;

public class PhysicalAddressMother implements BaseMother<PhysicalAddress> {

    public static final PhysicalAddressMother INSTANCE = new PhysicalAddressMother();

    private static final Faker FAKER = FakerInstance.INSTANCE;

    private PhysicalAddressMother() {}

    @Override
    public PhysicalAddress generate(EntityManager em) {
        return PhysicalAddress.builder()
            .address1(FAKER.address().fullAddress())
            .address2(FAKER.address().secondaryAddress())
            .city(FAKER.address().city())
            .postalCode(FAKER.address().zipCode())
            .country(FAKER.address().country())
            .stateProvince(FAKER.address().stateAbbr())
            .build();
    }
}
