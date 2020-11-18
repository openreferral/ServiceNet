package org.benetech.servicenet.mother;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import javax.persistence.EntityManager;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Referral;

public final class ReferralMother {

    public static final String DEFAULT_SHORTCODE = "AAAAAAAAAA";
    public static final String UPDATED_SHORTCODE = "BBBBBBBBBB";

    public static final ZonedDateTime DEFAULT_SENT_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    public static final ZonedDateTime UPDATED_SENT_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    public static final ZonedDateTime DEFAULT_FULFILLED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    public static final ZonedDateTime UPDATED_FULFILLED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    public static Referral createDefault() {
        return new Referral()
            .shortcode(DEFAULT_SHORTCODE)
            .sentAt(DEFAULT_SENT_AT)
            .fulfilledAt(DEFAULT_FULFILLED_AT);
    }

    public static Referral createDifferent() {
        return new Referral()
            .shortcode(UPDATED_SHORTCODE)
            .sentAt(UPDATED_SENT_AT)
            .fulfilledAt(UPDATED_FULFILLED_AT);
    }

    public static Referral createDefaultWithAllRelationsAndPersist(EntityManager em) {
        Organization organization1 = OrganizationMother.createDefaultWithAllRelationsAndPersist(em);
        Organization organization2 = OrganizationMother.createForServiceProviderAndPersist(em);

        Referral referral = createDefault();
        referral.setFrom(organization1);
        referral.setFromLocation(organization1.getLocations().stream().findFirst().orElse(null));
        referral.setTo(organization2);
        referral.setToLocation(organization2.getLocations().stream().findFirst().orElse(null));

        em.persist(referral);
        em.flush();
        return referral;
    }

    private ReferralMother() {
    }
}
