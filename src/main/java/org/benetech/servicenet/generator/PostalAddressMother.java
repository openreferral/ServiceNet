package org.benetech.servicenet.generator;

import com.github.javafaker.Faker;
import javax.persistence.EntityManager;
import org.benetech.servicenet.domain.PostalAddress;

public class PostalAddressMother implements BaseMother<PostalAddress> {

    public static final PostalAddressMother INSTANCE = new PostalAddressMother();

    private static final Faker FAKER = FakerInstance.INSTANCE;

    private PostalAddressMother() {}

    @Override
    public PostalAddress generate(EntityManager em) {
        return PostalAddress.builder()
            .address1(FAKER.address().fullAddress())
            .address2(FAKER.address().secondaryAddress())
            .city(FAKER.address().city())
            .country(FAKER.address().country())
            .postalCode(FAKER.address().zipCode())
            .stateProvince(FAKER.address().stateAbbr())
            .build();
    }
}
