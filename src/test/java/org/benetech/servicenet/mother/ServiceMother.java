package org.benetech.servicenet.mother;

import java.util.Collections;
import javax.persistence.EntityManager;
import org.benetech.servicenet.domain.Contact;
import org.benetech.servicenet.domain.Eligibility;
import org.benetech.servicenet.domain.Funding;
import org.benetech.servicenet.domain.HolidaySchedule;
import org.benetech.servicenet.domain.Language;
import org.benetech.servicenet.domain.PaymentAccepted;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.Program;
import org.benetech.servicenet.domain.RegularSchedule;
import org.benetech.servicenet.domain.RequiredDocument;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.ServiceArea;
import org.benetech.servicenet.domain.ServiceMetadata;
import org.benetech.servicenet.domain.ServiceTaxonomy;
import org.benetech.servicenet.domain.Taxonomy;

public final class ServiceMother {

    public static final String NAME = "Service name";
    public static final String ALTERNATE_NAME = "Service alternate name";
    public static final String DESCRIPTION = "Service description";
    public static final String TRANSPORTATION = "Transportation";
    public static final double LATITUDE = 20.3443;
    public static final double LONGITUDE = -78.2323;
    public static final String URL = "Service url";
    public static final String EMAIL = "Service email";
    public static final String STATUS = "Service status";
    public static final String INTERPRETATION_SERVICES = "Service interpretationServices";
    public static final String APPLICATION_PROCESS = "Service applicationProcess";
    public static final String WAIT_TIME = "Service waitTime";
    public static final String FEES = "Service fees";
    public static final String ACCREDITATIONS = "Service accreditations";
    public static final String LICENSES = "Service licenses";
    public static final String TYPE = "Service type";
    public static final String SERVICE_PROVIDER = "ServiceProvider";
    public static final String REQUIRED_DOCUMENT = "Required doc";

    public static Service createDefault() {
        return new Service()
            .name(NAME)
            .alternateName(ALTERNATE_NAME)
            .description(DESCRIPTION)
            .url(URL)
            .email(EMAIL)
            .status(STATUS)
            .interpretationServices(INTERPRETATION_SERVICES)
            .applicationProcess(APPLICATION_PROCESS)
            .waitTime(WAIT_TIME)
            .fees(FEES)
            .accreditations(ACCREDITATIONS)
            .licenses(LICENSES)
            .type(TYPE);
    }

    public static Service createForServiceProvider() {
        return new Service()
            .name(NAME)
            .type(TYPE)
            .description(DESCRIPTION)
            .applicationProcess(APPLICATION_PROCESS)
            .providerName(SERVICE_PROVIDER);
    }

    public static Service createDefaultWithAllRelationsAndPersist(EntityManager em) {
        Service srv = createDefault();

        Program program = ProgramMother.createDefault();
        RegularSchedule regularSchedule = RegularScheduleMother.createWithTwoOpeningHours();
        regularSchedule.getOpeningHours().forEach(openingHours -> {
            openingHours.setRegularSchedule(regularSchedule);
            em.persist(openingHours);
        });
        HolidaySchedule holidaySchedule = HolidayScheduleMother.createDefault();
        Funding funding = FundingMother.createDefault();
        Eligibility eligibility = EligibilityMother.createDefault();
        RequiredDocument requiredDocument = RequiredDocumentMother.createDefault();
        PaymentAccepted paymentAccepted = PaymentAcceptedMother.createDefault();
        Language language = LanguageMother.createDefault();
        Taxonomy taxonomy = TaxonomyMother.createDefault();
        ServiceTaxonomy serviceTaxonomy = ServiceTaxonomyMother.createDefault();
        serviceTaxonomy.taxonomy(taxonomy);
        Phone srvPhone = PhoneMother.createDefault();
        Phone contactPhone = PhoneMother.createDefault();
        Contact contact = ContactMother.createDefault();
        contact.setPhones(Collections.singleton(contactPhone));
        ServiceArea serviceArea = ServiceAreaMother.createDefault();
        ServiceMetadata serviceMetadata = ServiceMetadataMother.createDefault();

        srv.program(program);
        srv.regularSchedule(regularSchedule);
        srv.holidaySchedules(Collections.singleton(holidaySchedule));
        srv.funding(funding);
        srv.eligibility(eligibility);
        srv.docs(Collections.singleton(requiredDocument));
        srv.paymentsAccepteds(Collections.singleton(paymentAccepted));
        srv.langs(Collections.singleton(language));
        srv.taxonomies(Collections.singleton(serviceTaxonomy));
        srv.phones(Collections.singleton(srvPhone));
        srv.contacts(Collections.singleton(contact));
        srv.areas(Collections.singleton(serviceArea));
        srv.metadata(Collections.singleton(serviceMetadata));

        em.persist(program);
        em.persist(regularSchedule);
        em.persist(holidaySchedule);
        em.persist(funding);
        em.persist(eligibility);
        em.persist(requiredDocument);
        em.persist(paymentAccepted);
        em.persist(language);
        em.persist(taxonomy);
        em.persist(serviceTaxonomy);
        em.persist(srvPhone);
        em.persist(contactPhone);
        em.persist(contact);
        em.persist(srv);
        em.persist(serviceArea);
        em.persist(serviceMetadata);

        em.flush();
        return srv;
    }

    public static Service createForServiceProviderAndPersist(EntityManager em) {
        Service srv = createForServiceProvider();
        Eligibility e = EligibilityMother.createDefault();
        RequiredDocument rd = new RequiredDocument().document(REQUIRED_DOCUMENT);
        srv.eligibility(e);
        srv.docs(Collections.singleton(rd));

        em.persist(e);
        em.persist(rd);
        em.persist(srv);
        em.flush();
        return srv;
    }

    private ServiceMother() {
    }
}
