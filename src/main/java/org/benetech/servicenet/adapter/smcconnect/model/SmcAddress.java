package org.benetech.servicenet.adapter.smcconnect.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SmcAddress extends SmcBaseData {

    @SerializedName("address_1")
    private String address1;

    @SerializedName("address_2")
    private String address2;

    private String city;

    @SerializedName("state_province")
    private String stateProvince;

    @SerializedName("postal_code")
    private String postalCode;

    private String country;
}
