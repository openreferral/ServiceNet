package org.benetech.servicenet.adapter.sheltertech.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrganizationRaw {

    @SerializedName("alternate_name")
    private String alternateName;

    @SerializedName("certified")
    private Boolean certified;

    @SerializedName("email")
    private String email;

    @SerializedName("id")
    private String id;

    @SerializedName("legal_status")
    private String legalStatus;

    @SerializedName("long_description")
    private String longDescription;

    @SerializedName("name")
    private String name;

    @SerializedName("short_description")
    private String shortDescription;

    @SerializedName("status")
    private String status;

    @SerializedName("verified_at")
    private LocalDateTime verifiedAt;

    @SerializedName("website")
    private String website;

    @SerializedName("certified_at")
    private LocalDateTime certifiedAt;

    @SerializedName("services")
    private List<ServiceRaw> services;

    @SerializedName("schedule")
    private ScheduleRaw schedule;

    @SerializedName("phones")
    private List<PhoneRaw> phones;

    @SerializedName("address")
    private AddressRaw address;

    @SerializedName("notes")
    private List<NoteRaw> notes;

    @SerializedName("categories")
    private List<CategoryRaw> categories;

    @SerializedName("ratings")
    private List<String> ratings;

}
