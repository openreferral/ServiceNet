package org.benetech.servicenet.adapter.icarol;

import org.benetech.servicenet.adapter.icarol.model.ICarolAccessibility;
import org.benetech.servicenet.adapter.icarol.model.ICarolAgency;
import org.benetech.servicenet.adapter.icarol.model.ICarolContact;
import org.benetech.servicenet.adapter.icarol.model.ICarolContactDetails;
import org.benetech.servicenet.adapter.icarol.model.ICarolDay;
import org.benetech.servicenet.adapter.icarol.model.ICarolHours;
import org.benetech.servicenet.adapter.icarol.model.ICarolName;
import org.benetech.servicenet.adapter.icarol.model.ICarolProgram;
import org.benetech.servicenet.adapter.icarol.model.ICarolSite;
import org.benetech.servicenet.domain.AccessibilityForDisabilities;
import org.benetech.servicenet.domain.Eligibility;
import org.benetech.servicenet.domain.Language;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.OpeningHours;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.PostalAddress;
import org.benetech.servicenet.domain.Service;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ICarolDataMapperTest {

    private ICarolDataMapper mapper;
    private String PROVIDER_NAME = "provider name";

    private static final String ID = "id";
    private static final String PRIMARY = "Primary";
    private static final String PHONE_NUMBER = "PhoneNumber";
    private static final String PHYSICAL_LOCATION = "PhysicalLocation";
    private static final String POSTAL_ADDRESS = "PostalAddress";
    private static final String ACTIVE = "Active";

    @Before
    public void setUp() {
        mapper = ICarolDataMapper.INSTANCE;
    }

    @Test
    public void shouldExtractConfidentialOrganization() {
        ICarolAgency input = new ICarolAgency();
        input.setIsConfidential(true);
        ICarolName name = new ICarolName();
        name.setPurpose(PRIMARY);
        input.setNames(new ICarolName[] { name });
        input.setContactDetails(new ICarolContactDetails[]{});
        input.setStatus(ACTIVE);

        Organization extracted = mapper.extractOrganization(input, PROVIDER_NAME);

        assertTrue(extracted.getIsConfidential());
    }

    @Test
    public void shouldExtractConfidentialService() {
        ICarolProgram input = new ICarolProgram();
        input.setIsConfidential(true);
        ICarolName name = new ICarolName();
        name.setPurpose(PRIMARY);
        input.setNames(new ICarolName[] { name });
        input.setContactDetails(new ICarolContactDetails[]{});

        Service extracted = mapper.extractService(input, PROVIDER_NAME);

        assertTrue(extracted.getIsConfidential());
    }

    @Test
    public void shouldExtractNoOpeningHoursIfConfidential() {
        ICarolHours input = new ICarolHours();
        ICarolDay day = new ICarolDay();
        day.setDayOfWeek("Mon");
        input.setDays(new ICarolDay[]{ day });
        input.setIsConfidential(true);

        Set<OpeningHours> extracted = mapper.extractOpeningHours(input);

        assertEquals(0, extracted.size());
    }

    @Test
    public void shouldExtractConfidentialAccessibilityForDisabilities() {
        ICarolSite input = new ICarolSite();
        ICarolAccessibility accessibility = new ICarolAccessibility();
        accessibility.setIsConfidential(true);
        accessibility.setDisabled("accessibility");
        input.setAccessibility(accessibility);

        Optional<AccessibilityForDisabilities> extracted = mapper.extractAccessibilityForDisabilities(input);

        assertTrue(extracted.isPresent());
        assertTrue(extracted.get().getIsConfidential());
    }

    @Test
    public void shouldExtractConfidentialPhones() {
        ICarolContactDetails input = new ICarolContactDetails();
        input.setIsConfidential(true);
        ICarolContact contact = new ICarolContact();
        contact.setType(PHONE_NUMBER);
        input.setContact(contact);

        Set<Phone> extracted = mapper.extractPhones(new ICarolContactDetails[] { input });

        assertEquals(1, extracted.size());
        for (Phone entry : extracted) {
            assertTrue(entry.getIsConfidential());
        }
    }

    @Test
    public void shouldExtractConfidentialLocation() {
        ICarolContactDetails input = new ICarolContactDetails();
        input.setIsConfidential(true);
        ICarolContact contact = new ICarolContact();
        contact.setType(PHYSICAL_LOCATION);
        input.setContact(contact);

        Optional<Location> extracted = mapper.extractLocation(new ICarolContactDetails[] { input }, ID, PROVIDER_NAME);

        assertTrue(extracted.isPresent());
        assertTrue(extracted.get().getIsConfidential());
    }

    @Test
    public void shouldExtractNoLangsIfConfidential() {
        ICarolProgram input = new ICarolProgram();
        input.setIsConfidential(true);
        input.setLanguagesOffered("English");

        Set<Language> extracted = mapper.extractLangs(input);

        assertEquals(0, extracted.size());
    }

    @Test
    public void shouldExtractConfidentialEligibility() {
        ICarolProgram input = new ICarolProgram();
        input.setIsConfidential(true);
        input.setEligibility("Eligibility");

        Optional<Eligibility> extracted = mapper.extractEligibility(input);

        assertTrue(extracted.isPresent());
        assertTrue(extracted.get().getIsConfidential());
    }

    @Test
    public void shouldExtractConfidentialPhysicalAddress() {
        ICarolContactDetails input = new ICarolContactDetails();
        input.setIsConfidential(true);
        ICarolContact contact = new ICarolContact();
        contact.setType(PHYSICAL_LOCATION);
        input.setContact(contact);

        Optional<PhysicalAddress> extracted = mapper.extractPhysicalAddress(new ICarolContactDetails[] { input });

        assertTrue(extracted.isPresent());
        assertTrue(extracted.get().getIsConfidential());
    }

    @Test
    public void shouldExtractConfidentialPostalAddress() {
        ICarolContactDetails input = new ICarolContactDetails();
        input.setIsConfidential(true);
        ICarolContact contact = new ICarolContact();
        contact.setType(POSTAL_ADDRESS);
        input.setContact(contact);

        Optional<PostalAddress> extracted = mapper.extractPostalAddress(new ICarolContactDetails[] { input });

        assertTrue(extracted.isPresent());
        assertTrue(extracted.get().getIsConfidential());
    }
    
    @Test
    public void shouldExtractNonConfidentialOrganization() {
        ICarolAgency input = new ICarolAgency();
        ICarolName name = new ICarolName();
        name.setPurpose(PRIMARY);
        input.setNames(new ICarolName[] { name });
        input.setContactDetails(new ICarolContactDetails[]{});
        input.setStatus(ACTIVE);

        Organization extracted = mapper.extractOrganization(input, PROVIDER_NAME);

        assertNull(extracted.getIsConfidential());
    }

    @Test
    public void shouldExtractNonConfidentialService() {
        ICarolProgram input = new ICarolProgram();
        ICarolName name = new ICarolName();
        name.setPurpose(PRIMARY);
        input.setNames(new ICarolName[] { name });
        input.setContactDetails(new ICarolContactDetails[]{});

        Service extracted = mapper.extractService(input, PROVIDER_NAME);

        assertNull(extracted.getIsConfidential());
    }

    @Test
    public void shouldExtractOpeningHoursIfNotConfidential() {
        ICarolHours input = new ICarolHours();
        ICarolDay day = new ICarolDay();
        day.setDayOfWeek("Mon");
        input.setDays(new ICarolDay[]{ day });

        Set<OpeningHours> extracted = mapper.extractOpeningHours(input);

        assertEquals(1, extracted.size());
        for (OpeningHours entry : extracted) {
            assertNull(entry.getIsConfidential());
        }
    }

    @Test
    public void shouldExtractNonConfidentialAccessibilityForDisabilities() {
        ICarolSite input = new ICarolSite();
        ICarolAccessibility accessibility = new ICarolAccessibility();
        accessibility.setDisabled("accessibility");
        input.setAccessibility(accessibility);

        Optional<AccessibilityForDisabilities> extracted = mapper.extractAccessibilityForDisabilities(input);

        assertTrue(extracted.isPresent());
        assertNull(extracted.get().getIsConfidential());
    }

    @Test
    public void shouldExtractNonConfidentialPhones() {
        ICarolContactDetails input = new ICarolContactDetails();
        ICarolContact contact = new ICarolContact();
        contact.setType(PHONE_NUMBER);
        input.setContact(contact);

        Set<Phone> extracted = mapper.extractPhones(new ICarolContactDetails[] { input });

        assertEquals(1, extracted.size());
        for (Phone entry : extracted) {
            assertNull(entry.getIsConfidential());
        }
    }

    @Test
    public void shouldExtractNonConfidentialLocation() {
        ICarolContactDetails input = new ICarolContactDetails();
        ICarolContact contact = new ICarolContact();
        contact.setType(PHYSICAL_LOCATION);
        input.setContact(contact);

        Optional<Location> extracted = mapper.extractLocation(new ICarolContactDetails[] { input }, ID, PROVIDER_NAME);

        assertTrue(extracted.isPresent());
        assertNull(extracted.get().getIsConfidential());
    }

    @Test
    public void shouldExtractLangsIfNotConfidential() {
        ICarolProgram input = new ICarolProgram();
        input.setLanguagesOffered("English");

        Set<Language> extracted = mapper.extractLangs(input);

        assertEquals(1, extracted.size());
        for (Language entry : extracted) {
            assertNull(entry.getIsConfidential());
        }
    }

    @Test
    public void shouldExtractNonConfidentialEligibility() {
        ICarolProgram input = new ICarolProgram();
        input.setEligibility("Eligibility");

        Optional<Eligibility> extracted = mapper.extractEligibility(input);

        assertTrue(extracted.isPresent());
        assertNull(extracted.get().getIsConfidential());
    }

    @Test
    public void shouldExtractNonConfidentialPhysicalAddress() {
        ICarolContactDetails input = new ICarolContactDetails();
        ICarolContact contact = new ICarolContact();
        contact.setType(PHYSICAL_LOCATION);
        input.setContact(contact);

        Optional<PhysicalAddress> extracted = mapper.extractPhysicalAddress(new ICarolContactDetails[] { input });

        assertTrue(extracted.isPresent());
        assertNull(extracted.get().getIsConfidential());
    }

    @Test
    public void shouldExtractNonConfidentialPostalAddress() {
        ICarolContactDetails input = new ICarolContactDetails();
        ICarolContact contact = new ICarolContact();
        contact.setType(POSTAL_ADDRESS);
        input.setContact(contact);

        Optional<PostalAddress> extracted = mapper.extractPostalAddress(new ICarolContactDetails[] { input });

        assertTrue(extracted.isPresent());
        assertNull(extracted.get().getIsConfidential());
    }
}
