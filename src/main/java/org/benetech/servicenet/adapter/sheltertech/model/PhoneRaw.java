package org.benetech.servicenet.adapter.sheltertech.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class PhoneRaw {

    @SerializedName("id")
    private Integer id;

    @SerializedName("number")
    private String number;

    @SerializedName("extension")
    private String extension;

    @SerializedName("service_type")
    private String serviceType;

    @SerializedName("country_code")
    private String countryCode;

}
