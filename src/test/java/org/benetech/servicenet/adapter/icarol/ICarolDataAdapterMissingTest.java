package org.benetech.servicenet.adapter.icarol;

import static junit.framework.TestCase.assertEquals;
import static org.benetech.servicenet.config.Constants.EDEN_PROVIDER;
import static org.junit.Assert.assertTrue;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.benetech.servicenet.adapter.AdapterTestsUtils;
import org.benetech.servicenet.adapter.icarol.model.ICarolAgency;
import org.benetech.servicenet.adapter.icarol.model.ICarolDataToPersist;
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
import org.junit.Test;

public class ICarolDataAdapterMissingTest {

    private static final String MINIMAL_JSON = "icarol/minimal.json";
    private static final String AGENCY = "Agency";
    private static final String PROGRAM = "Program";
    private static final String SERVICE_SITE = "ServiceSite";
    private static final String SITE = "Site";
    private static final int MONDAY = 0;
    private static final int TUESDAY = 1;
    private static final int WEDNESDAY = 2;
    private static final int THURSDAY = 3;
    private static final int FRIDAY = 4;

    private final ICarolDataMapper mapper = ICarolDataMapper.INSTANCE;

    private ICarolDataToPersist data;

    @Before
    public void setUp() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(MINIMAL_JSON);
        data = new ICarolDataToPersist();
        JsonArray elements = new Gson().fromJson(json, JsonArray.class);

        for (JsonElement element : elements) {
            JsonObject jsonObject = element.getAsJsonObject();
            String type = jsonObject.get("type").getAsString();
            if (AGENCY.equals(type)) {
                data.addAgency(new Gson().fromJson(jsonObject, ICarolAgency.class));
            }
            if (PROGRAM.equals(type)) {
                data.addProgram(new Gson().fromJson(jsonObject, ICarolProgram.class));
            }
            if (SERVICE_SITE.equals(type)) {
                data.addServiceSite(new Gson().fromJson(jsonObject, ICarolServiceSite.class));
            }
            if (SITE.equals(type)) {
                data.addSite(new Gson().fromJson(jsonObject, ICarolSite.class));
            }
        }
    }

    @Test
    public void shouldNotThrowExceptionForMinimalDataForAgency() {
        ICarolAgency agency = data.getAgencies().get(0);
        Organization result = mapper.extractOrganization(agency, EDEN_PROVIDER);

        assertEquals("HOUSING AUTHORITY OF THE COUNTY OF Commoncounty (ABCD)", result.getName());
    }

    @Test
    public void shouldNotThrowExceptionForAgencyMinimalDataForPhones() {
        ICarolAgency agency = data.getAgencies().get(0);
        Set<Phone> phones = mapper.extractPhones(agency.getContactDetails());

        assertTrue(phones.stream().anyMatch(x -> x.getNumber().equalsIgnoreCase("123-465-7890")));
        assertTrue(phones.stream().anyMatch(x -> x.getNumber().equalsIgnoreCase("234-567-8901")));
        assertTrue(phones.stream().anyMatch(x -> x.getNumber().equalsIgnoreCase("345-678-9012")));
        assertTrue(phones.stream().anyMatch(x -> x.getNumber().equalsIgnoreCase("456-789-0123")));
        assertTrue(phones.stream().anyMatch(x -> x.getNumber().equalsIgnoreCase("567-890-1234")));
        assertTrue(phones.stream().anyMatch(x -> x.getNumber().equalsIgnoreCase("678-901-2345")));
    }

    @Test
    public void shouldNotThrowExceptionForAgencyStatus() {
        ICarolAgency agency = data.getAgencies().get(0);
        Organization organization = mapper.extractOrganization(agency, EDEN_PROVIDER);
        assertTrue(organization.getActive());
    }

    @Test
    public void shouldNotThrowExceptionOrganizationForOpeningHours() {
        ICarolAgency agency = data.getAgencies().get(0);
        Set<OpeningHours> hours = mapper.extractOpeningHours(agency.getHours());

        assertTrue(hours.stream().anyMatch(x -> x.getWeekday() == MONDAY));
        assertTrue(hours.stream().anyMatch(x -> x.getWeekday() == TUESDAY));
        assertTrue(hours.stream().anyMatch(x -> x.getWeekday() == WEDNESDAY));
        assertTrue(hours.stream().anyMatch(x -> x.getWeekday() == THURSDAY));
        assertTrue(hours.stream().anyMatch(x -> x.getWeekday() == FRIDAY));
    }

    @Test
    public void shouldNotThrowExceptionServiceForPostalAddress() {
        ICarolProgram program = data.getPrograms().get(0);

        Optional<PostalAddress> addressOpt = mapper.extractPostalAddress(program.getContactDetails());

        assertTrue(addressOpt.isPresent());
        PostalAddress result = addressOpt.get();
        assertEquals("12345 Cool Street", result.getAddress1());
        assertEquals("CarpetHanger", result.getCity());
        assertEquals("CA", result.getStateProvince());
    }

    @Test
    public void shouldNotThrowExceptionForProgramMinimalDataForPhones() {
        ICarolProgram program = data.getPrograms().get(0);
        Set<Phone> phones = mapper.extractPhones(program.getContactDetails());
        List<Phone> result = new ArrayList<>(phones);

        assertTrue(phones.stream().anyMatch(x -> x.getNumber().equalsIgnoreCase("123-465-7890")));
        assertTrue(phones.stream().anyMatch(x -> x.getNumber().equalsIgnoreCase("678-901-2345")));
        assertTrue(phones.stream().anyMatch(x -> x.getNumber().equalsIgnoreCase("789-012-3456")));
    }

    @Test
    public void shouldNotThrowExceptionServiceForOpeningHours() {
        ICarolProgram program = data.getPrograms().get(0);
        Set<OpeningHours> hours = mapper.extractOpeningHours(program.getHours());

        assertTrue(hours.stream().anyMatch(x -> x.getWeekday() == MONDAY));
        assertTrue(hours.stream().anyMatch(x -> x.getWeekday() == TUESDAY));
        assertTrue(hours.stream().anyMatch(x -> x.getWeekday() == WEDNESDAY));
        assertTrue(hours.stream().anyMatch(x -> x.getWeekday() == THURSDAY));
        assertTrue(hours.stream().anyMatch(x -> x.getWeekday() == FRIDAY));
    }

    @Test
    public void shouldNotThrowExceptionForProgramEligibility() {
        ICarolProgram program = data.getPrograms().get(0);

        Eligibility result = mapper.extractEligibility(program);

        assertEquals("Low-income family, elderly (age 62 or over), persons with disabilities, or other persons.",
            result.getEligibility());
    }

    @Test
    public void shouldNotThrowExceptionForSitePostalAddress() {
        ICarolSite site = data.getSites().get(0);

        Optional<PostalAddress> addressOpt = mapper.extractPostalAddress(site.getContactDetails());

        assertTrue(addressOpt.isPresent());
        PostalAddress result = addressOpt.get();
        assertEquals("12345 Cool Street", result.getAddress1());
        assertEquals("CarpetHanger", result.getCity());
        assertEquals("CA", result.getStateProvince());
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
}
