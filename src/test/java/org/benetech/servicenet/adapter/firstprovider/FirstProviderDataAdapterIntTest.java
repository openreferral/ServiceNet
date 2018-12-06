package org.benetech.servicenet.adapter.firstprovider;

import org.apache.commons.lang3.NotImplementedException;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.adapter.AdapterTestsUtils;
import org.benetech.servicenet.service.LocationService;
import org.benetech.servicenet.service.PhoneService;
import org.benetech.servicenet.service.PhysicalAddressService;
import org.benetech.servicenet.service.PostalAddressService;
import org.benetech.servicenet.service.dto.LocationDTO;
import org.benetech.servicenet.service.dto.PhoneDTO;
import org.benetech.servicenet.service.dto.PhysicalAddressDTO;
import org.benetech.servicenet.service.dto.PostalAddressDTO;
import org.benetech.servicenet.service.mapper.LocationMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
@Transactional
public class FirstProviderDataAdapterIntTest {

    @Autowired
    private FirstProviderDataAdapter adapter;

    @Autowired
    private LocationService locationService;

    @Autowired
    private PhoneService phoneService;

    @Autowired
    private PhysicalAddressService physicalAddressService;

    @Autowired
    private PostalAddressService postalAddressService;

    @Autowired
    private LocationMapper locationMapper;

    @Test
    public void shouldPersistEntitiesFromJsonData() throws IOException {
        String data = AdapterTestsUtils.readResourceAsString("FirstProviderData.json");
        adapter.persistData(data);
        int entriesNumber = 2;

        assertEquals(entriesNumber, locationService.findAll().size());
        assertEquals(entriesNumber, phoneService.findAll().size());
        assertEquals(entriesNumber, physicalAddressService.findAll().size());
        assertEquals(entriesNumber, postalAddressService.findAll().size());
    }

    @Test
    public void shouldPersistAddressesAndPhoneWithReferenceToTheLocation() throws IOException {
        String data = AdapterTestsUtils.readResourceAsString("FirstProviderData.json");
        adapter.persistData(data);
        LocationDTO firstLocation = locationService.findAll().get(0);
        PostalAddressDTO firstPostalAddress = postalAddressService.findAll().get(0);
        PhysicalAddressDTO firstPhysicalAddress = physicalAddressService.findAll().get(0);
        PhoneDTO firstPhone = phoneService.findAll().get(0);

        assertEquals(firstPostalAddress.getLocationId(), firstLocation.getId());
        assertEquals(firstPhysicalAddress.getLocationId(), firstLocation.getId());
        assertEquals(firstPhone.getLocationId(), firstLocation.getId());
    }

    @Test(expected = NotImplementedException.class)
    public void shouldThrowExceptionWhenUsedForWrongTypeOfData() {
        adapter.persistData(new ArrayList<>());
    }
}
