package org.benetech.servicenet.adapter.healthleads.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class HealthleadsService extends HealthleadsBaseData {

    @SerializedName("program_id")
    private String programId;

    private String name;

    @SerializedName("alternate_name")
    private String alternateName;

    private String description;

    private String url;

    private String email;

    private String status;

    @SerializedName("interpretation_services")
    private String interpretationServices;

    @SerializedName("application_process")
    private String applicationProcess;

    @SerializedName("wait_time")
    private String waitTime;

    private String fees;

    private String accreditations;

    private String licenses;

    @SerializedName("x_target_population")
    private String targetPopulation;

    @SerializedName("x_other_notes")
    private String otherNotes;

    @SerializedName("sn_num_total_referrals")
    private Integer totalReferrals;

    @SerializedName("sn_num_successful_referrals")
    private Integer successfulReferrals;

}
