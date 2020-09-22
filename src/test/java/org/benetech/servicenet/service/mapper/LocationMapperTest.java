package org.benetech.servicenet.service.mapper;

import static org.junit.Assert.assertEquals;

import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.mother.PhysicalAddressMother;
import org.benetech.servicenet.service.dto.provider.SimpleLocationDTO;
import org.junit.Test;

public class LocationMapperTest {

    private LocationMapper locationMapper = new LocationMapperImpl();

    @Test
    public void shouldMapPhysicalAddressToRecord() {
        PhysicalAddress physicalAddress = PhysicalAddressMother.createDefault();
        Location entry = new Location().physicalAddress(physicalAddress);

        SimpleLocationDTO result = locationMapper.toSimpleDto(entry);

        assertEquals(PhysicalAddressMother.ADDRESS_1, result.getPhysicalAddress().getAddress1());
        assertEquals(PhysicalAddressMother.ATTENTION, result.getPhysicalAddress().getAttention());
        assertEquals(PhysicalAddressMother.CITY, result.getPhysicalAddress().getCity());
        assertEquals(PhysicalAddressMother.REGION, result.getPhysicalAddress().getRegion());
        assertEquals(PhysicalAddressMother.STATE_PROVINCE, result.getPhysicalAddress().getStateProvince());
        assertEquals(PhysicalAddressMother.POSTAL_CODE, result.getPhysicalAddress().getPostalCode());
        assertEquals(PhysicalAddressMother.COUNTRY, result.getPhysicalAddress().getCountry());
    }
}
