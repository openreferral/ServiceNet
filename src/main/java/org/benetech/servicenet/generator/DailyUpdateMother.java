package org.benetech.servicenet.generator;

import com.github.javafaker.Faker;
import java.time.ZonedDateTime;
import java.util.Random;
import javax.persistence.EntityManager;
import org.benetech.servicenet.domain.DailyUpdate;

public class DailyUpdateMother implements BaseMother<DailyUpdate> {

    public static final DailyUpdateMother INSTANCE = new DailyUpdateMother();

    private static final Faker FAKER = FakerInstance.INSTANCE;

    private static final Random RANDOM = new Random();

    private DailyUpdateMother() {}

    @Override
    public DailyUpdate generate(EntityManager em) {
        DailyUpdate update =  DailyUpdate.builder()
            .update(FAKER.witcher().quote())
            .createdAt(ZonedDateTime.now())
            .build();
        if (RANDOM.nextBoolean()) {
            update.setExpiry(ZonedDateTime.now());
        }
        return update;
    }
}
