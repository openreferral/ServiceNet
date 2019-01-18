package org.benetech.servicenet.adapter.sheltertech.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class AddressRaw {

    @SerializedName("id")
    private Integer id;

    @SerializedName("attention")
    private String attention;

    @SerializedName("address_1")
    private String address1;

    @SerializedName("address_2")
    private String address2;

    @SerializedName("address_3")
    private String address3;

    @SerializedName("address_4")
    private String address4;

    @SerializedName("city")
    private String city;

    @SerializedName("state_province")
    private String stateProvince;

    @SerializedName("postal_code")
    private String postalCode;

    @SerializedName("country")
    private String country;

    @SerializedName("latitude")
    private Double latitude;

    @SerializedName("longitude")
    private Double longitude;

}
