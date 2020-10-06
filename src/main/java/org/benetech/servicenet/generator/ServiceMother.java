package org.benetech.servicenet.generator;

import static org.benetech.servicenet.config.Constants.SERVICE_PROVIDER;

import com.github.javafaker.Faker;
import javax.persistence.EntityManager;
import org.benetech.servicenet.domain.Service;

public class ServiceMother implements BaseMother<Service> {

    public static final ServiceMother INSTANCE = new ServiceMother();

    private static final Faker FAKER = FakerInstance.INSTANCE;

    @Override
    public Service generate(EntityManager em) {
        return Service.builder()
            .name(FAKER.commerce().productName())
            .description(FAKER.commerce().material())
            .url(FAKER.company().url())
            .email(FAKER.bothify("????##@????.com"))
            .status("Non-certified")
            .providerName(SERVICE_PROVIDER)
            .build();
    }
}
