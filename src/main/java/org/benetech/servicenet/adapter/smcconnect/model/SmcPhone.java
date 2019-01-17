package org.benetech.servicenet.adapter.smcconnect.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class SmcPhone extends SmcBaseData {

    @SerializedName("contact_id")
    private String contactId;

    @SerializedName("location_id")
    private String locationId;

    @SerializedName("organization_id")
    private String organizationId;

    @SerializedName("service_id")
    private String serviceId;

    @SerializedName("country_prefix")
    private String countryPrefix;

    private String department;

    private String extension;

    private String number;

    @SerializedName("number_type")
    private String numberType;

    @SerializedName("vanity_number")
    private String vanityNumber;
}
