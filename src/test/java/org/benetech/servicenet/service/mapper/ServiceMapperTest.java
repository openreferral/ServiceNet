package org.benetech.servicenet.service.mapper;

import org.benetech.servicenet.domain.Eligibility;
import org.benetech.servicenet.domain.Funding;
import org.benetech.servicenet.domain.HolidaySchedule;
import org.benetech.servicenet.domain.Language;
import org.benetech.servicenet.domain.PaymentAccepted;
import org.benetech.servicenet.domain.RegularSchedule;
import org.benetech.servicenet.domain.RequiredDocument;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.mother.ContactMother;
import org.benetech.servicenet.mother.EligibilityMother;
import org.benetech.servicenet.mother.FundingMother;
import org.benetech.servicenet.mother.HolidayScheduleMother;
import org.benetech.servicenet.mother.PhoneMother;
import org.benetech.servicenet.mother.RegularScheduleMother;
import org.benetech.servicenet.mother.ServiceMother;
import org.benetech.servicenet.mother.ServiceTaxonomyMother;
import org.benetech.servicenet.service.dto.ServiceRecordDTO;
import org.junit.Test;

import java.util.Collections;
import java.util.Set;

import static org.benetech.servicenet.mother.RegularScheduleMother.CLOSES_AT_1;
import static org.benetech.servicenet.mother.RegularScheduleMother.CLOSES_AT_2;
import static org.benetech.servicenet.mother.RegularScheduleMother.OPENS_AT_1;
import static org.benetech.servicenet.mother.RegularScheduleMother.OPENS_AT_2;
import static org.benetech.servicenet.mother.RegularScheduleMother.WEEKDAY_1;
import static org.benetech.servicenet.mother.RegularScheduleMother.WEEKDAY_2;
import static org.benetech.servicenet.mother.ServiceMother.ACCREDITATIONS;
import static org.benetech.servicenet.mother.ServiceMother.ALTERNATE_NAME;
import static org.benetech.servicenet.mother.ServiceMother.APPLICATION_PROCESS;
import static org.benetech.servicenet.mother.ServiceMother.DESCRIPTION;
import static org.benetech.servicenet.mother.ServiceMother.EMAIL;
import static org.benetech.servicenet.mother.ServiceMother.FEES;
import static org.benetech.servicenet.mother.ServiceMother.INTERPRETATION_SERVICES;
import static org.benetech.servicenet.mother.ServiceMother.LICENSES;
import static org.benetech.servicenet.mother.ServiceMother.NAME;
import static org.benetech.servicenet.mother.ServiceMother.STATUS;
import static org.benetech.servicenet.mother.ServiceMother.TYPE;
import static org.benetech.servicenet.mother.ServiceMother.URL;
import static org.benetech.servicenet.mother.ServiceMother.WAIT_TIME;
import static org.benetech.servicenet.mother.ServiceTaxonomyMother.DEFAULT_TAXONOMY_DETAILS;
import static org.benetech.servicenet.mother.ServiceTaxonomyMother.DIFFERENT_TAXONOMY_DETAILS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ServiceMapperTest {

    private ServiceMapper mapper = new ServiceMapperImpl();

    @Test
    public void shouldMapServiceToRecord() {
        Service entry = ServiceMother.createDefault();

        ServiceRecordDTO result = mapper.toRecord(entry);

        assertEquals(NAME, result.getService().getName());
        assertEquals(ALTERNATE_NAME, result.getService().getAlternateName());
        assertEquals(DESCRIPTION, result.getService().getDescription());

        assertEquals(URL, result.getService().getUrl());
        assertEquals(EMAIL, result.getService().getEmail());
        assertEquals(STATUS, result.getService().getStatus());
        assertEquals(INTERPRETATION_SERVICES, result.getService().getInterpretationServices());
        assertEquals(APPLICATION_PROCESS, result.getService().getApplicationProcess());
        assertEquals(WAIT_TIME, result.getService().getWaitTime());
        assertEquals(FEES, result.getService().getFees());
        assertEquals(ACCREDITATIONS, result.getService().getAccreditations());
        assertEquals(LICENSES, result.getService().getLicenses());
        assertEquals(TYPE, result.getService().getType());
    }

    @Test
    public void shouldMapFundingToRecord() {
        Funding funding = FundingMother.createDefault();
        Service entry = new Service().funding(funding);

        ServiceRecordDTO result = mapper.toRecord(entry);

        assertEquals(FundingMother.FUNDING_SOURCE, result.getFunding().getSource());
    }

    @Test
    public void shouldMapEligibilityToRecord() {
        Eligibility eligibility = EligibilityMother.createDefault();
        Service entry = new Service().eligibility(eligibility);

        ServiceRecordDTO result = mapper.toRecord(entry);

        assertEquals(EligibilityMother.ELIGIBILITY, result.getEligibility().getEligibility());
    }

    @Test
    public void shouldMapRegularScheduleToRecord() {
        RegularSchedule schedule = RegularScheduleMother.createWithTwoOpeningHours();
        Service entry = new Service().regularSchedule(schedule);

        ServiceRecordDTO result = mapper.toRecord(entry);

        assertTrue(result.getRegularScheduleOpeningHours().stream()
            .anyMatch(x ->
                x.getWeekday().equals(WEEKDAY_1)
                    && x.getOpensAt().equals(OPENS_AT_1)
                    && x.getClosesAt().equals(CLOSES_AT_1)));

        assertTrue(result.getRegularScheduleOpeningHours().stream()
            .anyMatch(x ->
                x.getWeekday().equals(WEEKDAY_2)
                    && x.getOpensAt().equals(OPENS_AT_2)
                    && x.getClosesAt().equals(CLOSES_AT_2)));

    }

    @Test
    public void shouldMapHolidayScheduleToRecord() {
        HolidaySchedule schedule = HolidayScheduleMother.createDefault();
        Service entry = new Service().holidaySchedules(Collections.singleton(schedule));

        ServiceRecordDTO result = mapper.toRecord(entry);

        assertEquals(HolidayScheduleMother.START_DATE, result.getHolidaySchedules().iterator().next().getStartDate());
        assertEquals(HolidayScheduleMother.END_DATE, result.getHolidaySchedules().iterator().next().getEndDate());
        assertEquals(HolidayScheduleMother.OPENS_AT, result.getHolidaySchedules().iterator().next().getOpensAt());
        assertEquals(HolidayScheduleMother.CLOSES_AT, result.getHolidaySchedules().iterator().next().getClosesAt());
        assertEquals(HolidayScheduleMother.CLOSED, result.getHolidaySchedules().iterator().next().isClosed());
    }

    @Test
    public void shouldMapLanguagesToRecord() {
        Service entry = new Service().langs(Set.of(
            new Language().language("en"),
            new Language().language("de")));

        ServiceRecordDTO result = mapper.toRecord(entry);

        assertTrue(result.getLangs().stream()
            .anyMatch(x ->
                x.getLanguage().equals("en")));

        assertTrue(result.getLangs().stream()
            .anyMatch(x ->
                x.getLanguage().equals("de")));

    }
    
    @Test
    public void shouldMapRequiredDocumentsToRecord() {
        Service entry = new Service().docs((Set.of(
            new RequiredDocument().document("doc1"),
            new RequiredDocument().document("doc2"))));

        ServiceRecordDTO result = mapper.toRecord(entry);

        assertTrue(result.getDocs().stream()
            .anyMatch(x ->
                x.getDocument().equals("doc1")));

        assertTrue(result.getDocs().stream()
            .anyMatch(x ->
                x.getDocument().equals("doc2")));

    }

    @Test
    public void shouldMapPaymentAcceptedsToRecord() {
        Service entry = new Service().paymentsAccepteds((Set.of(
            new PaymentAccepted().payment("payment 1"),
            new PaymentAccepted().payment("payment 2"))));

        ServiceRecordDTO result = mapper.toRecord(entry);

        assertTrue(result.getPaymentsAccepteds().stream()
            .anyMatch(x ->
                x.getPayment().equals("payment 1")));

        assertTrue(result.getPaymentsAccepteds().stream()
            .anyMatch(x ->
                x.getPayment().equals("payment 2")));

    }
    
    @Test
    public void shouldMapServiceTaxonomiesToRecord() {
        Service entry = new Service().taxonomies((Set.of(
            ServiceTaxonomyMother.createDefault(),
            ServiceTaxonomyMother.createDifferent())));

        ServiceRecordDTO result = mapper.toRecord(entry);

        assertTrue(result.getTaxonomies().stream()
            .anyMatch(x ->
                x.getTaxonomyDetails().equals(DEFAULT_TAXONOMY_DETAILS)));

        assertTrue(result.getTaxonomies().stream()
            .anyMatch(x ->
                x.getTaxonomyDetails().equals(DIFFERENT_TAXONOMY_DETAILS)));
    }

    @Test
    public void shouldMapPhonesToRecord() {
        Service entry = new Service().phones((Set.of(
            PhoneMother.createDefault(),
            PhoneMother.createDifferent())));

        ServiceRecordDTO result = mapper.toRecord(entry);

        assertTrue(result.getPhones().stream()
            .anyMatch(x ->
                x.getNumber().equals(PhoneMother.DEFAULT_NUMBER)
                    && x.getExtension().equals(PhoneMother.DEFAULT_EXTENSION)
                    && x.getType().equals(PhoneMother.DEFAULT_TYPE)
                    && x.getLanguage().equals(PhoneMother.DEFAULT_LANGUAGE)
                    && x.getDescription().equals(PhoneMother.DEFAULT_DESCRIPTION)));

        assertTrue(result.getPhones().stream()
            .anyMatch(x ->
                x.getNumber().equals(PhoneMother.DIFFERENT_NUMBER)
                    && x.getExtension().equals(PhoneMother.DIFFERENT_EXTENSION)
                    && x.getType().equals(PhoneMother.DIFFERENT_TYPE)
                    && x.getLanguage().equals(PhoneMother.DIFFERENT_LANGUAGE)
                    && x.getDescription().equals(PhoneMother.DIFFERENT_DESCRIPTION)));
    }

    @Test
    public void shouldMapContactsToRecord() {
        Service entry = new Service().contacts((Set.of(
            ContactMother.createDefault(),
            ContactMother.createDifferent())));

        ServiceRecordDTO result = mapper.toRecord(entry);

        assertTrue(result.getContacts().stream()
            .anyMatch(x ->
                x.getName().equals(ContactMother.DEFAULT_NAME)
                    && x.getTitle().equals(ContactMother.DEFAULT_TITLE)
                    && x.getDepartment().equals(ContactMother.DEFAULT_DEPARTMENT)
                    && x.getEmail().equals(ContactMother.DEFAULT_EMAIL)));

        assertTrue(result.getContacts().stream()
            .anyMatch(x ->
                x.getName().equals(ContactMother.DIFFERENT_NAME)
                    && x.getTitle().equals(ContactMother.DIFFERENT_TITLE)
                    && x.getDepartment().equals(ContactMother.DIFFERENT_DEPARTMENT)
                    && x.getEmail().equals(ContactMother.DIFFERENT_EMAIL)));
    }
}
