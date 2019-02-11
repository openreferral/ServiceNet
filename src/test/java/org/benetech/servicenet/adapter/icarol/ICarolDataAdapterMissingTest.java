package org.benetech.servicenet.adapter.icarol;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.benetech.servicenet.adapter.AdapterTestsUtils;
import org.benetech.servicenet.adapter.icarol.model.ICarolAgency;
import org.benetech.servicenet.adapter.icarol.model.ICarolContactDetails;
import org.benetech.servicenet.adapter.icarol.model.ICarolDataToPersist;
import org.benetech.servicenet.adapter.icarol.model.ICarolDay;
import org.benetech.servicenet.adapter.icarol.model.ICarolProgram;
import org.benetech.servicenet.adapter.icarol.model.ICarolServiceSite;
import org.benetech.servicenet.adapter.icarol.model.ICarolSite;

import org.benetech.servicenet.domain.AccessibilityForDisabilities;
import org.benetech.servicenet.domain.Eligibility;
import org.benetech.servicenet.domain.OpeningHours;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.PostalAddress;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ICarolDataAdapterMissingTest {

    private static final String MINIMAL_JSON = "icarol/minimal.json";
    private static final String PROVIDER_NAME = "Eden";

    private ICarolDataToPersist data;

    @Before
    public void setUp() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(MINIMAL_JSON);
        data = new ICarolDataToPersist();
        JsonArray elements = new Gson().fromJson(json, JsonArray.class);

        for (JsonElement element : elements) {
            JsonObject jsonObject = element.getAsJsonObject();
            String type = jsonObject.get("type").getAsString();
            if (type.equals("Agency")) {
                data.addAgency(new Gson().fromJson(jsonObject, ICarolAgency.class));
            }
            if (type.equals("Program")) {
                data.addProgram(new Gson().fromJson(jsonObject, ICarolProgram.class));
            }
            if (type.equals("ServiceSite")) {
                data.addServiceSite(new Gson().fromJson(jsonObject, ICarolServiceSite.class));
            }
            if (type.equals("Site")) {
                data.addSite(new Gson().fromJson(jsonObject, ICarolSite.class));
            }
        }
    }

    @Test
    @Ignore("ICarolConfidentialFieldsMapper.extractUrlIfNotConfidential() […] replace(\" \", \"\") is causing null " +
        "exception")
    public void shouldNotThrowExceptionForMinimalDataForAgency() {
        ICarolAgency agency = data.getAgencies().get(0);
        Organization result = ICarolDataMapper.INSTANCE.extractOrganization(agency, PROVIDER_NAME);

        assertEquals("HOUSING AUTHORITY OF THE COUNTY OF Commoncounty (ABCD)", result.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    @Ignore("Null pointer exception") //TODO: Remove
    public void shouldThrowExceptionForNullOrganizationName() {
        ICarolAgency agency = data.getAgencies().get(0);
        agency.getNames()[0].setValue(null);
        agency.getNames()[0].setPurpose(null);
        ICarolDataMapper.INSTANCE.extractOrganization(agency, PROVIDER_NAME);
    }

    @Test(expected = IllegalArgumentException.class)
    @Ignore("No exception is thrown") //TODO: Remove
    public void shouldThrowExceptionForBlankOrganizationName() {
        ICarolAgency agency = data.getAgencies().get(0);
        agency.getNames()[0].setValue("");
        ICarolDataMapper.INSTANCE.extractOrganization(agency, PROVIDER_NAME);
    }

    @Test
    public void shouldNotThrowExceptionForAgencyMinimalDataForPhones() {
        ICarolAgency agency = data.getAgencies().get(0);
        Set<Phone> phones = ICarolDataMapper.INSTANCE.extractPhones(agency.getContactDetails());
        List<Phone> result = new ArrayList<>(phones);

        assertEquals("123-465-7890", result.get(0).getNumber());
        assertEquals("234-567-8901", result.get(1).getNumber());
        assertEquals("345-678-9012", result.get(2).getNumber());
        assertEquals("456-789-0123", result.get(3).getNumber());
        assertEquals("567-890-1234", result.get(4).getNumber());
        assertEquals("678-901-2345", result.get(5).getNumber());
    }

    @Test(expected = IllegalArgumentException.class)
    @Ignore("No exception is thrown") //TODO: Remove
    public void shouldThrowExceptionForAgencyNullPhones() {
        ICarolAgency agency = data.getAgencies().get(0);
        for (ICarolContactDetails details : agency.getContactDetails()) {
            if (details.getContact().getType().equalsIgnoreCase("PhoneNumber")) {
                details.getContact().setNumber(null);
            }
        }

        ICarolDataMapper.INSTANCE.extractPhones(agency.getContactDetails());
    }

    @Test(expected = IllegalArgumentException.class)
    @Ignore("No exception is thrown") //TODO: Remove
    public void shouldThrowExceptionForAgencyBlankPhones() {
        ICarolAgency agency = data.getAgencies().get(0);
        for (ICarolContactDetails details : agency.getContactDetails()) {
            if (details.getContact().getType().equalsIgnoreCase("PhoneNumber")) {
                details.getContact().setNumber("");
            }
        }

        ICarolDataMapper.INSTANCE.extractPhones(agency.getContactDetails());
    }

    @Test
    @Ignore("ICarolConfidentialFieldsMapper.extractUrlIfNotConfidential() […] replace(\" \", \"\") is causing null " +
        "exception")
    public void shouldNotThrowExceptionForAgencyStatus() {
        ICarolAgency agency = data.getAgencies().get(0);
        Organization organization = ICarolDataMapper.INSTANCE.extractOrganization(agency, PROVIDER_NAME);
        assertTrue(organization.getActive());
    }

    @Test
    @Ignore("Null pointer") //TODO: Remove
    public void shouldThrowExceptionForNullOrganizationStatus() {
        ICarolAgency agency = data.getAgencies().get(0);
        agency.setStatus(null);
        Organization organization = ICarolDataMapper.INSTANCE.extractOrganization(agency, PROVIDER_NAME);
        assertFalse(organization.getActive());
    }

    @Test(expected = IllegalArgumentException.class)
    @Ignore("NullPointer exception is thrown") //TODO: Remove
    public void shouldThrowExceptionForAgencyBlankOrganizationStatus() {
        ICarolAgency agency = data.getAgencies().get(0);
        agency.setStatus("");
        Organization organization = ICarolDataMapper.INSTANCE.extractOrganization(agency, PROVIDER_NAME);
        assertFalse(organization.getActive());
    }

    @Test
    public void shouldNotThrowExceptionOrganizationForOpeningHours() {
        ICarolAgency agency = data.getAgencies().get(0);
        Set<OpeningHours> hours = ICarolDataMapper.INSTANCE.extractOpeningHours(agency.getHours());

        List<OpeningHours> result = new ArrayList<>(hours);

        assertEquals((Integer) 0, result.get(0).getWeekday());
        assertEquals((Integer) 1, result.get(1).getWeekday());
        assertEquals((Integer) 2, result.get(2).getWeekday());
        assertEquals((Integer) 3, result.get(3).getWeekday());
        assertEquals((Integer) 4, result.get(4).getWeekday());
    }

    @Test(expected = IllegalArgumentException.class)
    @Ignore("NullPointer exception is thrown") //TODO: Remove
    public void shouldThrowExceptionForAgencyNullOpeningHours() {
        ICarolAgency agency = data.getAgencies().get(0);

        for (ICarolDay day : agency.getHours().getDays()) {
            day.setDayOfWeek(null);
        }
        ICarolDataMapper.INSTANCE.extractOpeningHours(agency.getHours());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForAgencyBlankOpeningHours() {
        ICarolAgency agency = data.getAgencies().get(0);

        for (ICarolDay day : agency.getHours().getDays()) {
            day.setDayOfWeek("");
        }
        ICarolDataMapper.INSTANCE.extractOpeningHours(agency.getHours());
    }

    @Test
    public void shouldNotThrowExceptionServiceForPostalAddress() {
        ICarolProgram program = data.getPrograms().get(0);

        Optional<PostalAddress> addressOpt = ICarolDataMapper.INSTANCE.extractPostalAddress(program.getContactDetails());

        assertTrue(addressOpt.isPresent());
        PostalAddress result = addressOpt.get();
        assertEquals("12345 Cool Street", result.getAddress1());
        assertEquals("CarpetHanger", result.getCity());
        assertEquals("CA", result.getStateProvince());
    }

    @Test(expected = IllegalArgumentException.class)
    @Ignore("IllegalArgumentException should be thrown") //TODO: Remove
    public void shouldThrowExceptionForProgramNullPostalAddress() {
        ICarolProgram program = data.getPrograms().get(0);
        for (ICarolContactDetails details : program.getContactDetails()) {
            if (details.getContact().getType().equalsIgnoreCase("PostalAddress")) {
                details.getContact().setLine1(null);
                details.getContact().setCity(null);
                details.getContact().setStateProvince(null);
            }
        }

        ICarolDataMapper.INSTANCE.extractPostalAddress(program.getContactDetails());
    }

    @Test(expected = IllegalArgumentException.class)
    @Ignore("IllegalArgumentException should be thrown") //TODO: Remove
    public void shouldThrowExceptionForProgramBlankPostalAddress() {
        ICarolProgram program = data.getPrograms().get(0);
        for (ICarolContactDetails details : program.getContactDetails()) {
            if (details.getContact().getType().equalsIgnoreCase("PostalAddress")) {
                details.getContact().setLine1("");
                details.getContact().setCity("");
                details.getContact().setStateProvince("");
            }
        }

        ICarolDataMapper.INSTANCE.extractPostalAddress(program.getContactDetails());
    }

    @Test
    public void shouldNotThrowExceptionForProgramMinimalDataForPhones() {
        ICarolProgram program = data.getPrograms().get(0);
        Set<Phone> phones = ICarolDataMapper.INSTANCE.extractPhones(program.getContactDetails());
        List<Phone> result = new ArrayList<>(phones);

        assertEquals("123-465-7890", result.get(0).getNumber());
        assertEquals("678-901-2345", result.get(1).getNumber());
        assertEquals("789-012-3456", result.get(2).getNumber());
    }

    @Test(expected = IllegalArgumentException.class)
    @Ignore("No exception is thrown") //TODO: Remove
    public void shouldThrowExceptionForProgramNullPhones() {
        ICarolProgram program = data.getPrograms().get(0);
        for (ICarolContactDetails details : program.getContactDetails()) {
            if (details.getContact().getType().equalsIgnoreCase("PhoneNumber")) {
                details.getContact().setNumber(null);
            }
        }

        ICarolDataMapper.INSTANCE.extractPhones(program.getContactDetails());
    }

    @Test(expected = IllegalArgumentException.class)
    @Ignore("No exception is thrown") //TODO: Remove
    public void shouldThrowExceptionForProgramBlankPhones() {
        ICarolProgram program = data.getPrograms().get(0);
        for (ICarolContactDetails details : program.getContactDetails()) {
            if (details.getContact().getType().equalsIgnoreCase("PhoneNumber")) {
                details.getContact().setNumber("");
            }
        }

        ICarolDataMapper.INSTANCE.extractPhones(program.getContactDetails());
    }

    @Test
    public void shouldNotThrowExceptionServiceForOpeningHours() {
        ICarolProgram program = data.getPrograms().get(0);
        Set<OpeningHours> hours = ICarolDataMapper.INSTANCE.extractOpeningHours(program.getHours());

        List<OpeningHours> result = new ArrayList<>(hours);

        assertEquals((Integer) 0, result.get(0).getWeekday());
        assertEquals((Integer) 1, result.get(1).getWeekday());
        assertEquals((Integer) 2, result.get(2).getWeekday());
        assertEquals((Integer) 3, result.get(3).getWeekday());
        assertEquals((Integer) 4, result.get(4).getWeekday());
    }

    @Test(expected = IllegalArgumentException.class)
    @Ignore("NullPointer exception is thrown") //TODO: Remove
    public void shouldThrowExceptionForProgramNullOpeningHours() {
        ICarolProgram program = data.getPrograms().get(0);

        for (ICarolDay day : program.getHours().getDays()) {
            day.setDayOfWeek(null);
        }
        ICarolDataMapper.INSTANCE.extractOpeningHours(program.getHours());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForProgramBlankOpeningHours() {
        ICarolProgram program = data.getPrograms().get(0);

        for (ICarolDay day : program.getHours().getDays()) {
            day.setDayOfWeek("");
        }
        ICarolDataMapper.INSTANCE.extractOpeningHours(program.getHours());
    }

    @Test
    public void shouldNotThrowExceptionForProgramEligibility() {
        ICarolProgram program = data.getPrograms().get(0);

        Optional<Eligibility> eligibilityOpt = ICarolDataMapper.INSTANCE.extractEligibility(program);

        assertTrue(eligibilityOpt.isPresent());
        Eligibility result = eligibilityOpt.get();
        assertEquals("Low-income family, elderly (age 62 or over), persons with disabilities, or other persons.",
            result.getEligibility());
    }

    @Test(expected = IllegalArgumentException.class)
    @Ignore("NullPointer exception is thrown") //TODO: Remove
    public void shouldThrowExceptionForProgramNullEligibility() {
        ICarolProgram program = data.getPrograms().get(0);
        program.setEligibility(null);

        ICarolDataMapper.INSTANCE.extractEligibility(program);
    }

    @Test(expected = IllegalArgumentException.class)
    @Ignore("NullPointer exception is thrown") //TODO: Remove
    public void shouldThrowExceptionForProgramBlankEligibility() {
        ICarolProgram program = data.getPrograms().get(0);
        program.setEligibility("");

        ICarolDataMapper.INSTANCE.extractEligibility(program);
    }

    @Test
    public void shouldNotThrowExceptionForSitePostalAddress() {
        ICarolSite site = data.getSites().get(0);

        Optional<PostalAddress> addressOpt = ICarolDataMapper.INSTANCE.extractPostalAddress(site.getContactDetails());

        assertTrue(addressOpt.isPresent());
        PostalAddress result = addressOpt.get();
        assertEquals("12345 Cool Street", result.getAddress1());
        assertEquals("CarpetHanger", result.getCity());
        assertEquals("CA", result.getStateProvince());
    }

    @Test(expected = IllegalArgumentException.class)
    @Ignore("IllegalArgumentException should be thrown") //TODO: Remove
    public void shouldThrowExceptionForSiteNullPostalAddress() {
        ICarolSite site = data.getSites().get(0);
        for (ICarolContactDetails details : site.getContactDetails()) {
            if (details.getContact().getType().equalsIgnoreCase("PostalAddress")) {
                details.getContact().setLine1(null);
                details.getContact().setCity(null);
                details.getContact().setStateProvince(null);
            }
        }

        ICarolDataMapper.INSTANCE.extractPostalAddress(site.getContactDetails());
    }

    @Test(expected = IllegalArgumentException.class)
    @Ignore("IllegalArgumentException should be thrown") //TODO: Remove
    public void shouldThrowExceptionForSiteBlankPostalAddress() {
        ICarolSite site = data.getSites().get(0);
        for (ICarolContactDetails details : site.getContactDetails()) {
            if (details.getContact().getType().equalsIgnoreCase("PostalAddress")) {
                details.getContact().setLine1("");
                details.getContact().setCity("");
                details.getContact().setStateProvince("");
            }
        }

        ICarolDataMapper.INSTANCE.extractPostalAddress(site.getContactDetails());
    }

    @Test
    public void shouldNotThrowExceptionForSiteAccessibilityForDisabilities() {
        ICarolSite site = data.getSites().get(0);

        Optional<AccessibilityForDisabilities> accessibilityOpt = ICarolDataMapper
            .INSTANCE.extractAccessibilityForDisabilities(site);

        assertTrue(accessibilityOpt.isPresent());
        AccessibilityForDisabilities result = accessibilityOpt.get();
        assertEquals("Wheelchair accessible/Ramp/Special parking/Restroom", result.getAccessibility());
    }

    @Test(expected = IllegalArgumentException.class)
    @Ignore("IllegalArgumentException should be thrown") //TODO: Remove
    public void shouldThrowExceptionForSiteNullAccessibilityForDisabilities() {
        ICarolSite site = data.getSites().get(0);
        site.getAccessibility().setDisabled(null);

        ICarolDataMapper.INSTANCE.extractAccessibilityForDisabilities(site);
    }

    @Test(expected = IllegalArgumentException.class)
    @Ignore("IllegalArgumentException should be thrown") //TODO: Remove
    public void shouldThrowExceptionForSiteBlankAccessibilityForDisabilities() {
        ICarolSite site = data.getSites().get(0);
        site.getAccessibility().setDisabled("");

        ICarolDataMapper.INSTANCE.extractAccessibilityForDisabilities(site);
    }
}
