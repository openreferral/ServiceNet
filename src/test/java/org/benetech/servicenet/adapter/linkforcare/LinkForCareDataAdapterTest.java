package org.benetech.servicenet.adapter.linkforcare;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;
import static org.benetech.servicenet.config.Constants.LINK_FOR_CARE_PROVIDER;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Set;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.benetech.servicenet.MockedGeocodingConfiguration;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestDatabaseManagement;
import org.benetech.servicenet.adapter.AdapterTestsUtils;
import org.benetech.servicenet.adapter.shared.model.SingleImportData;
import org.benetech.servicenet.converter.XlsxFileConverter;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.service.OrganizationService;
import org.benetech.servicenet.service.dto.provider.ProviderLocationDTO;
import org.benetech.servicenet.service.dto.provider.ProviderOpeningHoursDTO;
import org.benetech.servicenet.service.dto.provider.ProviderOrganizationDTO;
import org.benetech.servicenet.service.dto.provider.ProviderRegularScheduleDTO;
import org.benetech.servicenet.service.dto.provider.ProviderServiceDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.xml.sax.SAXException;

// TODO: add more tests
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServiceNetApp.class, MockedGeocodingConfiguration.class})
public class LinkForCareDataAdapterTest {

    @Autowired
    private LinkForCareDataAdapter adapter;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private TestDatabaseManagement testDatabaseManagement;

    @Before
    public void clearDb() {
        testDatabaseManagement.clearDb();
    }

    @Test
    public void testSavingLAACData() throws IOException, OpenXML4JException, SAXException {
        XlsxFileConverter xlsxFileConverter = new XlsxFileConverter();
        InputStream xlsxIs = AdapterTestsUtils.readResourceAsSteam("linkforcare/complete.xlsx");
        File json = xlsxFileConverter.convertToFile(xlsxIs);
        SingleImportData importData = new SingleImportData(json, new DataImportReport(), LINK_FOR_CARE_PROVIDER, true);

        adapter.importData(importData);
        assertExtractedOrganizations();
    }

    private void assertExtractedOrganizations() {
        assertEquals(1, organizationService.findAll().size());
        for (Organization organization : organizationService.findAll()) {
            ProviderOrganizationDTO dto = organizationService.findOneDTOForProvider(organization.getId()).orElse(null);
            assertTrue(organization.isActive());
            assertNotNull(dto);
            assertEquals("asdasdasd dsd sd sd", dto.getName());
            assertEquals("asda,sdas,das,das,d", dto.getDescription());
            assertEquals("public@asdasdasd.asd", dto.getEmail());
            assertEquals("https://asdsdwdw.dwdwd/ddd", dto.getUrl());
            assertEquals("LinkForCare", dto.getAccountName());
            assertEquals("status details, status formula", dto.getCovidProtocols());
            ZonedDateTime expectedUpdatedAt = ZonedDateTime.of(2019, 5, 3, 19, 19, 19, 0, ZoneId.of("UTC"));
            assertEquals(expectedUpdatedAt.toInstant(), dto.getUpdatedAt().toInstant());
            assertEquals(LocalDate.of(2019, 4, 3), organization.getYearIncorporated());

            assertEquals(1, dto.getLocations().size());
            ProviderLocationDTO location = dto.getLocations().get(0);
            assertLocation(location);

            assertEquals(1, dto.getServices().size());
            ProviderServiceDTO service = dto.getServices().get(0);
            assertService(service);
        }
    }

    private void assertLocation(ProviderLocationDTO location) {
        assertEquals("Address Line 1", location.getAddress1());
        assertEquals("Address Line 2", location.getAddress2());
        assertEquals("Kansas City", location.getCity());
        assertEquals("Kansas", location.getCa());
        assertEquals("66000", location.getZipcode());
        assertEquals(Boolean.TRUE, location.getOpen247());

        ProviderRegularScheduleDTO regularSchedule = location.getRegularSchedule();
        assertNotNull(regularSchedule);

        Set<ProviderOpeningHoursDTO> openingHours = regularSchedule.getOpeningHours();
        assertNotNull(openingHours);
        assertEquals(5, openingHours.size());

        Optional<ProviderOpeningHoursDTO> mondayOpeningHours = openingHours.stream().filter(oh -> oh.getWeekday() == 1).findFirst();
        assertTrue(mondayOpeningHours.isPresent());
        assertEquals("08:00:00 AM", mondayOpeningHours.get().getOpensAt());
        assertEquals("04:30:00 PM", mondayOpeningHours.get().getClosesAt());
    }

    private void assertService(ProviderServiceDTO service) {
        assertEquals("Community and Government Resources", service.getName());
        assertEquals(9, service.getTaxonomyIds().size());
        assertEquals("In Center Hemodialysis available, In Center Nocturnal Dialysis available, "
                + "In Home Non Medical Services available, In Unit Washer and Dryer available, "
                + "Laundry Services available, Licensed Practical Nurses available, "
                + "Licensed Professional Counselor available, "
                + "Licensed Staff on Duty 24/7 available, "
                + "Light Housekeeping available, "
                + "Live In Care available, "
                + "Must Schedule 24 Hrs Or More In Advance, "
                + "Must Schedule 48 Hrs Or More In Advance, "
                + "Pet Care available, "
                + "Pet Therapy available, "
                + "Pharmacy Services available, "
                + "Physical Therapists available, "
                + "Physicians available, "
                + "Psychiatric Nurse available, "
                + "Psychiatrist available, "
                + "Psychoanalyst available, "
                + "Psychologist available, "
                + "Psychotherapist available, "
                + "Registered Dietitians, "
                + "Registered Nurse On Call, "
                + "Services Provided: 1, "
                + "Skilled Nursing Facility, "
                + "Snacks Provided, "
                + "Social Workers, "
                + "Spanish, "
                + "Speech Language Therapists, "
                + "Stress and Trauma, "
                + "Stroke Neurological Rehabilitation, "
                + "24/7 Multiple Shift Care, "
                + "24 Hour Crisis Services, "
                + "24 Hour Security Personnel, "
                + "24 Hour Supportive Services",
            service.getDescription());
        assertEquals("application process", service.getApplicationProcess());
        assertEquals("Must be ambulatory, "
                + "Must meet age guidelines, "
                + "Must meet disability guidelines, "
                + "Must meet income guidelines, "
                + "Must be a veteran, "
                + "Social Security Card Required",
            service.getEligibilityCriteria());
        assertEquals(1, service.getLocationIndexes().size());
    }
}
