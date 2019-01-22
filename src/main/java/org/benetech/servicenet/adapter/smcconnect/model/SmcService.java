package org.benetech.servicenet.adapter.smcconnect.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SmcService extends SmcBaseData {

    @SerializedName("location_id")
    private String locationId;

    @SerializedName("program_id")
    private String programId;

    @SerializedName("accepted_payments")
    private String acceptedPayments;

    @SerializedName("alternate_name")
    private String alternateName;

    @SerializedName("application_process")
    private String applicationProcess;

    private String audience;

    private String description;

    private String eligibility;

    private String email;

    private String fees;

    @SerializedName("funding_sources")
    private String fundingSources;

    @SerializedName("interpretation_services")
    private String interpretationServices;

    private String keywords;

    private String languages;

    @SerializedName("required_documents")
    private String requiredDocuments;

    private String name;

    @SerializedName("service_areas")
    private String serviceAreas;

    private String status;

    @SerializedName("wait_time")
    private String waitTime;

    @SerializedName("taxonomy_ids")
    private String taxonomyIds;
}
