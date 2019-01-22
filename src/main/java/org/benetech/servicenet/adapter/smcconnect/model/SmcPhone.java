package org.benetech.servicenet.adapter.smcconnect.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SmcPhone extends SmcBaseData {

    @SerializedName("contact_id")
    private String contactId;

    @SerializedName("country_prefix")
    private String countryPrefix;

    private String department;

    private String extension;

    private String number;

    @SerializedName("number_type")
    private String type;

    @SerializedName("vanity_number")
    private String vanityNumber;
}
