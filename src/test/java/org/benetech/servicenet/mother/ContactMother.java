package org.benetech.servicenet.mother;

import org.benetech.servicenet.domain.Contact;

public final class ContactMother {

    public static final String DEFAULT_EMAIL = "Contact email";
    public static final String DEFAULT_DEPARTMENT = "Contact department";
    public static final String DEFAULT_NAME = "Contact name";
    public static final String DEFAULT_TITLE = "Contact title";
    public static final String DIFFERENT_EMAIL = "Different Contact email";
    public static final String DIFFERENT_DEPARTMENT = "Different Contact department";
    public static final String DIFFERENT_NAME = "Different Contact name";
    public static final String DIFFERENT_TITLE = "Different Contact title";

    public static Contact createDefault() {
        return new Contact()
            .name(DEFAULT_NAME)
            .title(DEFAULT_TITLE)
            .department(DEFAULT_DEPARTMENT)
            .email(DEFAULT_EMAIL);
    }

    public static Contact createDifferent() {
        return new Contact()
            .name(DIFFERENT_NAME)
            .title(DIFFERENT_TITLE)
            .department(DIFFERENT_DEPARTMENT)
            .email(DIFFERENT_EMAIL);
    }

    private ContactMother() {
    }
}
