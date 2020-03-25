package org.benetech.servicenet.mother;

import org.benetech.servicenet.TestConstants;
import org.benetech.servicenet.config.Constants;
import org.benetech.servicenet.domain.Metadata;
import org.benetech.servicenet.domain.enumeration.ActionType;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

public final class MetadataMother {

    private static final UUID DEFAULT_RESOURCE_ID = TestConstants.UUID_1;
    private static final UUID DIFFERENT_RESOURCE_ID = TestConstants.UUID_2;

    private static final ZonedDateTime DEFAULT_LAST_ACTION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L),
        ZoneOffset.UTC);
    private static final ZonedDateTime DIFFERENT_LAST_ACTION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ActionType DEFAULT_LAST_ACTION_TYPE = ActionType.CREATE;
    private static final ActionType DIFFERENT_LAST_ACTION_TYPE = ActionType.UPDATE;

    private static final String DEFAULT_FIELD_NAME = "DEFAULT_FIELD_NAME";
    private static final String DIFFERENT_FIELD_NAME = "DIFFERENT_FIELD_NAME";

    private static final String DEFAULT_PREVIOUS_VALUE = "DEFAULT_PREVIOUS_VALUE";
    private static final String DIFFERENT_PREVIOUS_VALUE = "DIFFERENT_PREVIOUS_VALUE";

    private static final String DEFAULT_REPLACEMENT_VALUE = "DEFAULT_REPLACEMENT_VALUE";
    private static final String DIFFERENT_REPLACEMENT_VALUE = "DIFFERENT_REPLACEMENT_VALUE";

    private static final String DEFAULT_RESOURCE_CLASS = "DEFAULT_RESOURCE_CLASS";
    private static final String DIFFERENT_RESOURCE_CLASS = "DIFFERENT_RESOURCE_CLASS";

    public static Metadata createDefault() {
        return new Metadata()
            .resourceId(DEFAULT_RESOURCE_ID)
            .lastActionDate(DEFAULT_LAST_ACTION_DATE)
            .lastActionType(DEFAULT_LAST_ACTION_TYPE)
            .fieldName(DEFAULT_FIELD_NAME)
            .previousValue(DEFAULT_PREVIOUS_VALUE)
            .replacementValue(DEFAULT_REPLACEMENT_VALUE)
            .resourceClass(DEFAULT_RESOURCE_CLASS);
    }

    public static Metadata createDifferent() {
        return new Metadata()
            .resourceId(DIFFERENT_RESOURCE_ID)
            .lastActionDate(DIFFERENT_LAST_ACTION_DATE)
            .lastActionType(DIFFERENT_LAST_ACTION_TYPE)
            .fieldName(DIFFERENT_FIELD_NAME)
            .previousValue(DIFFERENT_PREVIOUS_VALUE)
            .replacementValue(DIFFERENT_REPLACEMENT_VALUE)
            .resourceClass(DIFFERENT_RESOURCE_CLASS);
    }

    public static Metadata createAllFields() {
        return new Metadata()
            .resourceId(DEFAULT_RESOURCE_ID)
            .lastActionDate(DEFAULT_LAST_ACTION_DATE)
            .lastActionType(ActionType.CREATE)
            .fieldName(Constants.ALL_FIELDS)
            .previousValue(null)
            .replacementValue(null)
            .user(UserMother.createDifferent())
            .resourceClass(DEFAULT_RESOURCE_CLASS);
    }

    public static Metadata createDefaultAndPersist(EntityManager em) {
        Metadata metadata = createDefault();
        metadata.setUserProfile(UserMother.createDefaultAndPersist(em));
        em.persist(metadata);
        em.flush();
        return metadata;
    }

    public static Metadata createDifferentAndPersist(EntityManager em) {
        Metadata metadata = createDifferent();
        metadata.setUserProfile(UserMother.createDifferentAndPersist(em));
        em.persist(metadata);
        em.flush();
        return metadata;
    }

    public static Metadata createAllFieldsAndPersist(EntityManager em) {
        Metadata metadata = createAllFields();
        metadata.setUserProfile(UserMother.createDifferentAndPersist(em));
        em.persist(metadata);
        em.flush();
        return metadata;
    }

    private MetadataMother() {
    }
}
