package org.benetech.servicenet.adapter.sheltertech.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ServiceRaw {

    @SerializedName("alternate_name")
    private String alternateName;

    @SerializedName("application_process")
    private String applicationProcess;

    @SerializedName("certified")
    private Boolean certified;

    @SerializedName("eligibility")
    private String eligibility;

    @SerializedName("email")
    private String email;

    @SerializedName("fee")
    private String fee;

    @SerializedName("id")
    private String id;

    @SerializedName("interpretation_services")
    private String interpretationServices;

    @SerializedName("long_description")
    private String longDescription;

    @SerializedName("name")
    private String name;

    @SerializedName("required_documents")
    private String requiredDocuments;

    @SerializedName("url")
    private String url;

    @SerializedName("verified_at")
    private LocalDateTime verifiedAt;

    @SerializedName("wait_time")
    private String waitTime;

    @SerializedName("certified_at")
    private LocalDateTime certifiedAt;

    @SerializedName("schedule")
    private ScheduleRaw schedule;

    @SerializedName("notes")
    private List<NoteRaw> notes;

    @SerializedName("categories")
    private List<CategoryRaw> categories;

    @SerializedName("eligibilities")
    private List<EligibilityRaw> eligibilities;

}
