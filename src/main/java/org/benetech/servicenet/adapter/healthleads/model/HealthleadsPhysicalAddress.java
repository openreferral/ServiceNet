package org.benetech.servicenet.adapter.healthleads.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class HealthleadsPhysicalAddress extends LocationRelatedHealthleadsData {

    private String attention;

    @SerializedName("address_1")
    private String address;

    private String city;

    private String region;

    @SerializedName("state_province")
    private String stateProvince;

    @SerializedName("postal_code")
    private String postalCode;

    private String country;
}
