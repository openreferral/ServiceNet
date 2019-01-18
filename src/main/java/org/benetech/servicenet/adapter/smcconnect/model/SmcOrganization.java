package org.benetech.servicenet.adapter.smcconnect.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class SmcOrganization extends SmcBaseData {

    @SerializedName("alternate_name")
    private String alternateName;

    @SerializedName("date_incorporated")
    private String dateIncorporated;

    @SerializedName("funding_sources")
    private String fundingSources;

    private String accreditations;

    private String description;

    private String email;

    private String licenses;

    @SerializedName("legal_status")
    private String legalStatus;

    @SerializedName("tax_id")
    private String taxId;

    @SerializedName("tax_status")
    private String taxStatus;

    private String name;

    private String website;
}
