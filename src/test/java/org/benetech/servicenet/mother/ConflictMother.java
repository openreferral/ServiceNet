package org.benetech.servicenet.mother;

import static org.benetech.servicenet.TestConstants.UUID_1;
import static org.benetech.servicenet.TestConstants.UUID_2;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import javax.persistence.EntityManager;
import org.benetech.servicenet.domain.Conflict;
import org.benetech.servicenet.domain.enumeration.ConflictStateEnum;

public class ConflictMother {

    public static final String DEFAULT_CURRENT_VALUE = "AAAAAAAAAA";
    public static final String UPDATED_CURRENT_VALUE = "BBBBBBBBBB";

    public static final ZonedDateTime DEFAULT_CURRENT_VALUE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L),
        ZoneOffset.UTC);
    public static final ZonedDateTime UPDATED_CURRENT_VALUE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    public static final String DEFAULT_OFFERED_VALUE = "AAAAAAAAAA";
    public static final String UPDATED_OFFERED_VALUE = "BBBBBBBBBB";

    public static final ZonedDateTime DEFAULT_OFFERED_VALUE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L),
        ZoneOffset.UTC);
    public static final ZonedDateTime UPDATED_OFFERED_VALUE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    public static final String DEFAULT_FIELD_NAME = "AAAAAAAAAA";
    public static final String UPDATED_FIELD_NAME = "BBBBBBBBBB";

    public static final String DEFAULT_ENTITY_PATH = "AAAAAAAAAA";
    public static final String UPDATED_ENTITY_PATH = "BBBBBBBBBB";

    public static final ConflictStateEnum DEFAULT_STATE = ConflictStateEnum.PENDING;
    public static final ConflictStateEnum UPDATED_STATE = ConflictStateEnum.ACCEPTED;

    public static final ZonedDateTime DEFAULT_STATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L),
        ZoneOffset.UTC);
    public static final ZonedDateTime UPDATED_STATE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    public static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L),
        ZoneOffset.UTC);
    public static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    public static Conflict createDefault() {
        Conflict conflict = Conflict.builder()
            .currentValue(DEFAULT_CURRENT_VALUE)
            .currentValueDate(DEFAULT_CURRENT_VALUE_DATE)
            .offeredValue(DEFAULT_OFFERED_VALUE)
            .offeredValueDate(DEFAULT_OFFERED_VALUE_DATE)
            .fieldName(DEFAULT_FIELD_NAME)
            .entityPath(DEFAULT_ENTITY_PATH)
            .state(DEFAULT_STATE)
            .stateDate(DEFAULT_STATE_DATE)
            .createdDate(DEFAULT_CREATED_DATE)
            .resourceId(UUID_1)
            .partnerResourceId(UUID_2)
            .build();
        conflict.setOwner(SystemAccountMother.createDefault());
        return conflict;
    }

    public static Conflict createDifferent() {
        Conflict conflict = Conflict.builder()
            .currentValue(UPDATED_CURRENT_VALUE)
            .currentValueDate(UPDATED_CURRENT_VALUE_DATE)
            .offeredValue(UPDATED_OFFERED_VALUE)
            .offeredValueDate(UPDATED_OFFERED_VALUE_DATE)
            .fieldName(UPDATED_FIELD_NAME)
            .entityPath(UPDATED_ENTITY_PATH)
            .state(UPDATED_STATE)
            .stateDate(UPDATED_STATE_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .resourceId(UUID_1)
            .partnerResourceId(UUID_2)
            .build();
        conflict.setOwner(SystemAccountMother.createDefault());
        return conflict;
    }

    public static Conflict createDefaultAndPersist(EntityManager em) {
        Conflict conflict = createDefault();
        conflict.setOwner(SystemAccountMother.createDefaultAndPersist(em));
        em.persist(conflict);
        em.flush();
        return conflict;
    }

    public static Conflict createDifferentAndPersist(EntityManager em) {
        Conflict conflict = createDifferent();
        conflict.setOwner(SystemAccountMother.createDifferentAndPersist(em));
        em.persist(conflict);
        em.flush();
        return conflict;
    }

    private ConflictMother() {
    }
}
