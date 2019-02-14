package org.benetech.servicenet.mother;

import org.benetech.servicenet.domain.Phone;

public final class PhoneMother {

    public static final String DEFAULT_NUMBER = "Phone number";
    public static final int DEFAULT_EXTENSION = 123;
    public static final String DEFAULT_TYPE = "Phone type";
    public static final String DEFAULT_LANGUAGE = "Phone language";
    public static final String DEFAULT_DESCRIPTION = "Phone description";
    public static final String DIFFERENT_NUMBER = "Phone number";
    public static final int DIFFERENT_EXTENSION = 123;
    public static final String DIFFERENT_TYPE = "Phone type";
    public static final String DIFFERENT_LANGUAGE = "Phone language";
    public static final String DIFFERENT_DESCRIPTION = "Phone description";

    public static Phone createDefault() {
        return new Phone()
            .number(DEFAULT_NUMBER)
            .extension(DEFAULT_EXTENSION)
            .type(DEFAULT_TYPE)
            .language(DEFAULT_LANGUAGE)
            .description(DEFAULT_DESCRIPTION);
    }

    public static Phone createDifferent() {
        return new Phone()
            .number(DIFFERENT_NUMBER)
            .extension(DIFFERENT_EXTENSION)
            .type(DIFFERENT_TYPE)
            .language(DIFFERENT_LANGUAGE)
            .description(DIFFERENT_DESCRIPTION);
    }
}
