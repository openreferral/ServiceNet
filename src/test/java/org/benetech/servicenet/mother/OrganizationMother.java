package org.benetech.servicenet.mother;

import org.benetech.servicenet.domain.Organization;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class OrganizationMother {

    public static final String DEFAULT_NAME = "AAAAAAAAAA";
    public static final String UPDATED_NAME = "BBBBBBBBBB";

    public static final String DEFAULT_ALTERNATE_NAME = "AAAAAAAAAA";
    public static final String UPDATED_ALTERNATE_NAME = "BBBBBBBBBB";

    public static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    public static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    public static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    public static final String UPDATED_EMAIL = "BBBBBBBBBB";

    public static final String DEFAULT_URL = "AAAAAAAAAA";
    public static final String UPDATED_URL = "BBBBBBBBBB";

    public static final String DEFAULT_TAX_STATUS = "AAAAAAAAAA";
    public static final String UPDATED_TAX_STATUS = "BBBBBBBBBB";

    public static final String DEFAULT_TAX_ID = "AAAAAAAAAA";
    public static final String UPDATED_TAX_ID = "BBBBBBBBBB";

    public static final LocalDate DEFAULT_YEAR_INCORPORATED = LocalDate.ofEpochDay(0L);
    public static final LocalDate UPDATED_YEAR_INCORPORATED = LocalDate.now(ZoneId.systemDefault());

    public static final String DEFAULT_LEGAL_STATUS = "AAAAAAAAAA";
    public static final String UPDATED_LEGAL_STATUS = "BBBBBBBBBB";

    public static final Boolean DEFAULT_ACTIVE = true;
    public static final Boolean DEFAULT_INACTIVE = false;
    public static final Boolean UPDATED_ACTIVE = true;

    public static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L),
        ZoneOffset.UTC);
    public static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    public static Organization createDefault(Boolean active) {
        Organization org = new Organization()
            .name(DEFAULT_NAME)
            .alternateName(DEFAULT_ALTERNATE_NAME)
            .description(DEFAULT_DESCRIPTION)
            .email(DEFAULT_EMAIL)
            .url(DEFAULT_URL)
            .taxStatus(DEFAULT_TAX_STATUS)
            .taxId(DEFAULT_TAX_ID)
            .yearIncorporated(DEFAULT_YEAR_INCORPORATED)
            .legalStatus(DEFAULT_LEGAL_STATUS)
            .active(active)
            .updatedAt(DEFAULT_UPDATED_AT);
        org.setAccount(SystemAccountMother.createDefault());
        return org;
    }

    public static Organization createDifferent() {
        Organization org = new Organization()
            .name(UPDATED_NAME)
            .alternateName(UPDATED_ALTERNATE_NAME)
            .description(UPDATED_DESCRIPTION)
            .email(UPDATED_EMAIL)
            .url(UPDATED_URL)
            .taxStatus(UPDATED_TAX_STATUS)
            .taxId(UPDATED_TAX_ID)
            .yearIncorporated(UPDATED_YEAR_INCORPORATED)
            .legalStatus(UPDATED_LEGAL_STATUS)
            .active(UPDATED_ACTIVE)
            .updatedAt(UPDATED_UPDATED_AT);
        org.setAccount(SystemAccountMother.createDifferent());
        return org;
    }

    public static Organization createDefaultAndPersist(EntityManager em) {
        Organization org = createDefault(DEFAULT_ACTIVE);
        org.setAccount(SystemAccountMother.createDefaultAndPersist(em));
        em.persist(org);
        em.flush();
        return org;
    }

    public static Organization createSimiliarAndPersist(EntityManager em) {
        Organization org = createDefault(DEFAULT_ACTIVE);
        org.setAccount(SystemAccountMother.createDifferentAndPersist(em));
        em.persist(org);
        em.flush();
        return org;
    }

    public static Organization createDifferentAndPersist(EntityManager em) {
        Organization org = createDifferent();
        org.setAccount(SystemAccountMother.createDifferentAndPersist(em));
        em.persist(org);
        em.flush();
        return org;
    }

    public static Organization createInactiveAndPersist(EntityManager em) {
        Organization org = createDefault(DEFAULT_INACTIVE);
        org.setAccount(SystemAccountMother.createDefaultAndPersist(em));
        em.persist(org);
        em.flush();
        return org;
    }

}
