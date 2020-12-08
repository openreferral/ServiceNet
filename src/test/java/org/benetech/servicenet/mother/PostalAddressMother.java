package org.benetech.servicenet.mother;

import org.benetech.servicenet.domain.PostalAddress;

public class PostalAddressMother {

    public static final String ADDRESS_1 = "Postal Address";
    public static final String ADDRESS_2 = "Physical Address 2";
    public static final String ATTENTION = "Postal Attention";
    public static final String CITY = "Postal City";
    public static final String REGION = "Postal Region";
    public static final String STATE_PROVINCE = "Postal State Province";
    public static final String POSTAL_CODE = "Postal Postal Code";
    public static final String COUNTRY = "Postal Postal Country";

    public static PostalAddress createDefault() {
        return new PostalAddress()
            .address1(ADDRESS_1)
            .attention(ATTENTION)
            .city(CITY)
            .region(REGION)
            .stateProvince(STATE_PROVINCE)
            .postalCode(POSTAL_CODE)
            .country(COUNTRY);
    }

    public static PostalAddress createForServiceProvider() {
        return new PostalAddress()
            .address1(ADDRESS_1)
            .address2(ADDRESS_2)
            .city(CITY)
            .postalCode(POSTAL_CODE)
            .stateProvince(STATE_PROVINCE);
    }

    private PostalAddressMother() {
    }
}
