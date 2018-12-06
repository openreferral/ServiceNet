package org.benetech.servicenet.adapter.firstprovider;

import org.benetech.servicenet.adapter.firstprovider.model.RawData;
import org.benetech.servicenet.adapter.shared.util.LocationUtils;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.PostalAddress;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FirstProviderDataMapperUnitTest {

    private static final String ADDRESS_1 = "Address 1";
    private static final String CITY = "City";
    private static final String STATE_PROVINCE = "State Province";
    private static final String PHONE = "123456789";
    private static final String LATITUDE = "54.380478";
    private static final String LONGITUDE = "18.606646";

    private RawData rawData;

    @Before
    public void init() {
        rawData = new RawData();
        rawData.setAddress1(ADDRESS_1);
        rawData.setCity(CITY);
        rawData.setStateProvince(STATE_PROVINCE);
        rawData.setPhone(PHONE);
        rawData.setGeoLocation(LATITUDE + ", " + LONGITUDE);
    }

    @Test
    public void shouldExtractPhysicalAddressFromRawData() {
        PhysicalAddress extracted = FirstProviderDataMapper.INSTANCE.extractPhysicalAddress(rawData);
        assertEquals(ADDRESS_1, extracted.getAddress1());
        assertEquals(CITY, extracted.getCity());
        assertEquals(STATE_PROVINCE, extracted.getStateProvince());
    }

    @Test
    public void shouldExtractPostalAddressFromRawData() {
        PostalAddress extracted = FirstProviderDataMapper.INSTANCE.extractPostalAddress(rawData);
        assertEquals(ADDRESS_1, extracted.getAddress1());
        assertEquals(CITY, extracted.getCity());
        assertEquals(STATE_PROVINCE, extracted.getStateProvince());
    }

    @Test
    public void shouldExtractLocationFromRawData() {
        Location extracted = FirstProviderDataMapper.INSTANCE.extractLocation(rawData);
        String expectedName = LocationUtils.buildLocationName(CITY, STATE_PROVINCE, ADDRESS_1);
        assertEquals(expectedName, extracted.getName());
        assertEquals(Double.valueOf(LATITUDE), extracted.getLatitude());
        assertEquals(Double.valueOf(LONGITUDE), extracted.getLongitude());
    }

    @Test
    public void shouldExtractPhoneFromRawData() {
        Phone extracted = FirstProviderDataMapper.INSTANCE.extractPhone(rawData);
        assertEquals(PHONE, extracted.getNumber());
    }
}
