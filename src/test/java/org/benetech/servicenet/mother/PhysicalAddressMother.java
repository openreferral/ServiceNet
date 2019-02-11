package org.benetech.servicenet.mother;

import org.benetech.servicenet.domain.PhysicalAddress;

public final class PhysicalAddressMother {

    public static final String ADDRESS_1 = "Physical Address";
    public static final String ATTENTION = "Physical Attention";
    public static final String CITY = "Physical City";
    public static final String REGION = "Physical Region";
    public static final String STATE_PROVINCE = "Physical State Province";
    public static final String POSTAL_CODE = "Physical Postal Code";
    public static final String COUNTRY = "Physical Postal Country";

    public static PhysicalAddress createDefault() {
        return new PhysicalAddress()
            .address1(ADDRESS_1)
            .attention(ATTENTION)
            .city(CITY)
            .region(REGION)
            .stateProvince(STATE_PROVINCE)
            .postalCode(POSTAL_CODE)
            .country(COUNTRY);
    }
}
